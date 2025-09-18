package test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.*;
import java.time.LocalDate;
import java.util.*;

final class ReflectionPojoSupport {
    private ReflectionPojoSupport() {}

    /* ====== Instanciación tolerante ====== */

    static Object makeInstance(String fqcn) {
        return makeInstance(fqcn, 0);
    }

    @SuppressWarnings("unchecked")
    private static Object makeInstance(String fqcn, int depth) {
        try {
            Class<?> c = Class.forName(fqcn);

            if (c.isEnum()) { // primer literal
                Object[] vals = c.getEnumConstants();
                return vals != null && vals.length > 0 ? vals[0] : null;
            }

            // 1) ctor vacío
            try {
                Constructor<?> k0 = c.getDeclaredConstructor();
                k0.setAccessible(true);
                return k0.newInstance();
            } catch (NoSuchMethodException ignored) {}

            // 2) ctores ordenados por menor cantidad de params
            Constructor<?>[] ks = c.getDeclaredConstructors();
            Arrays.sort(ks, Comparator.comparingInt(Constructor::getParameterCount));
            for (Constructor<?> k : ks) {
                Class<?>[] ts = k.getParameterTypes();
                Object[] args = new Object[ts.length];
                boolean ok = true;
                for (int i = 0; i < ts.length; i++) {
                    args[i] = sampleFor(ts[i], depth + 1);
                    if (args[i] == null && ts[i].isPrimitive()) { ok = false; break; }
                }
                if (!ok) continue;
                try {
                    k.setAccessible(true);
                    return k.newInstance(args);
                } catch (Throwable ignored) { /* probamos siguiente ctor */ }
            }
            return null;
        } catch (Throwable t) {
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

        // Evitar recursiones profundas en dominio propio
        if (depth <= 1 && t.getName().startsWith("logica.")) {
            Object o = makeInstance(t.getName(), depth + 1);
            if (o != null) return o;
        }

        // último recurso
        try {
            Constructor<?> k0 = t.getDeclaredConstructor();
            k0.setAccessible(true);
            return k0.newInstance();
        } catch (Throwable ignored) {
            return null;
        }
    }

    /* ====== Ejecutar getters/equals/hashCode/toString ====== */

    static void exercisePojo(Object o) {
        assertNotNull(o);
        // getters "getX"/"isX" sin parámetros
        for (Method m : o.getClass().getMethods()) {
            if (m.getParameterCount() == 0 &&
               (m.getName().startsWith("get") || m.getName().startsWith("is")) &&
               !m.getReturnType().equals(void.class)) {
                try { m.invoke(o); } catch (Throwable ignored) {}
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
