package logica;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import excepciones.UsuarioTipoIncorrectoException;
import logica.Interfaces.*;
import logica.Controladores.*;
/**
 * Fábrica para la construcción de un controlador de usuarios (uno distinto para cada invocación).
 * Se implementa en base al patrón Singleton.
 * @author TProg2017
 *
 */
public class fabrica {

    private static fabrica instancia;

    public fabrica() {
    };

    public static fabrica getInstance() {
        if (instancia == null) {
            instancia = new fabrica();
        }
        return instancia;
    }

    public IControladorUsuario getIControladorUsuario() {
        return new ControladorUsuario();
    }
    
    public IControladorEvento getIControladorEvento() {
        return new ControladorEvento();
    }
    
}
   

