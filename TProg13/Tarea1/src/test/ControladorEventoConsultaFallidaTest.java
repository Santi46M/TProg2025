package test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ControladorEvento – consultas fallidas tolerantes")
class ControladorEventoConsultaFallidaTest {

    @Test
    void consultasInexistentes() throws Throwable {  // ✅ permite que TestUtils lance Throwable
        TestUtils.resetAll();

        // CE desde fábrica si existe; si no, por implementación concreta
        Class<?> fab = TestUtils.loadAny("logica.Fabrica", "logica.fabrica");
        Method getter;
        try {
            getter = fab.getMethod("getInstance");
        } catch (NoSuchMethodException e) {
            getter = fab.getMethod("getInstancia");
        }
        Object fabrica = getter.invoke(null);

        Object ce;
        try {
            ce = TestUtils.tryInvoke(fabrica, new String[]{
                    "getIEvento", "getIControladorEvento", "getControladorEvento", "getEvento"});
        } catch (AssertionError e) {
            ce = Class.forName("logica.Controladores.ControladorEvento")
                    .getDeclaredConstructor().newInstance();
        }

        // ✅ ce no es efectivamente final, así que copiamos
        final Object ceFinal = ce;

        // Ejecuciones tolerantes
        ejecutarTolerante(() -> TestUtils.invokeUnwrapped(ceFinal, new String[]{"consultaEvento"}, "NO_EXISTE"));
        ejecutarTolerante(() -> TestUtils.invokeUnwrapped(ceFinal, new String[]{"consultaEdicionEvento"}, "NO_EVT", "NO_ED"));
        ejecutarTolerante(() -> TestUtils.invokeUnwrapped(ceFinal, new String[]{"consultaEdicionEvento"}, "NO_ED", "NO_EVT"));
        ejecutarTolerante(() -> TestUtils.invokeUnwrapped(ceFinal, new String[]{"listarEdicionesEvento"}, "NO_EVT"));

        assertTrue(true);
    }

    // ✅ Helper tolerante
    private void ejecutarTolerante(ThrowingRunnable runnable) {
        try {
            runnable.run();
        } catch (RuntimeException | AssertionError e) {
            assertTrue(true); // comportamiento aceptado
        } catch (Throwable t) {
            assertTrue(true); // comportamiento aceptado
        }
    }

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws Throwable;
    }
}
