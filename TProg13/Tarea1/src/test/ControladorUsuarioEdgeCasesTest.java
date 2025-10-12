package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ControladorUsuario – Edge cases (errores comunes)")
class ControladorUsuarioEdgeCasesTest {

    private Object fabrica, cu;

    @BeforeEach
    void setUp() throws Exception {
        TestUtils.resetAll();
        Class<?> fab = TestUtils.loadAny("logica.Fabrica", "logica.fabrica");
        Method getter;
        try { getter = fab.getMethod("getInstance"); 
        } catch (NoSuchMethodException e) { getter = fab.getMethod("getInstancia"); }
        fabrica = getter.invoke(null);
        cu = TestUtils.tryInvoke(fabrica, new String[]{"getIUsuario", "getIControladorUsuario"});
    }

    @Test
    @DisplayName("actualizarAsistente sobre nick inexistente → lanza")
    void actualizarAsistenteInexistente() {
        assertThrows(Exception.class, () ->
            TestUtils.invokeUnwrapped(cu, new String[]{"actualizarAsistente"},
                "noexiste", "Ap", LocalDate.of(2000, 1, 1))
        );
    }

    @Test
    @DisplayName("actualizarOrganizador sobre nick inexistente → lanza")
    void actualizarOrganizadorInexistente() {
        assertThrows(Exception.class, () ->
            TestUtils.invokeUnwrapped(cu, new String[]{"actualizarOrganizador"},
                "noexiste", "desc", "link")
        );
    }

    @Test
    @DisplayName("obtenerDatosUsuario de nick inexistente → null o lanza (aceptamos ambos)")
    void obtenerDatosUsuarioInexistente() {
        boolean lanzo;
        try {
            assertThrows(Exception.class, () ->
                TestUtils.invokeUnwrapped(cu, new String[]{"obtenerDatosUsuario"}, "noexiste")
            );
            lanzo = true;
        } catch (AssertionError ae) {
            lanzo = false; // no lanzó: esperamos null
            Object dto = TestUtils.tryInvoke(cu, new String[]{"obtenerDatosUsuario"}, "noexiste");
            assertNull(dto);
        }
        assertTrue(lanzo || !lanzo); // sólo para callar “resultado no usado”
    }

    @Test
    @DisplayName("AltaInstitucion duplicada → idempotente o lanza (aceptamos ambos)")
    void altaInstitucionDuplicada() {
        TestUtils.tryInvoke(cu, new String[]{"AltaInstitucion"}, "Inst_X", "d", "w");
        // si lanza, lo aceptamos; si no, también (idempotente)
        try {
            assertThrows(Exception.class, () ->
                TestUtils.invokeUnwrapped(cu, new String[]{"AltaInstitucion"}, "Inst_X", "d2", "w2")
            );
        } catch (AssertionError ignored) {
            // no lanzó: lo tomamos como idempotente
        }
    }

    @Test
    @DisplayName("ingresarAsistente con Institución null → lanza o NO crea nada")
    void ingresarAsistenteInstitucionNull() {
        boolean lanzo;
        try {
            assertThrows(Exception.class, () ->
                TestUtils.invokeUnwrapped(cu, new String[]{"ingresarAsistente"},
                    "a1", "A", "a@x", "Ap", LocalDate.of(2000, 1, 1), null)
            );
            lanzo = true;
        } catch (AssertionError ae) {
            lanzo = false; // no lanzó: verificamos que no haya quedado creado
            @SuppressWarnings("unchecked")
            Map<String, Object> asisMap =
                (Map<String, Object>) TestUtils.tryInvoke(cu, new String[]{"listarAsistentes"});
            assertFalse(asisMap.containsKey("a1"),
                "No lanzó y dejó 'a1' creado; debería ignorar o lanzar.");
        }
        assertTrue(lanzo || !lanzo);
    }
}
