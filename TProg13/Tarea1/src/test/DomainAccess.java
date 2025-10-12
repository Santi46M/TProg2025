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
            Class<?> manejadorUs = TestUtils.loadAny(
                "logica.Manejadores.ManejadorUsuario",
                "logica.Manejadores.manejadorUsuario"
            );
            Object manejadorUsr;
            try {
                manejadorUsr = manejadorUs.getMethod("getInstancia").invoke(null);
            } catch (NoSuchMethodException e) {
                manejadorUsr = manejadorUs.getMethod("getInstance").invoke(null);
            }
            assertNotNull(manejadorUsr, "No se pudo obtener ManejadorUsuario");
            return manejadorUsr;
        } catch ( NoSuchMethodException
               | IllegalAccessException
               | InvocationTargetException e) {
            throw new IllegalStateException("Error accediendo a ManejadorUsuario", e);
        }
    }

    static Object getManejadorEvento() {
        try {
            Class<?> manejadorEvn = TestUtils.loadAny(
                "logica.Manejadores.ManejadorEvento",
                "logica.Manejadores.manejadorEvento"
            );
            Object manejadorEv;
            try {
                manejadorEv = manejadorEvn.getMethod("getInstancia").invoke(null);
            } catch (NoSuchMethodException e) {
                manejadorEv = manejadorEvn.getMethod("getInstance").invoke(null);
            }
            return manejadorEv;
        } catch ( NoSuchMethodException
               | IllegalAccessException
               | InvocationTargetException e) {
            throw new IllegalStateException("Error accediendo a ManejadorEvento", e);
        }
    }

    /* ---------- Usuario ---------- */

    static Object obtenerUsuario(String nick) {
        Object manejadorUs = getManejadorUsuario();
        try {
            for (String m : new String[]{"obtenerUsuario", "getUsuario", "buscarUsuario"}) {
                try {
                    return manejadorUs.getClass().getMethod(m, String.class).invoke(manejadorUs, nick);
                } catch (NoSuchMethodException ignored) {
                    // probar siguiente nombre de método
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Error accediendo a usuario", e);
        }

        Object objeto = TestUtils.getFromPrivateMaps(manejadorUs, nick, "usuarios", "organizadores", "asistentes");
        if (objeto != null) return objeto;

        return buscarEnEstructuras(manejadorUs, nick, "Usuario");
    }

    /* ---------- Institucion ---------- */

    static Object obtenerInstitucion(String nombre) {
        Object manejadorUs = getManejadorUsuario();

        for (String m : new String[]{"obtenerInstitucion", "getInstitucion", "buscarInstitucion"}) {
            try {
                return manejadorUs.getClass().getMethod(m, String.class).invoke(manejadorUs, nombre);
            } catch (NoSuchMethodException ignored) {
                // probar siguiente nombre
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException("Error al obtener institución", e);
            }
        }

        Object objeto = TestUtils.getFromPrivateMaps(manejadorUs, nombre,
                "instituciones", "insts", "mapaInstituciones", "mapInstituciones",
                "institucionesMap", "mapaDeInstituciones", "institMap");
        if (objeto != null) return objeto;

        objeto = buscarEnEstructuras(manejadorUs, nombre, "Institucion");
        if (objeto != null) return objeto;

        try {
            Class<?> clase = manejadorUs.getClass();
            while (clase != null) {
                for (Field f : clase.getDeclaredFields()) {
                    f.setAccessible(true);
                    Object obj = f.get(manejadorUs);
                    Object match = pickFromContainer(obj, nombre, "Institucion");
                    if (match != null) return match;
                }
                clase = clase.getSuperclass();
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
                        Method getter = findAny(v.getClass(), "getNombre", "getName", "nombre");
                        if (getter != null) {
                            Object name = getter.invoke(v);
                            if (keyOrName.equals(String.valueOf(name))) return v;
                        }
                    }
                }
            } else if (container instanceof java.util.Collection<?> col) {
                for (Object v : col) {
                    if (v != null && v.getClass().getSimpleName().toLowerCase().contains(classNameHint.toLowerCase())) {
                        Method getter = findAny(v.getClass(), "getNombre", "getName", "nombre");
                        if (getter != null) {
                            Object name = getter.invoke(v);
                            if (keyOrName.equals(String.valueOf(name))) return v;
                        }
                    } else if (v instanceof java.util.Map<?, ?> mapa) {
                        Object inner = pickFromContainer(mapa, keyOrName, classNameHint);
                        if (inner != null) return inner;
                    }
                }
            }
        } catch (IllegalAccessException | InvocationTargetException ignored) {
            // reflexión fallida → continuar
        }

        return null;
    }

    private static Method findAny(Class<?> clase, String... names) {
        for (String n : names) {
            try {
                return clase.getMethod(n);
            } catch (NoSuchMethodException ignored) {
            					// probar siguiente nombre
            }
        }
        return null;
    }
}
