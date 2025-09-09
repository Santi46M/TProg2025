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

    /**
     * @wbp.parser.constructor
     */
    public ConsultaEdicionEventoFrame(IControladorUsuario iCU, IControladorEvento ICE) {
        super("Consulta Edición de Evento", true, true, true, true);
        setBounds(100, 100, 800, 500);
        getContentPane().setLayout(new BorderLayout());

        JPanel panelSeleccion = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblEvento = new JLabel("Evento:");
        comboEventos = new JComboBox<>();
        panelSeleccion.add(lblEvento);
        panelSeleccion.add(comboEventos);
        JLabel lblEdicion = new JLabel("Edición:");
        comboEdiciones = new JComboBox<>();
        panelSeleccion.add(lblEdicion);
        panelSeleccion.add(comboEdiciones);
        getContentPane().add(panelSeleccion, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new GridLayout(1, 2));
        JPanel panelDatos = new JPanel(new GridBagLayout());
        // Nombre Edición
        GridBagConstraints gbcNombreLabel = new GridBagConstraints();
        gbcNombreLabel.insets = new Insets(5, 5, 5, 5);
        gbcNombreLabel.anchor = GridBagConstraints.WEST;
        gbcNombreLabel.fill = GridBagConstraints.HORIZONTAL;
        gbcNombreLabel.gridx = 0;
        gbcNombreLabel.gridy = 0;
        panelDatos.add(new JLabel("Nombre Edición:"), gbcNombreLabel);

        GridBagConstraints gbcNombreField = new GridBagConstraints();
        gbcNombreField.insets = new Insets(5, 5, 5, 5);
        gbcNombreField.anchor = GridBagConstraints.WEST;
        gbcNombreField.fill = GridBagConstraints.HORIZONTAL;
        gbcNombreField.gridx = 1;
        gbcNombreField.gridy = 0;
        txtNombreEdicion = new JTextField();
        txtNombreEdicion.setEditable(false);
        txtNombreEdicion.setColumns(20);
        panelDatos.add(txtNombreEdicion, gbcNombreField);

        // Sigla
        GridBagConstraints gbcSiglaLabel = new GridBagConstraints();
        gbcSiglaLabel.insets = new Insets(5, 5, 5, 5);
        gbcSiglaLabel.anchor = GridBagConstraints.WEST;
        gbcSiglaLabel.fill = GridBagConstraints.HORIZONTAL;
        gbcSiglaLabel.gridx = 0;
        gbcSiglaLabel.gridy = 1;
        panelDatos.add(new JLabel("Sigla:"), gbcSiglaLabel);

        GridBagConstraints gbcSiglaField = new GridBagConstraints();
        gbcSiglaField.insets = new Insets(5, 5, 5, 5);
        gbcSiglaField.anchor = GridBagConstraints.WEST;
        gbcSiglaField.fill = GridBagConstraints.HORIZONTAL;
        gbcSiglaField.gridx = 1;
        gbcSiglaField.gridy = 1;
        txtSigla = new JTextField();
        txtSigla.setEditable(false);
        txtSigla.setColumns(20);
        panelDatos.add(txtSigla, gbcSiglaField);

        // Fecha Inicio
        GridBagConstraints gbcFechaInicioLabel = new GridBagConstraints();
        gbcFechaInicioLabel.insets = new Insets(5, 5, 5, 5);
        gbcFechaInicioLabel.anchor = GridBagConstraints.WEST;
        gbcFechaInicioLabel.fill = GridBagConstraints.HORIZONTAL;
        gbcFechaInicioLabel.gridx = 0;
        gbcFechaInicioLabel.gridy = 2;
        panelDatos.add(new JLabel("Fecha Inicio:"), gbcFechaInicioLabel);

        GridBagConstraints gbcFechaInicioField = new GridBagConstraints();
        gbcFechaInicioField.insets = new Insets(5, 5, 5, 5);
        gbcFechaInicioField.anchor = GridBagConstraints.WEST;
        gbcFechaInicioField.fill = GridBagConstraints.HORIZONTAL;
        gbcFechaInicioField.gridx = 1;
        gbcFechaInicioField.gridy = 2;
        txtFechaInicio = new JTextField();
        txtFechaInicio.setEditable(false);
        txtFechaInicio.setColumns(20);
        panelDatos.add(txtFechaInicio, gbcFechaInicioField);

        // Fecha Fin
        GridBagConstraints gbcFechaFinLabel = new GridBagConstraints();
        gbcFechaFinLabel.insets = new Insets(5, 5, 5, 5);
        gbcFechaFinLabel.anchor = GridBagConstraints.WEST;
        gbcFechaFinLabel.fill = GridBagConstraints.HORIZONTAL;
        gbcFechaFinLabel.gridx = 0;
        gbcFechaFinLabel.gridy = 3;
        panelDatos.add(new JLabel("Fecha Fin:"), gbcFechaFinLabel);

        GridBagConstraints gbcFechaFinField = new GridBagConstraints();
        gbcFechaFinField.insets = new Insets(5, 5, 5, 5);
        gbcFechaFinField.anchor = GridBagConstraints.WEST;
        gbcFechaFinField.fill = GridBagConstraints.HORIZONTAL;
        gbcFechaFinField.gridx = 1;
        gbcFechaFinField.gridy = 3;
        txtFechaFin = new JTextField();
        txtFechaFin.setEditable(false);
        txtFechaFin.setColumns(20);
        panelDatos.add(txtFechaFin, gbcFechaFinField);

        // Fecha Alta
        GridBagConstraints gbcFechaAltaLabel = new GridBagConstraints();
        gbcFechaAltaLabel.insets = new Insets(5, 5, 5, 5);
        gbcFechaAltaLabel.anchor = GridBagConstraints.WEST;
        gbcFechaAltaLabel.fill = GridBagConstraints.HORIZONTAL;
        gbcFechaAltaLabel.gridx = 0;
        gbcFechaAltaLabel.gridy = 4;
        panelDatos.add(new JLabel("Fecha Alta:"), gbcFechaAltaLabel);

        GridBagConstraints gbcFechaAltaField = new GridBagConstraints();
        gbcFechaAltaField.insets = new Insets(5, 5, 5, 5);
        gbcFechaAltaField.anchor = GridBagConstraints.WEST;
        gbcFechaAltaField.fill = GridBagConstraints.HORIZONTAL;
        gbcFechaAltaField.gridx = 1;
        gbcFechaAltaField.gridy = 4;
        txtFechaAlta = new JTextField();
        txtFechaAlta.setEditable(false);
        txtFechaAlta.setColumns(20);
        panelDatos.add(txtFechaAlta, gbcFechaAltaField);

        // Ciudad
        GridBagConstraints gbcCiudadLabel = new GridBagConstraints();
        gbcCiudadLabel.insets = new Insets(5, 5, 5, 5);
        gbcCiudadLabel.anchor = GridBagConstraints.WEST;
        gbcCiudadLabel.fill = GridBagConstraints.HORIZONTAL;
        gbcCiudadLabel.gridx = 0;
        gbcCiudadLabel.gridy = 5;
        panelDatos.add(new JLabel("Ciudad:"), gbcCiudadLabel);

        GridBagConstraints gbcCiudadField = new GridBagConstraints();
        gbcCiudadField.insets = new Insets(5, 5, 5, 5);
        gbcCiudadField.anchor = GridBagConstraints.WEST;
        gbcCiudadField.fill = GridBagConstraints.HORIZONTAL;
        gbcCiudadField.gridx = 1;
        gbcCiudadField.gridy = 5;
        txtCiudad = new JTextField();
        txtCiudad.setEditable(false);
        txtCiudad.setColumns(20);
        panelDatos.add(txtCiudad, gbcCiudadField);

        // País
        GridBagConstraints gbcPaisLabel = new GridBagConstraints();
        gbcPaisLabel.insets = new Insets(5, 5, 5, 5);
        gbcPaisLabel.anchor = GridBagConstraints.WEST;
        gbcPaisLabel.fill = GridBagConstraints.HORIZONTAL;
        gbcPaisLabel.gridx = 0;
        gbcPaisLabel.gridy = 6;
        panelDatos.add(new JLabel("País:"), gbcPaisLabel);

        GridBagConstraints gbcPaisField = new GridBagConstraints();
        gbcPaisField.insets = new Insets(5, 5, 5, 5);
        gbcPaisField.anchor = GridBagConstraints.WEST;
        gbcPaisField.fill = GridBagConstraints.HORIZONTAL;
        gbcPaisField.gridx = 1;
        gbcPaisField.gridy = 6;
        txtPais = new JTextField();
        txtPais.setEditable(false);
        txtPais.setColumns(20);
        panelDatos.add(txtPais, gbcPaisField);

        // Organizador
        GridBagConstraints gbcOrganizadorLabel = new GridBagConstraints();
        gbcOrganizadorLabel.insets = new Insets(5, 5, 5, 5);
        gbcOrganizadorLabel.anchor = GridBagConstraints.WEST;
        gbcOrganizadorLabel.fill = GridBagConstraints.HORIZONTAL;
        gbcOrganizadorLabel.gridx = 0;
        gbcOrganizadorLabel.gridy = 7;
        panelDatos.add(new JLabel("Organizador:"), gbcOrganizadorLabel);

        GridBagConstraints gbcOrganizadorField = new GridBagConstraints();
        gbcOrganizadorField.insets = new Insets(5, 5, 5, 5);
        gbcOrganizadorField.anchor = GridBagConstraints.WEST;
        gbcOrganizadorField.fill = GridBagConstraints.HORIZONTAL;
        gbcOrganizadorField.gridx = 1;
        gbcOrganizadorField.gridy = 7;
        txtOrganizador = new JTextField();
        txtOrganizador.setEditable(false);
        txtOrganizador.setColumns(20);
        panelDatos.add(txtOrganizador, gbcOrganizadorField);

        // Tipos de Registro
        GridBagConstraints gbcTiposRegistroLabel = new GridBagConstraints();
        gbcTiposRegistroLabel.insets = new Insets(5, 5, 5, 5);
        gbcTiposRegistroLabel.anchor = GridBagConstraints.WEST;
        gbcTiposRegistroLabel.fill = GridBagConstraints.HORIZONTAL;
        gbcTiposRegistroLabel.gridx = 0;
        gbcTiposRegistroLabel.gridy = 8;
        panelDatos.add(new JLabel("Tipos de Registro:"), gbcTiposRegistroLabel);

        GridBagConstraints gbcTiposRegistroField = new GridBagConstraints();
        gbcTiposRegistroField.insets = new Insets(5, 5, 5, 5);
        gbcTiposRegistroField.anchor = GridBagConstraints.WEST;
        gbcTiposRegistroField.fill = GridBagConstraints.HORIZONTAL;
        gbcTiposRegistroField.gridx = 1;
        gbcTiposRegistroField.gridy = 8;
        comboTiposRegistro = new JComboBox<>();
        comboTiposRegistro.setMaximumSize(new Dimension(300, 25));
        panelDatos.add(comboTiposRegistro, gbcTiposRegistroField);

        // Patrocinios
        GridBagConstraints gbcPatrociniosLabel = new GridBagConstraints();
        gbcPatrociniosLabel.insets = new Insets(5, 5, 5, 5);
        gbcPatrociniosLabel.anchor = GridBagConstraints.WEST;
        gbcPatrociniosLabel.fill = GridBagConstraints.HORIZONTAL;
        gbcPatrociniosLabel.gridx = 0;
        gbcPatrociniosLabel.gridy = 9;
        panelDatos.add(new JLabel("Patrocinios:"), gbcPatrociniosLabel);

        GridBagConstraints gbcPatrociniosField = new GridBagConstraints();
        gbcPatrociniosField.insets = new Insets(5, 5, 5, 5);
        gbcPatrociniosField.anchor = GridBagConstraints.WEST;
        gbcPatrociniosField.fill = GridBagConstraints.HORIZONTAL;
        gbcPatrociniosField.gridx = 1;
        gbcPatrociniosField.gridy = 9;
        comboPatrocinios = new JComboBox<>();
        comboPatrocinios.setMaximumSize(new Dimension(300, 25));
        panelDatos.add(comboPatrocinios, gbcPatrociniosField);

        panelCentral.add(panelDatos);
        // Elimina el panelCombos extra
        getContentPane().add(panelCentral, BorderLayout.CENTER);

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