package test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import logica.main;

public class MainEntrypointTest {

    @Test
    void main_noLanzaExcepciones_enHeadless() {
        System.setProperty("java.awt.headless", "true");
        assertDoesNotThrow(() -> main.main(new String[]{}));
    }
}
