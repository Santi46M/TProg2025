package presentacion;

import javax.swing.*;

import logica.Interfaces.*;

import java.awt.*;
import java.util.List;

public class ConsultaEdicionEventoFrame extends JInternalFrame {
    private JComboBox<String> comboEventos;
    private JComboBox<String> comboEdiciones;
    private DefaultListModel<String> modelTiposRegistro;
    private DefaultListModel<String> modelPatrocinios;
    private JList<String> listTiposRegistro;
    private JList<String> listPatrocinios;
    private JTextArea txtDatos;
    private JLabel lblCategorias;
    private JTextArea txtDetalle;

    private String[][] datosEventos;
    private String[][] categoriasEventos;
    private String[][] edicionesEventos;

    private boolean cargando = false;
    private final IControladorUsuario controlUsr;
    private final IControladorEvento controlEvt;

    public ConsultaEdicionEventoFrame(IControladorUsuario iCU, IControladorEvento ICE) {
        super("Consulta Edición de Evento", true, true, true, true);
        setBounds(100, 100, 800, 500);
        setLayout(new BorderLayout());
        this.controlUsr = iCU;
        this.controlEvt = ICE;

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

        JPanel panelCentral = new JPanel(new GridLayout(1, 3));
        JPanel panelDatos = new JPanel(new BorderLayout());
        txtDatos = new JTextArea(8, 30);
        txtDatos.setEditable(false);
        panelDatos.add(new JScrollPane(txtDatos), BorderLayout.CENTER);
        lblCategorias = new JLabel();
        panelDatos.add(lblCategorias, BorderLayout.SOUTH);
        panelCentral.add(panelDatos);

        JPanel panelTipos = new JPanel(new BorderLayout());
        panelTipos.add(new JLabel("Tipos de Registro"), BorderLayout.NORTH);
        modelTiposRegistro = new DefaultListModel<>();
        listTiposRegistro = new JList<>(modelTiposRegistro);
        panelTipos.add(new JScrollPane(listTiposRegistro), BorderLayout.CENTER);
        panelCentral.add(panelTipos);

        JPanel panelPatroc = new JPanel(new BorderLayout());
        panelPatroc.add(new JLabel("Patrocinios"), BorderLayout.NORTH);
        modelPatrocinios = new DefaultListModel<>();
        listPatrocinios = new JList<>(modelPatrocinios);
        panelPatroc.add(new JScrollPane(listPatrocinios), BorderLayout.CENTER);
        panelCentral.add(panelPatroc);

        add(panelCentral, BorderLayout.CENTER);

        JPanel panelDetalle = new JPanel(new BorderLayout());
        txtDetalle = new JTextArea(5, 60);
        txtDetalle.setEditable(false);
        panelDetalle.add(new JLabel("Detalle"), BorderLayout.NORTH);
        panelDetalle.add(new JScrollPane(txtDetalle), BorderLayout.CENTER);
        add(panelDetalle, BorderLayout.SOUTH);

        comboEventos.addActionListener(e -> {
            if (cargando) return;
            cargarEdicionesEvento();
        });
        comboEdiciones.addActionListener(e -> {
            if (cargando) return;
            mostrarDatosEdicion();
        });
        listTiposRegistro.addListSelectionListener(e -> mostrarDetalleTipoRegistro());
        listPatrocinios.addListSelectionListener(e -> mostrarDetallePatrocinio());

        cargarEventos();
        inicializarPreseleccion();
    }

