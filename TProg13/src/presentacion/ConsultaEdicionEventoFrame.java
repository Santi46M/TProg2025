package presentacion;

import javax.swing.*;

import logica.IControladorEvento;
import logica.IControladorUsuario;

import java.awt.*;

public class ConsultaEdicionEventoFrame extends JInternalFrame {
    private JComboBox<String> comboEventos;
    private JComboBox<String> comboEdiciones;
    private DefaultListModel<String> listModel;
    private JTextArea txtDatos;
    private String[][] datosEventos;
    private String[][] categoriasEventos;
    private String[][] edicionesEventos;
    private JList<String> listTiposRegistro;
    private JList<String> listPatrocinios;
    private JTextArea txtDetalle;
    private DefaultListModel<String> modelTiposRegistro;
    private DefaultListModel<String> modelPatrocinios;
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

    public ConsultaEdicionEventoFrame(IControladorUsuario iCU, IControladorEvento ICE) {
        super("Consulta Edición de Evento", true, true, true, true);
        setBounds(100, 100, 800, 500);
        setLayout(new BorderLayout());

        JPanel panelSeleccion = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblEvento = new JLabel("Evento:");
        comboEventos = new JComboBox<>();
        panelSeleccion.add(lblEvento);
        panelSeleccion.add(comboEventos);
        JLabel lblEdicion = new JLabel("Edición:");
        comboEdiciones = new JComboBox<>();
        panelSeleccion.add(lblEdicion);
        panelSeleccion.add(comboEdiciones);
        add(panelSeleccion, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new GridLayout(1, 2));
        JPanel panelDatos = new JPanel();
        panelDatos.setLayout(new BoxLayout(panelDatos, BoxLayout.Y_AXIS));
        txtNombreEdicion = new JTextField();
        txtNombreEdicion.setEditable(false);
        txtSigla = new JTextField();
        txtSigla.setEditable(false);
        txtFechaInicio = new JTextField();
        txtFechaInicio.setEditable(false);
        txtFechaFin = new JTextField();
        txtFechaFin.setEditable(false);
        txtFechaAlta = new JTextField();
        txtFechaAlta.setEditable(false);
        txtCiudad = new JTextField();
        txtCiudad.setEditable(false);
        txtPais = new JTextField();
        txtPais.setEditable(false);
        txtOrganizador = new JTextField();
        txtOrganizador.setEditable(false);
        panelDatos.add(new JLabel("Nombre Edición:"));
        panelDatos.add(txtNombreEdicion);
        panelDatos.add(new JLabel("Sigla:"));
        panelDatos.add(txtSigla);
        panelDatos.add(new JLabel("Fecha Inicio:"));
        panelDatos.add(txtFechaInicio);
        panelDatos.add(new JLabel("Fecha Fin:"));
        panelDatos.add(txtFechaFin);
        panelDatos.add(new JLabel("Fecha Alta:"));
        panelDatos.add(txtFechaAlta);
        panelDatos.add(new JLabel("Ciudad:"));
        panelDatos.add(txtCiudad);
        panelDatos.add(new JLabel("País:"));
        panelDatos.add(txtPais);
        panelDatos.add(new JLabel("Organizador:"));
        panelDatos.add(txtOrganizador);
        // ComboBoxes debajo de los datos y con tamaño reducido
        panelDatos.add(Box.createVerticalStrut(10));
        panelDatos.add(new JLabel("Tipos de Registro:"));
        comboTiposRegistro = new JComboBox<>();
        comboTiposRegistro.setMaximumSize(new Dimension(200, 25));
        panelDatos.add(comboTiposRegistro);
        panelDatos.add(new JLabel("Patrocinios:"));
        comboPatrocinios = new JComboBox<>();
        comboPatrocinios.setMaximumSize(new Dimension(200, 25));
        panelDatos.add(comboPatrocinios);
        panelCentral.add(panelDatos);
        // Elimina el panelCombos extra
        add(panelCentral, BorderLayout.CENTER);

        comboEventos.addActionListener(e -> cargarEdicionesEvento());
        comboEdiciones.addActionListener(e -> mostrarDatosEdicion());
        comboTiposRegistro.addActionListener(e -> {
            if (comboTiposRegistro.getSelectedIndex() != -1 && comboTiposRegistro.isPopupVisible()) {
                abrirConsultaTipoRegistro();
            }
        });
        comboPatrocinios.addActionListener(e -> {
            if (comboPatrocinios.getSelectedIndex() != -1 && comboPatrocinios.isPopupVisible()) {
                abrirConsultaPatrocinio();
            }
        });
    }

