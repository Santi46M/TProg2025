package test;

import logica.controladores.ControladorEvento;
import logica.clases.*;
import logica.datatypes.*;
import logica.enumerados.*;
import logica.manejadores.*;
import excepciones.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.*;

public class ControladorEventoExtraTest {

    private ControladorEvento ce;

    @BeforeEach
    void setUp() {
        // Limpio manejadores entre test
        ManejadorUsuario.reset();
        ManejadorEvento.reset();
        ce = new ControladorEvento();
    }

    private Eventos crearEventoBasico(String nombre) {
        Map<String, Categoria> cats = new HashMap<>();
        cats.put("Cat1", new Categoria("Cat1"));
        Eventos ev = new Eventos(nombre, "SIG-" + nombre, "desc", LocalDate.now(), cats, "img.png");
        ManejadorEvento.getInstancia().agregarEvento(ev);
        return ev;
    }

    private Organizador crearOrganizador(String nick) {
        Organizador org = new Organizador(nick, "Nombre", nick + "@mail", "pass", null, "desc", "link");
        ManejadorUsuario.getInstancia().addUsuario(org);
        return org;
    }

    private Asistente crearAsistente(String nick) {
        Asistente a = new Asistente(nick, "Nom", nick + "@mail", "pass", null, "Ape", LocalDate.of(2000, 1, 1), null);
        ManejadorUsuario.getInstancia().addUsuario(a);
        return a;
    }

    private Ediciones crearEdicion(Eventos ev, String nombre, Organizador org) {
        Ediciones ed = new Ediciones(ev, nombre, "SIG-" + nombre,
                LocalDate.now().minusDays(5), LocalDate.now().plusDays(5), LocalDate.now(),
                org, "Montevideo", "UY", "img.png", "video.mp4");
        ev.agregarEdicion(ed);
        ManejadorEvento.getInstancia().agregarEdicion(ed);
        return ed;
    }

    private TipoRegistro crearTipo(Ediciones ed, String nombre, float costo, int cupo) {
        TipoRegistro t = new TipoRegistro(ed, nombre, "desc", costo, cupo);
        ed.agregarTipoRegistro(nombre, t);
        ManejadorEvento.getInstancia().agregarTipoRegistro(t);
        return t;
    }

    private Institucion crearInstitucion(String nombre) {
        Institucion i = new Institucion(nombre, "desc", "link");
        ManejadorUsuario.getInstancia().addInstitucion(i);
        return i;
    }

    // === TESTS ===

    @Test
    void testAltaRegistroEdicionEventoOk() {
        Eventos ev = crearEventoBasico("EvReg");
        Organizador org = crearOrganizador("org");
        Ediciones ed = crearEdicion(ev, "Ed1", org);
        TipoRegistro tipo = crearTipo(ed, "T1", 10f, 2);
        Asistente a = crearAsistente("asis");

        assertDoesNotThrow(() ->
            ce.altaRegistroEdicionEvento("R1", a.getNickname(), ev.getNombre(), ed.getNombre(),
                    tipo.getNombre(), LocalDate.now(), tipo.getCosto(), ed.getFechaInicio())
        );
        assertEquals(1, ManejadorEvento.getInstancia().obtenerRegistros().size());
    }

    @Test
    void testAltaRegistroEdicionEventoYaRegistrado() {
        Eventos ev = crearEventoBasico("EvReg2");
        Organizador org = crearOrganizador("org2");
        Ediciones ed = crearEdicion(ev, "Ed2", org);
        TipoRegistro tipo = crearTipo(ed, "T2", 10f, 5);
        Asistente a = crearAsistente("asis2");

        ce.altaRegistroEdicionEvento("R10", a.getNickname(), ev.getNombre(), ed.getNombre(),
                tipo.getNombre(), LocalDate.now(), tipo.getCosto(), ed.getFechaInicio());

        assertThrows(RuntimeException.class, () ->
            ce.altaRegistroEdicionEvento("R11", a.getNickname(), ev.getNombre(), ed.getNombre(),
                    tipo.getNombre(), LocalDate.now(), tipo.getCosto(), ed.getFechaInicio())
        );
    }

    @Test
    void testAltaRegistroEdicionEventoCupoLleno() {
        Eventos ev = crearEventoBasico("EvReg3");
        Organizador org = crearOrganizador("org3");
        Ediciones ed = crearEdicion(ev, "Ed3", org);
        TipoRegistro tipo = crearTipo(ed, "T3", 10f, 1);
        Asistente a1 = crearAsistente("a1");
        Asistente a2 = crearAsistente("a2");

        ce.altaRegistroEdicionEvento("R20", a1.getNickname(), ev.getNombre(), ed.getNombre(),
                tipo.getNombre(), LocalDate.now(), tipo.getCosto(), ed.getFechaInicio());
        assertThrows(CupoTipoRegistroInvalidoException.class, () ->
            ce.altaRegistroEdicionEvento("R21", a2.getNickname(), ev.getNombre(), ed.getNombre(),
                    tipo.getNombre(), LocalDate.now(), tipo.getCosto(), ed.getFechaInicio())
        );
    }

    @Test
    void testAltaPatrocinioDTExcedido() {
        Eventos ev = crearEventoBasico("EvPat1");
        Organizador org = crearOrganizador("orgPat");
        Ediciones ed = crearEdicion(ev, "EdPat", org);
        TipoRegistro tipo = crearTipo(ed, "TPat", 100f, 10);
        Institucion inst = crearInstitucion("Insti");

        assertThrows(ValorPatrocinioExcedidoException.class, () -> {
            ce.altaPatrocinioDT(
                ed.getSigla(),
                inst.getNombre(),
                DTNivel.PLATA,
                tipo.getNombre(),
                100,
                LocalDate.now(),
                10,
                "CODPAT"
            );
        });
    }

