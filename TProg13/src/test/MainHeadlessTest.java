// test/MainHeadlessTest.java
package test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;

import presentacion.main;

public class MainHeadlessTest {
    @Test
    void main_no_falla_en_headless() {
        System.setProperty("java.awt.headless", "true");
        assertDoesNotThrow(() -> main.main(new String[]{}));
    }
}
