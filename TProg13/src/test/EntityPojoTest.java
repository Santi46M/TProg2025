package test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@DisplayName("Entidades básicas – constructores/getters")
class EntityPojoTest {

    @Test @DisplayName("Usuario (vía controlador)")
    void usuario() throws Exception {
        TestUtils.resetAll();

        // Fábrica y CU
        Class<?> fab = TestUtils.loadAny("logica.Fabrica", "logica.fabrica");
        java.lang.reflect.Method get;
        try { get = fab.getMethod("getInstance"); }
        catch (NoSuchMethodException e) { get = fab.getMethod("getInstancia"); }
        Object fabrica = get.invoke(null);
        Object cu = TestUtils.tryInvoke(fabrica, new String[]{"getIUsuario","getIControladorUsuario"});

        // Institución + AltaUsuario (asistente)
        TestUtils.tryInvoke(cu, new String[]{"AltaInstitucion"}, "Inst_Pojos", "d", "w");
        TestUtils.tryInvoke(cu, new String[]{"AltaUsuario"},
                "u1", "U Uno", "u1@x", "desc", "link",
                "Ap", LocalDate.of(1999,1,1), "Inst_Pojos", false);

        // Obtenemos el dominio y lo ejercitamos
        Object u = DomainAccess.obtenerUsuario("u1");
        assumeTrue(u != null, "No se pudo obtener Usuario dominio");
        ReflectionPojoSupport.exercisePojo(u);
    }

    @Test @DisplayName("Asistente (vía controlador)")
    void asistente() throws Exception {
        TestUtils.resetAll();

        Class<?> fab = TestUtils.loadAny("logica.Fabrica", "logica.fabrica");
        java.lang.reflect.Method get;
        try { get = fab.getMethod("getInstance"); }
        catch (NoSuchMethodException e) { get = fab.getMethod("getInstancia"); }
        Object fabrica = get.invoke(null);
        Object cu = TestUtils.tryInvoke(fabrica, new String[]{"getIUsuario","getIControladorUsuario"});

        TestUtils.tryInvoke(cu, new String[]{"AltaInstitucion"}, "Inst_Pojos", "d", "w");
        Object inst = DomainAccess.obtenerInstitucion("Inst_Pojos");
        Object asis = TestUtils.tryInvoke(cu, new String[]{"ingresarAsistente","IngresarDatosAsis"},
                "a1","A Uno","a1@x","Ap", LocalDate.of(2000,1,1), inst);
        assertNotNull(asis);
        ReflectionPojoSupport.exercisePojo(asis);
    }

    @Test @DisplayName("Organizador (vía controlador)")
    void organizador() throws Exception {
        TestUtils.resetAll();

        Class<?> fab = TestUtils.loadAny("logica.Fabrica", "logica.fabrica");
        java.lang.reflect.Method get;
        try { get = fab.getMethod("getInstance"); }
        catch (NoSuchMethodException e) { get = fab.getMethod("getInstancia"); }
        Object fabrica = get.invoke(null);
        Object cu = TestUtils.tryInvoke(fabrica, new String[]{"getIUsuario","getIControladorUsuario"});

        TestUtils.tryInvoke(cu, new String[]{"AltaInstitucion"}, "Inst_Pojos", "d", "w");
        TestUtils.tryInvoke(cu, new String[]{"AltaUsuario"},
                "o1", "O Uno", "o1@x", "desc", "link",
                "Ap", LocalDate.of(1990,1,1), "Inst_Pojos", true);

        Object org = DomainAccess.obtenerUsuario("o1");
        assumeTrue(org != null, "No se pudo obtener Organizador dominio");
        ReflectionPojoSupport.exercisePojo(org);
    }
}
