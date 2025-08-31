// test/FabricaSingletonTest.java
package test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import logica.fabrica;

public class FabricaSingletonTest {
    @Test
    void getInstance_devuelve_siempre_la_misma() {
        fabrica f1 = fabrica.getInstance();
        fabrica f2 = fabrica.getInstance();
        assertSame(f1, f2);
    }
}
