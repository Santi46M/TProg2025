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
import logica.CargaDatosPrueba;

import java.time.LocalDate;
public class main {

    private JFrame frame;
    private JDesktopPane desktopPane;
    private IControladorUsuario ICU;
    private AltaUsuarioFrame creUsrInternalFrame;
    private ConsultaUsuario conUsrInternalFrame;
    private ConsultaEventoFrame consultaEventoFrame;
    private ConsultaEdicionEventoFrame consultaEdicionEventoFrame;
    private ConsultaTipoRegistroFrame consultaTipoRegistroFrame;
    private ConsultaRegistroFrame consultaRegistroFrame;
    private ConsultaPatrocinioFrame consultaPatrocinioFrame;
    private AltaEventoFrame altaEventoFrame;
    private AltaTipoRegistroFrame altaTipoRegistroFrame;
    private AltaPatrocinioFrame altaPatrocinioFrame;
    private AltaInstitucionFrame altaInstitucionFrame;
    private RegistroEdicionEventoFrame registroEdicionEventoFrame;
    private AltaEdicionEvento altaEdicionEventoFrame;
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
        ICU = fabrica.getInstance().getIControladorUsuario();
        // Alta de usuario
        creUsrInternalFrame = new AltaUsuarioFrame(ICU);
        creUsrInternalFrame.setVisible(false);
        // Consulta de usuario
        conUsrInternalFrame = new ConsultaUsuario(ICU);
        conUsrInternalFrame.setVisible(false);
        // Frames de eventos (consultas)
        consultaEventoFrame = new ConsultaEventoFrame(new String[0], new String[0][0], new String[0][0], new String[0][0]);
        consultaEventoFrame.setVisible(false);
        consultaEdicionEventoFrame = new ConsultaEdicionEventoFrame();
        consultaEdicionEventoFrame.setVisible(false);
        consultaTipoRegistroFrame = new ConsultaTipoRegistroFrame();
        consultaTipoRegistroFrame.setVisible(false);
        consultaRegistroFrame = new ConsultaRegistroFrame(new String[0], new String[0][0], new String[0][0]);
        consultaRegistroFrame.setVisible(false);
        consultaPatrocinioFrame = new ConsultaPatrocinioFrame(new String[0], new String[0][0], new String[0][0], new String[0][0]);
        consultaPatrocinioFrame.setVisible(false);
        // Frames de eventos (altas)
        altaEventoFrame = new AltaEventoFrame(desktopPane);
        altaEventoFrame.setVisible(false);
        altaTipoRegistroFrame = new AltaTipoRegistroFrame(new logica.ControladorEvento());
        altaTipoRegistroFrame.setVisible(false);
        altaPatrocinioFrame = new AltaPatrocinioFrame(new String[0], new String[0][0], new String[0][0], new String[0], new java.util.HashSet<>(), new java.util.HashSet<>(), new double[0]);
        altaPatrocinioFrame.setVisible(false);
        altaInstitucionFrame = new AltaInstitucionFrame(new java.util.HashSet<>());
        altaInstitucionFrame.setVisible(false);
        registroEdicionEventoFrame = new RegistroEdicionEventoFrame();
        registroEdicionEventoFrame.setVisible(false);
        altaEdicionEventoFrame = new AltaEdicionEvento(ICU);
        altaEdicionEventoFrame.setVisible(false);
        
        frame.getContentPane().setLayout(null);
        // Agregamos Alta de usuario
        desktopPane.add(creUsrInternalFrame);
        desktopPane.add(conUsrInternalFrame);
        desktopPane.add(consultaEventoFrame);
        desktopPane.add(consultaEdicionEventoFrame);
        desktopPane.add(consultaTipoRegistroFrame);
        desktopPane.add(consultaRegistroFrame);
        desktopPane.add(consultaPatrocinioFrame);
        desktopPane.add(altaEventoFrame);
        desktopPane.add(altaTipoRegistroFrame);
        desktopPane.add(altaPatrocinioFrame);
        desktopPane.add(altaInstitucionFrame);
        desktopPane.add(registroEdicionEventoFrame);
        desktopPane.add(altaEdicionEventoFrame);
        
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
        JMenu menuEvento = new JMenu("Evento");
        menuBar.add(menuEvento);


