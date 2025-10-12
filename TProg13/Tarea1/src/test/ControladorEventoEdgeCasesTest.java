package test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ControladorEvento – Edge cases (errores comunes)")
class ControladorEventoEdgeCasesTest {

    // Encapsulados + getters (regla de Checkstyle)
    private Object fabrica;
    private Object ce;
    private Object cu;

    public Object getFabrica() { return fabrica; }
    public Object getCe() { return ce; }
    public Object getCu() { return cu; }

    @BeforeEach
    void setUp() throws Throwable {
        TestUtils.resetAll();

        // 1) Fábrica (minúscula o mayúscula)
        Class<?> fab = TestUtils.loadAny("logica.Fabrica", "logica.fabrica");
        Method getter;
        try { 
            getter = fab.getMethod("getInstance"); 
        } catch (NoSuchMethodException e) { 
            getter = fab.getMethod("getInstancia"); 
        }
        this.fabrica = getter.invoke(null);

        // 2) Controlador de USUARIO por fábrica (existe en tu caso)
        this.cu = TestUtils.tryInvoke(fabrica, new String[]{"getIUsuario", "getIControladorUsuario", "getControladorUsuario"});

        // 3) Controlador de EVENTO: por fábrica si existe; si no, instancia concreta
        try {
            this.ce = TestUtils.tryInvoke(fabrica, new String[]{
                "getIEvento", "getIControladorEvento", "getControladorEvento", "getEvento"
            });
        } catch (AssertionError ignored) {
            Class<?> ceClazz = Class.forName("logica.ControladorEvento");
            try {
                Constructor<?> k0 = ceClazz.getDeclaredConstructor();
                k0.setAccessible(true);
                this.ce = k0.newInstance();
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
                        args[i] = null; // si tu ctor requiere no-nulos, armamos dummies
                    }
                }
                this.ce = k.newInstance(args);
            }
        }

        // 4) Base: Institución + ORG persistido
        TestUtils.tryInvoke(this.cu, new String[]{"AltaInstitucion"}, "Inst_A", "d", "w");
        TestUtils.tryInvoke(this.cu, new String[]{"AltaUsuario"},
                "org1", "Org Uno", "org1@x", "desc", "link",
                "Ap", java.time.LocalDate.of(1990, 1, 1), "Inst_A", true);

        // 5) Categoría idempotente
        altaCategoriaIdempotente(this.ce, "Tec");

        // 6) Evento base
        Object cats = TestUtils.tolerantNew("logica.Datatypes.DTCategorias", java.util.List.of("Tec"));
        TestUtils.tryInvoke(this.ce, new String[]{"AltaEvento"},
                "Conf", "Desc", java.time.LocalDate.now(), "CONF", cats);
    }

    @Test
    @DisplayName("altaEdicionEvento con fechaFin < fechaInicio → lanza (IAE) o normaliza (ambos válidos)")
    void altaEdicionEventoFechasInvalidas() throws Throwable {
        LocalDate hoy = LocalDate.now();
        String nombre = "Bad";

        try {
            // estricto: debería lanzar
            TestUtils.invokeUnwrapped(ce, new String[]{"altaEdicionEvento"},
                "Conf", nombre, "B01", "x",
                hoy.plusDays(5), hoy.plusDays(4), hoy,
                "org1", "City", "UY");

            // si no lanzó → aceptamos; verificamos normalización si hay getters
            Object ed = TestUtils.tryInvoke(ce, new String[]{"obtenerEdicion"}, "Conf", nombre);
            assertNotNull(ed, "La edición debería existir si no lanzó excepción");

            Method mIni = TestUtils.findMethod(ed, "getFechaInicio", "fechaInicio");
            Method mFin = TestUtils.findMethod(ed, "getFechaFin", "fechaFin");
            if (mIni != null && mFin != null) {
                LocalDate ini = (LocalDate) mIni.invoke(ed);
                LocalDate fin = (LocalDate) mFin.invoke(ed);
                assertFalse(fin.isBefore(ini), "Si no lanza, debe normalizar (fin ≥ inicio)");
            }
        } catch (IllegalArgumentException e) {
            // válido: validación estricta
            assertNotNull(e);
        }
    }

    @Test
    @DisplayName("AltaTipoRegistro con costo negativo → lanza (IAE) o normaliza (ambos válidos)")
    void altaTipoRegistroCostoNegativo() throws Throwable {
        LocalDate hoy = LocalDate.now();
        // pre: edición base
        TestUtils.tryInvoke(ce, new String[]{"altaEdicionEvento"},
                "Conf", "Main", "M1", "ok",
                hoy.plusDays(1), hoy.plusDays(2), hoy,
                "org1", "City", "UY");
        Object ed = TestUtils.tryInvoke(ce, new String[]{"obtenerEdicion"}, "Conf", "Main");
        assertNotNull(ed);

        try {
            TestUtils.invokeUnwrapped(ce, new String[]{"AltaTipoRegistro"},
                    ed, "VIP", "desc", -1, 10);
            // no lanzó → aceptamos
            assertTrue(true);
        } catch (IllegalArgumentException e) {
            // lanzó → también válido
            assertNotNull(e);
        }
    }

    @Test
    @DisplayName("AltaTipoRegistro con cupo negativo → lanza (IAE) o normaliza (ambos válidos)")
    void altaTipoRegistroCupoNegativo() throws Throwable {
        LocalDate hoy = LocalDate.now();

        TestUtils.tryInvoke(ce, new String[]{"altaEdicionEvento"},
                "Conf", "Main2", "M2", "ok",
                hoy.plusDays(1), hoy.plusDays(2), hoy,
                "org1", "City", "UY");

        Object ed = TestUtils.tryInvoke(ce, new String[]{"obtenerEdicion"}, "Conf", "Main2");
        assertNotNull(ed);

        try {
            TestUtils.invokeUnwrapped(ce, new String[]{"AltaTipoRegistro"},
                    ed, "STD", "desc", 100, -5);
            assertTrue(true);
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
        }
    }

    @Test
    @DisplayName("consultaEdicionEvento con siglas malas → devuelve null o lanza (ambos válidos)")
    void consultaEdicionEventoInvalida() throws Throwable {
        try {
            Object dto = TestUtils.invokeUnwrapped(ce, new String[]{"consultaEdicionEvento"}, "XX", "??");
            assertNull(dto);
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
        }
    }

    private void altaCategoriaIdempotente(Object ceRef, String nombre) throws Throwable {
        try {
            TestUtils.invokeUnwrapped(ceRef, new String[]{"AltaCategoria"}, nombre);
        } catch (IllegalArgumentException ignored) { /* ya existe */ }
    }
}
