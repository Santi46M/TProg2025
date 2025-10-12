package test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import excepciones.UsuarioYaExisteException;
import logica.fabrica;
import logica.Interfaces.IControladorEvento;
import logica.Interfaces.IControladorUsuario;
import logica.Manejadores.ManejadorEvento;
import logica.CargaDatosPrueba;
import logica.Manejadores.manejadorAuxiliar;
import logica.Manejadores.manejadorUsuario;



public class TestCasosPrueba {

    static private fabrica fabrica = new fabrica();
    
    static private IControladorUsuario cUsuario = fabrica.getIControladorUsuario();
    static private IControladorEvento cEvento = fabrica.getIControladorEvento();
    static private manejadorAuxiliar mAux = manejadorAuxiliar.getInstancia();
    static private manejadorUsuario mUsr = manejadorUsuario.getInstancia();
    static private ManejadorEvento mEv = ManejadorEvento.getInstancia();

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
        assertTrue(mAux.existeCategoria("Tecnología"));
    }
    
    @Test
   	void testUsuarioYaExisteNickname() { 
          Assertions.assertThrows(
       		   UsuarioYaExisteException.class, () -> {cUsuario.altaUsuario(
       				   "atorres",
       				   "Ana",
       				   "atorres@gmail.com",
       				   null,
       				   null,
       				   "Torres",
       				   java.time.LocalDate.of(1990, 5, 12),
       				   "Facultad de Ingeniería",
       				   false,
       				   "contrasena123",
       				   "imagen.jpg"
       			  ); }
       		);
   	}
    
    @Test
   	void testUsuarioYaExisteEmail() { 
    	UsuarioYaExisteException ex = Assertions.assertThrows(
       		   UsuarioYaExisteException.class, () -> {cUsuario.altaUsuario(
       				   "paniTorres",
       				   "pani",
       				   "atorres@gmail.com",
       				   null,
       				   null,
       				   "Torres",
       				   java.time.LocalDate.of(1990, 5, 12),
       				   "Facultad de Ingeniería",
       				   false,
       				 "contrasena123",
     				   "imagen.jpg"
       			  ); }
       		);
          Assertions.assertEquals("Ya existe un usuario con ese email", ex.getMessage());
   	}
    
    @Test 
    void testInstitucionYaExistente(){
    	assertTrue(mUsr.existeInstitucion("ORT Uruguay"));
    }

    @Test
    void testEventoYaExiste() {
    	assertTrue(mEv.existeEvento("Montevideo Comics"));
    }
    
    @Test
    void testEventoNoExiste() {
    	assertFalse(mEv.existeEvento("eventoPrueba"));
    }
    
    @Test
    void testConsultaUsuarioNoExistente() {
    	
    }

}
