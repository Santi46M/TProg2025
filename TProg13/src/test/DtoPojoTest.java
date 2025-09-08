package test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.*;

@DisplayName("DTOs – constructores y getters (reflexión)")
class DtoPojoTest {

    @Test @DisplayName("DTCategorias")
    void dtCategorias() {
        Object o = ReflectionPojoSupport.makeInstance("logica.DTCategorias");
        assumeTrue(o != null, "No se pudo instanciar DTCategorias");
        ReflectionPojoSupport.exercisePojo(o);
    }

    @Test @DisplayName("DTDAtosUsuario / DTDatosUsuario")
    void dtDatosUsuario() {
        Object o = ReflectionPojoSupport.makeInstance("logica.DTDatosUsuario");
        assumeTrue(o != null, "No se pudo instanciar DTDatosUsuario");
        ReflectionPojoSupport.exercisePojo(o);
    }

    @Test @DisplayName("DTEdicion")
    void dtEdicion() {
        Object o = ReflectionPojoSupport.makeInstance("logica.DTEdicion");
        assumeTrue(o != null, "No se pudo instanciar DTEdicion");
        ReflectionPojoSupport.exercisePojo(o);
    }

    @Test @DisplayName("DTEvento")
    void dtEvento() {
        Object o = ReflectionPojoSupport.makeInstance("logica.DTEvento");
        assumeTrue(o != null, "No se pudo instanciar DTEvento");
        ReflectionPojoSupport.exercisePojo(o);
    }

    @Test @DisplayName("DTNivel")
    void dtNivel() {
        Object o = ReflectionPojoSupport.makeInstance("logica.DTNivel");
        assumeTrue(o != null, "No se pudo instanciar DTNivel");
        ReflectionPojoSupport.exercisePojo(o);
    }

    @Test @DisplayName("DTPatrocinio")
    void dtPatrocinio() {
        Object o = ReflectionPojoSupport.makeInstance("logica.DTPatrocinio");
        assumeTrue(o != null, "No se pudo instanciar DTPatrocinio");
        ReflectionPojoSupport.exercisePojo(o);
    }

    @Test @DisplayName("DTRegistro")
    void dtRegistro() {
        Object o = ReflectionPojoSupport.makeInstance("logica.DTRegistro");
        assumeTrue(o != null, "No se pudo instanciar DTRegistro");
        ReflectionPojoSupport.exercisePojo(o);
    }

    @Test @DisplayName("DTTipoRegistro")
    void dtTipoRegistro() {
        Object o = ReflectionPojoSupport.makeInstance("logica.DTTipoRegistro");
        assumeTrue(o != null, "No se pudo instanciar DTTipoRegistro");
        ReflectionPojoSupport.exercisePojo(o);
    }
}