    public void cargarEventos() {
        try {
            logica.Controladores.ControladorEvento controlador = new logica.Controladores.ControladorEvento();
            List<logica.Datatypes.DTEvento> eventos = controlador.listarEventos();
            String[] eventosArr = new String[eventos.size()];
            datosEventos = new String[eventos.size()][1];
            categoriasEventos = new String[eventos.size()][];
            edicionesEventos = new String[eventos.size()][];
            for (int i = 0; i < eventos.size(); i++) {
                logica.Datatypes.DTEvento ev = eventos.get(i);
                eventosArr[i] = ev.getNombre();
                datosEventos[i][0] = ev.getDescripcion();
                categoriasEventos[i] = ev.getCategorias().toArray(new String[0]);
                edicionesEventos[i] = ev.getEdiciones().toArray(new String[0]);
            }
            comboEventos.setModel(new DefaultComboBoxModel<>(eventosArr));
            if (eventosArr.length > 0) {
                comboEventos.setSelectedIndex(0);
                mostrarDatosEvento();
            } else {
                comboEventos.setModel(new DefaultComboBoxModel<>(new String[]{"No hay eventos"}));
                txtDatos.setText("");
                lblCategorias.setText("");
                modelTiposRegistro.clear();
                modelPatrocinios.clear();
                txtDetalle.setText("");
            }
        } catch (Exception ex) {
            comboEventos.setModel(new DefaultComboBoxModel<>(new String[]{"No hay eventos"}));
            txtDatos.setText("");
            lblCategorias.setText("");
            modelTiposRegistro.clear();
            modelPatrocinios.clear();
            txtDetalle.setText("");
        }
    }

    private void inicializarPreseleccion() {
        cargando = true;

        String siglaSel = controlEvt.getEdicionSeleccionadaSigla();
        if (siglaSel != null && edicionesEventos != null) {
            outer:
            for (int i = 0; i < comboEventos.getItemCount(); i++) {
                String[] eds = edicionesEventos[i];
                if (eds == null) continue;
                for (int j = 0; j < eds.length; j++) {
                    if (siglaSel.equals(eds[j])) {
                        comboEventos.setSelectedIndex(i);
                        cargarEdicionesEvento();
                        comboEdiciones.setSelectedIndex(j);
                        mostrarDatosEdicion();
                        break outer;
                    }
                }
            }
        } else {
            cargarEdicionesEvento();
            mostrarDatosEdicion();
        }

        cargando = false;
    }

    private void cargarEdicionesEvento() {
        int idx = comboEventos.getSelectedIndex();
        comboEdiciones.removeAllItems();
        txtDatos.setText("");
        lblCategorias.setText("");
        modelTiposRegistro.clear();
        modelPatrocinios.clear();
        txtDetalle.setText("");

        if (idx < 0 || edicionesEventos == null || idx >= edicionesEventos.length) return;

        String[] ediciones = edicionesEventos[idx];
        if (ediciones != null) {
            for (String ed : ediciones) comboEdiciones.addItem(ed);
        }
        if (comboEdiciones.getItemCount() > 0) {
            comboEdiciones.setSelectedIndex(0);
        }
        mostrarDatosEvento();
    }

    private void mostrarDatosEvento() {
        int idx = comboEventos.getSelectedIndex();
        if (idx < 0 || datosEventos == null || idx >= datosEventos.length) {
            txtDatos.setText("");
            lblCategorias.setText("");
            return;
        }
        txtDatos.setText(datosEventos[idx][0]);
        StringBuilder cats = new StringBuilder("Categorías: ");
        if (categoriasEventos[idx] != null) {
            for (String cat : categoriasEventos[idx]) cats.append(cat).append(", ");
            if (cats.length() > 12) cats.setLength(cats.length() - 2);
        }
        lblCategorias.setText(cats.toString());
    }

