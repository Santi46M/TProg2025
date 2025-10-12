package test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

@DisplayName("TipoRegistro – existe y es usable (enum o clase)")
class TipoRegistroEnumTest {

    @Test
    void tipoRegistroExisteUsable() throws Exception {
        Class<?> c = Class.forName("logica.Clases.TipoRegistro");
        assertNotNull(c, "No existe logica.Clases.TipoRegistro");

        if (c.isEnum()) {
            Object[] vals = c.getEnumConstants();
            assertNotNull(vals);
            assertTrue(vals.length >= 1, "Enum sin constantes");
        } else {
            Object o = ReflectionPojoSupport.makeInstance("logica.Clases.TipoRegistro");
            assertNotNull(o, "No se pudo instanciar TipoRegistro (clase)");
            ReflectionPojoSupport.exercisePojo(o);
        }
    }
}