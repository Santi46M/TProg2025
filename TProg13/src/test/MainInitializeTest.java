package test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import presentacion.main;

import java.lang.reflect.Method;

public class MainInitializeTest {

    @Test
    void initialize_reflection_noRompe() throws Exception {
        System.setProperty("java.awt.headless", "true");

        main m = new main();
        Method init = main.class.getDeclaredMethod("initialize");
        init.setAccessible(true);

        assertDoesNotThrow(() -> {
            try {
                init.invoke(m);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
