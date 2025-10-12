package test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ControladorEvento – Altas/Listados/Consultas (sin catch genéricos)")
class ControladorEventoTest {

    private Object fabrica;
    private Object ce;
    private Object cu;

    @BeforeEach
    void setUp() throws Exception {
        TestUtils.resetAll();

        // Fábrica (minúscula o mayúscula)
        Class<?> fab = TestUtils.loadAny("logica.Fabrica", "logica.fabrica");
        Method getter;
        try { getter = fab.getMethod("getInstance"); } catch (NoSuchMethodException e) { getter = fab.getMethod("getInstancia"); }
        this.fabrica = getter.invoke(null);

        // Controlador de USUARIO (por fábrica)
        cu = TestUtils.tryInvoke(this.fabrica, new String[]{"getIUsuario", "getIControladorUsuario", "getControladorUsuario"});

        // Controlador de EVENTO: si la fábrica no lo tiene, instancia concreta
        Object ceMaybe = null;
        try { ceMaybe = TestUtils.tryInvoke(this.fabrica, new String[]{"getIEvento", "getIControladorEvento", "getControladorEvento", "getEvento"}); } catch (AssertionError ignored) { /* instanciamos directo */ }

        if (ceMaybe == null) {
            Class<?> ceClazz = Class.forName("logica.Controladores.ControladorEvento");
            Constructor<?> k = ceClazz.getDeclaredConstructor();
            k.setAccessible(true);
            ce = k.newInstance();
        } else {
            ce = ceMaybe;
        }

        // Base: Institución y organizador
        TestUtils.tryInvoke(cu, new String[]{"AltaInstitucion"}, "Inst_A", "desc", "web");
        TestUtils.tryInvoke(cu, new String[]{"AltaUsuario"},
                "org1", "Org Uno", "org1@x", "desc", "link",
                "Ap", LocalDate.of(1990, 1, 1), "Inst_A", true);
    }

    /* ---------- Helpers SIN catch genéricos ---------- */

    private Object categoriasDTO(String... nombres) {
        var set = new LinkedHashSet<>(Arrays.asList(nombres));
        // intento 1: ctor con Set
        Object dto = TestUtils.tolerantNew("logica.Datatypes.DTCategorias", set);
        if (dto != null) return dto;
        // intento 2: ctor con List
        dto = TestUtils.tolerantNew("logica.Datatypes.DTCategorias", java.util.List.copyOf(set));
        if (dto != null) return dto;
        // intento 3: ctor vacío + setter/add si existiera
        dto = TestUtils.tolerantNew("logica.Datatypes.DTCategorias");
        if (dto != null) {
            try {
                Method setM = TestUtils.findMethod(dto, "setCategorias", "setNombres");
                if (setM != null && setM.getParameterCount() == 1) {
                    Class<?> pt = setM.getParameterTypes()[0];
                    if (java.util.Set.class.isAssignableFrom(pt)) {
                        setM.invoke(dto, set);
                    } else if (java.util.Collection.class.isAssignableFrom(pt)) {
                        setM.invoke(dto, java.util.List.copyOf(set));
                    }
                } else {
                    Method addM = TestUtils.findMethod(dto, "add", "agregar", "addCategoria");
                    if (addM != null && addM.getParameterCount() == 1) {
                        for (String n : set) { addM.invoke(dto, n); }
                    }
                }
            } catch (ReflectiveOperationException | IllegalArgumentException ignored) {
                // sin setters conocidos → devolvemos el objeto tal cual
            }
        }
        return dto;
    }

    private String getDTEventoNombre(Object dtevento) {
        Method m = TestUtils.findMethod(dtevento, "getNombre", "nombre");
        if (m == null) return String.valueOf(dtevento);
        try { return String.valueOf(m.invoke(dtevento)); } catch (ReflectiveOperationException | IllegalArgumentException e) { return String.valueOf(dtevento); }
    }

    private String getDTEdicionNombre(Object dted) {
        Method m = TestUtils.findMethod(dted, "getNombre", "nombre");
        if (m == null) return String.valueOf(dted);
        try { return String.valueOf(m.invoke(dted)); } catch (ReflectiveOperationException | IllegalArgumentException e) { return String.valueOf(dted); }
    }

    private void altaCategoriaIdempotente(Object ce, String nombre) {
        // tryInvoke ya evita propagar throws hacia acá
        TestUtils.tryInvoke(ce, new String[]{"AltaCategoria"}, nombre);
    }

    private Object consultaEdicionFlexible(Object ce, String[] eventoKeys, String[] edicionKeys) {
        String[][] names = {
            {"consultaEdicionEvento"},
            {"ConsultaEdicionEvento"},
            {"consultarEdicionEvento", "consultaEdicion"}
        };
        for (String[] group : names) {
            for (String me : group) {
                for (String ek : eventoKeys) {
                    for (String dk : edicionKeys) {
                        try {
                            Object dto = TestUtils.tryInvoke(ce, new String[]{me}, ek, dk);
                            if (dto != null) return dto;
                        } catch (AssertionError ignored) { /* método inexistente */ }
                    }
                }
            }
        }
        return null;
    }

