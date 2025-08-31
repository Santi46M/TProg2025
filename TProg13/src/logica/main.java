package logica;

import javax.swing.*;
import excepciones.UsuarioNoExisteException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import presentacion.*;


public class main {

    private JFrame frame;
    private JDesktopPane desktopPane;
    private IControladorUsuario ICU;
    private IControladorEvento ICE;
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


    public static void main(String[] args) {
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
        ICE = fabrica.getInstance().getIControladorEvento();
        creUsrInternalFrame = new AltaUsuarioFrame(ICU, ICE);
        creUsrInternalFrame.setVisible(false);
        conUsrInternalFrame = new ConsultaUsuario(ICU, ICE);
        conUsrInternalFrame.setVisible(false);
        consultaEventoFrame = new ConsultaEventoFrame(ICU, ICE);
        consultaEventoFrame.setVisible(false);
        consultaEdicionEventoFrame = new ConsultaEdicionEventoFrame(ICU, ICE);
        consultaEdicionEventoFrame.setVisible(false);
        consultaTipoRegistroFrame = new ConsultaTipoRegistroFrame(ICU, ICE);
        consultaTipoRegistroFrame.setVisible(false);
        consultaRegistroFrame = new ConsultaRegistroFrame(ICU, ICE);
        consultaRegistroFrame.setVisible(false);
        consultaPatrocinioFrame = new ConsultaPatrocinioFrame(ICU, ICE);
        consultaPatrocinioFrame.setVisible(false);
        altaEventoFrame = new AltaEventoFrame(ICU, ICE);
        altaEventoFrame.setVisible(false);
        altaTipoRegistroFrame = new AltaTipoRegistroFrame(ICU, ICE);
        altaTipoRegistroFrame.setVisible(false);
        altaPatrocinioFrame = new AltaPatrocinioFrame(ICU, ICE);
        altaPatrocinioFrame.setVisible(false);
        altaInstitucionFrame = new AltaInstitucionFrame(ICU, ICE);
        altaInstitucionFrame.setVisible(false);
        registroEdicionEventoFrame = new RegistroEdicionEventoFrame(ICU, ICE);
        registroEdicionEventoFrame.setVisible(false);
        altaEdicionEventoFrame = new AltaEdicionEvento(ICU, ICE);
        altaEdicionEventoFrame.setVisible(false);
        
        frame.getContentPane().setLayout(null);
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
        
        try {
            CargaDatosPrueba.cargar();
            System.out.println("Datos de prueba cargados correctamente.");
        } catch (Exception ex) {
            System.err.println("Error al cargar datos de prueba: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void initialize() {
    	
    	
        frame = new JFrame();
        frame.setTitle("Eventos.uy");
        frame.setBounds(200, 200, 800, 660);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        frame.setLayout(new BorderLayout());
        desktopPane = new JDesktopPane();
        desktopPane.setBounds(0, 0, 800, 600);
        frame.getContentPane().add(desktopPane, BorderLayout.CENTER);


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
                    if (creUsrInternalFrame == null || creUsrInternalFrame.isClosed()) {
                        creUsrInternalFrame = new AltaUsuarioFrame(ICU, ICE);
                        desktopPane.add(creUsrInternalFrame);
                    }
                    creUsrInternalFrame.cargarInstituciones();
                    creUsrInternalFrame.setVisible(true);
                    creUsrInternalFrame.toFront();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        JMenuItem itemConsultaUsuario = new JMenuItem("Consulta de Usuario");
        menuUsuario.add(itemConsultaUsuario);
        itemConsultaUsuario.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (conUsrInternalFrame == null || conUsrInternalFrame.isClosed()) {
                        conUsrInternalFrame = new ConsultaUsuario(ICU, ICE);
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
        // Mover Alta de Institución al menú Usuario
        JMenuItem itemAltaInstitucion = new JMenuItem("Alta de Institución");
        menuUsuario.add(itemAltaInstitucion);
        itemAltaInstitucion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (altaInstitucionFrame == null || altaInstitucionFrame.isClosed()) {
                        altaInstitucionFrame = new AltaInstitucionFrame(ICU, ICE);
                        desktopPane.add(altaInstitucionFrame);
                    }
                    altaInstitucionFrame.setVisible(true);
                    altaInstitucionFrame.toFront();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        JMenuItem itemConsultaEvento = new JMenuItem("Consulta de Evento");
        menuEvento.add(itemConsultaEvento);
        itemConsultaEvento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (consultaEventoFrame == null || consultaEventoFrame.isClosed()) {
                        consultaEventoFrame = new ConsultaEventoFrame(ICU, ICE);
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
                        consultaEdicionEventoFrame = new ConsultaEdicionEventoFrame(ICU, ICE);
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
                try {
                    if (consultaTipoRegistroFrame == null || consultaTipoRegistroFrame.isClosed()) {
                        consultaTipoRegistroFrame = new ConsultaTipoRegistroFrame(ICU, ICE);
                        desktopPane.add(consultaTipoRegistroFrame);
                    }
                    consultaTipoRegistroFrame.cargarEventos();
                    consultaTipoRegistroFrame.setVisible(true);
                    consultaTipoRegistroFrame.toFront();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        JMenuItem itemConsultaRegistro = new JMenuItem("Consulta de Registro");
        menuEvento.add(itemConsultaRegistro);
        itemConsultaRegistro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (consultaRegistroFrame == null || consultaRegistroFrame.isClosed()) {
                        consultaRegistroFrame = new ConsultaRegistroFrame(ICU, ICE);
                        desktopPane.add(consultaRegistroFrame);
                    }
                    consultaRegistroFrame.cargarUsuarios();
                    consultaRegistroFrame.setVisible(true);
                    consultaRegistroFrame.toFront();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        JMenuItem itemConsultaPatrocinio = new JMenuItem("Consulta de Patrocinio");
        menuEvento.add(itemConsultaPatrocinio);
        itemConsultaPatrocinio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (consultaPatrocinioFrame == null || consultaPatrocinioFrame.isClosed()) {
                        consultaPatrocinioFrame = new ConsultaPatrocinioFrame(ICU, ICE);
                        desktopPane.add(consultaPatrocinioFrame);
                    }
                    consultaPatrocinioFrame.cargarDatos();
                    consultaPatrocinioFrame.setVisible(true);
                    consultaPatrocinioFrame.toFront();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        JMenuItem itemAltaEvento = new JMenuItem("Alta de Evento");
        menuEvento.add(itemAltaEvento);
        itemAltaEvento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (altaEventoFrame == null || altaEventoFrame.isClosed()) {
                        altaEventoFrame = new AltaEventoFrame(ICU, ICE);
                        desktopPane.add(altaEventoFrame);
                    }
                    altaEventoFrame.cargarCategorias();
                    altaEventoFrame.setVisible(true);
                    altaEventoFrame.toFront();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        JMenuItem itemAltaTipoRegistro = new JMenuItem("Alta de Tipo de Registro");
        menuEvento.add(itemAltaTipoRegistro);
        itemAltaTipoRegistro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (altaTipoRegistroFrame == null || altaTipoRegistroFrame.isClosed()) {
                        altaTipoRegistroFrame = new AltaTipoRegistroFrame(ICU, ICE);
                        desktopPane.add(altaTipoRegistroFrame);
                    }
                    altaTipoRegistroFrame.cargarEventos();
                    altaTipoRegistroFrame.setVisible(true);
                    altaTipoRegistroFrame.toFront();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        JMenuItem itemAltaPatrocinio = new JMenuItem("Alta de Patrocinio");
        menuEvento.add(itemAltaPatrocinio);
        itemAltaPatrocinio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (altaPatrocinioFrame == null || altaPatrocinioFrame.isClosed()) {
                        altaPatrocinioFrame = new AltaPatrocinioFrame(ICU, ICE);
                        desktopPane.add(altaPatrocinioFrame);
                    }
                    altaPatrocinioFrame.cargarDatos();
                    altaPatrocinioFrame.setVisible(true);
                    altaPatrocinioFrame.toFront();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        JMenuItem itemAltaEdicionEvento = new JMenuItem("Alta de Edición de Evento");
        menuEvento.add(itemAltaEdicionEvento);
        itemAltaEdicionEvento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (altaEdicionEventoFrame == null || altaEdicionEventoFrame.isClosed()) {
                        altaEdicionEventoFrame = new AltaEdicionEvento(ICU, ICE);
                        desktopPane.add(altaEdicionEventoFrame);
                    }
                    altaEdicionEventoFrame.cargarEventos();
                    altaEdicionEventoFrame.cargarOrganizadores();
                    altaEdicionEventoFrame.setVisible(true);
                    altaEdicionEventoFrame.toFront();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        JMenuItem itemRegistroEdicionEvento = new JMenuItem("Registro/Edición de Evento");
        menuEvento.add(itemRegistroEdicionEvento);
        itemRegistroEdicionEvento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (registroEdicionEventoFrame == null || registroEdicionEventoFrame.isClosed()) {
                        registroEdicionEventoFrame = new RegistroEdicionEventoFrame(ICU, ICE);
                        desktopPane.add(registroEdicionEventoFrame);
                    }
                    registroEdicionEventoFrame.cargarDatos(); // <-- Llenar combos correctamente
                    registroEdicionEventoFrame.setVisible(true);
                    registroEdicionEventoFrame.toFront();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private String[] getEventos() {
        // Carga dinámica como en AltaEdicionEvento
        logica.ControladorEvento controlador = new logica.ControladorEvento();
        java.util.List<logica.DTEvento> eventos = controlador.listarEventos();
        String[] nombres = new String[eventos.size()];
        for (int i = 0; i < eventos.size(); i++) {
            nombres[i] = eventos.get(i).getNombre();
        }
        return nombres;
    }
    private String[][] getEdicionesPorEvento() {
        logica.ControladorEvento controlador = new logica.ControladorEvento();
        java.util.List<logica.DTEvento> eventos = controlador.listarEventos();
        String[][] ediciones = new String[eventos.size()][];
        for (int i = 0; i < eventos.size(); i++) {
            java.util.List<String> eds = controlador.listarEdicionesEvento(eventos.get(i).getNombre());
            ediciones[i] = eds.toArray(new String[0]);
        }
        return ediciones;
    }
    private String[][] getTiposPorEdicion() {
        var eventos = logica.ManejadorEvento.getInstancia().obtenerEventos();
        java.util.List<String[]> tiposList = new java.util.ArrayList<>();
        for (var e : eventos.values()) {
            for (var ed : e.getEdiciones().values()) {
                java.util.List<String> nombres = new java.util.ArrayList<>();
                for (logica.TipoRegistro tipo : ed.getTiposRegistro()) {
                    nombres.add(tipo.getNombre());
                }
                tiposList.add(nombres.toArray(new String[0]));
            }
        }
        return tiposList.toArray(new String[0][0]);
    }
    private double[] getCostosTipoRegistro() {
        java.util.List<Double> costos = new java.util.ArrayList<>();
        var eventos = logica.ManejadorEvento.getInstancia().obtenerEventos();
        for (var e : eventos.values()) {
            for (var ed : e.getEdiciones().values()) {
                for (logica.TipoRegistro tipo : ed.getTiposRegistro()) {
                    costos.add((double) tipo.getCosto());
                }
            }
        }
        return costos.stream().mapToDouble(Double::doubleValue).toArray();
    }

    private String[] getInstituciones() {
        return logica.manejadorUsuario.getInstancia().getInstituciones().toArray(new String[0]);
    }
    private java.util.Set<String> getCodigosPatrocinio() {
        java.util.Set<String> codigos = new java.util.HashSet<>();
        for (var p : logica.manejadorAuxiliar.getInstancia().listarPatrocinios()) {
            if (p != null && p.getCodigoPatrocinio() != null)
                codigos.add(p.getCodigoPatrocinio().toLowerCase());
        }
        return codigos;
    }
    private java.util.Set<String> getPatrociniosInstitucionEdicion() {
        java.util.Set<String> claves = new java.util.HashSet<>();
        for (var p : logica.manejadorAuxiliar.getInstancia().listarPatrocinios()) {
            if (p != null && p.getInstitucion() != null && p.getEdicion() != null && p.getInstitucion().getNombre() != null && p.getEdicion().getNombre() != null)
                claves.add(p.getInstitucion().getNombre().toLowerCase() + "-" + p.getEdicion().getNombre().toLowerCase());
        }
        return claves;
    }
    private String[][] getPatrociniosPorEdicion() {
        var eventos = logica.ManejadorEvento.getInstancia().obtenerEventos();
        java.util.List<String[]> patrociniosList = new java.util.ArrayList<>();
        for (var e : eventos.values()) {
            for (var ed : e.getEdiciones().values()) {
                java.util.List<String> pats = new java.util.ArrayList<>();
                for (var p : ed.getPatrocinios()) {
                    pats.add(p.getCodigoPatrocinio());
                }
                patrociniosList.add(pats.toArray(new String[0]));
            }
        }
        return patrociniosList.toArray(new String[0][0]);
    }
    private String[][] getDatosPatrocinio() {
        var eventos = logica.ManejadorEvento.getInstancia().obtenerEventos();
        java.util.List<String[]> datosList = new java.util.ArrayList<>();
        for (var e : eventos.values()) {
            for (var ed : e.getEdiciones().values()) {
                java.util.List<String> datosPat = new java.util.ArrayList<>();
                for (var p : ed.getPatrocinios()) {
                    String datos = "Institución: " + p.getInstitucion().getNombre() +
                        "\nNivel: " + p.getNivel().toString() +
                        "\nTipo Registro: " + p.getTipoRegistro().getNombre() +
                        "\nAporte: " + p.getAporte() +
                        "\nFecha: " + p.getFechaPatrocinio() +
                        "\nCantidad Registros Gratuitos: " + p.getCantidadRegistros() +
                        "\nCódigo: " + p.getCodigoPatrocinio();
                    datosPat.add(datos);
                }
                datosList.add(datosPat.toArray(new String[0]));
            }
        }
        return datosList.toArray(new String[0][0]);
    }
}