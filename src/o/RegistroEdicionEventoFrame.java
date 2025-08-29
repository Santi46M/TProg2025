package o;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegistroEdicionEventoFrame extends JInternalFrame {
    public RegistroEdicionEventoFrame(String[] eventos, String[][] edicionesPorEvento, String[][] tiposPorEdicion, String[] asistentes, int[][] cuposPorTipo, boolean[][] yaRegistrado) {
        super("Registro a Edición de Evento", true, true, true, true);
        setBounds(180, 180, 600, 350);
        setVisible(true);
        setLayout(new BorderLayout());

        JPanel panelSeleccion = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblEvento = new JLabel("Evento:");
        JComboBox<String> comboEventos = new JComboBox<>(eventos);
        panelSeleccion.add(lblEvento);
        panelSeleccion.add(comboEventos);
        JLabel lblEdicion = new JLabel("Edición:");
        JComboBox<String> comboEdiciones = new JComboBox<>();
        panelSeleccion.add(lblEdicion);
        panelSeleccion.add(comboEdiciones);
        JLabel lblTipo = new JLabel("Tipo de Registro:");
        JComboBox<String> comboTipos = new JComboBox<>();
        panelSeleccion.add(lblTipo);
        panelSeleccion.add(comboTipos);
        JLabel lblAsistente = new JLabel("Asistente:");
        JComboBox<String> comboAsistentes = new JComboBox<>(asistentes);
        panelSeleccion.add(lblAsistente);
        panelSeleccion.add(comboAsistentes);
        add(panelSeleccion, BorderLayout.NORTH);

        JTextArea txtInfo = new JTextArea(5, 40);
        txtInfo.setEditable(false);
        add(new JScrollPane(txtInfo), BorderLayout.CENTER);

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnCancelar = new JButton("Cancelar");
        JPanel panelBotones = new JPanel();
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);

        // Actualizar ediciones al seleccionar evento
        comboEventos.addActionListener(e -> {
            int idx = comboEventos.getSelectedIndex();
            comboEdiciones.removeAllItems();
            comboTipos.removeAllItems();
            txtInfo.setText("");
            if (idx < 0) return;
            for (String ed : edicionesPorEvento[idx]) {
                comboEdiciones.addItem(ed);
            }
        });
        // Actualizar tipos al seleccionar edición
        comboEdiciones.addActionListener(e -> {
            int idxEvento = comboEventos.getSelectedIndex();
            int idxEdicion = comboEdiciones.getSelectedIndex();
            comboTipos.removeAllItems();
            txtInfo.setText("");
            if (idxEvento < 0 || idxEdicion < 0) return;
            for (String tipo : tiposPorEdicion[idxEdicion]) {
                comboTipos.addItem(tipo);
            }
        });
        // Mostrar info al seleccionar tipo y asistente
        comboTipos.addActionListener(e -> mostrarInfo(comboEventos, comboEdiciones, comboTipos, comboAsistentes, cuposPorTipo, yaRegistrado, txtInfo));
        comboAsistentes.addActionListener(e -> mostrarInfo(comboEventos, comboEdiciones, comboTipos, comboAsistentes, cuposPorTipo, yaRegistrado, txtInfo));

        btnRegistrar.addActionListener(e -> {
            int idxEvento = comboEventos.getSelectedIndex();
            int idxEdicion = comboEdiciones.getSelectedIndex();
            int idxTipo = comboTipos.getSelectedIndex();
            int idxAsistente = comboAsistentes.getSelectedIndex();
            if (idxEvento < 0 || idxEdicion < 0 || idxTipo < 0 || idxAsistente < 0) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar evento, edición, tipo y asistente.");
                return;
            }
            if (cuposPorTipo[idxEdicion][idxTipo] <= 0) {
                JOptionPane.showMessageDialog(this, "Ya se alcanzó el cupo para este tipo de registro.");
                return;
            }
            if (yaRegistrado[idxEdicion][idxAsistente]) {
                JOptionPane.showMessageDialog(this, "El asistente ya está registrado a esta edición. Puede editar el registro o cancelar.");
                return;
            }
            // Simular alta de registro
            String fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String costo = "1000"; // Simulado, podrías obtenerlo de otro array
            JOptionPane.showMessageDialog(this, "Registro exitoso:\nAsistente: " + asistentes[idxAsistente] +
                    "\nEvento: " + eventos[idxEvento] +
                    "\nEdición: " + edicionesPorEvento[idxEvento][idxEdicion] +
                    "\nTipo: " + tiposPorEdicion[idxEdicion][idxTipo] +
                    "\nFecha: " + fecha +
                    "\nCosto: " + costo);
            // Actualizar cupo y estado de registro (simulado)
            cuposPorTipo[idxEdicion][idxTipo]--;
            yaRegistrado[idxEdicion][idxAsistente] = true;
            mostrarInfo(comboEventos, comboEdiciones, comboTipos, comboAsistentes, cuposPorTipo, yaRegistrado, txtInfo);
        });

        btnCancelar.addActionListener(e -> this.dispose());

        // Inicializar combos
        if (eventos.length > 0) {
            comboEventos.setSelectedIndex(0);
        }
    }

    private void mostrarInfo(JComboBox<String> comboEventos, JComboBox<String> comboEdiciones, JComboBox<String> comboTipos, JComboBox<String> comboAsistentes, int[][] cuposPorTipo, boolean[][] yaRegistrado, JTextArea txtInfo) {
        int idxEdicion = comboEdiciones.getSelectedIndex();
        int idxTipo = comboTipos.getSelectedIndex();
        int idxAsistente = comboAsistentes.getSelectedIndex();
        txtInfo.setText("");
        if (idxEdicion < 0 || idxTipo < 0 || idxAsistente < 0) return;
        int cupo = cuposPorTipo[idxEdicion][idxTipo];
        boolean registrado = yaRegistrado[idxEdicion][idxAsistente];
        txtInfo.setText("Cupo disponible: " + cupo + (registrado ? "\nEl asistente ya está registrado a esta edición." : ""));
    }
}
