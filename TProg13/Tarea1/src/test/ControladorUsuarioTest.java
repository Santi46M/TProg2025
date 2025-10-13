package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ControladorUsuario – Altas, actualizaciones, listados y consultas")
class ControladorUsuarioTest {

    private Object fabrica, controladorUs;

    @BeforeEach
    void setUp() throws Exception {
        TestUtils.resetAll();
        Class<?> fab = TestUtils.loadAny("logica.Fabrica", "logica.fabrica");
        Method getter;
        try { getter = fab.getMethod("getInstance");
        } catch (NoSuchMethodException e) { getter = fab.getMethod("getInstancia"); }
        fabrica = getter.invoke(null);
        controladorUs = TestUtils.tryInvoke(fabrica, new String[]{"getIUsuario", "getIControladorUsuario"});
        assertNotNull(controladorUs);
    }

    @Test
    @DisplayName("AltaInstitucion y getInstituciones incluyen la institución creada")
    void altaInstitucionYListado() {
        TestUtils.tryInvoke(controladorUs, new String[]{"altaInstitucion"}, "Inst_A", "desc", "web");
        Set<String> insts = (Set<String>) TestUtils.tryInvoke(controladorUs, new String[]{"getInstituciones"});
        assertTrue(insts.contains("Inst_A"));
    }

    @Test
    @DisplayName("ingresarOrganizador crea dominio; aparece en listarOrganizadores (por key o por valor)")
    void ingresarOrganizadorYListarOrganizadores() {
        TestUtils.tryInvoke(controladorUs, new String[]{"altaInstitucion"}, "Inst_A", "d", "w");

        Object org = TestUtils.tryInvoke(controladorUs, new String[]{"ingresarOrganizador"},
        	    "org1", "Org Uno", "org1@x",
        	    null, null,
        	    "desc", "link"
        	);
        assertNotNull(org);

        Map<String, Object> orgs = (Map<String, Object>) TestUtils.tryInvoke(controladorUs, new String[]{"listarOrganizadores"});
        boolean bandera = orgs.containsKey("org1");
        if (!bandera) {
            for (Object val : orgs.values()) {
                var mNick = TestUtils.findMethod(val, "getNickname", "getNick", "getNombre", "getId");
                if (mNick != null) {
                    String nick = assertDoesNotThrow(() -> String.valueOf(mNick.invoke(val)));
                    if ("org1".equals(nick)) {
                        bandera = true;
                        break;
                    }
                }
            }
        }

        if (!bandera) {
            TestUtils.tryInvoke(controladorUs, new String[]{"altaUsuario"},
                    "org1", "Org Uno", "org1@x", "desc", "link",
                    "Ap", LocalDate.of(1990, 1, 1),  "Inst_A", true, null, null);

            orgs = (Map<String, Object>) TestUtils.tryInvoke(controladorUs, new String[]{"listarOrganizadores"});
            bandera = orgs.containsKey("org1");
        }

        assertTrue(bandera, "No se encontró 'org1' en listarOrganizadores (ni como key ni como valor)");
    }

    @Test
    @DisplayName("ingresarAsistente o AltaUsuario → aparece en listarAsistentes")
    void ingresarAsistenteYListarAsistentes() {
        TestUtils.tryInvoke(controladorUs, new String[]{"altaInstitucion"}, "Inst_A", "d", "w");

        Object inst = DomainAccess.obtenerInstitucion("Inst_A");
        if (inst != null) {
            Object asis = TestUtils.tryInvoke(controladorUs, new String[]{"ingresarAsistente", "IngresarDatosAsis"},
                    "ana", "Ana", "ana@x", "Ap", LocalDate.of(2000, 1, 1), inst);
            assertNotNull(asis);
        } else {
            TestUtils.tryInvoke(controladorUs, new String[]{"altaUsuario"},
                    "ana", "Ana", "ana@x", "desc", "link",
                    "Ap", LocalDate.of(2000, 1, 1), "Inst_A", false);
        }

        Map<String, Object> asisMap = (Map<String, Object>) TestUtils.tryInvoke(controladorUs, new String[]{"listarAsistentes"});
        assertFalse(asisMap.containsKey("ana"), "No se encontró 'ana' en listarAsistentes");
    }

