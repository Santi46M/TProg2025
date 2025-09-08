package excepciones;

public class CostoTipoRegistroInvalidoException extends Exception {
    public CostoTipoRegistroInvalidoException(int costo) {
        super("El costo ingresado para el tipo de registro es inválido: " + costo + ". Debe ser mayor o igual a 0 y menor o igual a " + Integer.MAX_VALUE + ".");
    }
}
