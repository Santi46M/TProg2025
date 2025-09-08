package presentacion;

import javax.swing.*;
import java.awt.*;
import logica.ControladorEvento;
import logica.DTEvento;
import logica.Ediciones;
import logica.IControladorEvento;
import logica.IControladorUsuario;
import logica.TipoRegistro;
import java.util.List;
import java.util.ArrayList;

public class ConsultaTipoRegistroFrame extends JInternalFrame {
    private JComboBox<String> comboEventos;
    private JComboBox<String> comboEdiciones;
    private JComboBox<String> comboTipos;
    private JTextArea txtDatos;
    private List<DTEvento> eventosDTO;
    private List<String> edicionesActuales;
    private List<TipoRegistro> tiposActuales;

    public ConsultaTipoRegistroFrame(IControladorUsuario iCU, IControladorEvento iCE) {
        super("Consulta de Tipo de Registro", true, true, true, true);
        setBounds(150, 150, 500, 350);
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
        JLabel lblTipo = new JLabel("Tipo de Registro:");
        comboTipos = new JComboBox<>();
        panelSeleccion.add(lblTipo);
        panelSeleccion.add(comboTipos);
        add(panelSeleccion, BorderLayout.NORTH);

        txtDatos = new JTextArea(8, 40);
        txtDatos.setEditable(false);
        add(new JScrollPane(txtDatos), BorderLayout.CENTER);

        comboEventos.addActionListener(e -> cargarEdiciones());
        comboEdiciones.addActionListener(e -> cargarTipos());
        comboTipos.addActionListener(e -> mostrarDatosTipo());
    }

    public void cargarEventos() {
        try {
            ControladorEvento controlador = new ControladorEvento();
            eventosDTO = controlador.listarEventos();
            comboEventos.removeAllItems();
            for (DTEvento ev : eventosDTO) {
                comboEventos.addItem(ev.getNombre());
            }
            if (comboEventos.getItemCount() > 0) {
                comboEventos.setSelectedIndex(0);
                cargarEdiciones();
            } else {
                comboEdiciones.removeAllItems();
                comboTipos.removeAllItems();
                txtDatos.setText("");
            }
        } catch (Exception ex) {
            comboEventos.setModel(new DefaultComboBoxModel<>(new String[]{"No hay eventos"}));
            comboEdiciones.removeAllItems();
            comboTipos.removeAllItems();
            txtDatos.setText("");
        }
    }

    private void cargarEdiciones() {
        comboEdiciones.removeAllItems();
        comboTipos.removeAllItems();
        txtDatos.setText("");
        int idx = comboEventos.getSelectedIndex();
        if (idx < 0 || eventosDTO == null || idx >= eventosDTO.size()) return;
        DTEvento evento = eventosDTO.get(idx);
        edicionesActuales = evento.getEdiciones();
        for (String ed : edicionesActuales) {
            comboEdiciones.addItem(ed);
        }
        if (comboEdiciones.getItemCount() > 0) {
            comboEdiciones.setSelectedIndex(0);
            cargarTipos();
        }
    }

    private void cargarTipos() {
        comboTipos.removeAllItems();
        txtDatos.setText("");
        int idxEvento = comboEventos.getSelectedIndex();
        int idxEdicion = comboEdiciones.getSelectedIndex();
        if (idxEvento < 0 || idxEdicion < 0 || eventosDTO == null || idxEvento >= eventosDTO.size() || edicionesActuales == null || idxEdicion >= edicionesActuales.size()) return;
        String nombreEvento = eventosDTO.get(idxEvento).getNombre();
        String nombreEdicion = edicionesActuales.get(idxEdicion);
        Ediciones edicion = new ControladorEvento().obtenerEdicion(nombreEvento, nombreEdicion);
        if (edicion == null) return;
        tiposActuales = new ArrayList<>(edicion.getTiposRegistro());
        for (TipoRegistro tr : tiposActuales) {
            comboTipos.addItem(tr.getNombre());
        }
        if (comboTipos.getItemCount() > 0) {
            comboTipos.setSelectedIndex(0);
            mostrarDatosTipo();
        }
    }

    private void mostrarDatosTipo() {
        txtDatos.setText("");
        int idxTipo = comboTipos.getSelectedIndex();
        if (tiposActuales == null || idxTipo < 0 || idxTipo >= tiposActuales.size()) return;
        TipoRegistro tr = tiposActuales.get(idxTipo);
        StringBuilder sb = new StringBuilder();
        sb.append("Nombre: ").append(tr.getNombre()).append("\n");
        sb.append("Descripción: ").append(tr.getDescripcion()).append("\n");
        sb.append("Cupo: ").append(tr.getCupo()).append("\n");
        sb.append("Costo: ").append(tr.getCosto()).append("\n");
        txtDatos.setText(sb.toString());
    }
}