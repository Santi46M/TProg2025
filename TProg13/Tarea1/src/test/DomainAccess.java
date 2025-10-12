package test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;

final class DomainAccess {
    private DomainAccess() {}

    /* ---------- Carga de Manejadores con mayús/minús ---------- */

    static Object getManejadorUsuario() {
        try {
            Class<?> MU = TestUtils.loadAny(
                "logica.Manejadores.ManejadorUsuario",
                "logica.Manejadores.manejadorUsuario"
            );
            Object mu;
            try {
                mu = MU.getMethod("getInstancia").invoke(null);
            } catch (NoSuchMethodException e) {
                mu = MU.getMethod("getInstance").invoke(null);
            }
            assertNotNull(mu, "No se pudo obtener ManejadorUsuario");
            return mu;
        } catch ( NoSuchMethodException
               | IllegalAccessException
               | InvocationTargetException e) {
            throw new IllegalStateException("Error accediendo a ManejadorUsuario", e);
        }
    }

    static Object getManejadorEvento() {
        try {
            Class<?> ME = TestUtils.loadAny(
                "logica.Manejadores.ManejadorEvento",
                "logica.Manejadores.manejadorEvento"
            );
            Object me;
            try {
                me = ME.getMethod("getInstancia").invoke(null);
            } catch (NoSuchMethodException e) {
                me = ME.getMethod("getInstance").invoke(null);
            }
            return me;
        } catch ( NoSuchMethodException
               | IllegalAccessException
               | InvocationTargetException e) {
            throw new IllegalStateException("Error accediendo a ManejadorEvento", e);
        }
    }

    /* ---------- Usuario ---------- */

    static Object obtenerUsuario(String nick) {
        Object mu = getManejadorUsuario();
        try {
            for (String m : new String[]{"obtenerUsuario", "getUsuario", "buscarUsuario"}) {
                try {
                    return mu.getClass().getMethod(m, String.class).invoke(mu, nick);
                } catch (NoSuchMethodException ignored) {
                    // probar siguiente nombre de método
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Error accediendo a usuario", e);
        }

        Object v = TestUtils.getFromPrivateMaps(mu, nick, "usuarios", "organizadores", "asistentes");
        if (v != null) return v;

        return buscarEnEstructuras(mu, nick, "Usuario");
    }

    /* ---------- Institucion ---------- */

    static Object obtenerInstitucion(String nombre) {
        Object mu = getManejadorUsuario();

        for (String m : new String[]{"obtenerInstitucion", "getInstitucion", "buscarInstitucion"}) {
            try {
                return mu.getClass().getMethod(m, String.class).invoke(mu, nombre);
            } catch (NoSuchMethodException ignored) {
                // probar siguiente nombre
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException("Error al obtener institución", e);
            }
        }

        Object v = TestUtils.getFromPrivateMaps(mu, nombre,
                "instituciones", "insts", "mapaInstituciones", "mapInstituciones",
                "institucionesMap", "mapaDeInstituciones", "institMap");
        if (v != null) return v;

        v = buscarEnEstructuras(mu, nombre, "Institucion");
        if (v != null) return v;

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
        } catch (IllegalAccessException ignored) {
            // campo inaccesible → continuar
        }

        return null;
    }

    /* ---------- Utilidades internas ---------- */

    private static Object buscarEnEstructuras(Object holder, String keyOrName, String classNameHint) {
        for (Method m : holder.getClass().getMethods()) {
            if (m.getParameterCount() == 0) {
                try {
                    Object res = m.invoke(holder);
                    Object pick = pickFromContainer(res, keyOrName, classNameHint);
                    if (pick != null) return pick;
                } catch (IllegalAccessException | InvocationTargetException ignored) {
                    // método inaccesible → continuar
                }
            }
        }
        return null;
    }

    private static Object pickFromContainer(Object container, String keyOrName, String classNameHint) {
        if (container == null) return null;

        try {
            if (container instanceof java.util.Map<?, ?> map) {
                if (map.containsKey(keyOrName)) return map.get(keyOrName);
                for (Object v : map.values()) {
                    if (v != null && v.getClass().getSimpleName().toLowerCase().contains(classNameHint.toLowerCase())) {
                        Method g = findAny(v.getClass(), "getNombre", "getName", "nombre");
                        if (g != null) {
                            Object n = g.invoke(v);
                            if (keyOrName.equals(String.valueOf(n))) return v;
                        }
                    }
                }
            } else if (container instanceof java.util.Collection<?> col) {
                for (Object v : col) {
                    if (v != null && v.getClass().getSimpleName().toLowerCase().contains(classNameHint.toLowerCase())) {
                        Method g = findAny(v.getClass(), "getNombre", "getName", "nombre");
                        if (g != null) {
                            Object n = g.invoke(v);
                            if (keyOrName.equals(String.valueOf(n))) return v;
                        }
                    } else if (v instanceof java.util.Map<?, ?> m) {
                        Object inner = pickFromContainer(m, keyOrName, classNameHint);
                        if (inner != null) return inner;
                    }
                }
            }
        } catch (IllegalAccessException | InvocationTargetException ignored) {
            // reflexión fallida → continuar
        }

        return null;
    }

    private static Method findAny(Class<?> c, String... names) {
        for (String n : names) {
            try {
                return c.getMethod(n);
            } catch (NoSuchMethodException ignored) {
            					// probar siguiente nombre
            }
        }
        return null;
    }
}
