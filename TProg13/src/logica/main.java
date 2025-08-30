package logica;

import javax.swing.*;

import excepciones.UsuarioNoExisteException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.HashMap;
import presentacion.*;

import logica.fabrica;

import java.time.LocalDate;
public class main {

    private JFrame frame;
    private JDesktopPane desktopPane;
    private IControladorUsuario ICU;
    private AltaUsuarioFrame creUsrInternalFrame;
    private ConsultaUsuario conUsrInternalFrame;
//    private ListaUsuarios lisUsrInternalFrame;


    public static void main(String[] args) {
        // EventQueue asegura que Swing se ejecute en el hilo correcto
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    main window = new main();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public main() {
        initialize();
        
        // Inicialización
        
        ICU = fabrica.getInstance().getIControladorUsuario();
        
        // Se crean los tres InternalFrame y se incluyen al Frame principal ocultos.
        // De esta forma, no es necesario crear y destruir objetos lo que enlentece la ejecución.
        // Cada InternalFrame usa un layout diferente, simplemente para mostrar distintas opciones.
        // Alta de usuario
        creUsrInternalFrame = new AltaUsuarioFrame(ICU);
        creUsrInternalFrame.setVisible(false);
        // Consulta de usuario
        conUsrInternalFrame = new ConsultaUsuario(ICU);
        conUsrInternalFrame.setVisible(false);
        
        frame.getContentPane().setLayout(null);
        // Agregamos Alta de usuario
        desktopPane.add(creUsrInternalFrame);
        creUsrInternalFrame.setVisible(false);
        //Agregamos Consulta de usuario
        desktopPane.add(conUsrInternalFrame);
        conUsrInternalFrame.setVisible(false);
        
        // Cargar datos de prueba automáticamente al iniciar
        try {
            CargaDatosPrueba.cargar();
            System.out.println("Datos de prueba cargados correctamente.");
        } catch (Exception ex) {
            System.err.println("Error al cargar datos de prueba: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    private void initialize() {
    	
    	
        // Creo el JFrame principal
        frame = new JFrame();
        frame.setTitle("Eventos.uy");
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        // Creo el DesktopPane (contenedor de InternalFrames)
        desktopPane = new JDesktopPane();
        desktopPane.setBounds(0, 50, 800, 550);
        frame.getContentPane().add(desktopPane);


        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu menuSistema = new JMenu("Sistema");
        menuBar.add(menuSistema);

        JMenu menuUsuario = new JMenu("Usuario");
        menuBar.add(menuUsuario);


        JMenuItem itemAltaUsuario = new JMenuItem("Alta de Usuario");
        menuUsuario.add(itemAltaUsuario);
        itemAltaUsuario.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	try {
            		creUsrInternalFrame.cargarInstituciones();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            		
            		creUsrInternalFrame.setVisible(true);
            }
        });
        
        JMenuItem itemConsultaUsuario = new JMenuItem("Consulta de Usuario");
        menuUsuario.add(itemConsultaUsuario);
        itemConsultaUsuario.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	try {
					conUsrInternalFrame.cargarUsuarios();
				} catch (UsuarioNoExisteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	conUsrInternalFrame.setVisible(true);
            }
        });
        

    }
}