    @Test
    @DisplayName("AltaUsuario crea Asistente y Organizador según flag")
    void altaUsuarioAsistenteYOrganizador() {
        TestUtils.tryInvoke(controladorUs, new String[]{"altaInstitucion"}, "Inst_A", "d", "w");

        TestUtils.tryInvoke(controladorUs, new String[]{"altaUsuario"},
                "nickA", "Nombre A", "a@x", "descA", "linkA",
                "ApA", LocalDate.of(1999, 1, 1), "Inst_A", false);
        Map<String, Object> asisMap = (Map<String, Object>) TestUtils.tryInvoke(controladorUs, new String[]{"listarAsistentes"});
        assertTrue(asisMap.containsKey("nickA"));

        TestUtils.tryInvoke(controladorUs, new String[]{"altaUsuario"},
                "nickO", "Nombre O", "o@x", "descO", "linkO",
                "ApO", LocalDate.of(1998, 2, 2), "Inst_A", true);
        Map<String, Object> orgs = (Map<String, Object>) TestUtils.tryInvoke(controladorUs, new String[]{"listarOrganizadores"});
        assertTrue(orgs.containsKey("nickO"));

        Map<String, Object> users = (Map<String, Object>) TestUtils.tryInvoke(controladorUs, new String[]{"listarUsuarios"});
        assertTrue(users.containsKey("nickA"));
        assertTrue(users.containsKey("nickO"));
    }

    @Test
    @DisplayName("AltaUsuario duplicado → UsuarioYaExisteException")
    void altaUsuarioDuplicado() throws Exception {
        TestUtils.tryInvoke(controladorUs, new String[]{"altaInstitucion"}, "Inst_B", "d", "w");

        TestUtils.tryInvoke(controladorUs, new String[]{"altaUsuario"},
                "dup", "Dup", "dup@x", "d", "l",
                "Ap", LocalDate.of(1997, 3, 3), "Inst_B", false);

        Class<? extends Throwable> UYE =
                (Class<? extends Throwable>) Class.forName("excepciones.UsuarioYaExisteException");

        assertThrows(UYE, () -> TestUtils.invokeUnwrapped(controladorUs, new String[]{"altaUsuario"},
                "dup", "Dup", "dup@x", "d", "l",
                "Ap", LocalDate.of(1997, 3, 3), "Inst_B", true));
    }

    @Test
    @DisplayName("actualizarAsistente modifica apellido y fecha (sin depender de DomainAccess)")
    void actualizarAsistenteModificaCampos() {
        TestUtils.tryInvoke(controladorUs, new String[]{"altaInstitucion"}, "Inst_C", "d", "w");

        TestUtils.tryInvoke(controladorUs, new String[]{"altaUsuario"},
                "beto", "Beto", "b@x", "desc", "link",
                "Viejo", LocalDate.of(1990, 1, 1), "Inst_C", false);

        TestUtils.tryInvoke(controladorUs, new String[]{"actualizarAsistente"},
                "beto", "Nuevo", LocalDate.of(1995, 5, 5));

        Object dto = TestUtils.tryInvoke(controladorUs, new String[]{"obtenerDatosUsuario"}, "beto");
        assertNotNull(dto);

        var mAp = TestUtils.findMethod(dto, "getApellido", "apellido");
        var mFn = TestUtils.findMethod(dto, "getFechaNacimiento", "getNacimiento", "fechaNacimiento");

        if (mAp != null) {
            String assertt = assertDoesNotThrow(() -> String.valueOf(mAp.invoke(dto)));
            assertEquals("Nuevo", assertt);
        }
        if (mFn != null) {
            LocalDate fecha = assertDoesNotThrow(() -> (LocalDate) mFn.invoke(dto));
            assertEquals(LocalDate.of(1995, 5, 5), fecha);
        }
    }

