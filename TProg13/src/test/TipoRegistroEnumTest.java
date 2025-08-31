package test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

@DisplayName("TipoRegistro – existe y es usable (enum o clase)")
class TipoRegistroEnumTest {

    @Test
    void tipoRegistro_existe_y_usable() throws Exception {
        Class<?> c = Class.forName("logica.TipoRegistro");
        assertNotNull(c, "No existe logica.TipoRegistro");

        if (c.isEnum()) {
            Object[] vals = c.getEnumConstants();
            assertNotNull(vals);
            assertTrue(vals.length >= 1, "Enum sin constantes");
        } else {
            Object o = ReflectionPojoSupport.makeInstance("logica.TipoRegistro");
            assertNotNull(o, "No se pudo instanciar TipoRegistro (clase)");
            ReflectionPojoSupport.exercisePojo(o);
        }
    }
}