package web;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.ws.BindingProvider;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@WebFilter(urlPatterns = {
    "/evento/*",                 // ej: /evento/ConsultaEvento  o  /evento/<nombre>
    "/edicion/ConsultaEdicion"   // recibe ?evento=...
})
public class ContadorVisitasFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest req  = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        if (!"GET".equalsIgnoreCase(req.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        String evento = null;

        String servletPath = req.getServletPath();   // p.ej. "/evento"
        String pathInfo    = req.getPathInfo();      // p.ej. "/ConsultaEvento" o "/<nombre>" o null
        String queryEvento = trim(req.getParameter("evento"));

        System.out.println("[VISITAS][DBG.pre] servletPath=" + servletPath + " | pathInfo=" + pathInfo + " | param.evento=" + queryEvento);

        // 1) /evento/ConsultaEvento (o /evento + /ConsultaEvento): priorizar ?evento=
        boolean esConsultaEvento =
            "/evento/ConsultaEvento".equals(servletPath) ||
            ("/evento".equals(servletPath) && "/ConsultaEvento".equals(pathInfo));
        if (esConsultaEvento) {
            evento = queryEvento;
        }

        // 2) /edicion/ConsultaEdicion: también usa ?evento=
        boolean esConsultaEdicion =
            "/edicion/ConsultaEdicion".equals(servletPath) ||
            ("/edicion".equals(servletPath) && "/ConsultaEdicion".equals(pathInfo));
        if (isBlank(evento) && esConsultaEdicion) {
            evento = queryEvento;
        }

        // 3) Forma REST: /evento/<nombre>
        if (isBlank(evento) && "/evento".equals(servletPath) && pathInfo != null && pathInfo.length() > 1) {
            evento = URLDecoder.decode(pathInfo.substring(1), StandardCharsets.UTF_8);
            if (!isBlank(evento)) {
                System.out.println("[VISITAS][DBG.pre] Resuelto por REST: evento='" + evento + "'");
            }
        }

        // Defensa: no contar claves genéricas
        if ("ConsultaEvento".equalsIgnoreCase(evento)) {
            evento = null;
        }

        // Guardar contexto para log
        String reqUri  = req.getRequestURI();
        String nick    = (req.getSession(false) != null)
                ? String.valueOf(req.getSession(false).getAttribute("nick"))
                : "<anon>";

        // Dejar continuar primero (para que el servlet ponga atributos antes del forward)
        chain.doFilter(request, response);

        // 4) Post-chain: si aún no lo tenemos, intentar por ATRIBUTOS del request
        if (isBlank(evento)) {
            // candidatos típicos que podría setear el servlet antes del forward
            Object v;

            v = req.getAttribute("evento");
            if (v instanceof String s && !isBlank(s)) {
                evento = s.trim();
                System.out.println("[VISITAS][DBG.post] Resuelto por atributo 'evento'='" + evento + "'");
            }

            if (isBlank(evento)) {
                v = req.getAttribute("nombreEvento");
                if (v instanceof String s && !isBlank(s)) {
                    evento = s.trim();
                    System.out.println("[VISITAS][DBG.post] Resuelto por atributo 'nombreEvento'='" + evento + "'");
                }
            }

            if (isBlank(evento)) {
                v = req.getAttribute("evNombre");
                if (v instanceof String s && !isBlank(s)) {
                    evento = s.trim();
                    System.out.println("[VISITAS][DBG.post] Resuelto por atributo 'evNombre'='" + evento + "'");
                }
            }

            // Si el servlet puso un DTO (por ejemplo DtEvento) en algún atributo común
            if (isBlank(evento)) {
                String[] posiblesDTO = { "dtEvento", "eventoDTO", "eventoObj" };
                for (String key : posiblesDTO) {
                    v = req.getAttribute(key);
                    if (v != null) {
                        try {
                            Method m = v.getClass().getMethod("getNombre");
                            Object nombre = m.invoke(v);
                            if (nombre != null && !isBlank(nombre.toString())) {
                                evento = nombre.toString().trim();
                                System.out.println("[VISITAS][DBG.post] Resuelto por DTO '" + key + "'.getNombre()='" + evento + "'");
                                break;
                            }
                        } catch (Exception ignore) {
                            // sin getNombre() o fallo — seguimos probando
                        }
                    }
                }
            }
        }

        if ("ConsultaEvento".equalsIgnoreCase(evento)) {
            evento = null;
        }

        if (!isBlank(evento)) {
            System.out.println("[VISITAS] Preparando notificación | evento='" + evento
                    + "' | uri='" + reqUri + "' | nick='" + nick + "' | status=" + resp.getStatus());

            try {
                publicadores.PublicadorEstadisticasService service = new publicadores.PublicadorEstadisticasService();
                publicadores.PublicadorEstadisticas port = service.getPublicadorEstadisticasPort();

                String overrideEndpoint = System.getProperty("estadisticas.endpoint", null);
                if (overrideEndpoint != null && !overrideEndpoint.isBlank()) {
                    ((BindingProvider) port).getRequestContext().put(
                            BindingProvider.ENDPOINT_ADDRESS_PROPERTY, overrideEndpoint);
                    System.out.println("[VISITAS] Endpoint OVERRIDE = " + overrideEndpoint);
                } else {
                    Object actual = ((BindingProvider) port).getRequestContext()
                            .get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
                    System.out.println("[VISITAS] Endpoint por defecto del stub = " + actual);
                }

                ((BindingProvider) port).getRequestContext().put("com.sun.xml.ws.connect.timeout", 3000);
                ((BindingProvider) port).getRequestContext().put("com.sun.xml.ws.request.timeout", 3000);

                System.out.println("[VISITAS] Invocando registrarVisita('" + evento + "') …");
                port.registrarVisita(evento);
                System.out.println("[VISITAS] OK registrarVisita para '" + evento + "'");

            } catch (jakarta.xml.ws.WebServiceException wse) {
                System.out.println("[VISITAS][ERROR] WebServiceException: " + wse.getClass().getName()
                        + " | msg=" + String.valueOf(wse.getMessage()));
                Throwable c = wse.getCause();
                int hops = 0;
                while (c != null && hops++ < 5) {
                    System.out.println("   causa -> " + c.getClass().getName() + " | " + String.valueOf(c.getMessage()));
                    c = c.getCause();
                }
            } catch (Exception ex) {
                System.out.println("[VISITAS][ERROR] Genérico: " + ex.getClass().getName()
                        + " | msg=" + String.valueOf(ex.getMessage()));
            }
        } else {
            System.out.println("[VISITAS] No se pudo identificar el evento | uri='" + reqUri + "' | nick='" + nick + "'");
        }
    }

    @Override public void init(FilterConfig filterConfig) throws ServletException {}
    @Override public void destroy() {}

    private static String trim(String s){ return s == null ? null : s.trim(); }
    private static boolean isBlank(String s){ return s == null || s.trim().isEmpty(); }
}
