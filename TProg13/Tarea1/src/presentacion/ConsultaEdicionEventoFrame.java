package presentacion;


import javax.swing.JInternalFrame;
import javax.swing.JLabel;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import java.awt.BorderLayout;

import javax.swing.JDesktopPane;
import javax.swing.BorderFactory;

import javax.swing.JScrollPane;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.GridLayout;


import java.awt.FlowLayout;
import java.util.List;
import logica.Clases.Ediciones;
import logica.Clases.Patrocinio;
import logica.Clases.TipoRegistro;
import logica.Datatypes.DTEvento;
import logica.Interfaces.IControladorEvento;
import logica.Interfaces.IControladorUsuario;

public class ConsultaEdicionEventoFrame extends JInternalFrame {

    private final IControladorUsuario icu;
    private final IControladorEvento  ice;

    private JComboBox<String> comboEventos;
    private JComboBox<String> comboEdiciones;

    private JTextField txtNombreEdicion;
    private JTextField txtSigla;
    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JTextField txtFechaAlta;
    private JTextField txtCiudad;
    private JTextField txtPais;
    private JTextField txtOrganizador;

    private JComboBox<String> comboTiposRegistro;
    private JComboBox<String> comboPatrocinios;

    private String[][] edicionesEventos;
    private boolean cambiando = false;
    private JPanel panelGridRegistros;
    private JScrollPane scrollGridRegistros;

    // Imagen
    private JLabel lblImagenEdicion;

    /**
     * @wbp.parser.constructor
     */
    public ConsultaEdicionEventoFrame(IControladorUsuario iCU, IControladorEvento ICE) {
        super("Consulta Edición de Evento", true, true, true, true);
        this.icu = iCU;
        this.ice = ICE;

        comboTiposRegistro = new JComboBox<>();
        comboPatrocinios = new JComboBox<>();

        setBounds(100, 100, 900, 560);
        setLayout(new BorderLayout());

        // Selección
        JPanel panelSeleccion = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSeleccion.add(new JLabel("Evento:"));
        comboEventos = new JComboBox<>();
        panelSeleccion.add(comboEventos);
        panelSeleccion.add(new JLabel("Edición:"));
        comboEdiciones = new JComboBox<>();
        panelSeleccion.add(comboEdiciones);
        getContentPane().add(panelSeleccion, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout());
        add(center, BorderLayout.CENTER);

        JPanel panelDatos = new JPanel(new GridLayout(0, 2, 8, 6));
        center.add(panelDatos, BorderLayout.CENTER);

        panelDatos.add(labelR("Nombre Edición:"));  txtNombreEdicion = roField(panelDatos);
        panelDatos.add(labelR("Sigla:"));           txtSigla         = roField(panelDatos);
        panelDatos.add(labelR("Fecha Inicio:"));    txtFechaInicio   = roField(panelDatos);
        panelDatos.add(labelR("Fecha Fin:"));       txtFechaFin      = roField(panelDatos);
        panelDatos.add(labelR("Fecha Alta:"));      txtFechaAlta     = roField(panelDatos);
        panelDatos.add(labelR("Ciudad:"));          txtCiudad        = roField(panelDatos);
        panelDatos.add(labelR("País:"));            txtPais          = roField(panelDatos);
        panelDatos.add(labelR("Organizador:"));     txtOrganizador   = roField(panelDatos);

        panelDatos.add(labelR("Tipos de Registro:")); panelDatos.add(comboTiposRegistro);
        panelDatos.add(labelR("Patrocinios:")); panelDatos.add(comboPatrocinios);

        JLabel lblRegistros = new JLabel("Registros de la edición:");
        panelDatos.add(lblRegistros);
        panelDatos.add(new JLabel());

        panelGridRegistros = new JPanel();
        panelGridRegistros.setLayout(new GridLayout(0, 3, 8, 2));
        panelGridRegistros.add(new JLabel("Asistente", SwingConstants.CENTER));
        panelGridRegistros.add(new JLabel("Tipo de registro", SwingConstants.CENTER));
        panelGridRegistros.add(new JLabel("Costo", SwingConstants.CENTER));
        scrollGridRegistros = new JScrollPane(panelGridRegistros);
        panelDatos.add(scrollGridRegistros);
        panelDatos.add(new JLabel());

        // Lateral derecho con imagen
        JPanel derecha = new JPanel(new BorderLayout());
        derecha.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        lblImagenEdicion = new JLabel("Sin imagen", SwingConstants.CENTER);
        lblImagenEdicion.setPreferredSize(new Dimension(280, 210));
        lblImagenEdicion.setBorder(BorderFactory.createTitledBorder("Imagen de la edición"));
        derecha.add(lblImagenEdicion, BorderLayout.NORTH);
        center.add(derecha, BorderLayout.EAST);

        comboEventos.addActionListener(e -> { if (!cambiando) cargarEdicionesEvento(); });
        comboEdiciones.addActionListener(e -> { if (!cambiando) mostrarDatosEdicion(); });

        comboTiposRegistro.addActionListener(e -> {
            if (!comboTiposRegistro.isPopupVisible()) return;
            int eIdx = comboEventos.getSelectedIndex();
            int dIdx = comboEdiciones.getSelectedIndex();
            int tIdx = comboTiposRegistro.getSelectedIndex();
            if (eIdx < 0 || dIdx < 0 || tIdx < 0) return;
            String nombreEvento  = comboEventos.getItemAt(eIdx);
            String nombreEdicion = comboEdiciones.getItemAt(dIdx);
            String nombreTipo    = comboTiposRegistro.getItemAt(tIdx);
            ConsultaTipoRegistroFrame f = new ConsultaTipoRegistroFrame(iCU, ICE, nombreEvento, nombreEdicion, nombreTipo);
            abrirEnDesktop(f);
        });

        comboPatrocinios.addActionListener(e -> {
            if (!comboPatrocinios.isPopupVisible()) return;
            int eIdx = comboEventos.getSelectedIndex();
            int dIdx = comboEdiciones.getSelectedIndex();
            int pIdx = comboPatrocinios.getSelectedIndex();
            if (eIdx < 0 || dIdx < 0 || pIdx < 0) return;
            String nombreEvento  = comboEventos.getItemAt(eIdx);
            String nombreEdicion = comboEdiciones.getItemAt(dIdx);
            String codigoPat     = comboPatrocinios.getItemAt(pIdx);
            ConsultaPatrocinioFrame f = new ConsultaPatrocinioFrame(iCU, ICE, nombreEvento, nombreEdicion, codigoPat);
            abrirEnDesktop(f);
        });

        cargarEventos();
    }

