package logica;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import excepciones.UsuarioTipoIncorrectoException;

/**
 * Fábrica para la construcción de un controlador de usuarios (uno distinto para cada invocación).
 * Se implementa en base al patrón Singleton.
 * @author TProg2017
 *
 */
public class fabrica {

    private static fabrica instancia;

    private fabrica() {
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
    
    public void cargarUsuariosDesdeCSV() throws UsuarioTipoIncorrectoException {
        IControladorUsuario cUsuario = this.getIControladorUsuario();
        String ruta = "resources/2025Usuarios.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            boolean primeraLinea = true;
            while ((linea = br.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false; // saltea encabezado
                    continue;
                }
                String[] columnas = linea.split(",");
                String ref = columnas[0];
                String tipo = columnas[1];      // A u O
                String nickname = columnas[2];
                String nombre = columnas[3];
                String correo = columnas[4];
                if (tipo == "A"){
                	String rutaAsistente = "resources/2025Usuarios-Asistentes.csv";
                	try  (BufferedReader brA = new BufferedReader(new FileReader(rutaAsistente))) {
                		String lineaA;
                        while ((lineaA = brA.readLine()) != null) {
                            String[] columnasA = lineaA.split(",");
                            String refA = columnasA[0];
                            String apellido = columnasA[1];
                            LocalDate fechaNac = LocalDate.parse(columnasA[2], DateTimeFormatter.ofPattern("d/M/yyyy"));
                            String Institucion = columnasA[3];
                            if( ref == refA) {
                            	cUsuario.AltaUsuario(nickname, nombre, correo, null, null, apellido, fechaNac, Institucion, false);
                            }
                        }
                	}
                }else if (tipo == "O") {
                	String rutaAsistente = "resources/2025Usuarios-Organizadores.csv";
                	try  (BufferedReader brO = new BufferedReader(new FileReader(rutaAsistente))) {
                		String lineaO;
                        while ((lineaO = brO.readLine()) != null) {
                            String[] columnasO = lineaO.split(",");
                            String refO = columnasO[0];
                            String desc = columnasO[1];
                            String link = columnasO[3];
                            if( ref == refO) {
                            	cUsuario.AltaUsuario(nickname, nombre, correo, desc, link, null, null, null, true);
                            }
                        }
                	}
                }
                else throw new UsuarioTipoIncorrectoException(nickname);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
   

