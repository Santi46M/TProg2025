package presentacion;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import logica.*;
import java.time.LocalDate;

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
    private List<logica.Usuario> usuarios; // Cambiado para almacenar todos los usuarios
    private ControladorEvento controladorEvento;
    private IControladorUsuario controladorUsuario;

    public RegistroEdicionEventoFrame() {
        super("Registro a Edición de Evento", true, true, true, true);
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
        add(panelSeleccion, BorderLayout.NORTH);

        // Panel para el combo de asistentes debajo
        JPanel panelAsistentes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblAsistente = new JLabel("Usuario:");
        comboAsistentes = new JComboBox<>();
        panelAsistentes.add(lblAsistente);
        panelAsistentes.add(comboAsistentes);
        add(panelAsistentes, BorderLayout.CENTER);

        txtInfo = new JTextArea(5, 40);
        txtInfo.setEditable(false);
        add(new JScrollPane(txtInfo), BorderLayout.SOUTH);

        btnRegistrar = new JButton("Registrar");
        btnCancelar = new JButton("Cancelar");
        JPanel panelBotones = new JPanel();
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.PAGE_END);

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
        if (comboEventos.getItemCount() > 0) {
            comboEventos.setSelectedIndex(0);
        }
        // Cargar todos los usuarios SIEMPRE
        Map<String, logica.Usuario> mapUsuarios = controladorUsuario.listarUsuarios();
        usuarios = new ArrayList<>(mapUsuarios.values());
        comboAsistentes.removeAllItems();
        for (logica.Usuario u : usuarios) {
            comboAsistentes.addItem(u.getNickname());
        }
        if (comboAsistentes.getItemCount() > 0) {
            comboAsistentes.setSelectedIndex(0);
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
        if (comboEdiciones.getItemCount() > 0) {
            comboEdiciones.setSelectedIndex(0);
        }
        cargarTipos();
        // NO tocar comboAsistentes aquí, siempre debe estar disponible
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
        if (comboTipos.getItemCount() > 0) {
            comboTipos.setSelectedIndex(0);
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
        // Verificar si el usuario ya está registrado
        logica.Usuario usuario = usuarios.get(idxAsistente);
        boolean yaRegistrado = false;
        if (usuario instanceof Asistente) {
            Asistente asistente = (Asistente) usuario;
            for (Registro reg : asistente.getRegistros().values()) {
                if (reg.getEdicion().equals(edicion)) {
                    yaRegistrado = true;
                    break;
                }
            }
        }
        txtInfo.setText("Cupo disponible: " + cupoDisponible + (yaRegistrado ? "\nEl usuario ya está registrado a esta edición." : ""));
    }

    private void registrar() {
        int idxEvento = comboEventos.getSelectedIndex();
        int idxEdicion = comboEdiciones.getSelectedIndex();
        int idxTipo = comboTipos.getSelectedIndex();
        int idxAsistente = comboAsistentes.getSelectedIndex();
        if (idxEvento < 0 || idxEdicion < 0 || idxTipo < 0 || idxAsistente < 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar evento, edición, tipo y usuario.");
            return;
        }
        String nombreEvento = eventos.get(idxEvento).getNombre();
        String nombreEdicion = (String) comboEdiciones.getSelectedItem();
        String nombreTipo = (String) comboTipos.getSelectedItem();
        String nicknameAsistente = (String) comboAsistentes.getSelectedItem();
        Ediciones edicion = controladorEvento.obtenerEdicion(nombreEvento, nombreEdicion);
        TipoRegistro tipo = edicion.getTipoRegistro(nombreTipo);
        logica.Usuario usuario = usuarios.get(idxAsistente);
        if (edicion == null || tipo == null || usuario == null) {
            JOptionPane.showMessageDialog(this, "Datos inválidos para el registro.");
            return;
        }
        // Solo permitir registrar si es Asistente
        if (!(usuario instanceof Asistente)) {
            JOptionPane.showMessageDialog(this, "Solo los usuarios de tipo Asistente pueden registrarse.");
            return;
        }
        Asistente asistente = (Asistente) usuario;
        // Verificar cupo y registro previo
        int cantidadRegistrados = 0;
        for (Registro reg : edicion.getRegistros().values()) {
            if (reg.getTipoRegistro().equals(tipo)) {
                cantidadRegistrados++;
            }
        }
        if (cantidadRegistrados >= tipo.getCupo()) {
            JOptionPane.showMessageDialog(this, "No hay cupo disponible para este tipo de registro.");
            return;
        }
        for (Registro reg : asistente.getRegistros().values()) {
            if (reg.getEdicion().equals(edicion)) {
                JOptionPane.showMessageDialog(this, "El usuario ya está registrado a esta edición.");
                return;
            }
        }
        try {
            // Obtener los objetos necesarios
            LocalDate fechaRegistro = LocalDate.now();
            float costo = tipo.getCosto();
            LocalDate fechaInicio = edicion.getFechaInicio();
            controladorEvento.altaRegistroEdicionEvento(usuario, edicion, tipo, fechaRegistro, costo, fechaInicio);
            JOptionPane.showMessageDialog(this, "Registro realizado correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar: " + ex.getMessage());
        }
    }
}