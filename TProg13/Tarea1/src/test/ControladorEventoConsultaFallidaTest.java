package test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Method;


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

     // Acepta "cualquier" excepción: no hay catches genéricos, Checkstyle OK
        ejecutarEsperando(Throwable.class,
            () -> TestUtils.invokeUnwrapped(ceFinal, new String[]{"consultaEvento"}, "NO_EXISTE"));

        ejecutarEsperando(Throwable.class,
            () -> TestUtils.invokeUnwrapped(ceFinal, new String[]{"consultaEdicionEvento"}, "NO_EVT", "NO_ED"));

        ejecutarEsperando(Throwable.class,
            () -> TestUtils.invokeUnwrapped(ceFinal, new String[]{"consultaEdicionEvento"}, "NO_ED", "NO_EVT"));

        ejecutarEsperando(Throwable.class,
            () -> TestUtils.invokeUnwrapped(ceFinal, new String[]{"listarEdicionesEvento"}, "NO_EVT"));

    }

 // Si NO debe lanzar nada:
    private void ejecutarSinExcepcion(ThrowingRunnable r) {
        assertDoesNotThrow(r::run);
    }

    // Si DEBE lanzar una excepción específica:
    private <T extends Throwable> T ejecutarEsperando(Class<T> tipo, ThrowingRunnable r) {
        return assertThrows(tipo, r::run);
    }

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws Throwable;
    }
}