    private void mostrarDatosEdicion() {
        int idxEvento = comboEventos.getSelectedIndex();
        int idxEd = comboEdiciones.getSelectedIndex();
        modelTiposRegistro.clear();
        modelPatrocinios.clear();
        txtDetalle.setText("");

        if (idxEvento < 0 || idxEd < 0 || edicionesEventos == null || idxEvento >= edicionesEventos.length) {
            txtDatos.setText("");
            lblCategorias.setText("");
            return;
        }
        String[] ediciones = edicionesEventos[idxEvento];
        if (ediciones == null || idxEd >= ediciones.length) return;

        String nombreEdicion = ediciones[idxEd];
        String nombreEvento = comboEventos.getItemAt(idxEvento);
        logica.Clases.Ediciones edi = new logica.Controladores.ControladorEvento().obtenerEdicion(nombreEvento, nombreEdicion);
        if (edi == null) {
            txtDatos.setText("No se encontró la edición.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Nombre: ").append(edi.getNombre()).append("\n");
        sb.append("Sigla: ").append(edi.getSigla()).append("\n");
        sb.append("Fechas: ").append(edi.getFechaInicio()).append(" a ").append(edi.getFechaFin()).append("\n");
        sb.append("Fecha alta: ").append(edi.getFechaAlta()).append("\n");
        sb.append("Ciudad: ").append(edi.getCiudad()).append("\n");
        sb.append("País: ").append(edi.getPais()).append("\n");
        sb.append("Organizador: ").append(edi.getOrganizador() != null ? edi.getOrganizador().getNickname() : "").append("\n");
        txtDatos.setText(sb.toString());

        for (logica.Clases.TipoRegistro tr : edi.getTiposRegistro()) {
            modelTiposRegistro.addElement(tr.getNombre());
        }
        for (logica.Clases.Patrocinio p : edi.getPatrocinios()) {
            modelPatrocinios.addElement(p.getCodigoPatrocinio());
        }
    }

    private void mostrarDetalleTipoRegistro() {
        int idxEvento = comboEventos.getSelectedIndex();
        int idxEd = comboEdiciones.getSelectedIndex();
        int idxTipo = listTiposRegistro.getSelectedIndex();
        txtDetalle.setText("");
        if (idxEvento < 0 || idxEd < 0 || idxTipo < 0) return;

        String[] ediciones = edicionesEventos[idxEvento];
        if (ediciones == null || idxEd >= ediciones.length) return;

        String nombreEdicion = ediciones[idxEd];
        String nombreEvento = comboEventos.getItemAt(idxEvento);
        String nombreTipo = modelTiposRegistro.get(idxTipo);

        logica.Clases.Ediciones edi = new logica.Controladores.ControladorEvento().obtenerEdicion(nombreEvento, nombreEdicion);
        if (edi == null) return;

        logica.Clases.TipoRegistro tr = edi.getTipoRegistro(nombreTipo);
        if (tr == null) return;

        StringBuilder sb = new StringBuilder();
        sb.append("Tipo de Registro: ").append(tr.getNombre()).append("\n");
        sb.append("Descripción: ").append(tr.getDescripcion()).append("\n");
        sb.append("Cupo: ").append(tr.getCupo()).append("\n");
        sb.append("Costo: ").append(tr.getCosto()).append("\n");
        txtDetalle.setText(sb.toString());
    }

    private void mostrarDetallePatrocinio() {
        int idxEvento = comboEventos.getSelectedIndex();
        int idxEd = comboEdiciones.getSelectedIndex();
        int idxPat = listPatrocinios.getSelectedIndex();
        txtDetalle.setText("");
        if (idxEvento < 0 || idxEd < 0 || idxPat < 0) return;

        String[] ediciones = edicionesEventos[idxEvento];
        if (ediciones == null || idxEd >= ediciones.length) return;

        String nombreEdicion = ediciones[idxEd];
        String nombreEvento = comboEventos.getItemAt(idxEvento);
        String codigoPat = modelPatrocinios.get(idxPat);

        logica.Clases.Ediciones edi = new logica.Controladores.ControladorEvento().obtenerEdicion(nombreEvento, nombreEdicion);
        if (edi == null) return;

        logica.Clases.Patrocinio p = edi.getPatrocinio(codigoPat);
        if (p == null) return;

        StringBuilder sb = new StringBuilder();
        sb.append("Patrocinio: ").append(p.getCodigoPatrocinio()).append("\n");
        sb.append("Institución: ").append(p.getInstitucion() != null ? p.getInstitucion().getNombre() : "").append("\n");
        sb.append("Nivel: ").append(p.getNivel() != null ? p.getNivel().toString() : "").append("\n");
        sb.append("Aporte: ").append(p.getAporte()).append("\n");
        sb.append("Cantidad Registros: ").append(p.getCantidadRegistros()).append("\n");
        txtDetalle.setText(sb.toString());
    }
}