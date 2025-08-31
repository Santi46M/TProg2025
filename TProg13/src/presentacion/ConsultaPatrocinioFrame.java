package presentacion;

import javax.swing.*;

import logica.IControladorEvento;
import logica.IControladorUsuario;

import java.awt.*;

public class ConsultaPatrocinioFrame extends JInternalFrame {
    private JComboBox<String> comboEventos;
    private JComboBox<String> comboEdiciones;
    private JComboBox<String> comboPatrocinios;
    private JTextArea txtDatos;
    // Datos auxiliares para cascada
    private String[] eventos;
    private String[][] edicionesPorEvento;
    private String[][] patrociniosPorEdicion;
    private String[][] datosPatrocinio;

    public ConsultaPatrocinioFrame(IControladorUsuario iCU, IControladorEvento iCE) {
        super("Consulta de Patrocinio", true, true, true, true);
        setBounds(220, 220, 500, 320);
        setVisible(false);
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
        JLabel lblPatrocinio = new JLabel("Patrocinio:");
        comboPatrocinios = new JComboBox<>();
        panelSeleccion.add(lblPatrocinio);
        panelSeleccion.add(comboPatrocinios);
        add(panelSeleccion, BorderLayout.NORTH);

        txtDatos = new JTextArea(8, 40);
        txtDatos.setEditable(false);
        add(new JScrollPane(txtDatos), BorderLayout.CENTER);

        // Listeners para cascada
        comboEventos.addActionListener(e -> cargarEdiciones());
        comboEdiciones.addActionListener(e -> cargarPatrocinios());
        comboPatrocinios.addActionListener(e -> mostrarDatosPatrocinio());
    }

    public void cargarDatos() {
        // Cargar eventos y datos auxiliares desde la lógica
        logica.ControladorEvento controlador = new logica.ControladorEvento();
        java.util.List<logica.DTEvento> listaEventos = controlador.listarEventos();
        eventos = new String[listaEventos.size()];
        edicionesPorEvento = new String[listaEventos.size()][];
        java.util.List<String[]> patsList = new java.util.ArrayList<>();
        java.util.List<String[]> datosList = new java.util.ArrayList<>();
        for (int i = 0; i < listaEventos.size(); i++) {
            logica.DTEvento ev = listaEventos.get(i);
            eventos[i] = ev.getNombre();
            java.util.List<String> eds = controlador.listarEdicionesEvento(ev.getNombre());
            edicionesPorEvento[i] = eds.toArray(new String[0]);
            // Para cada edición, cargar patrocinios y datos
            for (String ed : eds) {
                logica.Ediciones edi = controlador.obtenerEdicion(ev.getNombre(), ed);
                java.util.List<String> pats = new java.util.ArrayList<>();
                java.util.List<String> datosPat = new java.util.ArrayList<>();
                if (edi != null) {
                    for (logica.Patrocinio p : edi.getPatrocinios()) {
                        pats.add(p.getCodigoPatrocinio());
                        String datos = "Institución: " + (p.getInstitucion() != null ? p.getInstitucion().getNombre() : "") +
                                "\nNivel: " + (p.getNivel() != null ? p.getNivel().toString() : "") +
                                "\nTipo Registro: " + (p.getTipoRegistro() != null ? p.getTipoRegistro().getNombre() : "") +
                                "\nAporte: " + p.getAporte() +
                                "\nFecha: " + (p.getFechaPatrocinio() != null ? p.getFechaPatrocinio().toString() : "") +
                                "\nCantidad Registros Gratuitos: " + p.getCantidadRegistros() +
                                "\nCódigo: " + p.getCodigoPatrocinio();
                        datosPat.add(datos);
                    }
                }
                patsList.add(pats.toArray(new String[0]));
                datosList.add(datosPat.toArray(new String[0]));
            }
        }
        patrociniosPorEdicion = patsList.toArray(new String[0][0]);
        datosPatrocinio = datosList.toArray(new String[0][0]);
        // Cargar combos
        comboEventos.removeAllItems();
        for (String ev : eventos) comboEventos.addItem(ev);
        if (eventos.length > 0) {
            comboEventos.setSelectedIndex(0);
            cargarEdiciones();
        } else {
            comboEdiciones.removeAllItems();
            comboPatrocinios.removeAllItems();
            txtDatos.setText("");
        }
    }

    private void cargarEdiciones() {
        int idx = comboEventos.getSelectedIndex();
        comboEdiciones.removeAllItems();
        comboPatrocinios.removeAllItems();
        txtDatos.setText("");
        if (idx < 0 || edicionesPorEvento == null || idx >= edicionesPorEvento.length) return;
        String[] ediciones = edicionesPorEvento[idx];
        for (String ed : ediciones) comboEdiciones.addItem(ed);
        if (ediciones.length > 0) {
            comboEdiciones.setSelectedIndex(0);
            cargarPatrocinios();
        }
    }

    private void cargarPatrocinios() {
        int idxEvento = comboEventos.getSelectedIndex();
        int idxEdicion = comboEdiciones.getSelectedIndex();
        comboPatrocinios.removeAllItems();
        txtDatos.setText("");
        if (idxEvento < 0 || idxEdicion < 0) return;
        // Calcular el índice global de la edición
        int idxGlobal = 0;
        for (int i = 0; i < idxEvento; i++) {
            idxGlobal += edicionesPorEvento[i].length;
        }
        idxGlobal += idxEdicion;
        if (patrociniosPorEdicion == null || idxGlobal >= patrociniosPorEdicion.length) return;
        String[] pats = patrociniosPorEdicion[idxGlobal];
        for (String pat : pats) comboPatrocinios.addItem(pat);
        if (pats.length > 0) {
            comboPatrocinios.setSelectedIndex(0);
            mostrarDatosPatrocinio();
        }
    }

    private void mostrarDatosPatrocinio() {
        int idxEvento = comboEventos.getSelectedIndex();
        int idxEdicion = comboEdiciones.getSelectedIndex();
        int idxPatrocinio = comboPatrocinios.getSelectedIndex();
        txtDatos.setText("");
        if (idxEvento < 0 || idxEdicion < 0 || idxPatrocinio < 0) return;
        // Calcular el índice global de la edición
        int idxGlobal = 0;
        for (int i = 0; i < idxEvento; i++) {
            idxGlobal += edicionesPorEvento[i].length;
        }
        idxGlobal += idxEdicion;
        if (datosPatrocinio == null || idxGlobal >= datosPatrocinio.length) return;
        String[] datos = datosPatrocinio[idxGlobal];
        if (idxPatrocinio >= datos.length) return;
        txtDatos.setText(datos[idxPatrocinio]);
    }
}