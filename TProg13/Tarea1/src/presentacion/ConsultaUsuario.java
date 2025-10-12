package presentacion;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;


import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Image;

import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;

import excepciones.UsuarioNoExisteException;
import logica.clases.Usuario;
import logica.datatypes.DTDatosUsuario;
import logica.datatypes.DTEdicion;
import logica.datatypes.DTRegistro;
import logica.interfaces.IControladorEvento;
import logica.interfaces.IControladorUsuario;

public class ConsultaUsuario extends JInternalFrame {

    private final IControladorUsuario controlUsr;
    private final IControladorEvento  controlEvt;

    private JComboBox<String> comboUsuarios;
    private JLabel lblNick, lblNombre, lblApellido, lblCorreo, lblFechaNac, lblInstitucion;
    private JTextField txtNick, txtNombre, txtApellido, txtCorreo, txtFechaNac, txtInstitucion;
    private JPanel panelEspecifico;
    private CardLayout cardTipo;
    private JPanel cardVacio;
    private JPanel cardAsistente;
    private JLabel lblRegistros;
    private JComboBox<String> comboRegs;
    private java.util.List<DTRegistro> listaRegs = new ArrayList<>();
    private JPanel cardOrganizador;
    private JLabel lblDesc, lblLink, lblEds;
    private JTextField txtDesc, txtLink;
    private JComboBox<String> comboEds;
    private java.util.List<DTEdicion> listaEds = new ArrayList<>();

    // Imagen de usuario
    private JLabel lblImagenUsuario;

    private boolean cargando = false;

