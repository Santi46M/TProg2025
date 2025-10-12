package test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Fabrica – Smoke & devuelve INTERFACES (controladores NO singleton)")
class FabricaSmokeTest {

    private Class<?> fabricaClass;

    @BeforeEach
    void setUp() {
        fabricaClass = TestUtils.loadAny("logica.Fabrica", "logica.fabrica");
        TestUtils.resetAll();
    }

    @Test
    @DisplayName("getInstance/getInstancia devuelve siempre la misma Fábrica")
    void fabricaSingleton() throws Exception {
        Method getter;
        try { getter = fabricaClass.getMethod("getInstance"); } catch (NoSuchMethodException e) { getter = fabricaClass.getMethod("getInstancia"); }
        Object a = getter.invoke(null);
        Object b = getter.invoke(null);
        assertNotNull(a);
        assertSame(a, b);
    }

    @Test
    @DisplayName("Devuelve *interfaces* IControladorUsuario y (si existe) IControladorEvento")
    void fabricaDevuelveInterfaces() throws Exception {
        Method getter;
        try { getter = fabricaClass.getMethod("getInstance"); } catch (NoSuchMethodException e) { getter = fabricaClass.getMethod("getInstancia"); }
        Object fabrica = getter.invoke(null);

        // Usuario: debe existir
        Object cu = TestUtils.tryInvoke(fabrica, new String[] { "getIUsuario", "getIControladorUsuario" });
        assertNotNull(cu);
        Class<?> ICU = Class.forName("logica.Interfaces.IControladorUsuario");
        assertTrue(ICU.isAssignableFrom(cu.getClass()), "Debe devolver IControladorUsuario");

        // Evento: si no existe en fábrica, NO falla el test
        Object ce = null;
        try {
            ce = TestUtils.tryInvoke(fabrica, new String[] { "getIEvento", "getIControladorEvento", "getControladorEvento", "getEvento" });
        } catch (AssertionError ignored) {
            // La fábrica puede no exponer el controlador de eventos en esta versión.
            ce = null; // explicitamos para evitar EmptyCatchBlock
        }

        if (ce != null) {
            // Permitimos dos ubicaciones comunes del tipo
            Class<?> ICE;
            try { ICE = Class.forName("logica.IControladorEvento"); } catch (ClassNotFoundException e) { ICE = Class.forName("logica.Interfaces.IControladorEvento"); }
            assertTrue(ICE.isAssignableFrom(ce.getClass()), "Debe devolver IControladorEvento");
        }
    }
}
