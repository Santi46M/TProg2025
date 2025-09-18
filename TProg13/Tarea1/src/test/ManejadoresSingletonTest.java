package test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.lang.reflect.Method;

@DisplayName("Manejadores – Singletons consistentes (tolerante mayúsc./minúsc.)")
class ManejadoresSingletonTest {

    @BeforeEach
    void reset() { TestUtils.resetAll(); }

    private static Method getGetter(Class<?> clazz) throws NoSuchMethodException {
        try { return clazz.getMethod("getInstancia"); }
        catch (NoSuchMethodException e) { return clazz.getMethod("getInstance"); }
    }

    @Test
    @DisplayName("ManejadorUsuario.getInstancia/getInstance → mismo objeto")
    void manejadorUsuarioSingleton() throws Exception {
        // Soporta logica.ManejadorUsuario y logica.manejadorUsuario
        Class<?> clazz = TestUtils.loadAny("logica.Manejadores.ManejadorUsuario", "logica.Manejadores.manejadorUsuario");
        Method getter = getGetter(clazz);
        Object a = getter.invoke(null);
        Object b = getter.invoke(null);
        assertNotNull(a);
        assertSame(a, b, "ManejadorUsuario no es singleton (a != b)");
    }

    @Test
    @DisplayName("ManejadorEvento.getInstancia/getInstance → mismo objeto")
    void manejadorEventoSingleton() throws Exception {
        // Soporta logica.ManejadorEvento y logica.manejadorEvento
        Class<?> clazz = TestUtils.loadAny("logica.Manejadores.ManejadorEvento", "logica.Manejadores.manejadorEvento");
        Method getter = getGetter(clazz);
        Object a = getter.invoke(null);
        Object b = getter.invoke(null);
        assertNotNull(a);
        assertSame(a, b, "ManejadorEvento no es singleton (a != b)");
    }
}