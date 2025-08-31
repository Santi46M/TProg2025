package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import excepciones.*;
import logica.fabrica;
import logica.IControladorEvento;
import logica.IControladorUsuario;
import logica.CargaDatosPrueba;

public class TestsCasosPrueba {

    static private fabrica fabrica = new fabrica();
    static private IControladorUsuario cUsuario = fabrica.getIControladorUsuario();
    static private IControladorEvento cEvento = fabrica.getIControladorEvento();

    /*@BeforeAll
    static void cargarDatosFabrica() {
        // Verifica que no lance excepciones
        assertDoesNotThrow(() -> fabrica.cargarUsuariosDesdeCSV());
    }*/
    
    
    @Test
    void testLogResumenDatos() {
        assertDoesNotThrow(() -> CargaDatosPrueba.logResumenDatos());
    }
    
    @Test
    void testCargarInstituciones() {
        assertDoesNotThrow(() -> CargaDatosPrueba.cargarInstitucionesEjemplo());
    }
    
    @Test
    void testCargarCategorias() {
        assertDoesNotThrow(() -> CargaDatosPrueba.cargarCategorias());
    }

    @Test
    void testCargarEventos() {
        assertDoesNotThrow(() -> CargaDatosPrueba.cargarEventosEjemplo());
    }

    @Test
    void testCargarUsuarios() {
        assertDoesNotThrow(() -> CargaDatosPrueba.cargarUsuariosEjemplo());
    }
    
    @Test
    void testCargarEdiciones() {
        assertDoesNotThrow(() -> CargaDatosPrueba.cargarEdicionesEjemplo());
    }

    @Test
    void testCargarTipoRegistro() {
        assertDoesNotThrow(() -> CargaDatosPrueba.cargarTipoRegistroEjemplo());
    }

    @Test
    void testCargarRegistros() {
        assertDoesNotThrow(() -> CargaDatosPrueba.cargarRegistrosEjemplo());
    }

    @Test
    void testCargarPatrocinios() {
        assertDoesNotThrow(() -> CargaDatosPrueba.cargarPatrociniosEjemplo());
    }
    

}