    /**
     * @wbp.parser.constructor
     */
    public ConsultaEdicionEventoFrame(IControladorUsuario iCU, IControladorEvento ICE,
                                      String nombreEvento, String nombreEdicion) {
        this(iCU, ICE);
        if (nombreEvento != null) seleccionarItemPorTexto(comboEventos, nombreEvento);
        cargarEdicionesEvento();
        if (nombreEdicion != null) seleccionarItemPorTexto(comboEdiciones, nombreEdicion);
        mostrarDatosEdicion();
    }

    /**
     * @wbp.parser.constructor
     */
    public ConsultaEdicionEventoFrame(IControladorUsuario iCU, IControladorEvento ICE, String siglaEdicion) {
        this(iCU, ICE);
        if (siglaEdicion == null || siglaEdicion.isEmpty()) return;

        Ediciones ed = ICE.obtenerEdicionPorSigla(siglaEdicion);
        if (ed == null) return;
        String nombreEdicion = ed.getNombre();

        String nombreEvento = ICE.encontrarEventoPorSigla(siglaEdicion);
        if (nombreEvento == null || nombreEvento.isEmpty()) return;

        seleccionarItemPorTexto(comboEventos, nombreEvento);
        cargarEdicionesEvento();
        seleccionarItemPorTexto(comboEdiciones, nombreEdicion);
        mostrarDatosEdicion();
    }

    public void cargarEventos() {
        List<DTEvento> eventos = ice.listarEventos();
        String[] arr = new String[eventos.size()];
        edicionesEventos = new String[eventos.size()][];

        for (int i = 0; i < eventos.size(); i++) {
            DTEvento ev = eventos.get(i);
            arr[i] = ev.getNombre();
            edicionesEventos[i] = ev.getEdiciones().toArray(new String[0]);
        }

        cambiando = true;
        comboEventos.setModel(new DefaultComboBoxModel<>(arr));
        if (arr.length > 0) comboEventos.setSelectedIndex(0);
        cambiando = false;

        cargarEdicionesEvento();
    }

    private void cargarEdicionesEvento() {
        int idx = comboEventos.getSelectedIndex();
        cambiando = true;
        comboEdiciones.removeAllItems();
        if (idx >= 0 && edicionesEventos != null && idx < edicionesEventos.length) {
            String[] eds = edicionesEventos[idx];
            for (String ed : eds) comboEdiciones.addItem(ed);
            if (eds.length > 0) comboEdiciones.setSelectedIndex(0);
        }
        cambiando = false;
        mostrarDatosEdicion();
    }