    public ConsultaEdicionEventoFrame(IControladorUsuario iCU, IControladorEvento ICE, String nombreEvento, String nombreEdicion) {
        this(iCU, ICE);
        cargarEventos();
        if (nombreEvento != null && nombreEdicion != null) {
            for (int i = 0; i < comboEventos.getItemCount(); i++) {
                if (comboEventos.getItemAt(i).equals(nombreEvento)) {
                    comboEventos.setSelectedIndex(i);
                    break;
                }
            }
            cargarEdicionesEvento();
            for (int j = 0; j < comboEdiciones.getItemCount(); j++) {
                if (comboEdiciones.getItemAt(j).equals(nombreEdicion)) {
                    comboEdiciones.setSelectedIndex(j);
                    break;
                }
            }
            mostrarDatosEdicion();
        }
    }

    public void cargarEventos() {
        try {
            logica.ControladorEvento controlador = new logica.ControladorEvento();
            java.util.List<logica.DTEvento> eventos = controlador.listarEventos();
            String[] eventosArr = new String[eventos.size()];
            datosEventos = new String[eventos.size()][1];
            categoriasEventos = new String[eventos.size()][];
            edicionesEventos = new String[eventos.size()][];
            for (int i = 0; i < eventos.size(); i++) {
                logica.DTEvento ev = eventos.get(i);
                eventosArr[i] = ev.getNombre();
                datosEventos[i][0] = ev.getDescripcion();
                categoriasEventos[i] = ev.getCategorias().toArray(new String[0]);
                edicionesEventos[i] = ev.getEdiciones().toArray(new String[0]);
            }
            comboEventos.setModel(new DefaultComboBoxModel<>(eventosArr));
            comboEventos.revalidate();
            comboEventos.repaint();
            if (eventosArr.length > 0) {
                comboEventos.setSelectedIndex(0);
                mostrarDatosEvento();
            } else {
                txtNombreEdicion.setText("");
                txtSigla.setText("");
                txtFechaInicio.setText("");
                txtFechaFin.setText("");
                txtFechaAlta.setText("");
                txtCiudad.setText("");
                txtPais.setText("");
                txtOrganizador.setText("");
                comboEdiciones.setModel(new DefaultComboBoxModel<>(new String[]{}));
                comboTiposRegistro.removeAllItems();
                comboPatrocinios.removeAllItems();
            }
        } catch (Exception ex) {
            comboEventos.setModel(new DefaultComboBoxModel<>(new String[]{"No hay eventos"}));
            comboEventos.revalidate();
            comboEventos.repaint();
            txtNombreEdicion.setText("");
            txtSigla.setText("");
            txtFechaInicio.setText("");
            txtFechaFin.setText("");
            txtFechaAlta.setText("");
            txtCiudad.setText("");
            txtPais.setText("");
            txtOrganizador.setText("");
            comboEdiciones.setModel(new DefaultComboBoxModel<>(new String[]{}));
            comboTiposRegistro.removeAllItems();
            comboPatrocinios.removeAllItems();
        }
    }

    private void cargarEdicionesEvento() {
        int idx = comboEventos.getSelectedIndex();
        comboEdiciones.removeAllItems();
        if (idx < 0 || edicionesEventos == null || idx >= edicionesEventos.length) {
            return;
        }
        String[] ediciones = edicionesEventos[idx];
        for (String ed : ediciones) {
            comboEdiciones.addItem(ed);
        }
        if (ediciones.length > 0) {
            comboEdiciones.setSelectedIndex(0);
            mostrarDatosEdicion();
        }
    }

    private void mostrarDatosEvento() {
        int idx = comboEventos.getSelectedIndex();
        if (idx < 0 || datosEventos == null || idx >= datosEventos.length) {
            txtNombreEdicion.setText("");
            txtSigla.setText("");
            txtFechaInicio.setText("");
            txtFechaFin.setText("");
            txtFechaAlta.setText("");
            txtCiudad.setText("");
            txtPais.setText("");
            txtOrganizador.setText("");
            comboEdiciones.setModel(new DefaultComboBoxModel<>(new String[]{}));
            comboTiposRegistro.removeAllItems();
            comboPatrocinios.removeAllItems();
            return;
        }
        comboEdiciones.setModel(new DefaultComboBoxModel<>(edicionesEventos[idx]));
        comboEdiciones.revalidate();
        comboEdiciones.repaint();
        comboTiposRegistro.removeAllItems();
        comboPatrocinios.removeAllItems();
        // Limpiar campos edición
        txtNombreEdicion.setText("");
        txtSigla.setText("");
        txtFechaInicio.setText("");
        txtFechaFin.setText("");
        txtFechaAlta.setText("");
        txtCiudad.setText("");
        txtPais.setText("");
        txtOrganizador.setText("");
    }

