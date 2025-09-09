package presentacion;

import javax.swing.*;

import excepciones.UsuarioNoExisteException;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.*;
import logica.*;
import java.time.LocalDate;
import logica.Interfaces.*;
import logica.Clases.*;
import logica.Datatypes.*;

public class ConsultaUsuario extends JInternalFrame {
    private IControladorUsuario controlUsr;
    private IControladorEvento controlEvt;
    private Vector<String> usuarios;
    private JComboBox<String> comboUsuarios;
    private JButton btnCerrar;
    private JPanel panelSeleccion;
    private JPanel panelDatos;
    private CardLayout cardLayout;
    private boolean habilitarAutoSeleccion = false;

    public ConsultaUsuario(IControladorUsuario icu, IControladorEvento iCE) {
        controlUsr = icu;
        controlEvt  = iCE;
        cardLayout = new CardLayout();
        getContentPane().setLayout(cardLayout);

        // Divido en dos paneles, uno para seleccionar el usuario y otro para mostrar los datos
        // Panel para seleccionar el usuario
        panelSeleccion = new JPanel(null);
        getContentPane().add(panelSeleccion, "SELECCION");
        
        // Panel con los datos del usuario
        panelDatos = new JPanel(new BorderLayout());
        getContentPane().add(panelDatos, "DATOS");
        
        // Configuración básica
        setTitle("Listar Usuarios");
        setResizable(true);
        setMaximizable(true);
        setIconifiable(true);
        setClosable(true);
        setBounds(new Rectangle(300, 300, 500, 200));

        JLabel label = new JLabel("Seleccione un usuario:");
        label.setBounds(89, 51, 150, 20);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panelSeleccion.add(label);
  
        // Agrego todos los usuarios a un vector
        usuarios = new Vector<>();
        usuarios.addAll(controlUsr.listarUsuarios().keySet());
        
        // Agrego ese vector a un combobox para que seleccione que usuario quiere
        comboUsuarios = new JComboBox<>(usuarios);
        comboUsuarios.setBounds(284, 49, 180, 25);
        panelSeleccion.add(comboUsuarios);
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(284, 109, 120, 30);
        panelSeleccion.add(btnCancelar);
        
        btnCancelar.addActionListener(e -> dispose());

        comboUsuarios.addItemListener(e -> {
            if (!habilitarAutoSeleccion) return;
            if (e.getStateChange() == ItemEvent.SELECTED) {
                dispararSeleccion();
            }
        });

        habilitarAutoSeleccion = true;
    }

    private void dispararSeleccion() {
        cardLayout.show(getContentPane(), "DATOS");
        String usuarioSeleccionado = (String) comboUsuarios.getSelectedItem();

        if (usuarioSeleccionado == null || usuarioSeleccionado.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un usuario.");
            return;
        }
        // Limpiamos el panel (por si hay datos viejos)
        panelDatos.removeAll();
        setTitle("Datos del usuario");

        JPanel panelContenedor = new JPanel(new BorderLayout());
        panelDatos.add(panelContenedor, BorderLayout.CENTER);

        // Panel donde van los datos (con absolute)

        JPanel panelDatosUsuario = new JPanel();
        panelDatosUsuario.setLayout(new BoxLayout(panelDatosUsuario, BoxLayout.Y_AXIS)); 
        panelContenedor.add(panelDatosUsuario, BorderLayout.CENTER);

        JPanel panelInfoBasica = new JPanel(new GridLayout(0, 2, 10, 10)); 
        panelDatosUsuario.add(panelInfoBasica);
                
        JLabel lblNick = new JLabel("Nickname:");
        panelInfoBasica.add(lblNick);

        JTextArea txtNick = new JTextArea();
        txtNick.setEditable(false);
        panelInfoBasica.add(txtNick);

        JLabel lblNombre = new JLabel("Nombre:");
        panelInfoBasica.add(lblNombre);

        JTextArea txtNombre = new JTextArea();
        txtNombre.setEditable(false);
        panelInfoBasica.add(txtNombre);

        JLabel lblCorreo = new JLabel("Correo electrónico:");
        panelInfoBasica.add(lblCorreo);

        JTextArea txtCorreo = new JTextArea();
        txtCorreo.setEditable(false);
        panelInfoBasica.add(txtCorreo);
                
        // Agregamos un boton para cancelar la sekeccion
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> {
            cardLayout.show(getContentPane(), "SELECCION");
            setTitle("Listar Usuarios");
        });
        panelSur.add(btnCerrar);
        panelContenedor.add(panelSur, BorderLayout.SOUTH);

        Map<String, Usuario> prueba = controlUsr.listarUsuarios();
                
