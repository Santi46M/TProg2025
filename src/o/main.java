package o;

import javax.swing.*;

public class main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Ignorar errores de look and feel
        }

        JFrame frame = new JFrame("Prueba de Ventanas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JDesktopPane desktopPane = new JDesktopPane();
        frame.setContentPane(desktopPane);

        // Ventana de Alta de Usuario
        AltaUsuarioFrame altaUsuarioFrame = new AltaUsuarioFrame();
        altaUsuarioFrame.setLocation(20, 20);
        desktopPane.add(altaUsuarioFrame);
        altaUsuarioFrame.setVisible(true);

        frame.setVisible(true);
    }
}