    private void mostrarDatosEdicion() {
        int idxEvento = comboEventos.getSelectedIndex();
        int idxEd = comboEdiciones.getSelectedIndex();
        comboTiposRegistro.removeAllItems();
        comboPatrocinios.removeAllItems();
        if (idxEvento < 0 || idxEd < 0 || edicionesEventos == null || idxEvento >= edicionesEventos.length) {
            txtNombreEdicion.setText("");
            txtSigla.setText("");
            txtFechaInicio.setText("");
            txtFechaFin.setText("");
            txtFechaAlta.setText("");
            txtCiudad.setText("");
            txtPais.setText("");
            txtOrganizador.setText("");
            return;
        }
        String[] ediciones = edicionesEventos[idxEvento];
        if (idxEd >= ediciones.length) return;
        String nombreEdicion = ediciones[idxEd];
        String nombreEvento = comboEventos.getItemAt(idxEvento);
        logica.Ediciones edi = new logica.ControladorEvento().obtenerEdicion(nombreEvento, nombreEdicion);
        if (edi == null) {
            txtNombreEdicion.setText("No se encontró la edición.");
            return;
        }
        txtNombreEdicion.setText(edi.getNombre());
        txtSigla.setText(edi.getSigla());
        txtFechaInicio.setText(String.valueOf(edi.getFechaInicio()));
        txtFechaFin.setText(String.valueOf(edi.getFechaFin()));
        txtFechaAlta.setText(String.valueOf(edi.getFechaAlta()));
        txtCiudad.setText(edi.getCiudad());
        txtPais.setText(edi.getPais());
        txtOrganizador.setText(edi.getOrganizador() != null ? edi.getOrganizador().getNickname() : "");
        for (logica.TipoRegistro tr : edi.getTiposRegistro()) {
            comboTiposRegistro.addItem(tr.getNombre());
        }
        for (logica.Patrocinio p : edi.getPatrocinios()) {
            comboPatrocinios.addItem(p.getCodigoPatrocinio());
        }
    }

    private void abrirConsultaTipoRegistro() {
        int idxEvento = comboEventos.getSelectedIndex();
        int idxEd = comboEdiciones.getSelectedIndex();
        int idxTipo = comboTiposRegistro.getSelectedIndex();
        if (idxEvento < 0 || idxEd < 0 || idxTipo < 0) return;
        String nombreEvento = comboEventos.getItemAt(idxEvento);
        String nombreEdicion = comboEdiciones.getItemAt(idxEd);
        String nombreTipo = comboTiposRegistro.getItemAt(idxTipo);
        ConsultaTipoRegistroFrame frameTipo = new ConsultaTipoRegistroFrame(null, null, nombreEvento, nombreEdicion, nombreTipo);
        JDesktopPane desktop = getDesktopPane();
        if (desktop != null) {
            desktop.add(frameTipo);
            frameTipo.setVisible(true);
            frameTipo.toFront();
        } else {
            frameTipo.setVisible(true);
        }
    }

    private void abrirConsultaPatrocinio() {
        int idxEvento = comboEventos.getSelectedIndex();
        int idxEd = comboEdiciones.getSelectedIndex();
        int idxPat = comboPatrocinios.getSelectedIndex();
        if (idxEvento < 0 || idxEd < 0 || idxPat < 0) return;
        String nombreEvento = comboEventos.getItemAt(idxEvento);
        String nombreEdicion = comboEdiciones.getItemAt(idxEd);
        String codigoPat = comboPatrocinios.getItemAt(idxPat);
        ConsultaPatrocinioFrame framePat = new ConsultaPatrocinioFrame(null, null, nombreEvento, nombreEdicion, codigoPat);
        JDesktopPane desktop = getDesktopPane();
        if (desktop != null) {
            desktop.add(framePat);
            framePat.setVisible(true);
            framePat.toFront();
        } else {
            framePat.setVisible(true);
        }
    }
}