package test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
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

    @BeforeAll
    static void cargarDatosPrueba() throws Exception {
    	CargaDatosPrueba.cargarInstitucionesEjemplo();
    	CargaDatosPrueba.cargarCategorias();
    	CargaDatosPrueba.cargarEventosEjemplo();
    	CargaDatosPrueba.cargarUsuariosEjemplo();
    	CargaDatosPrueba.cargarEdicionesEjemplo();
    	CargaDatosPrueba.cargarTipoRegistroEjemplo();
    	CargaDatosPrueba.cargarPatrociniosEjemplo();
    	CargaDatosPrueba.cargarRegistrosEjemplo();
    	CargaDatosPrueba.logResumenDatos();
    }
     
    @Test
    void testCategoriaYaExiste() {
        assertTrue(cEvento.getManejadorAux().existeCategoria("Tecnología"));
    }
    
    @Test
	void testClienteYaExisteNickname() { 
       Assertions.assertThrows(
    		   UsuarioYaExisteException.class, () -> {cUsuario.AltaUsuario(
    				   "atorres",
    				   "Ana",
    				   "atorres@gmail.com",
    				   null,
    				   null,
    				   "Torres",
    				   java.time.LocalDate.of(1990, 5, 12),
    				   "Facultad de Ingeniería",
    				   false
    			  );}
    		);
	}

}