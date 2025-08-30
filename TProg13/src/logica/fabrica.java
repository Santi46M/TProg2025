package logica;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import exceptions.CategoriaSinSeleccionarException;
import exceptions.EdicionYaExisteException;
import exceptions.EventoYaExisteException;
import exceptions.InstitucionYaExisteException;
import exceptions.UsuarioYaExisteException;
import exceptions.FechasCruzadasException;
import exceptions.ValorPatrocinioExcedidoException;

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