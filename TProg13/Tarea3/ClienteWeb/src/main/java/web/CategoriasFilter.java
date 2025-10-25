package web;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import publicadores.DtCategorias;

@WebFilter("/*")
public class CategoriasFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            try {
                // === EXACTAS estas dos líneas para crear el port de eventos ===
                publicadores.PublicadorEventoService service = new publicadores.PublicadorEventoService();
                publicadores.PublicadorEvento port = service.getPublicadorEventoPort();

                // Puede devolver DTCategorias[] o un wrapper con getItem()
                List<DtCategorias> dtCategorias = new ArrayList<>();
                Object res = port.listarDTCategorias();

                if (res instanceof DtCategorias[]) {
                    DtCategorias[] arr = (DtCategorias[]) res;
                    dtCategorias = (arr == null) ? List.of() : Arrays.asList(arr);
                } else if (res != null) {
                    try {
                        // Wrapper JAX-WS típico con método getItem()
                        @SuppressWarnings("unchecked")
                        List<DtCategorias> items = (List<DtCategorias>) res.getClass()
                                .getMethod("getItem").invoke(res);
                        if (items != null) dtCategorias.addAll(items);
                    } catch (ReflectiveOperationException ignore) {
                        // Si no hay getItem, dejamos la lista vacía
                    }
                }

                request.setAttribute("dtCategorias", dtCategorias);
            } catch (Exception e) {
                // Si falla el servicio, no bloqueamos la navegación
                request.setAttribute("dtCategorias", List.of());
            }
        }
        chain.doFilter(request, response);
    }

    @Override public void init(FilterConfig filterConfig) throws ServletException {}
    @Override public void destroy() {}
}