        JMenuItem itemAltaUsuario = new JMenuItem("Alta de Usuario");
        menuUsuario.add(itemAltaUsuario);
        itemAltaUsuario.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    creUsrInternalFrame.cargarInstituciones();
                } catch (Exception e1) {
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
                    // Si la instancia es null o está cerrada, crear una nueva y agregarla al desktopPane
                    if (conUsrInternalFrame == null || conUsrInternalFrame.isClosed()) {
                        conUsrInternalFrame = new ConsultaUsuario(ICU);
                        desktopPane.add(conUsrInternalFrame);
                    }
                    conUsrInternalFrame.cargarUsuarios();
                    conUsrInternalFrame.setVisible(true);
                    conUsrInternalFrame.toFront();
                } catch (UsuarioNoExisteException ex) {
                    ex.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        // Menú Evento
        JMenuItem itemConsultaEvento = new JMenuItem("Consulta de Evento");
        menuEvento.add(itemConsultaEvento);
        itemConsultaEvento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (consultaEventoFrame == null || consultaEventoFrame.isClosed()) {
                        consultaEventoFrame = new ConsultaEventoFrame(new String[0], new String[0][0], new String[0][0], new String[0][0]);
                        desktopPane.add(consultaEventoFrame);
                    }
                    consultaEventoFrame.cargarEventos();
                    consultaEventoFrame.setVisible(true);
                    consultaEventoFrame.toFront();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        JMenuItem itemConsultaEdicion = new JMenuItem("Consulta de Edición de Evento");
        menuEvento.add(itemConsultaEdicion);
        itemConsultaEdicion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (consultaEdicionEventoFrame == null || consultaEdicionEventoFrame.isClosed()) {
                        consultaEdicionEventoFrame = new ConsultaEdicionEventoFrame();
                        desktopPane.add(consultaEdicionEventoFrame);
                    }
                    consultaEdicionEventoFrame.cargarEventos();
                    consultaEdicionEventoFrame.setVisible(true);
                    consultaEdicionEventoFrame.toFront();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        JMenuItem itemConsultaTipoRegistro = new JMenuItem("Consulta de Tipo de Registro");
        menuEvento.add(itemConsultaTipoRegistro);
        itemConsultaTipoRegistro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                consultaTipoRegistroFrame.cargarEventos();
                consultaTipoRegistroFrame.setVisible(true);
                consultaTipoRegistroFrame.toFront();
            }
        });
        JMenuItem itemConsultaRegistro = new JMenuItem("Consulta de Registro");
        menuEvento.add(itemConsultaRegistro);
        itemConsultaRegistro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                consultaRegistroFrame.setVisible(true);
            }
        });
        JMenuItem itemConsultaPatrocinio = new JMenuItem("Consulta de Patrocinio");
        menuEvento.add(itemConsultaPatrocinio);
        itemConsultaPatrocinio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                consultaPatrocinioFrame.setVisible(true);
            }
        });
        // Menú Evento (agregar altas)
        JMenuItem itemAltaEvento = new JMenuItem("Alta de Evento");
        menuEvento.add(itemAltaEvento);
        itemAltaEvento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                altaEventoFrame.cargarCategorias();
                altaEventoFrame.setVisible(true);
            }
        });
        JMenuItem itemAltaTipoRegistro = new JMenuItem("Alta de Tipo de Registro");
        menuEvento.add(itemAltaTipoRegistro);
        itemAltaTipoRegistro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                altaTipoRegistroFrame.cargarEventos();
                altaTipoRegistroFrame.setVisible(true);
                altaTipoRegistroFrame.toFront();
            }
        });
        JMenuItem itemAltaPatrocinio = new JMenuItem("Alta de Patrocinio");
        menuEvento.add(itemAltaPatrocinio);
        itemAltaPatrocinio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                altaPatrocinioFrame.setVisible(true);
            }
        });
        JMenuItem itemAltaInstitucion = new JMenuItem("Alta de Institución");
        menuEvento.add(itemAltaInstitucion);
        itemAltaInstitucion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                altaInstitucionFrame.setVisible(true);
            }
        });
        JMenuItem itemRegistroEdicionEvento = new JMenuItem("Registro en Edición de Evento");
        menuEvento.add(itemRegistroEdicionEvento);
        itemRegistroEdicionEvento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registroEdicionEventoFrame.cargarDatos();
                registroEdicionEventoFrame.setVisible(true);
            }
        });
        JMenuItem itemAltaEdicionEvento = new JMenuItem("Alta de Edición de Evento");
        menuEvento.add(itemAltaEdicionEvento);
        itemAltaEdicionEvento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                altaEdicionEventoFrame.cargarEventos();
                altaEdicionEventoFrame.cargarOrganizadores();
                altaEdicionEventoFrame.setVisible(true);
                altaEdicionEventoFrame.toFront();
            }
        });
    }
}