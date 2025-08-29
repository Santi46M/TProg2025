package logica;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import excepciones.CategoriaSinSeleccionarException;
import excepciones.EdicionYaExisteException;
import excepciones.EventoYaExisteException;
import excepciones.InstitucionYaExisteException;
import excepciones.UsuarioYaExisteException;
import excepciones.FechasCruzadasException;
import excepciones.ValorPatrocinioExcedidoException;

public class fabrica {
	private static fabrica instancia;
	public static fabrica getInstancia() {
		if (instancia == null) {
			instancia = new fabrica();
		}
		return instancia;
	}
	

   // public static iControladorUsuario crearControladorUsuario() {
   //     return new controladorUsuario(); 
    //}

    
  //  public static iControladorEvento crearControladorEvento() {
    //    return new controladorEvento();
    //} 
	//80% JUnit sala 402 lunes 8/9 16:00 hrs
}