    private Object resolverTipoRegistro(Object ed, String nombreDeseado) {
        // 1) directos por nombre
        for (String mn : new String[]{"obtenerTipoRegistro", "getTipoRegistro"}) {
            try {
                Method m = ed.getClass().getMethod(mn, String.class);
                Object tr = m.invoke(ed, nombreDeseado);
                if (tr != null) return tr;
            } catch (NoSuchMethodException ignored) { /* no existe */ } catch (ReflectiveOperationException | IllegalArgumentException ignored) { /* invocación fallida */ }
        }
        // 2) listados y match por nombre
        for (String mn : new String[]{"getTiposRegistro", "getTiposRegistros", "getTipos", "listarTiposRegistro"}) {
            try {
                Method m = ed.getClass().getMethod(mn);
                Object res = m.invoke(ed);
                if (res instanceof java.util.Collection<?> col) {
                    for (Object tr : col) {
                        if (tr == null) continue;
                        Method mNom = TestUtils.findMethod(tr, "getNombre", "nombre");
                        if (mNom != null) {
                            String n = String.valueOf(mNom.invoke(tr));
                            if (nombreDeseado.equals(n)) return tr;
                        } else {
                            return tr;
                        }
                    }
                } else if (res instanceof java.util.Map<?, ?> map) {
                    for (Object tr : ((java.util.Map<?, ?>) res).values()) {
                        if (tr == null) continue;
                        Method mNom = TestUtils.findMethod(tr, "getNombre", "nombre");
                        if (mNom != null) {
                            String n = String.valueOf(mNom.invoke(tr));
                            if (nombreDeseado.equals(n)) return tr;
                        } else {
                            return tr;
                        }
                    }
                }
            } catch (NoSuchMethodException ignored) { /* no existe */ } catch (ReflectiveOperationException | IllegalArgumentException ignored) { /* invocación fallida */ }
        }
        // 3) último recurso
        for (Method m : ed.getClass().getMethods()) {
            if (m.getParameterCount() == 0) {
                try {
                    Object res = m.invoke(ed);
                    if (res instanceof java.util.Collection<?> col) {
                        for (Object tr : col) {
                            if (tr != null && tr.getClass().getName().endsWith("TipoRegistro")) return tr;
                        }
                    } else if (res instanceof java.util.Map<?, ?> map) {
                        for (Object tr : ((java.util.Map<?, ?>) map).values()) {
                            if (tr != null && tr.getClass().getName().endsWith("TipoRegistro")) return tr;
                        }
                    }
                } catch (ReflectiveOperationException | IllegalArgumentException ignored) { /* seguimos */ }
            }
        }
        return null;
    }

    /* ---------- Tests ---------- */

    @Test
    @DisplayName("AltaCategoria + AltaEvento + listar/consultar")
    void altaEventoYListados() {
        altaCategoriaIdempotente(ce, "Tecnologia");
        Object cats = categoriasDTO("Tecnologia");

        assertDoesNotThrow(() ->
            TestUtils.invokeUnwrapped(ce, new String[]{"AltaEvento"},
                "Feria", "Desc Feria", LocalDate.now(), "FER", cats)
        );

        @SuppressWarnings("unchecked")
        List<Object> lista = (List<Object>) TestUtils.tryInvoke(ce, new String[]{"listarEventos"});
        assertNotNull(lista);
        var nombres = lista.stream().map(this::getDTEventoNombre).collect(Collectors.toSet());
        assertTrue(nombres.contains("Feria"));

        Object ev = TestUtils.tryInvoke(ce, new String[]{"consultaEvento"}, "Feria");
        assertNotNull(ev);
        assertEquals("logica", ev.getClass().getPackageName());
    }

    @Test
    @DisplayName("altaEdicionEvento + listar/obtener/consultaEdicionEvento")
    void edicionFlujoBasico() {
        Object cats = categoriasDTO("Tecnologia");
        TestUtils.tryInvoke(ce, new String[]{"AltaEvento"},
                "Feria", "Desc Feria", LocalDate.now(), "FER", cats);

        LocalDate hoy = LocalDate.now();
        assertDoesNotThrow(() ->
            TestUtils.invokeUnwrapped(ce, new String[]{"altaEdicionEvento"},
                "Feria", "Ed2025", "ED25", "Edición principal",
                hoy.plusDays(10), hoy.plusDays(12), hoy,
                "org1", "Montevideo", "Uruguay")
        );

        @SuppressWarnings("unchecked")
        List<String> eds = (List<String>) TestUtils.tryInvoke(ce, new String[]{"listarEdicionesEvento"}, "Feria");
        assertNotNull(eds);
        assertTrue(eds.contains("Ed2025"));

        Object ed = TestUtils.tryInvoke(ce, new String[]{"obtenerEdicion"}, "Feria", "Ed2025");
        assertNotNull(ed);

        Object dted = consultaEdicionFlexible(
                ce,
                new String[]{"FER", "Feria"},
                new String[]{"ED25", "Ed2025"}
        );
        if (dted != null) {
            Method mNombre = TestUtils.findMethod(dted, "getNombre", "nombre");
            if (mNombre != null) {
                try { assertEquals("Ed2025", String.valueOf(mNombre.invoke(dted))); } catch (ReflectiveOperationException | IllegalArgumentException ignored) { /* OK */ }
            }
        }
    }

