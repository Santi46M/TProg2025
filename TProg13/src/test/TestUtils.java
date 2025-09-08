package test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.*;
import java.util.*;

final class TestUtils {
    private TestUtils() {}

    /* ---------- Invocación flexible ---------- */

    static Method findMethod(Object o, String... names) {
        for (String n : names) {
            for (Method m : o.getClass().getMethods()) {
                if (m.getName().equals(n)) return m;
            }
        }
        return null;
    }

    static Object tryInvoke(Object target, String[] names, Object... args) {
        Method m = findMethod(target, names);
        if (m == null) {
            String all = Arrays.stream(target.getClass().getMethods())
                .map(Method::getName).toList().toString();
            fail("No se encontró ninguno de: " + String.join(", ", names) +
                 " en " + target.getClass().getName() + ". Públicos: " + all);
        }
        try { return m.invoke(target, args); }
        catch (InvocationTargetException e) { throw new RuntimeException(e.getTargetException()); }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    /* ---------- Carga tolerante de clases (soporta fabrica minúscula) ---------- */

    static Class<?> loadAny(String... names) {
        for (String n : names) {
            try { return Class.forName(n); }
            catch (Throwable ignored) {} // atrapamos también NoClassDefFoundError
        }
        throw new RuntimeException("No pude cargar ninguna clase: " + Arrays.toString(names));
    }

    /* ---------- Reset SOLO de singletons reales (Fábrica y Manejadores) ---------- */

    static void resetSingleton(Class<?> c) {
        for (var fname : new String[]{"instancia", "instance"}) {
            try {
                Field f = c.getDeclaredField(fname);
                f.setAccessible(true);
                f.set(null, null);
                return;
            } catch (NoSuchFieldException ignored) {
            } catch (Exception e) { throw new RuntimeException(e); }
        }
    }

    static void resetAll() {
        for (String cn : List.of(
                "logica.Fabrica",
                "logica.fabrica",              // <— por si tu factory es minúscula
                "logica.ManejadorUsuario",
                "logica.manejadorUsuario",     // <— NUEVO
                "logica.ManejadorEvento",
                "logica.manejadorEvento"       // <— NUEVO
        )) {
            try { resetSingleton(Class.forName(cn)); } catch (Throwable ignored) {}
        }
    }

    /* ---------- Constructores tolerantes (por si hace falta crear DTOs) ---------- */

    static Object tolerantNew(String fqcn, Object... args) {
        try {
            Class<?> c = Class.forName(fqcn);
            outer: for (Constructor<?> k : c.getDeclaredConstructors()) {
                if (k.getParameterCount() != args.length) continue;
                Class<?>[] ts = k.getParameterTypes();
                for (int i=0;i<ts.length;i++) {
                    if (args[i]==null) continue;
                    if (!ts[i].isAssignableFrom(args[i].getClass())) continue outer;
                }
                k.setAccessible(true);
                return k.newInstance(args);
            }
            Constructor<?> k0 = c.getDeclaredConstructor();
            k0.setAccessible(true);
            return k0.newInstance();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No hay constructor compatible en " + fqcn, e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    static Object getFromPrivateMaps(Object holder, String key, String... fields) {
        Class<?> c = holder.getClass();
        for (String fname : fields) {
            try {
                Field f = c.getDeclaredField(fname);
                f.setAccessible(true);
                Object v = f.get(holder);
                if (v instanceof Map<?, ?> m) return ((Map<String, Object>) m).get(key);
            } catch (NoSuchFieldException ignored) {
            } catch (Exception e) { throw new RuntimeException(e); }
        }
        return null;
    }
    
 // Lanza la excepción original (sin envolver) para poder usar assertThrows con su tipo real.
    static Object invokeUnwrapped(Object target, String[] names, Object... args) throws Throwable {
        Method m = findMethod(target, names);
        if (m == null) {
            String all = Arrays.stream(target.getClass().getMethods())
                .map(Method::getName).toList().toString();
            throw new AssertionError("No se encontró ninguno de: " + String.join(", ", names) +
                                     " en " + target.getClass().getName() + ". Públicos: " + all);
        }
        try { return m.invoke(target, args); }
        catch (InvocationTargetException e) { throw e.getTargetException(); }
    }
    
    static java.lang.reflect.Method findMethod(Object target, String name, Class<?>... paramTypes) {
        try {
            java.lang.reflect.Method m = target.getClass().getMethod(name, paramTypes);
            m.setAccessible(true);
            return m;
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
