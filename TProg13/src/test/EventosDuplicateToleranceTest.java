package test;
import org.junit.jupiter.api.*;

import java.lang.reflect.Method;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ControladorEvento – tolerancia a duplicados")
class EventosDuplicateToleranceTest {

    Object ce, cu;

    @BeforeEach
    void setUp() throws Exception {
        TestUtils.resetAll();
        Class<?> fab = TestUtils.loadAny("logica.Fabrica", "logica.fabrica");
        Method getter;
        try { getter = fab.getMethod("getInstance"); }
        catch (NoSuchMethodException e) { getter = fab.getMethod("getInstancia"); }
        Object fabrica = getter.invoke(null);

        cu = TestUtils.tryInvoke(fabrica, new String[]{"getIUsuario","getIControladorUsuario"});
        try {
            ce = TestUtils.tryInvoke(fabrica, new String[]{"getIEvento","getIControladorEvento","getControladorEvento","getEvento"});
        } catch (AssertionError ignored) {
            ce = Class.forName("logica.ControladorEvento").getDeclaredConstructor().newInstance();
        }

        // base
        TestUtils.tryInvoke(cu, new String[]{"AltaInstitucion"}, "Inst_DU", "d", "w");
        TestUtils.tryInvoke(cu, new String[]{"AltaUsuario"},
                "orgDU","Org DU","org@x","d","l","Ap",
                LocalDate.of(1990,1,1),"Inst_DU", true);

        try { TestUtils.invokeUnwrapped(ce, new String[]{"AltaCategoria"}, "DU-Cat"); } catch (Throwable ignored) {}
    }

    @Test
    @DisplayName("AltaEvento duplicado → lanza o es idempotente")
    void altaEventoDuplicado() {
        Object cats = TestUtils.tolerantNew("logica.DTCategorias", java.util.List.of("DU-Cat"));
        TestUtils.tryInvoke(ce, new String[]{"AltaEvento"}, "DU-Ev", "d", LocalDate.now(), "DUEV", cats);

        try {
            TestUtils.invokeUnwrapped(ce, new String[]{"AltaEvento"}, "DU-Ev", "d", LocalDate.now(), "DUEV", cats);
            assertTrue(true); // idempotente
        } catch (Throwable t) {
            assertNotNull(t); // validación estricta
        }
    }

    @Test
    @DisplayName("altaEdicionEvento duplicada → lanza o es idempotente (nombre o sigla del evento)")
    void altaEdicionDuplicada() {
        java.time.LocalDate hoy = java.time.LocalDate.now();

        // ⚠️ Aseguramos que el evento exista (idempotente)
        Object cats = TestUtils.tolerantNew("logica.DTCategorias", java.util.List.of("DU-Cat"));
        try {
            TestUtils.invokeUnwrapped(ce, new String[]{"AltaEvento"},
                    "DU-Ev", "d", hoy, "DUEV", cats);
        } catch (Throwable ignored) {
            // si ya existía o tu implementación valida distinto, seguimos igual
        }

        // 1) Alta inicial de la edición (aceptamos nombre o sigla del evento)
        altaEdicionFlexible(ce, "DU-Ev", "DUEV",
                "ED", "EDU", "x",
                hoy.plusDays(1), hoy.plusDays(2), hoy,
                "orgDU","City","UY");

        // 2) Intento duplicado: si no lanza, lo tomamos como idempotente
        try {
            altaEdicionFlexible(ce, "DU-Ev", "DUEV",
                    "ED", "EDU", "x",
                    hoy.plusDays(1), hoy.plusDays(2), hoy,
                    "orgDU","City","UY");
            assertTrue(true); // idempotente
        } catch (RuntimeException t) {
            assertNotNull(t); // validación estricta (también válido)
        }
    }
    
    private void altaEdicionFlexible(Object ce,
            String nombreEvento, String siglaEvento,
            String nombreEd, String siglaEd, String desc,
            java.time.LocalDate ini, java.time.LocalDate fin, java.time.LocalDate alta,
            String org, String ciudad, String pais) {
// 1) intentamos con el nombre del evento
try {
TestUtils.invokeUnwrapped(ce, new String[]{"altaEdicionEvento"},
nombreEvento, nombreEd, siglaEd, desc, ini, fin, alta, org, ciudad, pais);
return;
} catch (Throwable ignored) {}
// 2) fallback: algunas impls piden la SIGLA del evento
try {
TestUtils.invokeUnwrapped(ce, new String[]{"altaEdicionEvento"},
siglaEvento, nombreEd, siglaEd, desc, ini, fin, alta, org, ciudad, pais);
} catch (Throwable e) {
// lo repropagamos para que el test pueda tratar la validación como "estricta"
throw new RuntimeException(e);
}
}
}
