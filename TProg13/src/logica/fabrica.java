package logica;

import java.io.BufferedReader;
import java.io.FileReader;

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
    
    public void cargarCSV(String nombreArchivo) {
        String ruta = "resources/" + nombreArchivo;
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] columnas = linea.split(",");
                // aca va lo especifico para cada csv
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
