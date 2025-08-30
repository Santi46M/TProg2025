package presentacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import logica.ControladorEvento;
import logica.Ediciones;
import logica.Eventos;
import logica.TipoRegistro;
import logica.Patrocinio;
import java.util.List;

public class ConsultaEdicionEventoFrame extends JInternalFrame {
    private JComboBox<String> comboEventos;
    private JComboBox<String> comboEdiciones;
    private JTextArea txtDetalles;
    private JList<String> listTiposRegistro;
    private JList<String> listPatrocinios;

    public ConsultaEdicionEventoFrame() {
        super("Consulta Edición de Evento", true, true, true, true);
        setBounds(100, 100, 700, 500);
        setLayout(new BorderLayout());

        JPanel panelTop = new JPanel(new FlowLayout());
        panelTop.add(new JLabel("Evento:"));
        comboEventos = new JComboBox<>();
        comboEdiciones = new JComboBox<>(); // Inicializar antes de cargarEventos
        cargarEventos();
        panelTop.add(comboEventos);
        panelTop.add(new JLabel("Edición:"));
        panelTop.add(comboEdiciones);
        JButton btnConsultar = new JButton("Consultar");
        panelTop.add(btnConsultar);
        add(panelTop, BorderLayout.NORTH);

        JPanel panelCenter = new JPanel(new GridLayout(1, 3));
        txtDetalles = new JTextArea();
        txtDetalles.setEditable(false);
        panelCenter.add(new JScrollPane(txtDetalles));

        listTiposRegistro = new JList<>();
        panelCenter.add(new JScrollPane(listTiposRegistro));
        listPatrocinios = new JList<>();
        panelCenter.add(new JScrollPane(listPatrocinios));
        add(panelCenter, BorderLayout.CENTER);

        comboEventos.addActionListener(e -> cargarEdiciones());
        btnConsultar.addActionListener(e -> mostrarDetallesEdicion());
        //listTiposRegistro.addListSelectionListener(e -> mostrarDetalleTipoRegistro());
        //listPatrocinios.addListSelectionListener(e -> mostrarDetallePatrocinio());
    }

    private void cargarEventos() {
        comboEventos.removeAllItems();
        List<logica.DTEvento> eventos = new ControladorEvento().listarEventos();
        for (logica.DTEvento e : eventos) comboEventos.addItem(e.getNombre());
        cargarEdiciones();
    }

    private void cargarEdiciones() {
        comboEdiciones.removeAllItems();
        String evento = (String) comboEventos.getSelectedItem();
        if (evento == null) return;
        List<String> ediciones = new ControladorEvento().listarEdicionesEvento(evento);
        for (String ed : ediciones) comboEdiciones.addItem(ed);
    }

    private void mostrarDetallesEdicion() {
        String evento = (String) comboEventos.getSelectedItem();
        String edicion = (String) comboEdiciones.getSelectedItem();
        if (evento == null || edicion == null) {
            txtDetalles.setText("Seleccione evento y edición.");
            return;
        }
        Ediciones edi = new ControladorEvento().obtenerEdicion(evento, edicion);
        if (edi == null) {
            txtDetalles.setText("No se encontró la edición.");
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
        txtDetalles.setText(sb.toString());
        // Tipos de registro
        DefaultListModel<String> modelTipos = new DefaultListModel<>();
        for (TipoRegistro tr : edi.getTiposRegistro()) {
            modelTipos.addElement(tr.getNombre());
        }
        listTiposRegistro.setModel(modelTipos);
        // Patrocinios
        DefaultListModel<String> modelPats = new DefaultListModel<>();
        for (Patrocinio p : edi.getPatrocinios()) {
            modelPats.addElement(p.getCodigoPatrocinio());
        }
        listPatrocinios.setModel(modelPats);
    }
    
    /*
    private void mostrarDetalleTipoRegistro() {
        String evento = (String) comboEventos.getSelectedItem();
        String edicion = (String) comboEdiciones.getSelectedItem();
        String tipo = listTiposRegistro.getSelectedValue();
        if (evento == null || edicion == null || tipo == null) return;
        Ediciones edi = new ControladorEvento().obtenerEdicion(evento, edicion);
        if (edi == null) return;
        TipoRegistro tr = edi.getTiposRegistro().get(tipo);
        if (tr == null) return;
        JOptionPane.showMessageDialog(this, "Tipo de Registro:\nNombre: " + tr.getNombre() + "\nDescripción: " + tr.getDescripcion() + "\nCosto: " + tr.getCosto() + "\nCupo: " + tr.getCupo());
    }

    private void mostrarDetallePatrocinio() {
        String evento = (String) comboEventos.getSelectedItem();
        String edicion = (String) comboEdiciones.getSelectedItem();
        String codPat = listPatrocinios.getSelectedValue();
        if (evento == null || edicion == null || codPat == null) return;
        Ediciones edi = new ControladorEvento().obtenerEdicion(evento, edicion);
        if (edi == null) return;
        for (Patrocinio p : edi.getPatrocinios()) {
            if (p.getCodigoPatrocinio().equals(codPat)) {
                JOptionPane.showMessageDialog(this, "Patrocinio:\nCódigo: " + p.getCodigoPatrocinio() + "\nInstitución: " + (p.getInstitucion() != null ? p.getInstitucion().getNombre() : "") + "\nNivel: " + (p.getNivel() != null ? p.getNivel().toString() : "") + "\nAporte: " + p.getAporte() + "\nCantidad Registros: " + p.getCantidadRegistros());
                break;
            }
        }
    }
    */
}