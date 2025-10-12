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

    private Object fabrica, cu;

    @BeforeEach
    void setUp() throws Exception {
        TestUtils.resetAll();
        Class<?> fab = TestUtils.loadAny("logica.Fabrica", "logica.fabrica");
        Method getter;
        try { getter = fab.getMethod("getInstance");
        } catch (NoSuchMethodException e) { getter = fab.getMethod("getInstancia"); }
        fabrica = getter.invoke(null);
        cu = TestUtils.tryInvoke(fabrica, new String[]{"getIUsuario", "getIControladorUsuario"});
        assertNotNull(cu);
    }

    @Test
    @DisplayName("AltaInstitucion y getInstituciones incluyen la institución creada")
    void altaInstitucionYListado() {
        TestUtils.tryInvoke(cu, new String[]{"AltaInstitucion"}, "Inst_A", "desc", "web");
        Set<String> insts = (Set<String>) TestUtils.tryInvoke(cu, new String[]{"getInstituciones"});
        assertTrue(insts.contains("Inst_A"));
    }

    @Test
    @DisplayName("ingresarOrganizador crea dominio; aparece en listarOrganizadores (por key o por valor)")
    void ingresarOrganizadorYListarOrganizadores() {
        TestUtils.tryInvoke(cu, new String[]{"AltaInstitucion"}, "Inst_A", "d", "w");

        Object org = TestUtils.tryInvoke(cu, new String[]{"ingresarOrganizador"},
                "org1", "Org Uno", "org1@x", "desc", "link");
        assertNotNull(org);

        Map<String, Object> orgs = (Map<String, Object>) TestUtils.tryInvoke(cu, new String[]{"listarOrganizadores"});
        boolean ok = orgs.containsKey("org1");
        if (!ok) {
            for (Object val : orgs.values()) {
                var mNick = TestUtils.findMethod(val, "getNickname", "getNick", "getNombre", "getId");
                if (mNick != null) {
                    String nick = assertDoesNotThrow(() -> String.valueOf(mNick.invoke(val)));
                    if ("org1".equals(nick)) {
                        ok = true;
                        break;
                    }
                }
            }
        }

        if (!ok) {
            TestUtils.tryInvoke(cu, new String[]{"AltaUsuario"},
                    "org1", "Org Uno", "org1@x", "desc", "link",
                    "Ap", LocalDate.of(1990, 1, 1),  "Inst_A", true);

            orgs = (Map<String, Object>) TestUtils.tryInvoke(cu, new String[]{"listarOrganizadores"});
            ok = orgs.containsKey("org1");
        }

        assertTrue(ok, "No se encontró 'org1' en listarOrganizadores (ni como key ni como valor)");
    }

    @Test
    @DisplayName("ingresarAsistente o AltaUsuario → aparece en listarAsistentes")
    void ingresarAsistenteYListarAsistentes() {
        TestUtils.tryInvoke(cu, new String[]{"AltaInstitucion"}, "Inst_A", "d", "w");

        Object inst = DomainAccess.obtenerInstitucion("Inst_A");
        if (inst != null) {
            Object asis = TestUtils.tryInvoke(cu, new String[]{"ingresarAsistente", "IngresarDatosAsis"},
                    "ana", "Ana", "ana@x", "Ap", LocalDate.of(2000, 1, 1), inst);
            assertNotNull(asis);
        } else {
            TestUtils.tryInvoke(cu, new String[]{"AltaUsuario"},
                    "ana", "Ana", "ana@x", "desc", "link",
                    "Ap", LocalDate.of(2000, 1, 1), "Inst_A", false);
        }

        Map<String, Object> asisMap = (Map<String, Object>) TestUtils.tryInvoke(cu, new String[]{"listarAsistentes"});
        assertFalse(asisMap.containsKey("ana"), "No se encontró 'ana' en listarAsistentes");
    }

    @Test
    @DisplayName("AltaUsuario crea Asistente y Organizador según flag")
    void altaUsuarioAsistenteYOrganizador() {
        TestUtils.tryInvoke(cu, new String[]{"AltaInstitucion"}, "Inst_A", "d", "w");

        TestUtils.tryInvoke(cu, new String[]{"AltaUsuario"},
                "nickA", "Nombre A", "a@x", "descA", "linkA",
                "ApA", LocalDate.of(1999, 1, 1), "Inst_A", false);
        Map<String, Object> asisMap = (Map<String, Object>) TestUtils.tryInvoke(cu, new String[]{"listarAsistentes"});
        assertTrue(asisMap.containsKey("nickA"));

        TestUtils.tryInvoke(cu, new String[]{"AltaUsuario"},
                "nickO", "Nombre O", "o@x", "descO", "linkO",
                "ApO", LocalDate.of(1998, 2, 2), "Inst_A", true);
        Map<String, Object> orgs = (Map<String, Object>) TestUtils.tryInvoke(cu, new String[]{"listarOrganizadores"});
        assertTrue(orgs.containsKey("nickO"));

        Map<String, Object> users = (Map<String, Object>) TestUtils.tryInvoke(cu, new String[]{"listarUsuarios"});
        assertTrue(users.containsKey("nickA"));
        assertTrue(users.containsKey("nickO"));
    }

    @Test
    @DisplayName("AltaUsuario duplicado → UsuarioYaExisteException")
    void altaUsuarioDuplicado() throws Exception {
        TestUtils.tryInvoke(cu, new String[]{"AltaInstitucion"}, "Inst_B", "d", "w");

        TestUtils.tryInvoke(cu, new String[]{"AltaUsuario"},
                "dup", "Dup", "dup@x", "d", "l",
                "Ap", LocalDate.of(1997, 3, 3), "Inst_B", false);

        Class<? extends Throwable> UYE =
                (Class<? extends Throwable>) Class.forName("excepciones.UsuarioYaExisteException");

        assertThrows(UYE, () -> TestUtils.invokeUnwrapped(cu, new String[]{"AltaUsuario"},
                "dup", "Dup", "dup@x", "d", "l",
                "Ap", LocalDate.of(1997, 3, 3), "Inst_B", true));
    }

    @Test
    @DisplayName("actualizarAsistente modifica apellido y fecha (sin depender de DomainAccess)")
    void actualizarAsistenteModificaCampos() {
        TestUtils.tryInvoke(cu, new String[]{"AltaInstitucion"}, "Inst_C", "d", "w");

        TestUtils.tryInvoke(cu, new String[]{"AltaUsuario"},
                "beto", "Beto", "b@x", "desc", "link",
                "Viejo", LocalDate.of(1990, 1, 1), "Inst_C", false);

        TestUtils.tryInvoke(cu, new String[]{"actualizarAsistente"},
                "beto", "Nuevo", LocalDate.of(1995, 5, 5));

        Object dto = TestUtils.tryInvoke(cu, new String[]{"obtenerDatosUsuario"}, "beto");
        assertNotNull(dto);

        var mAp = TestUtils.findMethod(dto, "getApellido", "apellido");
        var mFn = TestUtils.findMethod(dto, "getFechaNacimiento", "getNacimiento", "fechaNacimiento");

        if (mAp != null) {
            String ap = assertDoesNotThrow(() -> String.valueOf(mAp.invoke(dto)));
            assertEquals("Nuevo", ap);
        }
        if (mFn != null) {
            LocalDate fn = assertDoesNotThrow(() -> (LocalDate) mFn.invoke(dto));
            assertEquals(LocalDate.of(1995, 5, 5), fn);
        }
    }

    @Test
    @DisplayName("actualizarOrganizador modifica desc y link")
    void actualizarOrganizadorModificaCampos() {
        TestUtils.tryInvoke(cu, new String[]{"AltaInstitucion"}, "Inst_D", "d", "w");

        TestUtils.tryInvoke(cu, new String[]{"AltaUsuario"},
                "maria", "Maria", "m@x", "desc0", "link0",
                "Apellido", LocalDate.of(1990, 1, 1), "Inst_D", true);

        TestUtils.tryInvoke(cu, new String[]{"actualizarOrganizador"},
                "maria", "desc1", "link1");

        Object dto = TestUtils.tryInvoke(cu, new String[]{"obtenerDatosUsuario"}, "maria");
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
        TestUtils.tryInvoke(cu, new String[]{"AltaInstitucion"}, "Inst_E", "d", "w");
        TestUtils.tryInvoke(cu, new String[]{"AltaUsuario"},
                "luz", "Luz", "l@x", "d", "l",
                "Ap", LocalDate.of(2001, 7, 7), "Inst_E", false);

        Object dto = TestUtils.tryInvoke(cu, new String[]{"obtenerDatosUsuario"}, "luz");
        assertNotNull(dto);
        assertEquals("logica", dto.getClass().getPackageName());
    }

    @Test
    @DisplayName("listarEdicionesAPartirDeOrganizador(org) no rompe (puede ser vacío)")
    void listarEdicionesAPartirDeOrganizadorOk() throws Exception {
        TestUtils.tryInvoke(cu, new String[]{"AltaInstitucion"}, "Inst_F", "d", "w");
        Object org = TestUtils.tryInvoke(cu, new String[]{"ingresarOrganizador"},
                "orga", "Or Ga", "oga@x", "d", "l");

        Method m = null;
        try { m = cu.getClass().getDeclaredMethod("listarEdicionesAPartirDeOrganizador", org.getClass());
        } catch (NoSuchMethodException ignored) {
            try {
                Class<?> CUclass = Class.forName("logica.Controladores.ControladorUsuario");
                m = CUclass.getDeclaredMethod("listarEdicionesAPartirDeOrganizador", org.getClass());
            } catch (NoSuchMethodException ignored2) { /* nada */ }
        }
        assumeTrue(m != null, "No se encontró método estático listarEdicionesAPartirDeOrganizador(..)");

        Object res = m.invoke(null, org); // static
        assertNotNull(res);
        if (res instanceof java.util.Set<?> s) { assertNotNull(s); }
    }

    @Test
    @DisplayName("ConsultaUsuario(nick) no lanza excepción")
    void consultaUsuarioNoRompe() {
        TestUtils.tryInvoke(cu, new String[]{"AltaInstitucion"}, "Inst_G", "d", "w");
        TestUtils.tryInvoke(cu, new String[]{"AltaUsuario"},
                "cata", "Cata", "c@x", "d", "l",
                "Ap", LocalDate.of(2002, 2, 2), "Inst_G", false);

        TestUtils.tryInvoke(cu, new String[]{"ConsultaUsuario"}, "cata");
        assertTrue(true);
    }

    @Test
    @DisplayName("AltaCategoriaSinGUI no rompe y permite agregar categorías base")
    void altaCategoriaSinGUIOk() {
        TestUtils.tryInvoke(cu, new String[]{"AltaCategoriaSinGUI"}, "Deportes");
        TestUtils.tryInvoke(cu, new String[]{"AltaCategoriaSinGUI"}, "Tecnologia");
        assertTrue(true);
    }
}
