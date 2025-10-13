package test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ControladorEvento – consultas fallidas tolerantes (sin tocar lógica)")
class ControladorEventoConsultaFallidaTest {

    @Test
    void consultasInexistentes() throws Exception {
        TestUtils.resetAll();

        // CE desde fábrica si existe; si no, implementación concreta
        Class<?> fab = TestUtils.loadAny("logica.Fabrica", "logica.fabrica");
        Method getter;
        try { getter = fab.getMethod("getInstance"); }
        catch (NoSuchMethodException e) { getter = fab.getMethod("getInstancia"); }
        Object fabrica = getter.invoke(null);

        Object controladorEv;
        try {
            controladorEv = TestUtils.tryInvoke(
                    fabrica,
                    new String[]{"getIEvento", "getIControladorEvento", "getControladorEvento", "getEvento"});
        } catch (AssertionError e) {
            controladorEv = Class.forName("logica.controladores.ControladorEvento")
                    .getDeclaredConstructor().newInstance();
        }

        // 1) consultaEvento("NO_EXISTE")
        assertTrue(lanzaOEsNulo(() ->
                TestUtils.invokeUnwrapped(controladorEv, new String[]{"consultaEvento"}, "NO_EXISTE")),
                "consultaEvento debe lanzar excepción O devolver null ante inexistente");

        // 2) consultaEdicionEvento("NO_EVT","NO_ED") — orden evento, edición
        assertTrue(lanzaOEsNulo(() ->
                TestUtils.invokeUnwrapped(controladorEv, new String[]{"consultaEdicionEvento"}, "NO_EVT", "NO_ED")),
                "consultaEdicionEvento(ev, ed) debe lanzar excepción O devolver null");

        // 3) consultaEdicionEvento("NO_ED","NO_EVT") — algunas firmas invierten semántica
        assertTrue(lanzaOEsNulo(() ->
                TestUtils.invokeUnwrapped(controladorEv, new String[]{"consultaEdicionEvento"}, "NO_ED", "NO_EVT")),
                "consultaEdicionEvento(x, y) alternativa debe lanzar excepción O devolver null");

        // 4) listarEdicionesEvento("NO_EVT")
        assertTrue(lanzaONuloOVacia(() ->
                TestUtils.invokeUnwrapped(controladorEv, new String[]{"listarEdicionesEvento"}, "NO_EVT")),
                "listarEdicionesEvento debe lanzar excepción O devolver null/colección vacía");
    }

    // ===== Helpers tolerantes (no exigen throws si la lógica devuelve null/vacío) =====

    /** Ejecuta y retorna true si: lanzó excepción, o devolvió null. */
    private static boolean lanzaOEsNulo(ThrowingSupplier<Object> run) {
        try {
            Object res = run.get();
            return res == null; // sin excepción: válido sólo si devuelve null
        } catch (Throwable t) {
            return true; // lanzó: también válido
        }
    }

    /** Ejecuta y retorna true si: lanzó excepción, o devolvió null, o una colección vacía. */
    private static boolean lanzaONuloOVacia(ThrowingSupplier<Object> run) {
        try {
            Object res = run.get();
            if (res == null) return true;
            if (res instanceof Collection<?>) return ((Collection<?>) res).isEmpty();
            // Si tu implementación devuelve un array en lugar de Collection:
            if (res.getClass().isArray()) return java.lang.reflect.Array.getLength(res) == 0;
            // Cualquier otro retorno no nulo y no colección/array se considera no válido en "listar"
            return false;
        } catch (Throwable t) {
            return true; // lanzó: válido
        }
    }

    @FunctionalInterface
    private interface ThrowingSupplier<T> {
        T get() throws Throwable;
    }
}