        if ((controlUsr.listarAsistentes() != null) && controlUsr.listarAsistentes().containsKey(usuarioSeleccionado)) {
            DTDatosUsuario datos;
            try {
                datos = controlUsr.obtenerDatosUsuario(usuarioSeleccionado); // deja precargado el usuario
            } catch (UsuarioNoExisteException e1) {
                e1.printStackTrace();
                return;
            }
            final DTDatosUsuario datosFinal = datos;
            final String nickFinal = datos.getNickname();

            txtNick.setText(datosFinal.getNickname());
            txtNombre.setText(datosFinal.getNombre());
            txtCorreo.setText(datosFinal.getEmail());
                    
            JPanel panelAsistente = new JPanel(new GridLayout(0, 2, 10, 10));
            panelDatosUsuario.add(panelAsistente);

            JLabel lblApellido = new JLabel("Apellido:");
            panelAsistente.add(lblApellido);

            JTextArea txtApellido = new JTextArea(datosFinal.getApellido());
            txtApellido.setEditable(false);
            panelAsistente.add(txtApellido);

            JLabel lblFechaNac = new JLabel("Fecha de nacimiento:");
            panelAsistente.add(lblFechaNac);

            JTextArea txtFecha = new JTextArea(String.valueOf(datosFinal.getFechaNac()));
            txtFecha.setEditable(false);
            panelAsistente.add(txtFecha);

            DefaultComboBoxModel<String> modelReg = new DefaultComboBoxModel<>();
            java.util.List<DTRegistro> listaRegs = new java.util.ArrayList<>(datosFinal.getRegistros());
            for (DTRegistro r : listaRegs) {
                modelReg.addElement(r.getId() + " – " + r.getEdicion() + " – " + r.getTipoRegistro());
            }
            JComboBox<String> comboRegs = new JComboBox<>(modelReg);
            comboRegs.setPreferredSize(new Dimension(380, 26));
            panelDatosUsuario.add(comboRegs);

            comboRegs.addItemListener(ev -> {
                if (ev.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                    int idx = comboRegs.getSelectedIndex();
                    if (idx >= 0) {
                        DTRegistro r = listaRegs.get(idx);
                        try {
                            controlUsr.obtenerDatosUsuario(nickFinal);  // deja usuario consultado
                            controlUsr.seleccionarRegistro(r.getId());   // deja registro seleccionado
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this, "No se pudo seleccionar el registro: " + ex.getMessage());
                            return;
                        }
                        ConsultaRegistroFrame frame = new ConsultaRegistroFrame(controlUsr, controlEvt);
                        getDesktopPane().add(frame);
                        frame.setVisible(true);
                    }
                }
            });

            panelDatosUsuario.revalidate();
            panelDatosUsuario.repaint();

        } else if ((controlUsr.listarOrganizadores() != null) && controlUsr.listarOrganizadores().containsKey(usuarioSeleccionado)) {
            DTDatosUsuario datos;
            try {
                datos = controlUsr.obtenerDatosUsuario(usuarioSeleccionado); // deja precargado el usuario
            } catch (UsuarioNoExisteException e1) {
                e1.printStackTrace();
                return;
            }
            final DTDatosUsuario datosFinal = datos;
            final String nickFinal = datos.getNickname();

            txtNick.setText(datosFinal.getNickname());
            txtNombre.setText(datosFinal.getNombre());
            txtCorreo.setText(datosFinal.getEmail());

            JPanel panelOrganizador = new JPanel(new GridLayout(0, 2, 10, 10)); 
            panelDatosUsuario.add(panelOrganizador);

            JLabel lblDesc = new JLabel("Descripción:");
            panelOrganizador.add(lblDesc);

            JTextArea txtDesc = new JTextArea(datosFinal.getDesc());
            txtDesc.setEditable(false);
            panelOrganizador.add(txtDesc);

            JLabel lblLink = new JLabel("Link:");
            panelOrganizador.add(lblLink);
                    
            JTextArea txtLink = new JTextArea(datosFinal.getLink());
            txtLink.setEditable(false);
            panelOrganizador.add(txtLink);

            DefaultComboBoxModel<String> modelEd = new DefaultComboBoxModel<>();
            java.util.List<DTEdicion> listaEds = new java.util.ArrayList<>(datosFinal.getEdiciones());
            for (DTEdicion ed : listaEds) {
                modelEd.addElement(ed.getNombre() + " (" + ed.getSigla() + ")");
            }
            JComboBox<String> comboEds = new JComboBox<>(modelEd);
            comboEds.setPreferredSize(new Dimension(380, 26));
            panelDatosUsuario.add(comboEds);

            // ni bien se selecciona, abrimos el frame de detalle (Consulta de Edición de Evento)
            comboEds.addItemListener(ev -> {
                if (ev.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                    int idx = comboEds.getSelectedIndex();
                    if (idx >= 0) {
                        DTEdicion ed = listaEds.get(idx);
                        try {
                            controlUsr.obtenerDatosUsuario(nickFinal);  // deja usuario consultado
                            controlEvt.seleccionarEdicion(ed.getSigla()); // deja edición seleccionada
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this, "No se pudo seleccionar la edición: " + ex.getMessage());
                            return;
                        }
                        ConsultaEdicionEventoFrame frame = new ConsultaEdicionEventoFrame(controlUsr, controlEvt);
                        getDesktopPane().add(frame);
                        frame.setVisible(true);
                    }
                }
            });

            panelDatosUsuario.revalidate();
            panelDatosUsuario.repaint();

        }
        // Mostramos el panel que tiene los datos
        cardLayout.show(getContentPane(), "DATOS");
        panelDatos.revalidate();
        panelDatos.repaint();
    }

    public void cargarUsuarios() throws UsuarioNoExisteException {
        // Este método se usa para obtener los usuarios del sistema cada vez que ingresemos al caso de uso, sino solo lo obtiene al inicio y nunca más
        DefaultComboBoxModel<String> model;
        usuarios.clear();
        usuarios.addAll(controlUsr.listarUsuarios().keySet());
        model = new DefaultComboBoxModel<String>(usuarios);
        habilitarAutoSeleccion = false;
        comboUsuarios.setModel(model);
        comboUsuarios.setSelectedIndex(-1);
        habilitarAutoSeleccion = true;
    }
}