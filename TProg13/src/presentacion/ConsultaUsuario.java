package presentacion;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Vector;

import excepciones.UsuarioNoExisteException;
import logica.Clases.Usuario;
import logica.Datatypes.DTDatosUsuario;
import logica.Datatypes.DTEdicion;
import logica.Datatypes.DTRegistro;
import logica.Interfaces.IControladorEvento;
import logica.Interfaces.IControladorUsuario;

public class ConsultaUsuario extends JInternalFrame {

    private final IControladorUsuario controlUsr;
    private final IControladorEvento  controlEvt;

    private Vector<String> usuarios;
    private JComboBox<String> comboUsuarios;

    private JPanel panelSeleccion;
    private JPanel panelDatos;
    private CardLayout cardLayout;

    // Flag para evitar que los listeners se disparen mientras poblamos combos
    private boolean cargandoCombo = false;

    public ConsultaUsuario(IControladorUsuario icu, IControladorEvento iCE) {
        this.controlUsr = icu;
        this.controlEvt  = iCE;

        cardLayout = new CardLayout();
        getContentPane().setLayout(cardLayout);

        // Panel selección
        panelSeleccion = new JPanel(null);
        getContentPane().add(panelSeleccion, "SELECCION");

        // Panel datos
        panelDatos = new JPanel(new BorderLayout());
        getContentPane().add(panelDatos, "DATOS");

        setTitle("Listar Usuarios");
        setResizable(true);
        setMaximizable(true);
        setIconifiable(true);
        setClosable(true);
        setBounds(new Rectangle(300, 300, 520, 210));

        JLabel label = new JLabel("Seleccione un usuario:");
        label.setBounds(50, 51, 180, 20);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        panelSeleccion.add(label);

        usuarios = new Vector<>();
        usuarios.addAll(controlUsr.listarUsuarios().keySet());

        comboUsuarios = new JComboBox<>(usuarios);
        comboUsuarios.setBounds(240, 49, 200, 25);
        panelSeleccion.add(comboUsuarios);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(240, 109, 120, 30);
        btnCancelar.addActionListener(e -> dispose());
        panelSeleccion.add(btnCancelar);

        // ActionListener: abre los datos del usuario elegido
        comboUsuarios.addActionListener(e -> {
            if (cargandoCombo) return;
            String sel = (String) comboUsuarios.getSelectedItem();
            if (sel != null && !sel.isEmpty()) {
                mostrarDatosUsuario(sel);
            }
        });
    }

    private void mostrarDatosUsuario(String nickname) {
        // Cambiamos a la vista de datos
        cardLayout.show(getContentPane(), "DATOS");

        // Limpiamos content
        panelDatos.removeAll();
        setTitle("Datos del usuario");

        JPanel panelContenedor   = new JPanel(new BorderLayout());
        JPanel panelDatosUsuario = new JPanel();
        panelDatosUsuario.setLayout(new BoxLayout(panelDatosUsuario, BoxLayout.Y_AXIS));
        panelDatos.add(panelContenedor, BorderLayout.CENTER);
        panelContenedor.add(panelDatosUsuario, BorderLayout.CENTER);

        JPanel panelInfoBasica = new JPanel(new GridLayout(0, 2, 10, 10));
        panelDatosUsuario.add(panelInfoBasica);

        JLabel lblNick   = new JLabel("Nickname:");
        JLabel lblNombre = new JLabel("Nombre:");
        JLabel lblCorreo = new JLabel("Correo electrónico:");

        JTextArea txtNick   = new JTextArea(); txtNick.setEditable(false);
        JTextArea txtNombre = new JTextArea(); txtNombre.setEditable(false);
        JTextArea txtCorreo = new JTextArea(); txtCorreo.setEditable(false);

        panelInfoBasica.add(lblNick);   panelInfoBasica.add(txtNick);
        panelInfoBasica.add(lblNombre); panelInfoBasica.add(txtNombre);
        panelInfoBasica.add(lblCorreo); panelInfoBasica.add(txtCorreo);

        // Botón cerrar -> vuelve a SELECCION
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> {
            cardLayout.show(getContentPane(), "SELECCION");
            setTitle("Listar Usuarios");
            // opcional: limpiar selección del combo principal
            cargandoCombo = true;
            comboUsuarios.setSelectedIndex(-1);
            comboUsuarios.setSelectedItem(null);
            cargandoCombo = false;
        });
        panelSur.add(btnCerrar);
        panelContenedor.add(panelSur, BorderLayout.SOUTH);

        Map<String, Usuario> todos = controlUsr.listarUsuarios();
        if (!todos.containsKey(nickname)) {
            JOptionPane.showMessageDialog(this, "Usuario no existe.");
            return;
        }

        // Obtenemos DTO
        DTDatosUsuario datos;
        try {
            datos = controlUsr.obtenerDatosUsuario(nickname);
        } catch (UsuarioNoExisteException ex) {
            JOptionPane.showMessageDialog(this, "Usuario no existe.");
            return;
        }

