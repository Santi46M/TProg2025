package test;
import java.lang.reflect.*;

import static org.junit.jupiter.api.Assertions.*;

final class DomainAccess {
    private DomainAccess() {}

    /* ---------- Carga de Manejadores con mayús/minús ---------- */

    static Object getManejadorUsuario() {
        try {
            Class<?> MU = TestUtils.loadAny("logica.ManejadorUsuario", "logica.manejadorUsuario");
            Object mu;
            try { mu = MU.getMethod("getInstancia").invoke(null); }
            catch (NoSuchMethodException e) { mu = MU.getMethod("getInstance").invoke(null); }
            assertNotNull(mu, "No se pudo obtener ManejadorUsuario");
            return mu;
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    static Object getManejadorEvento() {
        try {
            Class<?> ME = TestUtils.loadAny("logica.ManejadorEvento", "logica.manejadorEvento");
            Object me;
            try { me = ME.getMethod("getInstancia").invoke(null); }
            catch (NoSuchMethodException e) { me = ME.getMethod("getInstance").invoke(null); }
            return me;
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    /* ---------- Usuario (se mantiene) ---------- */

    static Object obtenerUsuario(String nick) {
        Object mu = getManejadorUsuario();
        try {
            for (String m : new String[]{"obtenerUsuario","getUsuario","buscarUsuario"}) {
                try { return mu.getClass().getMethod(m, String.class).invoke(mu, nick); }
                catch (NoSuchMethodException ignored) {}
            }
        } catch (Exception e) { throw new RuntimeException(e); }

        // Fallback a campos privados típicos
        Object v = TestUtils.getFromPrivateMaps(mu, nick, "usuarios","organizadores","asistentes");
        if (v != null) return v;

        // Escaneo genérico de maps/collections
        return buscarEnEstructuras(mu, nick, "Usuario");
    }

    /* ---------- Institucion (parche robusto) ---------- */

    static Object obtenerInstitucion(String nombre) {
        Object mu = getManejadorUsuario();

        // 1) Métodos públicos habituales con (String)
        for (String m : new String[]{"obtenerInstitucion","getInstitucion","buscarInstitucion"}) {
            try { return mu.getClass().getMethod(m, String.class).invoke(mu, nombre); }
            catch (NoSuchMethodException ignored) {}
            catch (Exception e) { throw new RuntimeException(e); }
        }

        // 2) Campos privados más comunes
        Object v = TestUtils.getFromPrivateMaps(mu, nombre,
                "instituciones","insts","mapaInstituciones","mapInstituciones",
                "institucionesMap","mapaDeInstituciones","institMap");
        if (v != null) return v;

        // 3) Escaneo genérico de métodos sin parámetros que devuelven Map/Collection
        v = buscarEnEstructuras(mu, nombre, "Institucion");
        if (v != null) return v;

        // 4) Último recurso: escanear TODOS los campos (incluye superclases)
        try {
            Class<?> c = mu.getClass();
            while (c != null) {
                for (Field f : c.getDeclaredFields()) {
                    f.setAccessible(true);
                    Object obj = f.get(mu);
                    Object match = pickFromContainer(obj, nombre, "Institucion");
                    if (match != null) return match;
                }
                c = c.getSuperclass();
            }
        } catch (Exception ignored) {}

        return null;
    }

    /* ---------- Utilidades internas ---------- */

    // Busca en cualquier Map/Collection que expongan métodos públicos sin parámetros
    private static Object buscarEnEstructuras(Object holder, String keyOrName, String classNameHint) {
        for (Method m : holder.getClass().getMethods()) {
            if (m.getParameterCount() == 0) {
                try {
                    Object res = m.invoke(holder);
                    Object pick = pickFromContainer(res, keyOrName, classNameHint);
                    if (pick != null) return pick;
                } catch (Throwable ignored) {}
            }
        }
        return null;
    }

    // Selecciona un objeto del contenedor según key o nombre (getNombre/getName/nombre)
    @SuppressWarnings("unchecked")
    private static Object pickFromContainer(Object container, String keyOrName, String classNameHint) {
        if (container == null) return null;

        try {
            if (container instanceof java.util.Map<?,?> map) {
                // 1) por clave exacta
                if (map.containsKey(keyOrName)) return map.get(keyOrName);
                // 2) por valor que "parezca" la clase buscada y nombre
                for (Object v : map.values()) {
                    if (v != null && v.getClass().getSimpleName().toLowerCase().contains(classNameHint.toLowerCase())) {
                        Method g = findAny(v.getClass(), "getNombre","getName","nombre");
                        if (g != null) {
                            Object n = g.invoke(v);
                            if (keyOrName.equals(String.valueOf(n))) return v;
                        }
                    }
                }
            } else if (container instanceof java.util.Collection<?> col) {
                for (Object v : col) {
                    if (v != null && v.getClass().getSimpleName().toLowerCase().contains(classNameHint.toLowerCase())) {
                        Method g = findAny(v.getClass(), "getNombre","getName","nombre");
                        if (g != null) {
                            Object n = g.invoke(v);
                            if (keyOrName.equals(String.valueOf(n))) return v;
                        }
                    } else if (v instanceof java.util.Map<?,?> m) {
                        Object inner = pickFromContainer(m, keyOrName, classNameHint);
                        if (inner != null) return inner;
                    }
                }
            }
        } catch (Throwable ignored) {}
        return null;
    }

    private static Method findAny(Class<?> c, String... names) {
        for (String n : names) {
            try { return c.getMethod(n); } catch (NoSuchMethodException ignored) {}
        }
        return null;
    }
}