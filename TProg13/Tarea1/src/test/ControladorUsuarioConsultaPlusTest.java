package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;            // ← ESTA es la clave

import java.lang.reflect.Method;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


@DisplayName("ControladorUsuario – ConsultaUsuario/obtenerDatos/updates encadenados")
class ControladorUsuarioConsultaPlusTest {

    private Object controladorUs;

    @BeforeEach
    void setUp() throws Exception {
        TestUtils.resetAll();
        Class<?> fab = TestUtils.loadAny("logica.Fabrica", "logica.fabrica");
        Method getter;
        try { 
        	getter = fab.getMethod("getInstance"); 
        } catch (NoSuchMethodException e) { 
        	getter = fab.getMethod("getInstancia"); 
        }
        Object fabrica = getter.invoke(null);
        controladorUs = TestUtils.tryInvoke(fabrica, new String[]{"getIUsuario", "getIControladorUsuario"});
        TestUtils.tryInvoke(controladorUs, new String[]{"AltaInstitucion"}, "Inst_CUP", "d", "w");
    }

    @Test
    @DisplayName("ConsultaUsuario + obtenerDatosUsuario tras varias actualizaciones")
    void consultaYDatos() {
        // alta asistente
        TestUtils.tryInvoke(controladorUs, new String[]{"AltaUsuario"},
                "cupi", "Cu Pi", "cupi@x", "d", "l", "Ap0",
                LocalDate.of(2000, 1, 1), "Inst_CUP", false);

        // actualizar asistente
        TestUtils.tryInvoke(controladorUs, new String[]{"actualizarAsistente"},
                "cupi", "Ap1", LocalDate.of(2001, 1, 1));

        // llamada a ConsultaUsuario (sea lo que sea que haga, no debería romper)
        assertDoesNotThrow(() ->
        TestUtils.invokeUnwrapped(controladorUs, new String[]{"ConsultaUsuario"}, "cupi")
        		);

     // obtener datos y verificar cambios
        Object dto = TestUtils.tryInvoke(controladorUs, new String[]{"obtenerDatosUsuario"}, "cupi");
        assertNotNull(dto);

        var mAp = TestUtils.findMethod(dto, "getApellido", "apellido");
        var mFn = TestUtils.findMethod(dto, "getFechaNacimiento", "getNacimiento", "fechaNacimiento");

        if (mAp != null) {
            var variable = assertDoesNotThrow(() -> String.valueOf(mAp.invoke(dto)));
            assertEquals("Ap1", variable);
        }
        if (mFn != null) {
            var variable = assertDoesNotThrow(() -> (LocalDate) mFn.invoke(dto));
            assertEquals(LocalDate.of(2001, 1, 1), variable);
        }
    }
    @Test
    @DisplayName("Cambiar a organizador y actualizarOrganizador")
    void cambioYUpdateOrganizador() {
        // alta como organizador
        TestUtils.tryInvoke(controladorUs, new String[]{"AltaUsuario"},
                "orgx", "Org X" , "orgx@x", "d0", "l0", "Ap",
                LocalDate.of(1990, 1, 1), "Inst_CUP", true);

        // actualizar datos de organizador
        TestUtils.tryInvoke(controladorUs, new String[]{"actualizarOrganizador"},
                "orgx", "d1", "l1");

        Object dto = TestUtils.tryInvoke(controladorUs, new String[]{"obtenerDatosUsuario"}, "orgx");
        assertNotNull(dto);

        var mDesc = TestUtils.findMethod(dto, "getDescripcion", "descripcion");
        var mLink = TestUtils.findMethod(dto, "getLink", "link", "getWeb");

        if (mDesc != null) {
            var desc = assertDoesNotThrow(() -> String.valueOf(mDesc.invoke(dto)));
            assertEquals("d1", desc);
        }
        if (mLink != null) {
            var link = assertDoesNotThrow(() -> String.valueOf(mLink.invoke(dto)));
            assertEquals("l1", link);
        }
    }
}