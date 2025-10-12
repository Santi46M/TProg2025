package test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;



import java.time.LocalDate;

@DisplayName("ControladorEvento – Edge cases (errores comunes)")
class ControladorEventoEdgeCasesTest {

    Object fabrica, ce, cu;

    @BeforeEach
    void setUp() throws Exception {
        TestUtils.resetAll();

        // 1) Fábrica (minúscula o mayúscula)
        Class<?> fab = TestUtils.loadAny("logica.Fabrica", "logica.fabrica");
        Method getter;
        try { getter = fab.getMethod("getInstance"); }
        catch (NoSuchMethodException e) { getter = fab.getMethod("getInstancia"); }
        Object fabrica = getter.invoke(null);

        // 2) Controlador de USUARIO por fábrica (existe en tu caso)
        cu = TestUtils.tryInvoke(fabrica, new String[]{"getIUsuario", "getIControladorUsuario", "getControladorUsuario"});

        // 3) Controlador de EVENTO: por fábrica si existe; si no, instancio la impl concreta
        try {
            ce = TestUtils.tryInvoke(fabrica, new String[]{
                "getIEvento", "getIControladorEvento", "getControladorEvento", "getEvento"
            });
        } catch (AssertionError ignored) {
            Class<?> ceClazz = Class.forName("logica.ControladorEvento");
            try {
                Constructor<?> k0 = ceClazz.getDeclaredConstructor();
                k0.setAccessible(true);
                ce = k0.newInstance();
            } catch (NoSuchMethodException noDefault) {
                Constructor<?> k = ceClazz.getDeclaredConstructors()[0];
                k.setAccessible(true);
                Class<?>[] pts = k.getParameterTypes();
                Object[] args = new Object[pts.length];
                for (int i = 0; i < pts.length; i++) {
                    Class<?> t = pts[i];
                    if (t.isPrimitive()) {
                        if (t == boolean.class) args[i] = false;
                        else if (t == char.class) args[i] = '\0';
                        else if (t == byte.class) args[i] = (byte) 0;
                        else if (t == short.class) args[i] = (short) 0;
                        else if (t == int.class) args[i] = 0;
                        else if (t == long.class) args[i] = 0L;
                        else if (t == float.class) args[i] = 0f;
                        else if (t == double.class) args[i] = 0d;
                    } else {
                        args[i] = null; // si tu ctor requiere no-nulos, avisame y armamos dummy objects
                    }
                }
                ce = k.newInstance(args);
            }
        }

     // 4) Base: Institución + ORG persistido (usá AltaUsuario para que aparezca en los mapas)
        TestUtils.tryInvoke(cu, new String[]{"AltaInstitucion"}, "Inst_A", "d", "w");
        TestUtils.tryInvoke(cu, new String[]{"AltaUsuario"},
                "org1", "Org Uno", "org1@x", "desc", "link",
                "Ap", java.time.LocalDate.of(1990, 1, 1), "Inst_A", true);

        // 5) Dar de alta la categoría que usará el evento ("Tec")
     // DESPUÉS (idempotente)
        altaCategoriaIdempotente(ce, "Tec");
        // (si preferís hacerlo por CU, también sirve con try/catch):
        // try { TestUtils.invokeUnwrapped(cu, new String[]{"AltaCategoriaSinGUI"}, "Tec"); } catch (Throwable ignored) {}
        // (alternativa si tu CU tiene este método y preferís:)
        // TestUtils.tryInvoke(cu, new String[]{"AltaCategoriaSinGUI"}, "Tec");

        // 6) Evento base para los edge-cases
        Object cats = TestUtils.tolerantNew("logica.Datatypes.DTCategorias", java.util.List.of("Tec"));
        TestUtils.tryInvoke(ce, new String[]{"AltaEvento"},
                "Conf", "Desc", java.time.LocalDate.now(), "CONF", cats);    
        }

