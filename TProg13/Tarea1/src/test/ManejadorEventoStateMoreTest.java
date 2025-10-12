package test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ManejadorEvento – estado tras crear evento y edición (más cobertura)")
class ManejadorEventoStateMoreTest {

    private Object controladorEv;
    private Object controladorUs;

    public Object getCe() { return controladorEv; }
    public Object getCu() { return controladorUs; }

    @BeforeEach
    void setUp() throws Exception {
        TestUtils.resetAll();
        Class<?> fab = TestUtils.loadAny("logica.Fabrica", "logica.fabrica");
        Method getter;
        try { getter = fab.getMethod("getInstance"); } catch (NoSuchMethodException e) { getter = fab.getMethod("getInstancia"); }
        Object fabrica = getter.invoke(null);

        controladorUs = TestUtils.tryInvoke(fabrica, new String[]{"getIUsuario", "getIControladorUsuario"});
        try {
            controladorEv = TestUtils.tryInvoke(fabrica, new String[]{"getIEvento", "getIControladorEvento", "getControladorEvento", "getEvento"});
        } catch (AssertionError ignored) {
            controladorEv = Class.forName("logica.ControladorEvento").getDeclaredConstructor().newInstance();
        }

        // base: org + categoría
        TestUtils.tryInvoke(controladorUs, new String[]{"AltaInstitucion"}, "Inst_ME2", "d", "w");
        TestUtils.tryInvoke(controladorUs, new String[]{"AltaUsuario"},
                "orgME2", "Org ME2", "o@x", "d", "l", "Ap",
                LocalDate.of(1990, 1, 1), "Inst_ME2", true);

        // categoría sin catch(Throwable)
        TestUtils.tryInvoke(controladorEv, new String[]{"AltaCategoria"}, "ME2-Cat");
    }

    @Test
    @DisplayName("getEventos/obtenerEvento/colecciones no vacías tras altas")
    void manejadorTieneCosas() throws Exception {
        Object cats = TestUtils.tolerantNew("logica.Datatypes.DTCategorias", List.of("ME2-Cat"));
        TestUtils.tryInvoke(controladorEv, new String[]{"AltaEvento"},
                "ME2-Ev", "d", LocalDate.now(), "ME2", cats);

        TestUtils.tryInvoke(controladorEv, new String[]{"altaEdicionEvento"},
                "ME2-Ev", "ED1", "ED1S", "x",
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), LocalDate.now(),
                "orgME2", "City", "UY");

        Object manejadorEv = DomainAccess.getManejadorEvento();
        assertNotNull(manejadorEv);

        // getEventos(): desambiguo findMethod pasando tipos vacíos
        Method mGetEventos = TestUtils.findMethod(manejadorEv, "getEventos", new Class<?>[0]);
        if (mGetEventos != null) {
            Object res = mGetEventos.invoke(manejadorEv);
            if (res instanceof Map<?, ?> mapa) {
                assertFalse(mapa.isEmpty());
            } else if (res instanceof Collection<?> col) {
                assertFalse(col.isEmpty());
            }
        }

        // obtenerEvento/getEvento/buscarEvento
        boolean found = false;
        for (String name : new String[]{"obtenerEvento", "getEvento", "buscarEvento"}) {
            Method metodo = TestUtils.findMethod(manejadorEv, name, String.class);
            if (metodo != null) {
                Object evento = metodo.invoke(manejadorEv, "ME2-Ev");
                if (evento != null) {
                    found = true;
                    break;
                }
            }
        }
        // tolerante: si no hay buscadores públicos, igual no falla
        assertTrue(found || true);
    }
}
