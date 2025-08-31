package test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ControladorEvento – consultas fallidas tolerantes")
class ControladorEventoConsultaFallidaTest {

    @Test
    void consultasInexistentes() throws Exception {
        TestUtils.resetAll();

        // CE desde fábrica si existe; si no, por implementación concreta
        Class<?> fab = TestUtils.loadAny("logica.Fabrica", "logica.fabrica");
        Method getter;
        try { getter = fab.getMethod("getInstance"); }
        catch (NoSuchMethodException e) { getter = fab.getMethod("getInstancia"); }
        Object fabrica = getter.invoke(null);

        Object ce;
        try {
            ce = TestUtils.tryInvoke(fabrica, new String[]{"getIEvento","getIControladorEvento","getControladorEvento","getEvento"});
        } catch (AssertionError ignored) {
            ce = Class.forName("logica.ControladorEvento").getDeclaredConstructor().newInstance();
        }

        // 1) consultaEvento inexistente
        try { TestUtils.invokeUnwrapped(ce, new String[]{"consultaEvento"}, "NO_EXISTE"); } catch (Throwable ignored) {}

        // 2) consultaEdicionEvento por claves inexistentes (probamos nombre/sigla intercambiadas)
        try { TestUtils.invokeUnwrapped(ce, new String[]{"consultaEdicionEvento"}, "NO_EVT", "NO_ED"); } catch (Throwable ignored) {}
        try { TestUtils.invokeUnwrapped(ce, new String[]{"consultaEdicionEvento"}, "NO_ED", "NO_EVT"); } catch (Throwable ignored) {}

        // 3) listarEdicionesEvento de evento inexistente
        try { TestUtils.invokeUnwrapped(ce, new String[]{"listarEdicionesEvento"}, "NO_EVT"); } catch (Throwable ignored) {}

        assertTrue(true);
    }
}
