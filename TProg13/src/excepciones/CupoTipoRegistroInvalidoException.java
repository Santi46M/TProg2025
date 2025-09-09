package excepciones;

public class CupoTipoRegistroInvalidoException extends Exception {
    public CupoTipoRegistroInvalidoException(int cupo) {
        super("El cupo ingresado para el tipo de registro es inválido: " + cupo + ". Debe ser mayor a 0 y menor o igual a " + Integer.MAX_VALUE + ".");
    }
}
