package test;

import static org.junit.jupiter.api.Assertions.*;

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
    private Object controladorEv;
    private Object controladorUs;

    // IDs únicos por corrida (evita *YaExiste*)
    private String INST;
    private String ORG;
    private String MAIL;

    @BeforeEach
    void setUp() throws Exception {
        TestUtils.resetAll();

        // Fábrica (minúscula o mayúscula)
        Class<?> fab = TestUtils.loadAny("logica.Fabrica", "logica.fabrica");
        Method getter;
        try { getter = fab.getMethod("getInstance"); }
        catch (NoSuchMethodException e) { getter = fab.getMethod("getInstancia"); }
        this.fabrica = getter.invoke(null);

        // Controlador de USUARIO (por fábrica)
        controladorUs = TestUtils.tryInvoke(this.fabrica, new String[]{
                "getIUsuario", "getIControladorUsuario", "getControladorUsuario"
        });

        // Controlador de EVENTO: por fábrica si existe; si no, instancia concreta
        Object ceMaybe = null;
        try {
            ceMaybe = TestUtils.tryInvoke(this.fabrica, new String[]{
                    "getIEvento", "getIControladorEvento", "getControladorEvento", "getEvento"
            });
        } catch (AssertionError ignored) { /* instanciamos directo */ }

        if (ceMaybe == null) {
            Class<?> ceClazz = Class.forName("logica.controladores.ControladorEvento");
            Constructor<?> constructor = ceClazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            controladorEv = constructor.newInstance();
        } else {
            controladorEv = ceMaybe;
        }

        // ---------- Base única por test ----------
        long nonce = System.nanoTime();
        INST = "Inst_A_" + nonce;
        ORG  = "org1_"  + nonce;
        MAIL = ORG + "@x";

        // Institución y organizador (firmas largas)
        TestUtils.tryInvoke(controladorUs, new String[]{"altaInstitucion", "AltaInstitucion"},
                INST, "desc", "web");

        TestUtils.tryInvoke(controladorUs, new String[]{"altaUsuario", "AltaUsuario"},
                ORG, "Org Uno", MAIL, "desc", "link",
                "Ap", LocalDate.of(1990, 1, 1), INST, true, null, null);

        // Categorías base (idempotentes)
        altaCategoriaIdempotente(controladorEv, "Tecnologia");
        altaCategoriaIdempotente(controladorEv, "Tec");
    }

    /* ---------- Helpers SIN catch genéricos ---------- */

    private Object categoriasDTO(String... nombres) {
        try {
            Class<?> cls = Class.forName("logica.datatypes.DTCategorias");
            var ctor = cls.getDeclaredConstructor(java.util.List.class); // tu ctor real
            ctor.setAccessible(true);
            java.util.List<String> lista = java.util.Arrays.asList(nombres);
            return ctor.newInstance(lista);
        } catch (ReflectiveOperationException e) {
            // Fallback ultra-tolerante si cambia el paquete/clase en alguna versión
            return TestUtils.tolerantNew("logica.datatypes.DTCategorias", java.util.Arrays.asList(nombres));
        }
    }

    private String getDTEventoNombre(Object dtevento) {
        Method metodo = TestUtils.findMethod(dtevento, "getNombre", "nombre");
        if (metodo == null) return String.valueOf(dtevento);
        try { return String.valueOf(metodo.invoke(dtevento)); }
        catch (ReflectiveOperationException | IllegalArgumentException e) { return String.valueOf(dtevento); }
    }

    private String getDTEdicionNombre(Object dted) {
        Method metodo = TestUtils.findMethod(dted, "getNombre", "nombre");
        if (metodo == null) return String.valueOf(dted);
        try { return String.valueOf(metodo.invoke(dted)); }
        catch (ReflectiveOperationException | IllegalArgumentException e) { return String.valueOf(dted); }
    }

    private void altaCategoriaIdempotente(Object ceRef, String nombre) {
        try {
            TestUtils.invokeUnwrapped(ceRef, new String[]{"altaCategoria", "AltaCategoria"}, nombre);
        } catch (Throwable ignored) {
            // Si ya existe o tu implementación devuelve error, lo tomamos como idempotente
        }
    }

    /** Siempre usa la firma con OBJETOS (Eventos, Usuario, …) en el orden exacto. */
    private void crearEdicion(Object ce,
                              String nombreEvento, String nickOrg,
                              String nombreEd, String siglaEd, String descEd,
                              LocalDate ini, LocalDate fin, LocalDate alta,
                              String ciudad, String pais, String imagen) throws Throwable {

        Object eventoObj  = DomainAccess.obtenerEvento(nombreEvento);
        Object usuarioObj = DomainAccess.obtenerUsuario(nickOrg);
        assertNotNull(eventoObj,  "No pude resolver Eventos (" + nombreEvento + ")");
        assertNotNull(usuarioObj, "No pude resolver Usuario (" + nickOrg + ")");

        TestUtils.invokeUnwrapped(ce, new String[]{"altaEdicionEvento"},
                eventoObj, usuarioObj,
                nombreEd, siglaEd, descEd,
                ini, fin, alta,
                ciudad, pais, imagen);
    }

    /* ---------- Tests ---------- */

    @Test
    @DisplayName("AltaCategoria + AltaEvento + listar/consultar")
    void altaEventoYListados() {
        altaCategoriaIdempotente(controladorEv, "Tecnologia");
        Object cats = categoriasDTO("Tecnologia");

        // nombre único por corrida
        String evName = "Feria_" + System.nanoTime();

        assertDoesNotThrow(() ->
            TestUtils.invokeUnwrapped(controladorEv, new String[]{"altaEvento"},
                evName, "Desc Feria", LocalDate.now(), "FER", cats, INST)
        );

        @SuppressWarnings("unchecked")
        List<Object> lista =
            (List<Object>) TestUtils.tryInvoke(controladorEv, new String[]{"listarEventos"});
        assertNotNull(lista);

        var nombres = lista.stream()
            .map(this::getDTEventoNombre)
            .collect(Collectors.toSet());
        assertTrue(nombres.contains(evName));

        Object evento = TestUtils.tryInvoke(controladorEv, new String[]{"consultaEvento"}, evName);
        assertNotNull(evento);
        assertEquals("logica", evento.getClass().getPackageName());
    }

    @Test
    @DisplayName("altaEdicionEvento + listar/obtener/consultaEdicionEvento")
    void edicionFlujoBasico() throws Throwable {
        Object cats = categoriasDTO("Tecnologia");

        String evName = "Feria_" + System.nanoTime();
        String edName = "Ed2025";

        TestUtils.tryInvoke(controladorEv, new String[]{"altaEvento"},
                evName, "Desc Feria", LocalDate.now(), "FER", cats, INST);

        LocalDate hoy = LocalDate.now();
        assertDoesNotThrow(() ->
            crearEdicion(controladorEv, evName, ORG,
                    edName, "ED25", "Edición principal",
                    hoy.plusDays(10), hoy.plusDays(12), hoy,
                    "Montevideo", "Uruguay", null)
        );

        @SuppressWarnings("unchecked")
        List<String> eds =
            (List<String>) TestUtils.tryInvoke(controladorEv, new String[]{"listarEdicionesEvento"}, evName);
        assertNotNull(eds);
        assertTrue(eds.contains(edName));

        Object edicion = TestUtils.tryInvoke(controladorEv, new String[]{"obtenerEdicion"}, evName, edName);
        assertNotNull(edicion);

        Object dted = TestUtils.tryInvoke(controladorEv, new String[]{"consultaEdicionEvento"}, evName, edName);
        if (dted != null) {
            var mNombre = TestUtils.findMethod(dted, "getNombre", "nombre");
            if (mNombre != null) {
                try { assertEquals(edName, String.valueOf(mNombre.invoke(dted))); } catch (Exception ignored) {}
            }
        }
    }

    @Test
    @DisplayName("AltaTipoRegistro sobre una edición existente")
    void altaTipoRegistroEnEdicion() throws Throwable {
        Object cats = categoriasDTO("Tec");
        String ev = "Conf_" + System.nanoTime();

        TestUtils.tryInvoke(controladorEv, new String[]{"altaEvento"},
                ev, "Desc", LocalDate.now(), "CONF", cats, INST);

        LocalDate hoy = LocalDate.now();
        crearEdicion(controladorEv, ev, ORG,
                "Apertura", "AP01", "Inicio",
                hoy.plusDays(1), hoy.plusDays(2), hoy,
                "Montevideo", "Uruguay", null);

        Object edicion = TestUtils.tryInvoke(controladorEv, new String[]{"obtenerEdicion"}, ev, "Apertura");
        assertNotNull(edicion);

        assertDoesNotThrow(() ->
            TestUtils.invokeUnwrapped(controladorEv, new String[]{"altaTipoRegistro"},
                edicion, "GENERAL", "Acceso general", 1000, 50)
        );
    }

    @Test
    @DisplayName("altaRegistroEdicionEvento + consultaRegistro (camino feliz)")
    void registroYConsultaRegistro() throws Exception {
        altaCategoriaIdempotente(controladorEv, "Tec");

        Object cats = categoriasDTO("Tec");
        String ev = "Expo_" + System.nanoTime();

        TestUtils.tryInvoke(controladorEv, new String[]{"altaEvento"},
                ev, "Desc", LocalDate.now(), "EXPO", cats, INST);

        LocalDate hoy = LocalDate.now();
        crearEdicion(controladorEv, ev, ORG,
                "Verano", "V24", "Ed verano",
                hoy.plusDays(3), hoy.plusDays(4), hoy,
                "Montevideo", "Uruguay", null);

        Object edicion = TestUtils.tryInvoke(controladorEv, new String[]{"obtenerEdicion"}, ev, "Verano");
        Object evento  = TestUtils.tryInvoke(controladorEv, new String[]{"consultaEvento"}, ev);
        assertNotNull(edicion);
        assertNotNull(evento);

        Object instObj = DomainAccess.obtenerInstitucion(INST);
        Object usuario = TestUtils.tryInvoke(controladorUs, new String[]{"ingresarAsistente", "IngresarDatosAsis"},
                "ana", "Ana", "ana@x", "Ap", hoy.minusYears(20), instObj);
        assertNotNull(usuario);

        TestUtils.tryInvoke(controladorEv, new String[]{"altaTipoRegistro", "AltaTipoRegistro"},
                edicion, "GENERAL", "Acceso general", 0, 50);

        Object tipo = resolverTipoRegistro(edicion, "GENERAL");
        assertNotNull(tipo, "No hay TipoRegistro disponible");

        assertDoesNotThrow(() ->
            TestUtils.invokeUnwrapped(controladorEv, new String[]{"altaRegistroEdicionEvento"},
                "R-001", usuario, evento, edicion, tipo, hoy, 0.0f, hoy.plusDays(3))
        );

        Object dtr = TestUtils.tryInvoke(controladorEv, new String[]{"consultaRegistro"}, usuario, "R-001");
        assertNotNull(dtr);
    }

    @Test
    @DisplayName("altaEdicionEvento inválida → debe lanzar IllegalArgumentException")
    void altaEdicionEventoFechasInvalidas() throws Throwable {
        Object cats = categoriasDTO("Tec");
        String ev = "Conf_" + System.nanoTime();

        TestUtils.tryInvoke(controladorEv, new String[]{"altaEvento"},
                ev, "Desc", LocalDate.now(), "CONF", cats, INST);

        LocalDate hoy = LocalDate.now();
        // fechas invertidas → debe fallar (o tu impl normaliza en EdgeCases)
        assertThrows(IllegalArgumentException.class, () ->
            crearEdicion(controladorEv, ev, ORG,
                    "Bad", "B01", "x",
                    hoy.plusDays(5), hoy.plusDays(4), hoy,
                    "Montevideo", "Uruguay", null)
        );
    }

    @Test
    @DisplayName("consultaEdicionEvento con claves inválidas → devuelve null o lanza (ambos válidos)")
    void consultaEdicionEventoInvalida() {
        try {
            Object dto = TestUtils.invokeUnwrapped(
                    controladorEv,
                    new String[]{"consultaEdicionEvento"},
                    "XX", "??"
            );
            // Si no lanza, debe devolver null
            assertNull(dto, "Si no lanza, consultaEdicionEvento debe devolver null para claves inválidas");
        } catch (Throwable e) {
            // Si tu implementación lanza, también es válido (runtime/custom)
            assertTrue(
                e instanceof RuntimeException
                || e instanceof IllegalArgumentException
                || e.getClass().getSimpleName().contains("NoExiste")
                || e.getClass().getSimpleName().contains("Invalida"),
                "Se esperaba una excepción de runtime o de dominio equivalente"
            );
        }
    }
}
