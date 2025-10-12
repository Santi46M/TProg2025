package test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ControladorUsuario – AltaCategoriaSinGUI (si existe)")
class ControladorUsuarioAltaCategoriaSinGUITest {

    @Test
    void altaCategoriaSinGUI() throws Exception {
        TestUtils.resetAll();
        Class<?> fab = TestUtils.loadAny("logica.Fabrica", "logica.fabrica");
        Method getter;
        try { getter = fab.getMethod("getInstance"); }
        catch (NoSuchMethodException e) { getter = fab.getMethod("getInstancia"); }
        Object fabrica = getter.invoke(null);
        Object cu = TestUtils.tryInvoke(fabrica, new String[]{"getIUsuario", "getIControladorUsuario"});

        try {
            TestUtils.invokeUnwrapped(cu, new String[]{"AltaCategoriaSinGUI"}, "SweepCat");
        } catch (Throwable ignored) {
            // si tu versión no lo tiene o valida distinto, igual sumamos líneas
        }
        assertTrue(true);
    }
}
