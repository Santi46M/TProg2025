package presentacion;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import logica.*;

public class RegistroEdicionEventoFrame extends JInternalFrame {
    private JComboBox<String> comboEventos;
    private JComboBox<String> comboEdiciones;
    private JComboBox<String> comboTipos;
    private JComboBox<String> comboAsistentes;
    private JTextArea txtInfo;
    private JButton btnRegistrar;
    private JButton btnCancelar;

    private List<DTEvento> eventos;
    private String[][] edicionesPorEvento;
    private String[][] tiposPorEdicion;
    private List<Asistente> asistentes;
    private ControladorEvento controladorEvento;
    private IControladorUsuario controladorUsuario;

    public RegistroEdicionEventoFrame() {
        super("Registro a Edición de Evento", true, true, true, true);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setBounds(180, 180, 600, 350);
        setVisible(false);
        setLayout(new BorderLayout());
        controladorEvento = new ControladorEvento();
        controladorUsuario = fabrica.getInstance().getIControladorUsuario();

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
        JLabel lblAsistente = new JLabel("Asistente:");
        comboAsistentes = new JComboBox<>();
        panelSeleccion.add(lblAsistente);
        panelSeleccion.add(comboAsistentes);
        add(panelSeleccion, BorderLayout.NORTH);

        txtInfo = new JTextArea(5, 40);
        txtInfo.setEditable(false);
        add(new JScrollPane(txtInfo), BorderLayout.CENTER);

        btnRegistrar = new JButton("Registrar");
        btnCancelar = new JButton("Cancelar");
        JPanel panelBotones = new JPanel();
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);

        comboEventos.addActionListener(e -> cargarEdiciones());
        comboEdiciones.addActionListener(e -> cargarTipos());
        comboTipos.addActionListener(e -> mostrarInfo());
        comboAsistentes.addActionListener(e -> mostrarInfo());

