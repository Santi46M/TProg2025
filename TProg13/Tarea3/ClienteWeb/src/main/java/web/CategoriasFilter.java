package web;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import publicadores.DtEvento;
import publicadores.DtEventoArray;

@WebFilter("/*")
public class CategoriasFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            try {
                // create the publicador port (defensive)
                publicadores.PublicadorEventoService service = new publicadores.PublicadorEventoService();
                publicadores.PublicadorEvento port = null;
                try { port = service.getPublicadorEventoPort(); } catch (Exception ignore) { }

                List<DtEvento> eventos = new ArrayList<>();

                if (port != null) {
                    try {
                        // Preferred typed wrapper
                        DtEventoArray arr = port.listarEventos();
                        if (arr != null && arr.getItem() != null) {
                            eventos.addAll(arr.getItem());
                        }
                    } catch (Throwable t) {
                        // Fallback: try reflective access to listarEventos
                        try {
                            Object raw = port.getClass().getMethod("listarEventos").invoke(port);
                            if (raw instanceof DtEvento[]) {
                                DtEvento[] darr = (DtEvento[]) raw;
                                for (DtEvento d : darr) eventos.add(d);
                            } else if (raw != null) {
                                try {
                                    @SuppressWarnings("unchecked")
                                    List<DtEvento> items = (List<DtEvento>) raw.getClass().getMethod("getItem").invoke(raw);
                                    if (items != null) eventos.addAll(items);
                                } catch (Exception ignore) {}
                            }
                        } catch (Exception ignore) { }
                    }
                }

                // From events, extract unique category names
                Set<String> cats = new HashSet<>();
                for (DtEvento ev : eventos) {
                    if (ev == null) continue;
                    try {
                        DtEvento.Categorias c = ev.getCategorias();
                        if (c != null && c.getCategoria() != null) {
                            for (String s : c.getCategoria()) if (s != null) cats.add(s);
                        }
                    } catch (Exception ignore) {}
                }

                // Expose as a simple list of category names (menu.jsp handles multiple shapes)
                List<String> dtCategorias = new ArrayList<>(cats);
                request.setAttribute("dtCategorias", dtCategorias);
            } catch (Exception e) {
                // If anything fails, expose an empty list and continue
                request.setAttribute("dtCategorias", List.of());
            }
        }
        chain.doFilter(request, response);
    }

    @Override public void init(FilterConfig filterConfig) throws ServletException {}
    @Override public void destroy() {}
}