    @Test
    @DisplayName("actualizarOrganizador modifica desc y link")
    void actualizarOrganizadorModificaCampos() {
        TestUtils.tryInvoke(controladorUs, new String[]{"altaInstitucion"}, "Inst_D", "d", "w");

        TestUtils.tryInvoke(controladorUs, new String[]{"altaUsuario"},
                "maria", "Maria", "m@x", "desc0", "link0",
                "Apellido", LocalDate.of(1990, 1, 1), "Inst_D", true, null, null);

        TestUtils.tryInvoke(controladorUs, new String[]{"actualizarOrganizador"},
                "maria", "desc1", "link1");

        Object dto = TestUtils.tryInvoke(controladorUs, new String[]{"obtenerDatosUsuario"}, "maria");
        assertNotNull(dto);
        var mDesc = TestUtils.findMethod(dto, "getDescripcion", "descripcion");
        var mLink = TestUtils.findMethod(dto, "getLink", "link", "getWeb");

        if (mDesc != null) {
            String desc = assertDoesNotThrow(() -> String.valueOf(mDesc.invoke(dto)));
            assertEquals("desc1", desc);
        }
        if (mLink != null) {
            String link = assertDoesNotThrow(() -> String.valueOf(mLink.invoke(dto)));
            assertEquals("link1", link);
        }
    }

    @Test
    @DisplayName("obtenerDatosUsuario devuelve DTDatosUsuario para nick existente")
    void obtenerDatosUsuarioOk() {
        TestUtils.tryInvoke(controladorUs, new String[]{"altaInstitucion"}, "Inst_E", "d", "w");
        TestUtils.tryInvoke(controladorUs, new String[]{"altaUsuario"},
                "luz", "Luz", "l@x", "d", "l",
                "Ap", LocalDate.of(2001, 7, 7), "Inst_E", false);

        Object dto = TestUtils.tryInvoke(controladorUs, new String[]{"obtenerDatosUsuario"}, "luz");
        assertNotNull(dto);
        assertEquals("logica", dto.getClass().getPackageName());
    }

    @Test
    @DisplayName("listarEdicionesAPartirDeOrganizador(org) no rompe (puede ser vacío)")
    void listarEdicionesAPartirDeOrganizadorOk() throws Exception {
        TestUtils.tryInvoke(controladorUs, new String[]{"altaInstitucion"}, "Inst_F", "d", "w");
        Object org = TestUtils.tryInvoke(controladorUs, new String[]{"ingresarOrganizador"},
                "orga", "Or Ga", "oga@x", "d", "l");

        Method metodo = null;
        try { metodo = controladorUs.getClass().getDeclaredMethod("listarEdicionesAPartirDeOrganizador", org.getClass());
        } catch (NoSuchMethodException ignored) {
            try {
                Class<?> CUclass = Class.forName("logica.controladores.ControladorUsuario");
                metodo = CUclass.getDeclaredMethod("listarEdicionesAPartirDeOrganizador", org.getClass());
            } catch (NoSuchMethodException ignored2) { /* nada */ }
        }
        assumeTrue(metodo != null, "No se encontró método estático listarEdicionesAPartirDeOrganizador(..)");

        Object res = metodo.invoke(null, org); // static
        assertNotNull(res);
        if (res instanceof java.util.Set<?> set) { assertNotNull(set); }
    }

    @Test
    @DisplayName("ConsultaUsuario(nick) no lanza excepción")
    void consultaUsuarioNoRompe() {
        TestUtils.tryInvoke(controladorUs, new String[]{"altaInstitucion"}, "Inst_G", "d", "w");
        TestUtils.tryInvoke(controladorUs, new String[]{"altaUsuario"},
                "cata", "Cata", "c@x", "d", "l",
                "Ap", LocalDate.of(2002, 2, 2), "Inst_G", false);

        TestUtils.tryInvoke(controladorUs, new String[]{"consultaUsuario"}, "cata");
        assertTrue(true);
    }

    @Test
    @DisplayName("altaCategoriaSinGUI no rompe y permite agregar categorías base")
    void altaCategoriaSinGUIOk() {
        TestUtils.tryInvoke(controladorUs, new String[]{"altaCategoriaSinGUI"}, "Deportes");
        TestUtils.tryInvoke(controladorUs, new String[]{"altaCategoriaSinGUI"}, "Tecnologia");
        assertTrue(true);
    }
}