        txtNick.setText(datos.getNickname());
        txtNombre.setText(datos.getNombre());
        txtCorreo.setText(datos.getEmail());

        // ¿Es asistente?
        if (controlUsr.listarAsistentes() != null &&
            controlUsr.listarAsistentes().containsKey(nickname)) {

            JPanel panelAsistente = new JPanel(new GridLayout(0, 2, 10, 10));
            panelDatosUsuario.add(panelAsistente);

            panelAsistente.add(new JLabel("Apellido:"));
            JTextArea txtApellido = new JTextArea(datos.getApellido());
            txtApellido.setEditable(false);
            panelAsistente.add(txtApellido);

            panelAsistente.add(new JLabel("Fecha de nacimiento:"));
            JTextArea txtFecha = new JTextArea(String.valueOf(datos.getFechaNac()));
            txtFecha.setEditable(false);
            panelAsistente.add(txtFecha);

            // Combo de registros
            DefaultComboBoxModel<String> modelReg = new DefaultComboBoxModel<>();
            java.util.List<DTRegistro> listaRegs = new java.util.ArrayList<>(datos.getRegistros());
            for (DTRegistro r : listaRegs) {
                modelReg.addElement(r.getId() + " – " + r.getEdicion() + " – " + r.getTipoRegistro());
            }
            JComboBox<String> comboRegs = new JComboBox<>(modelReg);
            comboRegs.setPreferredSize(new Dimension(380, 26));
            panelDatosUsuario.add(comboRegs);

            // Dejar sin selección para que elegir el primero dispare
            comboRegs.setSelectedIndex(-1);
            comboRegs.setSelectedItem(null);

            comboRegs.addActionListener(ev -> {
                if (cargandoCombo) return;
                int idx = comboRegs.getSelectedIndex();
                if (idx >= 0) {
                    DTRegistro r = listaRegs.get(idx);
                    ConsultaRegistroFrame frame =
                        new ConsultaRegistroFrame(controlUsr, controlEvt, datos.getNickname(), r.getId());
                    if (getDesktopPane() != null) getDesktopPane().add(frame);
                    frame.setVisible(true);
                    frame.toFront();

                    // opcional: limpiar para volver a elegir el mismo
                    cargandoCombo = true;
                    comboRegs.setSelectedIndex(-1);
                    comboRegs.setSelectedItem(null);
                    cargandoCombo = false;
                }
            });

        // ¿Es organizador?
        } else if (controlUsr.listarOrganizadores() != null &&
                   controlUsr.listarOrganizadores().containsKey(nickname)) {

            JPanel panelOrganizador = new JPanel(new GridLayout(0, 2, 10, 10));
            panelDatosUsuario.add(panelOrganizador);

            panelOrganizador.add(new JLabel("Descripción:"));
            JTextArea txtDesc = new JTextArea(datos.getDesc());
            txtDesc.setEditable(false);
            panelOrganizador.add(txtDesc);

            panelOrganizador.add(new JLabel("Link:"));
            JTextArea txtLink = new JTextArea(datos.getLink());
            txtLink.setEditable(false);
            panelOrganizador.add(txtLink);

            // Combo de ediciones
            DefaultComboBoxModel<String> modelEd = new DefaultComboBoxModel<>();
            java.util.List<DTEdicion> listaEds = new java.util.ArrayList<>(datos.getEdiciones());
            for (DTEdicion ed : listaEds) {
                modelEd.addElement(ed.getNombre() + " (" + ed.getSigla() + ")");
            }
            JComboBox<String> comboEds = new JComboBox<>(modelEd);
            comboEds.setPreferredSize(new Dimension(380, 26));
            panelDatosUsuario.add(comboEds);

            // Dejar sin selección
            comboEds.setSelectedIndex(-1);
            comboEds.setSelectedItem(null);

            comboEds.addActionListener(ev -> {
                if (cargandoCombo) return;
                int idx = comboEds.getSelectedIndex();
                if (idx >= 0) {
                    DTEdicion ed = listaEds.get(idx);
                    ConsultaEdicionEventoFrame frame =
                        new ConsultaEdicionEventoFrame(controlUsr, controlEvt, ed.getSigla());
                    if (getDesktopPane() != null) getDesktopPane().add(frame);
                    frame.setVisible(true);
                    frame.toFront();

                    // opcional: limpiar para volver a elegir el mismo
                    cargandoCombo = true;
                    comboEds.setSelectedIndex(-1);
                    comboEds.setSelectedItem(null);
                    cargandoCombo = false;
                }
            });
        }

        panelDatos.revalidate();
        panelDatos.repaint();
    }

    /** Refresca la lista de usuarios y deja el combo sin selección. */
    public void cargarUsuarios() {
        DefaultComboBoxModel<String> model;
        usuarios.clear();
        usuarios.addAll(controlUsr.listarUsuarios().keySet());
        model = new DefaultComboBoxModel<>(usuarios);

        cargandoCombo = true;
        comboUsuarios.setModel(model);
        comboUsuarios.setSelectedIndex(-1);
        comboUsuarios.setSelectedItem(null);
        cargandoCombo = false;
    }
}