        btnRegistrar.addActionListener(e -> registrar());
        btnCancelar.addActionListener(e -> this.dispose());
    }

    public void cargarDatos() {
        // Cargar eventos
        eventos = controladorEvento.listarEventos();
        comboEventos.removeAllItems();
        for (DTEvento ev : eventos) {
            comboEventos.addItem(ev.getNombre());
        }
        // Cargar asistentes
        Map<String, Asistente> mapAsistentes = controladorUsuario.listarAsistentes();
        asistentes = new ArrayList<>(mapAsistentes.values());
        comboAsistentes.removeAllItems();
        for (Asistente a : asistentes) {
            comboAsistentes.addItem(a.getNickname());
        }
        // Inicializar combos dependientes
        cargarEdiciones();
    }

    private void cargarEdiciones() {
        comboEdiciones.removeAllItems();
        int idxEvento = comboEventos.getSelectedIndex();
        if (idxEvento < 0) return;
        DTEvento evento = eventos.get(idxEvento);
        for (String ed : evento.getEdiciones()) {
            comboEdiciones.addItem(ed);
        }
        cargarTipos();
    }

    private void cargarTipos() {
        comboTipos.removeAllItems();
        int idxEvento = comboEventos.getSelectedIndex();
        int idxEdicion = comboEdiciones.getSelectedIndex();
        if (idxEvento < 0 || idxEdicion < 0) return;
        String nombreEvento = eventos.get(idxEvento).getNombre();
        String nombreEdicion = (String) comboEdiciones.getSelectedItem();
        Ediciones edicion = controladorEvento.obtenerEdicion(nombreEvento, nombreEdicion);
        if (edicion == null) return;
        for (TipoRegistro tr : edicion.getTiposRegistro()) {
            comboTipos.addItem(tr.getNombre());
        }
        mostrarInfo();
    }

    private void mostrarInfo() {
        int idxEvento = comboEventos.getSelectedIndex();
        int idxEdicion = comboEdiciones.getSelectedIndex();
        int idxTipo = comboTipos.getSelectedIndex();
        int idxAsistente = comboAsistentes.getSelectedIndex();
        txtInfo.setText("");
        if (idxEvento < 0 || idxEdicion < 0 || idxTipo < 0 || idxAsistente < 0) return;
        String nombreEvento = eventos.get(idxEvento).getNombre();
        String nombreEdicion = (String) comboEdiciones.getSelectedItem();
        String nombreTipo = (String) comboTipos.getSelectedItem();
        String nicknameAsistente = (String) comboAsistentes.getSelectedItem();
        Ediciones edicion = controladorEvento.obtenerEdicion(nombreEvento, nombreEdicion);
        if (edicion == null) return;
        TipoRegistro tipo = edicion.getTipoRegistro(nombreTipo);
        if (tipo == null) return;
        // Calcular cupo disponible
        int cantidadRegistrados = 0;
        for (Registro reg : edicion.getRegistros().values()) {
            if (reg.getTipoRegistro().equals(tipo)) {
                cantidadRegistrados++;
            }
        }
        int cupoDisponible = tipo.getCupo() - cantidadRegistrados;
        // Verificar si el asistente ya está registrado
        Asistente asistente = null;
        for (Asistente a : asistentes) {
            if (a.getNickname().equals(nicknameAsistente)) {
                asistente = a;
                break;
            }
        }
        boolean yaRegistrado = false;
        if (asistente != null) {
            for (Registro reg : asistente.getRegistros().values()) {
                if (reg.getEdicion().equals(edicion)) {
                    yaRegistrado = true;
                    break;
                }
            }
        }
        txtInfo.setText("Cupo disponible: " + cupoDisponible + (yaRegistrado ? "\nEl asistente ya está registrado a esta edición." : ""));
    }

    private void registrar() {
        int idxEvento = comboEventos.getSelectedIndex();
        int idxEdicion = comboEdiciones.getSelectedIndex();
        int idxTipo = comboTipos.getSelectedIndex();
        int idxAsistente = comboAsistentes.getSelectedIndex();
        if (idxEvento < 0 || idxEdicion < 0 || idxTipo < 0 || idxAsistente < 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar evento, edición, tipo y asistente.");
            return;
        }
        String nombreEvento = eventos.get(idxEvento).getNombre();
        String nombreEdicion = (String) comboEdiciones.getSelectedItem();
        String nombreTipo = (String) comboTipos.getSelectedItem();
        String nicknameAsistente = (String) comboAsistentes.getSelectedItem();
        Ediciones edicion = controladorEvento.obtenerEdicion(nombreEvento, nombreEdicion);
        TipoRegistro tipo = edicion.getTipoRegistro(nombreTipo);
        Asistente asistente = null;
        for (Asistente a : asistentes) {
            if (a.getNickname().equals(nicknameAsistente)) {
                asistente = a;
                break;
            }
        }
        if (edicion == null || tipo == null || asistente == null) {
            JOptionPane.showMessageDialog(this, "Datos inválidos para el registro.");
            return;
        }
        // Verificar cupo y registro previo
        int cantidadRegistrados = 0;
        for (Registro reg : edicion.getRegistros().values()) {
            if (reg.getTipoRegistro().equals(tipo)) {
                cantidadRegistrados++;
            }
        }
        if (cantidadRegistrados >= tipo.getCupo()) {
            JOptionPane.showMessageDialog(this, "Ya se alcanzó el cupo para este tipo de registro.");
            return;
        }
        for (Registro reg : asistente.getRegistros().values()) {
            if (reg.getEdicion().equals(edicion)) {
                JOptionPane.showMessageDialog(this, "El asistente ya está registrado a esta edición. Puede editar el registro o cancelar.");
                return;
            }
        }
        // Realizar alta
        try {
            controladorEvento.altaRegistroEdicionEvento("a", asistente, edicion, tipo, java.time.LocalDate.now(), tipo.getCosto(), edicion.getFechaInicio());
            JOptionPane.showMessageDialog(this, "Registro exitoso.");
            mostrarInfo();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar: " + ex.getMessage());
        }
    }
}