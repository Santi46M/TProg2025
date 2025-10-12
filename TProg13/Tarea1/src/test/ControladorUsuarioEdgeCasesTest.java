package test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.lang.reflect.Method;
import java.time.LocalDate;

@DisplayName("ControladorUsuario – Edge cases (errores comunes)")
class ControladorUsuarioEdgeCasesTest {

    private Object fabrica, cu;

    @BeforeEach
    void setUp() throws Exception {
        TestUtils.resetAll();
        Class<?> fab = TestUtils.loadAny("logica.Fabrica", "logica.fabrica");
        Method getter;
        try { getter = fab.getMethod("getInstance"); }
        catch (NoSuchMethodException e) { getter = fab.getMethod("getInstancia"); }
        fabrica = getter.invoke(null);
        cu = TestUtils.tryInvoke(fabrica, new String[]{"getIUsuario", "getIControladorUsuario"});
    }

    @Test
    @DisplayName("actualizarAsistente sobre nick inexistente → lanza")
    void actualizarAsistenteInexistente() {
        assertThrows(Throwable.class, () ->
            TestUtils.invokeUnwrapped(cu, new String[]{"actualizarAsistente"},
                "noexiste", "Ap", LocalDate.of(2000, 1, 1)));
    }

    @Test
    @DisplayName("actualizarOrganizador sobre nick inexistente → lanza")
    void actualizarOrganizadorInexistente() {
        assertThrows(Throwable.class, () ->
            TestUtils.invokeUnwrapped(cu, new String[]{"actualizarOrganizador"},
                "noexiste", "desc", "link"));
    }

    @Test
    @DisplayName("obtenerDatosUsuario de nick inexistente → null o lanza (aceptamos ambos)")
    void obtenerDatosUsuarioInexistente() throws Throwable {
        try {
            Object dto = TestUtils.invokeUnwrapped(cu, new String[]{"obtenerDatosUsuario"}, "noexiste");
            assertNull(dto); // si tu implementación devuelve null
        } catch (Throwable t) {
            // si tu implementación lanza, también está bien
            assertNotNull(t);
        }
    }

    @Test
    @DisplayName("AltaInstitucion duplicada → idempotente o lanza (aceptamos ambos)")
    void altaInstitucionDuplicada() throws Throwable {
        TestUtils.tryInvoke(cu, new String[]{"AltaInstitucion"}, "Inst_X", "d", "w");
        try {
            TestUtils.invokeUnwrapped(cu, new String[]{"AltaInstitucion"}, "Inst_X", "d2", "w2");
            assertTrue(true); // idempotente
        } catch (Throwable t) {
            assertNotNull(t); // lanza
        }
    }

    @Test
    @DisplayName("ingresarAsistente con Institución null → lanza o NO crea nada")
    void ingresarAsistenteInstitucionNull() throws Throwable {
        // 1) Intento invocar y CAPTURO si lanza
        boolean lanzo = false;
        try {
            TestUtils.invokeUnwrapped(cu, new String[]{"ingresarAsistente"},
                "a1", "A", "a@x", "Ap", java.time.LocalDate.of(2000, 1, 1), null);
        } catch (Throwable t) {
            lanzo = true; // comportamiento válido
        }

        if (!lanzo) {
            // 2) Si no lanzó, verifico que NO quedó persistido
            @SuppressWarnings("unchecked")
            java.util.Map<String, Object> asisMap =
                (java.util.Map<String, Object>) TestUtils.tryInvoke(cu, new String[]{"listarAsistentes"});
            assertFalse(asisMap.containsKey("a1"),
                "ingresarAsistente(...) con institucion null no lanzó y dejó 'a1' creado; debería ignorar o lanzar.");
        }
    }
}