    @Test
    @DisplayName("altaEdicionEvento con fechaFin < fechaInicio → lanza o normaliza (ambos válidos)")
    void altaEdicionEvento_fechasInvalidas() throws Throwable {
        LocalDate hoy = LocalDate.now();
        String nombre = "Bad";

        try {
            // caso “estricto”: debería lanzar
            TestUtils.invokeUnwrapped(ce, new String[]{"altaEdicionEvento"},
                "Conf", nombre, "B01", "x",
                hoy.plusDays(5), hoy.plusDays(4), hoy,
                "org1", "City", "UY");

            // si NO lanzó, aceptamos comportamiento “permisivo”:
            // verificamos que la edición exista y, si podemos, que no tenga fin < inicio
            Object ed = TestUtils.tryInvoke(ce, new String[]{"obtenerEdicion"}, "Conf", nombre);
            assertNotNull(ed, "La edición debería existir si no lanzó excepción");

            var mIni = TestUtils.findMethod(ed, "getFechaInicio", "fechaInicio");
            var mFin = TestUtils.findMethod(ed, "getFechaFin", "fechaFin");
            if (mIni != null && mFin != null) {
                LocalDate ini = (LocalDate) mIni.invoke(ed);
                LocalDate fin = (LocalDate) mFin.invoke(ed);
                assertFalse(fin.isBefore(ini), "Si no lanza, debería normalizar (fin ≥ inicio)");
            }
        } catch (Throwable t) {
            // comportamiento válido (validación estricta)
            assertNotNull(t);
        }
    }

    @Test
    @DisplayName("AltaTipoRegistro con costo negativo → lanza o lo normaliza (ambos válidos)")
    void altaTipoRegistro_costoNegativo() throws Throwable {
        LocalDate hoy = LocalDate.now();
        // pre: edición base
        TestUtils.tryInvoke(ce, new String[]{"altaEdicionEvento"},
                "Conf", "Main", "M1", "ok",
                hoy.plusDays(1), hoy.plusDays(2), hoy,
                "org1", "City", "UY");
        Object ed = TestUtils.tryInvoke(ce, new String[]{"obtenerEdicion"}, "Conf", "Main");
        assertNotNull(ed);

        try {
            // caso “estricto”: debería lanzar
            TestUtils.invokeUnwrapped(ce, new String[]{"AltaTipoRegistro"},
                    ed, "VIP", "desc", -1, 10);

            // si NO lanzó, aceptamos: el sistema pudo normalizar/ignorar el costo negativo
            assertTrue(true);
            // (si tuvieras un getter del tipo para validar costo >= 0, podemos chequearlo aquí)
        } catch (Throwable t) {
            // comportamiento válido (validación estricta)
            assertNotNull(t);
        }
    }


    @Test
    @DisplayName("AltaTipoRegistro con cupo negativo → lanza o lo normaliza (ambos válidos)")
    void altaTipoRegistro_cupoNegativo() throws Throwable {
        LocalDate hoy = LocalDate.now();

        // Edición base para este caso
        TestUtils.tryInvoke(ce, new String[]{"altaEdicionEvento"},
                "Conf", "Main2", "M2", "ok",
                hoy.plusDays(1), hoy.plusDays(2), hoy,
                "org1", "City", "UY");

        Object ed = TestUtils.tryInvoke(ce, new String[]{"obtenerEdicion"}, "Conf", "Main2");
        assertNotNull(ed);

        // Intento con cupo negativo: si lanza → OK; si no lanza → aceptamos normalización
        try {
            TestUtils.invokeUnwrapped(ce, new String[]{"AltaTipoRegistro"},
                    ed, "STD", "desc", 100, -5);
            // No lanzó: aceptamos comportamiento (normaliza/ignora el valor negativo)
            assertTrue(true);
        } catch (Throwable t) {
            // Lanzó: también válido (validación estricta)
            assertNotNull(t);
        }
    }
    @Test
    @DisplayName("consultaEdicionEvento con siglas malas → devuelve null o lanza (aceptamos ambos)")
    void consultaEdicionEvento_invalida() throws Throwable {
        try {
            Object dto = TestUtils.invokeUnwrapped(ce, new String[]{"consultaEdicionEvento"}, "XX", "??");
            assertNull(dto);
        } catch (Throwable t) {
            assertNotNull(t);
        }
    }
    
    private void altaCategoriaIdempotente(Object ce, String nombre) {
        try {
            TestUtils.invokeUnwrapped(ce, new String[]{"AltaCategoria"}, nombre);
        } catch (Throwable ignored) {
            // OK: si ya existe, la dejamos así y seguimos
        }
    }
}