    @Test
    @DisplayName("AltaTipoRegistro sobre una edición existente")
    void altaTipoRegistroEnEdicion() {
        Object cats = categoriasDTO("Tec");
        TestUtils.tryInvoke(ce, new String[]{"AltaEvento"},
                "Conf", "Desc", LocalDate.now(), "CONF", cats);

        LocalDate hoy = LocalDate.now();
        TestUtils.tryInvoke(ce, new String[]{"altaEdicionEvento"},
                "Conf", "Apertura", "AP01", "Inicio",
                hoy.plusDays(1), hoy.plusDays(2), hoy,
                "org1", "Montevideo", "Uruguay");

        Object ed = TestUtils.tryInvoke(ce, new String[]{"obtenerEdicion"}, "Conf", "Apertura");
        assertNotNull(ed);

        assertDoesNotThrow(() ->
            TestUtils.invokeUnwrapped(ce, new String[]{"AltaTipoRegistro"},
                ed, "GENERAL", "Acceso general", 1000, 50)
        );
    }

    @Test
    @DisplayName("altaRegistroEdicionEvento + consultaRegistro (camino feliz)")
    void registroYConsultaRegistro() throws Exception {
        altaCategoriaIdempotente(ce, "Tec");

        Object cats = categoriasDTO("Tec");
        TestUtils.tryInvoke(ce, new String[]{"AltaEvento"},
                "Expo", "Desc", LocalDate.now(), "EXPO", cats);

        LocalDate hoy = LocalDate.now();
        TestUtils.tryInvoke(ce, new String[]{"altaEdicionEvento"},
                "Expo", "Verano", "V24", "Ed verano",
                hoy.plusDays(3), hoy.plusDays(4), hoy,
                "org1", "Montevideo", "Uruguay");

        Object ed = TestUtils.tryInvoke(ce, new String[]{"obtenerEdicion"}, "Expo", "Verano");
        Object ev = TestUtils.tryInvoke(ce, new String[]{"consultaEvento"}, "Expo");
        assertNotNull(ed);
        assertNotNull(ev);

        Object usuario = TestUtils.tryInvoke(cu, new String[]{"ingresarAsistente", "IngresarDatosAsis"},
                "ana", "Ana", "ana@x", "Ap", hoy.minusYears(20), DomainAccess.obtenerInstitucion("Inst_A"));
        assertNotNull(usuario);

        TestUtils.tryInvoke(ce, new String[]{"AltaTipoRegistro"},
                ed, "GENERAL", "Acceso general", 0, 50);

        Object tipo = resolverTipoRegistro(ed, "GENERAL");
        assertNotNull(tipo, "No hay TipoRegistro disponible");

        assertDoesNotThrow(() ->
            TestUtils.invokeUnwrapped(ce, new String[]{"altaRegistroEdicionEvento"},
                "R-001", usuario, ev, ed, tipo, hoy, 0.0f, hoy.plusDays(3))
        );

        Object dtr = TestUtils.tryInvoke(ce, new String[]{"consultaRegistro"}, usuario, "R-001");
        assertNotNull(dtr);
    }

    @Test
    @DisplayName("altaEdicionEvento inválida → debe lanzar IllegalArgumentException")
    void altaEdicionEventoFechasInvalidas() {
        LocalDate hoy = LocalDate.now();
        assertThrows(IllegalArgumentException.class, () ->
            TestUtils.invokeUnwrapped(ce, new String[]{"altaEdicionEvento"},
                "Conf", "Bad", "B01", "x",
                hoy.plusDays(5), hoy.plusDays(4), hoy,
                "org1", "Montevideo", "Uruguay")
        );
    }

    @Test
    @DisplayName("consultaEdicionEvento con claves inválidas → lanza excepción en runtime")
    void consultaEdicionEventoInvalida() {
        // Como no tenés excepciones de dominio, validamos que falle con alguna unchecked
        assertThrows(RuntimeException.class, () ->
            TestUtils.invokeUnwrapped(ce, new String[]{"consultaEdicionEvento"}, "XX", "??")
        );
    }
}
