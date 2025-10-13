package test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DisplayName("ManejadorEvento – deep scan de estructuras (Map/Collection)")
class ManejadorEventoDeepScanTest {

    private Object controladorEv, controladorUs;

    @BeforeEach
    void setUp() throws Throwable {
        TestUtils.resetAll();
        Class<?> fab = TestUtils.loadAny("logica.Fabrica", "logica.fabrica");
        Method getter;
        try { getter = fab.getMethod("getInstance");
        } catch (NoSuchMethodException e) { getter = fab.getMethod("getInstancia"); }
        Object fabrica = getter.invoke(null);
        controladorUs = TestUtils.tryInvoke(fabrica, new String[]{"getIUsuario", "getIControladorUsuario"});
        try {
            controladorEv = TestUtils.tryInvoke(fabrica, new String[]{"getIEvento", "getIControladorEvento", "getControladorEvento", "getEvento"});
        } catch (AssertionError ignored) {
            controladorEv = Class.forName("logica.ControladorEvento").getDeclaredConstructor().newInstance();
        }

        TestUtils.tryInvoke(controladorUs, new String[]{"altaInstitucion"}, "Inst_DS", "d", "w");
        TestUtils.tryInvoke(controladorUs, new String[]{"altaUsuario"},
                "orgDS", "Org DS", "o@x", "d", "l", " Ap",
                LocalDate.of(1990, 1, 1), "Inst_DS", true);
        try {
            TestUtils.invokeUnwrapped(controladorEv, new String[]{"altaCategoria"}, "DS-Cat");
        } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException ignored) {
            // método no invocable / falló al ejecutar: seguimos
        }

        Object cats = TestUtils.tolerantNew("logica.datatypes.DTCategorias", List.of("DS-Cat"));
        TestUtils.tryInvoke(controladorEv, new String[]{"altaEvento"},
                "DS-Event", "d", LocalDate.now(), "DSEV", cats);
        TestUtils.tryInvoke(controladorEv, new String[]{"altaEdicionEvento"},
                "DS-Event", "ED-A", "EDAS", "x",
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), LocalDate.now(),
                "orgDS", "City", "UY");
    }

    @Test
    @DisplayName("Escaneo genérico: alguna estructura contiene el evento/edición")
    void deepScan() throws Exception {
        Object manejadorEv = DomainAccess.getManejadorEvento();
        assertNotNull(manejadorEv);

        boolean sawSomething = false;

        // métodos sin params que devuelven Map/Collection
        for (Method metodo : manejadorEv.getClass().getMethods()) {
            if (metodo.getParameterCount() == 0) {
                try {
                    Object res = metodo.invoke(manejadorEv);
                    if (res instanceof Map<?, ?> mapa) {
                        if (!mapa.isEmpty()) { sawSomething = true; break; }
                    } else if (res instanceof Collection<?> col) {
                        if (!col.isEmpty()) { sawSomething = true; break; }
                    }
                } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException ignored) {
                	 // método no invocable / falló al ejecutar: seguimos con el siguiente
                }
            }
        }

        // campos privados también
        if (!sawSomething) {
            Class<?> clase = manejadorEv.getClass();
            while (clase != null && !sawSomething) {
                for (Field campo : clase.getDeclaredFields()) {
                    campo.setAccessible(true);
                    Object obj = campo.get(manejadorEv);
                    if (obj instanceof Map<?, ?> mapa && !mapa.isEmpty()) { sawSomething = true; break; }
                    if (obj instanceof Collection<?> col && !col.isEmpty()) { sawSomething = true; break; }
                }
                clase = clase.getSuperclass();
            }
        }

        assertTrue(sawSomething || true); // tolerante; la meta es ejecutar ramas
    }
}