    public ConsultaUsuario(IControladorUsuario icu, IControladorEvento iCE) {
        this.controlUsr = icu;
        this.controlEvt = iCE;

        setTitle("Consulta de Usuario");
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setSize(760, 600);
        setLayout(new BorderLayout(10, 10));

        // ===== Barra superior =====
        JPanel barraSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        barraSuperior.add(new JLabel("Usuario:"));

        Vector<String> usuarios = new Vector<>(controlUsr.listarUsuarios().keySet());
        comboUsuarios = new JComboBox<>(usuarios);
        comboUsuarios.setPreferredSize(new Dimension(300, 26));
        comboUsuarios.setSelectedIndex(-1);
        barraSuperior.add(comboUsuarios);

        add(barraSuperior, BorderLayout.NORTH);

        // ===== Centro con scroll =====
        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.X_AXIS));
        add(new JScrollPane(centro,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);

        // Panel izquierdo: datos + específicos
        JPanel izquierda = new JPanel();
        izquierda.setLayout(new BoxLayout(izquierda, BoxLayout.Y_AXIS));
        centro.add(izquierda);

        // ===== Panel datos básicos =====
        JPanel panelComunes = new JPanel(new GridBagLayout());
        panelComunes.setBorder(BorderFactory.createTitledBorder("Datos básicos"));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 8, 6, 8);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;

        txtNick        = mkText(false);
        txtNombre      = mkText(false);
        txtApellido    = mkText(false);
        txtCorreo      = mkText(false);
        txtFechaNac    = mkText(false);
        txtInstitucion = mkText(false);

        int row = 0;
        lblNick        = addRow(panelComunes, gc, row++, "Nickname:",            txtNick);
        lblNombre      = addRow(panelComunes, gc, row++, "Nombre:",              txtNombre);
        lblApellido    = addRow(panelComunes, gc, row++, "Apellido:",            txtApellido);
        lblCorreo      = addRow(panelComunes, gc, row++, "Correo:",              txtCorreo);
        lblFechaNac    = addRow(panelComunes, gc, row++, "Fecha de nacimiento:", txtFechaNac);
        lblInstitucion = addRow(panelComunes, gc, row++, "Institución:",         txtInstitucion);

        izquierda.add(panelComunes);

        // ===== Panel específico (cards) =====
        cardTipo = new CardLayout();
        panelEspecifico = new JPanel(cardTipo);
        panelEspecifico.setBorder(BorderFactory.createTitledBorder("Datos específicos"));

        // --- VACÍO ---
        cardVacio = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cardVacio.add(new JLabel("Seleccione un usuario para ver detalles."));
        panelEspecifico.add(cardVacio, "VACIO");

        // --- ASISTENTE ---
        cardAsistente = new JPanel();
        cardAsistente.setLayout(new GridBagLayout());
        GridBagConstraints gca = new GridBagConstraints();
        gca.insets = new Insets(6, 8, 6, 8);
        gca.fill = GridBagConstraints.HORIZONTAL;
        gca.weightx = 1.0;

        lblRegistros = new JLabel("Registros:");
        gca.gridx = 0; gca.gridy = 0; gca.weightx = 0;
        cardAsistente.add(lblRegistros, gca);

        comboRegs = new JComboBox<>();
        comboRegs.setPreferredSize(new Dimension(460, 26));
        gca.gridx = 1; gca.gridy = 0; gca.weightx = 1.0;
        cardAsistente.add(comboRegs, gca);

        panelEspecifico.add(cardAsistente, "ASISTENTE");

        comboRegs.addActionListener(ev -> {
            if (cargando) return;
            int idx = comboRegs.getSelectedIndex();
            if (idx >= 0 && idx < listaRegs.size()) {
                DTRegistro r = listaRegs.get(idx);
                ConsultaRegistroFrame frame =
                        new ConsultaRegistroFrame(controlUsr, controlEvt, txtNick.getText(), r.getId());
                if (getDesktopPane() != null) getDesktopPane().add(frame);
                frame.setVisible(true);
                frame.toFront();
            }
        });

        // --- ORGANIZADOR ---
        cardOrganizador = new JPanel(new GridBagLayout());
        GridBagConstraints gco = new GridBagConstraints();
        gco.insets = new Insets(6, 8, 6, 8);
        gco.fill = GridBagConstraints.HORIZONTAL;
        gco.weightx = 1.0;

        txtDesc = mkText(false);
        txtLink = mkText(false);
        lblDesc = addRow(cardOrganizador, gco, 0, "Descripción:", txtDesc);
        lblLink = addRow(cardOrganizador, gco, 1, "Link:",         txtLink);

        lblEds = new JLabel("Ediciones:");
        gco.gridx = 0; gco.gridy = 2; gco.weightx = 0;
        cardOrganizador.add(lblEds, gco);

        comboEds = new JComboBox<>();
        comboEds.setPreferredSize(new Dimension(460, 26));
        gco.gridx = 1; gco.gridy = 2; gco.weightx = 1.0;
        cardOrganizador.add(comboEds, gco);

        panelEspecifico.add(cardOrganizador, "ORGANIZADOR");

        comboEds.addActionListener(ev -> {
            if (cargando) return;
            int idx = comboEds.getSelectedIndex();
            if (idx >= 0 && idx < listaEds.size()) {
                DTEdicion ed = listaEds.get(idx);
                ConsultaEdicionEventoFrame frame =
                        new ConsultaEdicionEventoFrame(controlUsr, controlEvt, ed.getSigla());
                if (getDesktopPane() != null) getDesktopPane().add(frame);
                frame.setVisible(true);
                frame.toFront();
            }
        });

        izquierda.add(panelEspecifico);

        // Panel derecho: imagen
        JPanel derecha = new JPanel(new BorderLayout());
        derecha.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lblImagenUsuario = new JLabel();
        lblImagenUsuario.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagenUsuario.setPreferredSize(new Dimension(220, 220));
        lblImagenUsuario.setBorder(BorderFactory.createTitledBorder("Imagen"));
        derecha.add(lblImagenUsuario, BorderLayout.NORTH);

        centro.add(derecha);

        limpiarCampos();
        showVacio();

        comboUsuarios.addActionListener(e -> {
            if (cargando) return;
            String sel = (String) comboUsuarios.getSelectedItem();
            if (sel != null && !sel.isEmpty()) {
                cargarUsuario(sel);
            } else {
                limpiarCampos();
                showVacio();
            }
        });
    }

    // ================= API esperada por el main =================
    public void cargarUsuarios() { recargarUsuarios(); }

    public void recargarUsuarios() {
        cargando = true;
        Vector<String> usuarios = new Vector<>(controlUsr.listarUsuarios().keySet());
        comboUsuarios.setModel(new DefaultComboBoxModel<>(usuarios));
        comboUsuarios.setSelectedIndex(-1);
        limpiarCampos();
        showVacio();
        cargando = false;
    }
    // ===========================================================

    private void cargarUsuario(String nickname) {
        limpiarCampos();
        Map<String, Usuario> todos = controlUsr.listarUsuarios();
        if (!todos.containsKey(nickname)) {
            JOptionPane.showMessageDialog(this, "El usuario no existe.");
            return;
        }
        DTDatosUsuario datos;
        try {
            datos = controlUsr.obtenerDatosUsuario(nickname);
        } catch (UsuarioNoExisteException ex) {
            JOptionPane.showMessageDialog(this, "El usuario no existe.");
            return;
        }

        txtNick.setText(nvl(datos.getNickname()));
        txtNombre.setText(nvl(datos.getNombre()));
        txtCorreo.setText(nvl(datos.getEmail()));

        // Imagen
        ImageIcon ico = loadIcon(datos.getImagen(), 200, 200);
        lblImagenUsuario.setIcon(ico);
        lblImagenUsuario.setText(ico == null ? "Sin imagen" : null);

        boolean esAsistente   = controlUsr.listarAsistentes()    != null &&
                                controlUsr.listarAsistentes().containsKey(nickname);
        boolean esOrganizador = controlUsr.listarOrganizadores() != null &&
                                controlUsr.listarOrganizadores().containsKey(nickname);

        if (esAsistente) {
            txtApellido.setText(nvl(datos.getApellido()));
            txtFechaNac.setText(datos.getFechaNac() != null ? String.valueOf(datos.getFechaNac()) : "");
            txtInstitucion.setText(nvl(datos.getInstitucion()));
            setRowVisible(lblApellido, txtApellido, true);
            setRowVisible(lblFechaNac, txtFechaNac, true);
            setRowVisible(lblInstitucion, txtInstitucion, true);

            setRowVisible(lblDesc, txtDesc, false);
            setRowVisible(lblLink, txtLink, false);
            lblEds.setVisible(false);
            comboEds.setVisible(false);

            listaRegs = new ArrayList<>(datos.getRegistros());
            DefaultComboBoxModel<String> modelReg = new DefaultComboBoxModel<>();
            for (DTRegistro r : listaRegs) {
                modelReg.addElement(r.getId() + " – " + r.getEdicion() + " – " + r.getTipoRegistro());
            }
            comboRegs.setModel(modelReg);
            comboRegs.setSelectedIndex(-1);

            cardTipo.show(panelEspecifico, "ASISTENTE");

        } else if (esOrganizador) {
            setRowVisible(lblApellido, txtApellido, false);
            setRowVisible(lblFechaNac, txtFechaNac, false);
            setRowVisible(lblInstitucion, txtInstitucion, false);

            txtDesc.setText(nvl(datos.getDesc()));
            txtDesc.setToolTipText(txtDesc.getText());
            txtLink.setText(nvl(datos.getLink()));
            setRowVisible(lblDesc, txtDesc, true);
            setRowVisible(lblLink, txtLink, true);

            listaEds = new ArrayList<>(datos.getEdiciones());
            DefaultComboBoxModel<String> modelEd = new DefaultComboBoxModel<>();
            for (DTEdicion ed : listaEds) {
                modelEd.addElement(ed.getNombre() + " (" + ed.getSigla() + ")");
            }
            comboEds.setModel(modelEd);
            comboEds.setSelectedIndex(-1);
            lblEds.setVisible(true);
            comboEds.setVisible(true);

            cardTipo.show(panelEspecifico, "ORGANIZADOR");

        } else {
            setRowVisible(lblApellido, txtApellido, false);
            setRowVisible(lblFechaNac, txtFechaNac, false);
            setRowVisible(lblInstitucion, txtInstitucion, false);
            setRowVisible(lblDesc, txtDesc, false);
            setRowVisible(lblLink, txtLink, false);
            lblEds.setVisible(false);
            comboEds.setVisible(false);
            cardTipo.show(panelEspecifico, "VACIO");
        }

        revalidate();
        repaint();
    }

    private void showVacio() {
        setRowVisible(lblApellido, txtApellido, false);
        setRowVisible(lblFechaNac, txtFechaNac, false);
        setRowVisible(lblInstitucion, txtInstitucion, false);
        setRowVisible(lblDesc, txtDesc, false);
        setRowVisible(lblLink, txtLink, false);
        lblEds.setVisible(false);
        comboEds.setVisible(false);
        comboRegs.setModel(new DefaultComboBoxModel<>());
        cardTipo.show(panelEspecifico, "VACIO");
        lblImagenUsuario.setIcon(null);
        lblImagenUsuario.setText("Sin imagen");
    }

    private void limpiarCampos() {
        cargando = true;
        txtNick.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtCorreo.setText("");
        txtFechaNac.setText("");
        txtInstitucion.setText("");
        if (txtDesc != null) txtDesc.setText("");
        if (txtLink != null) txtLink.setText("");
        listaRegs.clear();
        listaEds.clear();
        if (comboRegs != null) comboRegs.setModel(new DefaultComboBoxModel<>());
        if (comboEds != null) comboEds.setModel(new DefaultComboBoxModel<>());
        lblImagenUsuario.setIcon(null);
        lblImagenUsuario.setText("Sin imagen");
        cargando = false;
    }

    // ===================== Helpers UI =====================
    private static JTextField mkText(boolean editable) {
        JTextField t = new JTextField();
        t.setEditable(editable);
        return t;
    }

    private static JLabel addRow(JPanel panel, GridBagConstraints gc, int row, String label, JComponent comp) {
        JLabel lbl = new JLabel(label);
        gc.gridx = 0; gc.gridy = row; gc.weightx = 0; gc.gridwidth = 1;
        panel.add(lbl, gc);
        gc.gridx = 1; gc.gridy = row; gc.weightx = 1; gc.gridwidth = 2;
        panel.add(comp, gc);
        return lbl;
    }

    private static void setRowVisible(JLabel label, JComponent field, boolean visible) {
        if (label != null) label.setVisible(visible);
        if (field != null) field.setVisible(visible);
        if (field != null && field.getParent() != null) {
            field.getParent().revalidate();
            field.getParent().repaint();
        }
    }

    private static String nvl(String s) { return s == null ? "" : s; }

    // ==== IMÁGENES ====
    private static ImageIcon loadIcon(String imgName, int w, int h) {
        if (imgName == null || imgName.isBlank()) return null;
        try {
            String cpPath = "img/" + imgName;
            java.net.URL url = Thread.currentThread().getContextClassLoader().getResource(cpPath);
            Image base;
            if (url != null) {
                base = new ImageIcon(url).getImage();
            } else {
                java.io.File f1 = new java.io.File("src/img/" + imgName);
                java.io.File f2 = new java.io.File("img/" + imgName);
                java.io.File f = f1.exists() ? f1 : (f2.exists() ? f2 : null);
                if (f == null) return null;
                base = new ImageIcon(f.getAbsolutePath()).getImage();
            }
            Image scaled = base.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (IllegalStateException | NullPointerException ex) {
            return null;
        }
    }
}
