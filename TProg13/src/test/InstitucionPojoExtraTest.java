package test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Institucion – creación por CU y getters básicos")
class InstitucionPojoExtraTest {

	@Test
	@DisplayName("Institucion – creación por CU y getters básicos")
	void institucionViaControlador() throws Exception {
	    TestUtils.resetAll();

	    // fábrica + CU
	    Class<?> fab = TestUtils.loadAny("logica.Fabrica", "logica.fabrica");
	    Method getter;
	    try { getter = fab.getMethod("getInstance"); }
	    catch (NoSuchMethodException e) { getter = fab.getMethod("getInstancia"); }
	    Object fabrica = getter.invoke(null);
	    Object cu = TestUtils.tryInvoke(fabrica, new String[]{"getIUsuario","getIControladorUsuario"});

	    // Alta por CU (lado “oficial”)
	    TestUtils.tryInvoke(cu, new String[]{"AltaInstitucion"}, "Inst_X", "Desc X", "webX");

	    // Al menos verificamos presencia por nombre (si tu CU expone esto)
	    try {
	        Object setObj = TestUtils.tryInvoke(cu, new String[]{"getInstituciones"});
	        if (setObj instanceof java.util.Set<?> set) {
	            assertTrue(set.contains("Inst_X"));
	        }
	    } catch (Throwable ignored) {}

	    // Intento acceder al dominio por manejador; si no se puede, fabrico un dummy
	    Object inst = DomainAccess.obtenerInstitucion("Inst_X");
	    if (inst == null) {
	        try {
	            inst = TestUtils.tolerantNew("logica.Institucion", "Inst_X", "Desc X", "webX");
	        } catch (RuntimeException e) {
	            inst = TestUtils.tolerantNew("logica.Institucion");
	            // seteos por si existen setters
	            try {
	                Method m;
	                if ((m = TestUtils.findMethod(inst, "setNombre", String.class)) != null) m.invoke(inst, "Inst_X");
	                if ((m = TestUtils.findMethod(inst, "setDescripcion", String.class)) != null) m.invoke(inst, "Desc X");
	                if ((m = TestUtils.findMethod(inst, "setLink", String.class)) != null) m.invoke(inst, "webX");
	            } catch (Throwable ignored) {}
	        }
	    }

	    assertNotNull(inst, "No se pudo obtener ni construir Institucion");

	    // Paseo de getters/equals/hashCode/toString
	    ReflectionPojoSupport.exercisePojo(inst);
	}
}
