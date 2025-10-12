// test/FabricaSingletonTest.java
package test;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.Test;

import logica.fabrica;

public class FabricaSingletonTest {
    @Test
    void getInstanceDevuelveSiempreLaMisma() {
        fabrica f1 = fabrica.getInstance();
        fabrica f2 = fabrica.getInstance();
        assertSame(f1, f2);
    }
}