    private void mostrarDatosEdicion() {
        int idxEvento = comboEventos.getSelectedIndex();
        int idxEd     = comboEdiciones.getSelectedIndex();

        limpiarCampos();

        if (idxEvento < 0 || idxEd < 0 || edicionesEventos == null
                || idxEvento >= edicionesEventos.length) return;

        String nombreEvento  = comboEventos.getItemAt(idxEvento);
        String nombreEdicion = comboEdiciones.getItemAt(idxEd);

        Ediciones ed = ice.obtenerEdicion(nombreEvento, nombreEdicion);
        if (ed == null) return;

        txtNombreEdicion.setText(ed.getNombre());
        txtSigla.setText(ed.getSigla());
        txtFechaInicio.setText(String.valueOf(ed.getFechaInicio()));
        txtFechaFin.setText(String.valueOf(ed.getFechaFin()));
        txtFechaAlta.setText(String.valueOf(ed.getFechaAlta()));
        txtCiudad.setText(ed.getCiudad());
        txtPais.setText(ed.getPais());
        txtOrganizador.setText(ed.getOrganizador() != null ? ed.getOrganizador().getNickname() : "");

        comboTiposRegistro.removeAllItems();
        for (TipoRegistro tr : ed.getTiposRegistro()) comboTiposRegistro.addItem(tr.getNombre());

        comboPatrocinios.removeAllItems();
        for (Patrocinio p : ed.getPatrocinios()) comboPatrocinios.addItem(p.getCodigoPatrocinio());

        panelGridRegistros.removeAll();
        panelGridRegistros.add(new JLabel("Asistente", SwingConstants.CENTER));
        panelGridRegistros.add(new JLabel("Tipo de registro", SwingConstants.CENTER));
        panelGridRegistros.add(new JLabel("Costo", SwingConstants.CENTER));
        for (logica.Clases.Registro reg : ed.getRegistros().values()) {
            panelGridRegistros.add(new JLabel(reg.getUsuario().getNickname()));
            panelGridRegistros.add(new JLabel(reg.getTipoRegistro().getNombre()));
            panelGridRegistros.add(new JLabel(String.valueOf(reg.getCosto())));
        }
        panelGridRegistros.revalidate();
        panelGridRegistros.repaint();

        // Imagen
        ImageIcon icon = loadIcon(ed.getImagen(), 300, 210);
        lblImagenEdicion.setIcon(icon);
        lblImagenEdicion.setText(icon == null ? "Sin imagen" : null);
    }

    // helpers

    private void limpiarCampos() {
        txtNombreEdicion.setText("");
        txtSigla.setText("");
        txtFechaInicio.setText("");
        txtFechaFin.setText("");
        txtFechaAlta.setText("");
        txtCiudad.setText("");
        txtPais.setText("");
        txtOrganizador.setText("");
        comboTiposRegistro.removeAllItems();
        comboPatrocinios.removeAllItems();
        panelGridRegistros.removeAll();
        panelGridRegistros.add(new JLabel("Asistente", SwingConstants.CENTER));
        panelGridRegistros.add(new JLabel("Tipo de registro", SwingConstants.CENTER));
        panelGridRegistros.add(new JLabel("Costo", SwingConstants.CENTER));
        panelGridRegistros.revalidate();
        panelGridRegistros.repaint();
        lblImagenEdicion.setIcon(null);
        lblImagenEdicion.setText("Sin imagen");
    }

    private void seleccionarItemPorTexto(JComboBox<String> combo, String texto) {
        if (texto == null) return;
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (texto.equals(combo.getItemAt(i))) {
                cambiando = true;
                combo.setSelectedIndex(i);
                cambiando = false;
                return;
            }
        }
    }

    private JLabel labelR(String s) {
        JLabel l = new JLabel(s);
        l.setHorizontalAlignment(SwingConstants.LEFT);
        return l;
    }

    private JTextField roField(JPanel parent) {
        JTextField t = new JTextField();
        t.setEditable(false);
        parent.add(t);
        return t;
    }

    private void abrirEnDesktop(JInternalFrame f) {
        JDesktopPane desk = getDesktopPane();
        if (desk != null) desk.add(f);
        f.setVisible(true);
        f.toFront();
    }

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
