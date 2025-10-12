package test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class ReflectionPojoSupport {
    private ReflectionPojoSupport() {}

    /* ====== Instanciación tolerante ====== */

    static Object makeInstance(String fqcn) {
        return makeInstance(fqcn, 0);
    }

    private static Object makeInstance(String fqcn, int depth) {
        try {
            Class<?> c = Class.forName(fqcn);

            if (c.isEnum()) {
                Object[] vals = c.getEnumConstants();
                return vals != null && vals.length > 0 ? vals[0] : null;
            }

            // 1) Constructor vacío
            try {
                Constructor<?> k0 = c.getDeclaredConstructor();
                k0.setAccessible(true);
                return k0.newInstance();
            } catch (NoSuchMethodException e) {
                // no hay ctor vacío, seguimos
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                return null;
            }

            // 2) Constructores ordenados por menor cantidad de parámetros
            Constructor<?>[] ks = c.getDeclaredConstructors();
            Arrays.sort(ks, Comparator.comparingInt(Constructor::getParameterCount));
            for (Constructor<?> k : ks) {
                Class<?>[] ts = k.getParameterTypes();
                Object[] args = new Object[ts.length];
                boolean ok = true;
                for (int i = 0; i < ts.length; i++) {
                    args[i] = sampleFor(ts[i], depth + 1);
                    if (args[i] == null && ts[i].isPrimitive()) {
                        ok = false;
                        break;
                    }
                }
                if (!ok) {
                    continue;
                }
                try {
                    k.setAccessible(true);
                    return k.newInstance(args);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    // intentamos siguiente ctor
                }
            }
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private static Object sampleFor(Class<?> t, int depth) {
        if (t == String.class) return "x";
        if (t == int.class || t == Integer.class) return 0;
        if (t == long.class || t == Long.class) return 0L;
        if (t == double.class || t == Double.class) return 0.0;
        if (t == float.class || t == Float.class) return 0.0f;
        if (t == boolean.class || t == Boolean.class) return false;
        if (t == char.class || t == Character.class) return '\0';
        if (t == LocalDate.class) return LocalDate.now();

        if (t.isEnum()) {
            Object[] vals = t.getEnumConstants();
            return vals != null && vals.length > 0 ? vals[0] : null;
        }

        if (Collection.class.isAssignableFrom(t)) {
            if (Set.class.isAssignableFrom(t)) return new HashSet<>(List.of("x"));
            return new ArrayList<>(List.of("x"));
        }
        if (Map.class.isAssignableFrom(t)) return new HashMap<>();

        // Evitar recursión infinita en dominio propio
        if (depth <= 1 && t.getName().startsWith("logica.")) {
            Object o = makeInstance(t.getName(), depth + 1);
            if (o != null) return o;
        }

        // último recurso
        try {
            Constructor<?> k0 = t.getDeclaredConstructor();
            k0.setAccessible(true);
            return k0.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    /* ====== Ejecutar getters/equals/hashCode/toString ====== */

    static void exercisePojo(Object o) {
        assertNotNull(o);
        // getters "getX"/"isX" sin parámetros
        for (Method m : o.getClass().getMethods()) {
            if (m.getParameterCount() == 0
                    && (m.getName().startsWith("get") || m.getName().startsWith("is"))
                    && !m.getReturnType().equals(void.class)) {
                try {
                    m.invoke(o);
                } catch (IllegalAccessException | InvocationTargetException ignored) {
                    // getter inaccesible o con error → continuar
                }
            }
        }
        // equals/hashCode/toString básicos
        o.equals(o);
        o.hashCode();
        o.toString();
        assertNotEquals(o, new Object());
        assertNotEquals(o, null);
    }
}
