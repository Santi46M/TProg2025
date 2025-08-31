package test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import logica.main;

public class MainConstructorTest {

    @Test
    void constructor_inicializa_ok_enHeadless() {
        System.setProperty("java.awt.headless", "true");
        assertDoesNotThrow(() -> new main());
    }
}
