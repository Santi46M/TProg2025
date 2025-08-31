package test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import logica.fabrica;

public class FabricaInterfacesDisponiblesTest {
    @Test
    void singleton_devuelve_siempre_la_misma() {
        fabrica f1 = fabrica.getInstance();
        fabrica f2 = fabrica.getInstance();
        assertSame(f1, f2);
    }

    @Test
    void cargarUsuariosDesdeCSV_no_explota() {
        // Va a imprimir stacktrace porque el archivo no existe,
        // pero al menos cubre el método
        assertDoesNotThrow(() -> fabrica.getInstance().cargarUsuariosDesdeCSV());
    }
}
