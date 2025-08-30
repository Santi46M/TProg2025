package logica;


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

}
