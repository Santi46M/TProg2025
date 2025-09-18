package presentacion;

import javax.swing.*;
import logica.CargaDatosPrueba;
import logica.Interfaces.*;
import logica.fabrica;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class main {

    private static final Color P_BG_APP   = new Color(240, 248, 255);
    private static final Color P_MENU_BG  = new Color(230, 236, 246);
    private static final Color P_MENU_FG  = new Color(25, 25, 25);
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
    private ModificarDatosUsuarioFrame modificarDatosUsuarioFrame;

    public static void main(String[] args) {
        try {
            boolean puesto = false;
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Metal".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    puesto = true;
                    break;
                }
            }
            if (!puesto) UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignore) {}

        EventQueue.invokeLater(() -> {
            try {
                main window = new main();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
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
        desktopPane.setBackground(P_BG_APP);
        frame.getContentPane().add(desktopPane, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setOpaque(true);
        menuBar.setBackground(P_MENU_BG);
        menuBar.setForeground(P_MENU_FG);
        frame.setJMenuBar(menuBar);

        JMenu menuSistema = new JMenu("Sistema");
        styleMenu(menuSistema);
        menuBar.add(menuSistema);

        JMenuItem itemCargaDatos = new JMenuItem("Cargar Datos Iniciales");
        styleMenuItem(itemCargaDatos);
        menuSistema.add(itemCargaDatos);
        itemCargaDatos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    CargaDatosPrueba.cargar();
                } catch (Exception ex) {
                }
            }
        });

        JMenu menuUsuario = new JMenu("Usuario");
        styleMenu(menuUsuario);
        menuBar.add(menuUsuario);
        JMenu menuEvento = new JMenu("Evento");
        styleMenu(menuEvento);
        menuBar.add(menuEvento);

        JMenuItem itemAltaUsuario = new JMenuItem("Alta de Usuario");
        styleMenuItem(itemAltaUsuario);
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
        styleMenuItem(itemConsultaUsuario);
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
            	} catch (Exception ex) {
            	    ex.printStackTrace();
            	}
            }
        });

        JMenuItem itemAltaInstitucion = new JMenuItem("Alta de Institución");
        styleMenuItem(itemAltaInstitucion);
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

        JMenuItem itemModificarUsuario = new JMenuItem("Modificar Datos de Usuario");
        styleMenuItem(itemModificarUsuario);
        menuUsuario.add(itemModificarUsuario);
        itemModificarUsuario.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    java.util.Map<String, logica.Clases.Usuario> usuariosMap = ICU.listarUsuarios();
                    String[] usuarios = usuariosMap.keySet().toArray(new String[0]);
                    String[][] datosUsuarios = new String[usuarios.length][7];
                    for (int i = 0; i < usuarios.length; i++) {
                        logica.Clases.Usuario u = usuariosMap.get(usuarios[i]);
                        datosUsuarios[i][0] = u.getNickname();
                        datosUsuarios[i][1] = u.getEmail();
                        datosUsuarios[i][2] = u.getNombre();
                        if (u instanceof logica.Clases.Asistente) {
                            logica.Clases.Asistente a = (logica.Clases.Asistente) u;
                            datosUsuarios[i][3] = a.getApellido() != null ? a.getApellido() : "";
                            datosUsuarios[i][4] = a.getFechaDeNacimiento() != null ? a.getFechaDeNacimiento().toString() : "";
                            datosUsuarios[i][5] = "";
                            datosUsuarios[i][6] = a.getInstitucion() != null ? a.getInstitucion().getNombre() : "";
                        } else if (u instanceof logica.Clases.Organizador) {
                            logica.Clases.Organizador o = (logica.Clases.Organizador) u;
                            datosUsuarios[i][3] = "";
                            datosUsuarios[i][4] = "";
                            datosUsuarios[i][5] = o.getDesc() != null ? o.getDesc() : "";
                            datosUsuarios[i][6] = o.getLink() != null ? o.getLink() : "";
                        } else {
                            datosUsuarios[i][3] = "";
                            datosUsuarios[i][4] = "";
                            datosUsuarios[i][5] = "";
                            datosUsuarios[i][6] = "";
                        }
                    }
                    if (modificarDatosUsuarioFrame == null || modificarDatosUsuarioFrame.isClosed()) {
                        modificarDatosUsuarioFrame = new ModificarDatosUsuarioFrame(ICU, usuarios, datosUsuarios);
                        desktopPane.add(modificarDatosUsuarioFrame);
                    }
                    modificarDatosUsuarioFrame.setVisible(true);
                    modificarDatosUsuarioFrame.toFront();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        JMenuItem itemConsultaEvento = new JMenuItem("Consulta de Evento");
        styleMenuItem(itemConsultaEvento);
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
     styleMenuItem(itemConsultaEdicion);
     menuEvento.add(itemConsultaEdicion);

     itemConsultaEdicion.addActionListener(e -> {
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
     });

        JMenuItem itemConsultaTipoRegistro = new JMenuItem("Consulta de Tipo de Registro");
        styleMenuItem(itemConsultaTipoRegistro);
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
        styleMenuItem(itemConsultaRegistro);
        menuUsuario.add(itemConsultaRegistro);
        itemConsultaRegistro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (consultaRegistroFrame == null || consultaRegistroFrame.isClosed()) {
                        consultaRegistroFrame = new ConsultaRegistroFrame(ICU, ICE);
                        desktopPane.add(consultaRegistroFrame);
                    }
                    consultaRegistroFrame.cargarAsistentes(); 
                    consultaRegistroFrame.setVisible(true);
                    consultaRegistroFrame.toFront();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        JMenuItem itemConsultaPatrocinio = new JMenuItem("Consulta de Patrocinio");
        styleMenuItem(itemConsultaPatrocinio);
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
        styleMenuItem(itemAltaEvento);
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
        styleMenuItem(itemAltaTipoRegistro);
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
        styleMenuItem(itemAltaPatrocinio);
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
        styleMenuItem(itemAltaEdicionEvento);
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
        styleMenuItem(itemRegistroEdicionEvento);
        menuUsuario.add(itemRegistroEdicionEvento);
        itemRegistroEdicionEvento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (registroEdicionEventoFrame == null || registroEdicionEventoFrame.isClosed()) {
                        registroEdicionEventoFrame = new RegistroEdicionEventoFrame(ICU, ICE);
                        desktopPane.add(registroEdicionEventoFrame);
                    }
                    registroEdicionEventoFrame.cargarDatos(); 
                    registroEdicionEventoFrame.setVisible(true);
                    registroEdicionEventoFrame.toFront();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void styleMenu(JMenu m) {
        m.setOpaque(true);
        m.setBackground(P_MENU_BG);
        m.setForeground(P_MENU_FG);
    }
    private void styleMenuItem(JMenuItem mi) {
        mi.setOpaque(true);
        mi.setBackground(P_MENU_BG);
        mi.setForeground(P_MENU_FG);
    }

    private void cambiarLookAndFeel(String nombre) {
        try {
            String clase = null;
            if ("System".equalsIgnoreCase(nombre)) {
                clase = UIManager.getSystemLookAndFeelClassName();
            } else {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if (info.getName().equalsIgnoreCase(nombre)) {
                        clase = info.getClassName();
                        break;
                    }
                }
            }
            if (clase == null) {
                JOptionPane.showMessageDialog(frame,
                    "El Look&Feel \"" + nombre + "\" no está disponible en este sistema.",
                    "Apariencia", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            UIManager.setLookAndFeel(clase);
            SwingUtilities.updateComponentTreeUI(frame);

            if (frame.getJMenuBar() != null) {
                frame.getJMenuBar().setOpaque(true);
                frame.getJMenuBar().setBackground(P_MENU_BG);
                frame.getJMenuBar().setForeground(P_MENU_FG);
                for (MenuElement e : frame.getJMenuBar().getSubElements()) {
                    if (e.getComponent() instanceof JMenu m) styleMenu(m);
                    if (e.getComponent() instanceof JMenuItem mi) styleMenuItem(mi);
                }
            }
            desktopPane.setBackground(P_BG_APP);
            frame.pack();            
            frame.setSize(800, 660); 
            frame.repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "No se pudo aplicar el Look&Feel.\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    // =========================================

    private String[] getEventos() {
        logica.Controladores.ControladorEvento controlador = new logica.Controladores.ControladorEvento();
        java.util.List<logica.Datatypes.DTEvento> eventos = controlador.listarEventos();
        String[] nombres = new String[eventos.size()];
        for (int i = 0; i < eventos.size(); i++) {
            nombres[i] = eventos.get(i).getNombre();
        }
        return nombres;
    }
    private String[][] getEdicionesPorEvento() {
        logica.Controladores.ControladorEvento controlador = new logica.Controladores.ControladorEvento();
        java.util.List<logica.Datatypes.DTEvento> eventos = controlador.listarEventos();
        String[][] ediciones = new String[eventos.size()][];
        for (int i = 0; i < eventos.size(); i++) {
            java.util.List<String> eds = controlador.listarEdicionesEvento(eventos.get(i).getNombre());
            ediciones[i] = eds.toArray(new String[0]);
        }
        return ediciones;
    }
    private String[][] getTiposPorEdicion() {
        var eventos = logica.Manejadores.ManejadorEvento.getInstancia().obtenerEventos();
        java.util.List<String[]> tiposList = new java.util.ArrayList<>();
        for (var e : eventos.values()) {
            for (var ed : e.getEdiciones().values()) {
                java.util.List<String> nombres = new java.util.ArrayList<>();
                for (logica.Clases.TipoRegistro tipo : ed.getTiposRegistro()) {
                    nombres.add(tipo.getNombre());
                }
                tiposList.add(nombres.toArray(new String[0]));
            }
        }
        return tiposList.toArray(new String[0][0]);
    }
    private double[] getCostosTipoRegistro() {
        java.util.List<Double> costos = new java.util.ArrayList<>();
        var eventos = logica.Manejadores.ManejadorEvento.getInstancia().obtenerEventos();
        for (var e : eventos.values()) {
            for (var ed : e.getEdiciones().values()) {
                for (logica.Clases.TipoRegistro tipo : ed.getTiposRegistro()) {
                    costos.add((double) tipo.getCosto());
                }
            }
        }
        return costos.stream().mapToDouble(Double::doubleValue).toArray();
    }

    private String[] getInstituciones() {
        return logica.Manejadores.manejadorUsuario.getInstancia().getInstituciones().toArray(new String[0]);
    }
    private java.util.Set<String> getCodigosPatrocinio() {
        java.util.Set<String> codigos = new java.util.HashSet<>();
        for (var p : logica.Manejadores.manejadorAuxiliar.getInstancia().listarPatrocinios()) {
            if (p != null && p.getCodigoPatrocinio() != null)
                codigos.add(p.getCodigoPatrocinio().toLowerCase());
        }
        return codigos;
    }
    private java.util.Set<String> getPatrociniosInstitucionEdicion() {
        java.util.Set<String> claves = new java.util.HashSet<>();
        for (var p : logica.Manejadores.manejadorAuxiliar.getInstancia().listarPatrocinios()) {
            if (p != null && p.getInstitucion() != null && p.getEdicion() != null && p.getInstitucion().getNombre() != null && p.getEdicion().getNombre() != null)
                claves.add(p.getInstitucion().getNombre().toLowerCase() + "-" + p.getEdicion().getNombre().toLowerCase());
        }
        return claves;
    }
    private String[][] getPatrociniosPorEdicion() {
        var eventos = logica.Manejadores.ManejadorEvento.getInstancia().obtenerEventos();
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
        var eventos = logica.Manejadores.ManejadorEvento.getInstancia().obtenerEventos();
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