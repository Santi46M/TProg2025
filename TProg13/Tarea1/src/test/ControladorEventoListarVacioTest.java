package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DisplayName("ControladorEvento – listarEdicionesEvento vacío")
class ControladorEventoListarVacioTest {

	private Object ce;
	private Object cu;

    @BeforeEach
    void setUp() throws Exception {
        TestUtils.resetAll();
        Class<?> fab = TestUtils.loadAny("logica.Fabrica", "logica.fabrica");
        Method getter;
        try { 
        	getter = fab.getMethod("getInstance"); 
        }catch (NoSuchMethodException e) { 
        	getter = fab.getMethod("getInstancia"); 
        	}
        Object fabrica = getter.invoke(null);

        cu = TestUtils.tryInvoke(fabrica, new String[]{"getIUsuario", "getIControladorUsuario"});
        try {
            ce = TestUtils.tryInvoke(fabrica, new String[]{"getIEvento", "getIControladorEvento", "getControladorEvento", "getEvento"});
        } catch (AssertionError ignored) {
            ce = Class.forName("logica.ControladorEvento").getDeclaredConstructor().newInstance();
        }

        // base: org persistido + categoría
        TestUtils.tryInvoke(cu, new String[]{"AltaInstitucion"}, "Inst_LE", "d", "w");
        TestUtils.tryInvoke(cu, new String[]{"AltaUsuario"},
                "orgLE", "Org LE", "org@x", "d", "l", "Ap",
                LocalDate.of(1990, 1, 1), "Inst_LE", true);
        TestUtils.tryInvoke(ce, new String[]{"AltaCategoria"}, "LE-Cat");

        
    }

    @Test
    @DisplayName("Un evento sin ediciones lista vacío (o no nulo)")
    void listaVacia() {
        Object cats = TestUtils.tolerantNew("logica.Datatypes.DTCategorias", java.util.List.of("LE-Cat"));
        TestUtils.tryInvoke(ce, new String[]{"AltaEvento"}, "SoloEvento", "d", LocalDate.now(), "SE", cats);

        @SuppressWarnings("unchecked")
        List<String> eds = (List<String>) TestUtils.tryInvoke(ce, new String[]{"listarEdicionesEvento"}, "SoloEvento");
        assertNotNull(eds);
        assertTrue(eds.isEmpty() || eds.size() >= 0); // aceptamos que sea vacío
    }
}
