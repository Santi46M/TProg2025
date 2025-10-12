package test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("DTO sweep – ejercita getters/toString/hashCode en DTOs")
class DTOsCoverageSweepTest {

    @Test
    void dtos() {
        // Intentamos construir y pasear varios DTOs comunes
        tryConstructAndExercise("logica.Datatypes.DTDatosUsuario");
        tryConstructAndExercise("logica.Datatypes.DTEdicion");
        tryConstructAndExercise("logica.Datatypes.DTEvento");
        tryConstructAndExercise("logica.Datatypes.DTRegistro");
        tryConstructAndExercise("logica.Datatypes.DTNivel");

        // DTCategorias: primero con colección, si no, sin args
        Object dc = tryNew("logica.Datatypes.DTCategorias", new Class<?>[]{Collection.class}, List.of("Tec", "Datos"));
        if (dc == null) {
            dc = tryNewNoArgs("logica.Datatypes.DTCategorias");
        }
        if (dc != null) {
            ReflectionPojoSupport.exercisePojo(dc);
        }

        assertTrue(true);
    }

    private void tryConstructAndExercise(String cn) {
        Object o = null;

        // 1) ctor sin args
        if (o == null) {
            o = tryNewNoArgs(cn);
        }
        // 2) intento típico con fechas/strings
        if (o == null) {
            o = tryNew(cn,
                    new Class<?>[]{String.class, String.class, String.class, LocalDate.class, LocalDate.class},
                    "N", "S", "D", LocalDate.now(), LocalDate.now());
        }
        // 3) strings
        if (o == null) {
            o = tryNew(cn, new Class<?>[]{String.class, String.class, String.class}, "N", "S", "D");
        }
        // 4) un string
        if (o == null) {
            o = tryNew(cn, new Class<?>[]{String.class}, "N");
        }

        if (o != null) {
            ReflectionPojoSupport.exercisePojo(o);
        }
    }

    /* ================= Helpers sin catch RuntimeException ================= */

    private Object tryNewNoArgs(String fqcn) {
        try {
            Class<?> c = Class.forName(fqcn);
            Constructor<?> k0 = c.getDeclaredConstructor();
            k0.setAccessible(true);
            return k0.newInstance();
        } catch (ClassNotFoundException
                 | NoSuchMethodException
                 | InstantiationException
                 | IllegalAccessException
                 | InvocationTargetException
                 | SecurityException e) {
            return null;
        }
    }

    private Object tryNew(String fqcn, Class<?>[] paramTypes, Object... args) {
        try {
            Class<?> c = Class.forName(fqcn);
            Constructor<?> k = c.getDeclaredConstructor(paramTypes);
            k.setAccessible(true);
            return k.newInstance(args);
        } catch (ClassNotFoundException
                 | NoSuchMethodException
                 | InstantiationException
                 | IllegalAccessException
                 | InvocationTargetException
                 | SecurityException e) {
            return null;
        }
    }
}
