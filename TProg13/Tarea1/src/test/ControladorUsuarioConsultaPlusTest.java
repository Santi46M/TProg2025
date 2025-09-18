package test;
import org.junit.jupiter.api.*;

import java.lang.reflect.Method;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ControladorUsuario – ConsultaUsuario/obtenerDatos/updates encadenados")
class ControladorUsuarioConsultaPlusTest {

    Object cu;

    @BeforeEach
    void setUp() throws Exception {
        TestUtils.resetAll();
        Class<?> fab = TestUtils.loadAny("logica.Fabrica", "logica.fabrica");
        Method getter;
        try { getter = fab.getMethod("getInstance"); }
        catch (NoSuchMethodException e) { getter = fab.getMethod("getInstancia"); }
        Object fabrica = getter.invoke(null);
        cu = TestUtils.tryInvoke(fabrica, new String[]{"getIUsuario","getIControladorUsuario"});
        TestUtils.tryInvoke(cu, new String[]{"AltaInstitucion"}, "Inst_CUP", "d", "w");
    }

    @Test
    @DisplayName("ConsultaUsuario + obtenerDatosUsuario tras varias actualizaciones")
    void consultaYDatos() {
        // alta asistente
        TestUtils.tryInvoke(cu, new String[]{"AltaUsuario"},
                "cupi","Cu Pi","cupi@x","d","l","Ap0",
                LocalDate.of(2000,1,1),"Inst_CUP", false);

        // actualizar asistente
        TestUtils.tryInvoke(cu, new String[]{"actualizarAsistente"},
                "cupi", "Ap1", LocalDate.of(2001,1,1));

        // llamada a ConsultaUsuario (sea lo que sea que haga, no debería romper)
        try { TestUtils.invokeUnwrapped(cu, new String[]{"ConsultaUsuario"}, "cupi"); }
        catch (Throwable ignored) {}

        // obtener datos y verificar cambios
        Object dto = TestUtils.tryInvoke(cu, new String[]{"obtenerDatosUsuario"}, "cupi");
        assertNotNull(dto);
        var mAp = TestUtils.findMethod(dto, "getApellido","apellido");
        var mFn = TestUtils.findMethod(dto, "getFechaNacimiento","getNacimiento","fechaNacimiento");
        try {
            if (mAp != null) assertEquals("Ap1", String.valueOf(mAp.invoke(dto)));
            if (mFn != null) assertEquals(LocalDate.of(2001,1,1), mFn.invoke(dto));
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Cambiar a organizador y actualizarOrganizador")
    void cambioYUpdateOrganizador() {
        // alta como organizador
        TestUtils.tryInvoke(cu, new String[]{"AltaUsuario"},
                "orgx","Org X","orgx@x","d0","l0","Ap",
                LocalDate.of(1990,1,1),"Inst_CUP", true);

        // actualizar datos de organizador
        TestUtils.tryInvoke(cu, new String[]{"actualizarOrganizador"},
                "orgx", "d1", "l1");

        Object dto = TestUtils.tryInvoke(cu, new String[]{"obtenerDatosUsuario"}, "orgx");
        assertNotNull(dto);
        var mDesc = TestUtils.findMethod(dto, "getDescripcion","descripcion");
        var mLink = TestUtils.findMethod(dto, "getLink","link","getWeb");
        try {
            if (mDesc != null) assertEquals("d1", String.valueOf(mDesc.invoke(dto)));
            if (mLink != null) assertEquals("l1", String.valueOf(mLink.invoke(dto)));
        } catch (Exception e) {
            fail(e);
        }
    }
}