    @Test
    void testAltaPatrocinioDTDobleCodigo() {
        Eventos ev = crearEventoBasico("EvPat2");
        Organizador org = crearOrganizador("orgPat2");
        Ediciones ed = crearEdicion(ev, "EdPat2", org);
        TipoRegistro tipo = crearTipo(ed, "TPat2", 10f, 5);
        Institucion inst = crearInstitucion("Insti2");

        assertDoesNotThrow(() -> {
            ce.altaPatrocinioDT(
                ed.getSigla(),
                inst.getNombre(),
                DTNivel.BRONCE,
                tipo.getNombre(),
                1000,
                LocalDate.now(),
                5,
                "CODX"
            );
        });

        assertThrows(PatrocinioYaExisteException.class, () -> {
            ce.altaPatrocinioDT(
                ed.getSigla(),
                inst.getNombre(),
                DTNivel.BRONCE,
                tipo.getNombre(),
                1000,
                LocalDate.now(),
                5,
                "CODX"
            );
        });
    }

    @Test
    void testListarEdicionesArchivables() {
        Eventos ev = crearEventoBasico("EvArc");
        Organizador org = crearOrganizador("orgArc");
        Ediciones ed1 = new Ediciones(ev, "EdAntigua", "SIG-A",
                LocalDate.now().minusDays(10), LocalDate.now().minusDays(1), LocalDate.now(),
                org, "Montevideo", "UY", DTEstado.Aceptada);
        ev.agregarEdicion(ed1);
        org.getEdiciones().put(ed1.getNombre(), ed1);

        List<String> res = ce.listarEdicionesArchivables(org.getNickname());
        assertTrue(res.stream().anyMatch(s -> s.contains("EdAntigua")));
    }

    @Test
    void testArchivarEdicionInexistente() {
        assertThrows(IllegalArgumentException.class, () -> ce.archivarEdicion("noexiste"));
    }
    
    @Test
    void testArchivarEdicionFlujoCompleto() {
        // Crear evento y edición aceptada
        Eventos ev = crearEventoBasico("EvArchiv");
        Organizador org = crearOrganizador("orgArchiv");
        Ediciones ed = new Ediciones(ev, "EdFinalizada", "SIG-FIN",
                LocalDate.now().minusDays(10), LocalDate.now().minusDays(1), LocalDate.now(),
                org, "Montevideo", "UY", DTEstado.Aceptada);
        ev.agregarEdicion(ed);
        ManejadorEvento.getInstancia().agregarEdicion(ed);
        org.getEdiciones().put(ed.getNombre(), ed);

        // Registrar un asistente con un tipo y un registro
        TipoRegistro tipo = crearTipo(ed, "TArch", 10f, 10);
        Asistente asis = crearAsistente("asisArchiv");
        Registro reg = new Registro("REGARCH", asis, ed, tipo,
                LocalDate.now().minusDays(2), 10f, LocalDate.now().minusDays(1), ev);
        ed.agregarRegistro("REGARCH", reg);
        ManejadorEvento.getInstancia().agregarRegistro(reg);

        // Acción principal: archivar
        assertDoesNotThrow(() -> ce.archivarEdicion(ev.getNombre() + "::" + ed.getNombre()));

        // Verificar efectos
        assertEquals(DTEstado.Archivada, ed.getEstado(), "La edición debería quedar archivada");
    }
    @Test
    void testAltaPatrocinioDTExitoso() {
        Eventos ev = crearEventoBasico("EvPatOK");
        Organizador org = crearOrganizador("orgPatOK");
        Ediciones ed = crearEdicion(ev, "EdPatOK", org);
        TipoRegistro tipo = crearTipo(ed, "TPatOK", 100f, 10);
        Institucion inst = crearInstitucion("InstiOK");

        assertDoesNotThrow(() -> 
            ce.altaPatrocinioDT(ed.getSigla(), inst.getNombre(), DTNivel.BRONCE,
                    tipo.getNombre(), 1000, LocalDate.now(), 10, "CODPATOK")
        );

        Patrocinio p = ManejadorAuxiliar.getInstancia()
                .listarPatrocinios()
                .stream()
                .filter(x -> x.getCodigoPatrocinio().equals("CODPATOK"))
                .findFirst()
                .orElse(null);
        assertNotNull(p, "El patrocinio debería haberse creado correctamente");
    }

    @Test
    void testArchivarEdicionNoAceptada() {
        Eventos ev = crearEventoBasico("EvNA");
        Organizador org = crearOrganizador("orgNA");
        Ediciones ed = new Ediciones(ev, "EdNoAcept", "SIG-NO",
                LocalDate.now().minusDays(10), LocalDate.now().minusDays(1), LocalDate.now(),
                org, "Montevideo", "UY", DTEstado.Ingresada);
        ev.agregarEdicion(ed);
        ManejadorEvento.getInstancia().agregarEdicion(ed);
        org.getEdiciones().put(ed.getNombre(), ed);

        // Como la edición no está aceptada, debería lanzar excepción
        assertThrows(IllegalStateException.class, () -> ce.archivarEdicion(ed.getNombre()));
    }
    
    
}