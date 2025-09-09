package presentacion;

import javax.swing.*;
import java.awt.*;
import logica.Controladores.ControladorEvento;
import logica.Datatypes.DTEvento;
import logica.Clases.Ediciones;
import logica.Interfaces.*;
import logica.Clases.TipoRegistro;
import java.util.List;
import java.util.ArrayList;

public class ConsultaTipoRegistroFrame extends JInternalFrame {
    private JComboBox<String> comboEventos;
    private JComboBox<String> comboEdiciones;
    private JComboBox<String> comboTipos;
    private JTextField txtNombre;
    private JTextField txtDescripcion;
    private JTextField txtCupo;
    private JTextField txtCosto;
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

        JPanel panelDatos = new JPanel();
        panelDatos.setLayout(new BoxLayout(panelDatos, BoxLayout.Y_AXIS));
        JPanel panelCampos = new JPanel(new GridLayout(0, 2, 10, 10));
        panelDatos.add(panelCampos);
        panelCampos.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        txtNombre.setEditable(false);
        panelCampos.add(txtNombre);
        panelCampos.add(new JLabel("Descripción:"));
        txtDescripcion = new JTextField();
        txtDescripcion.setEditable(false);
        panelCampos.add(txtDescripcion);
        panelCampos.add(new JLabel("Cupo:"));
        txtCupo = new JTextField();
        txtCupo.setEditable(false);
        panelCampos.add(txtCupo);
        panelCampos.add(new JLabel("Costo:"));
        txtCosto = new JTextField();
        txtCosto.setEditable(false);
        panelCampos.add(txtCosto);
        add(panelDatos, BorderLayout.CENTER);

        comboEventos.addActionListener(e -> cargarEdiciones());
        comboEdiciones.addActionListener(e -> cargarTipos());
        comboTipos.addActionListener(e -> mostrarDatosTipo());
    }

    public ConsultaTipoRegistroFrame(IControladorUsuario iCU, IControladorEvento iCE, String nombreEvento, String nombreEdicion, String nombreTipo) {
        this(iCU, iCE);
        cargarEventos();
        // Selecciona el evento, edición y tipo en los combos
        if (nombreEvento != null && nombreEdicion != null && nombreTipo != null) {
            for (int i = 0; i < comboEventos.getItemCount(); i++) {
                if (comboEventos.getItemAt(i).equals(nombreEvento)) {
                    comboEventos.setSelectedIndex(i);
                    break;
                }
            }
            cargarEdiciones();
            for (int j = 0; j < comboEdiciones.getItemCount(); j++) {
                if (comboEdiciones.getItemAt(j).equals(nombreEdicion)) {
                    comboEdiciones.setSelectedIndex(j);
                    break;
                }
            }
            cargarTipos();
            for (int k = 0; k < comboTipos.getItemCount(); k++) {
                if (comboTipos.getItemAt(k).equals(nombreTipo)) {
                    comboTipos.setSelectedIndex(k);
                    break;
                }
            }
            mostrarDatosTipo();
        }
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
                txtNombre.setText("");
                txtDescripcion.setText("");
                txtCupo.setText("");
                txtCosto.setText("");
            }
        } catch (Exception ex) {
            comboEventos.setModel(new DefaultComboBoxModel<>(new String[]{"No hay eventos"}));
            comboEdiciones.removeAllItems();
            comboTipos.removeAllItems();
            txtNombre.setText("");
            txtDescripcion.setText("");
            txtCupo.setText("");
            txtCosto.setText("");
        }
    }

    private void cargarEdiciones() {
        comboEdiciones.removeAllItems();
        comboTipos.removeAllItems();
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtCupo.setText("");
        txtCosto.setText("");
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
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtCupo.setText("");
        txtCosto.setText("");
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
        int idxTipo = comboTipos.getSelectedIndex();
        if (tiposActuales == null || idxTipo < 0 || idxTipo >= tiposActuales.size()) {
            txtNombre.setText("");
            txtDescripcion.setText("");
            txtCupo.setText("");
            txtCosto.setText("");
            return;
        }
        TipoRegistro tr = tiposActuales.get(idxTipo);
        txtNombre.setText(tr.getNombre());
        txtDescripcion.setText(tr.getDescripcion());
        txtCupo.setText(String.valueOf(tr.getCupo()));
        txtCosto.setText(String.valueOf(tr.getCosto()));
    }
}