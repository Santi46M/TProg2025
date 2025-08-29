package o;

import javax.swing.*;
import java.awt.*;

public class ConsultaTipoRegistroFrame extends JInternalFrame {
    public ConsultaTipoRegistroFrame(String[] eventos, String[][] edicionesPorEvento, String[][] tiposPorEdicion, String[][] datosTipoRegistro) {
        super("Consulta de Tipo de Registro", true, true, true, true);
        setBounds(150, 150, 500, 350);
        setVisible(true);
        setLayout(new BorderLayout());

        // Panel selección evento y edición
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
        add(panelSeleccion, BorderLayout.NORTH);

        // Panel datos tipo de registro
        JTextArea txtDatos = new JTextArea(8, 40);
        txtDatos.setEditable(false);
        add(new JScrollPane(txtDatos), BorderLayout.CENTER);

        // Actualizar ediciones al seleccionar evento
        comboEventos.addActionListener(e -> {
            int idx = comboEventos.getSelectedIndex();
            comboEdiciones.removeAllItems();
            comboTipos.removeAllItems();
            txtDatos.setText("");
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
            txtDatos.setText("");
            if (idxEvento < 0 || idxEdicion < 0) return;
            for (String tipo : tiposPorEdicion[idxEdicion]) {
                comboTipos.addItem(tipo);
            }
        });
        // Mostrar datos al seleccionar tipo
        comboTipos.addActionListener(e -> {
            int idxEvento = comboEventos.getSelectedIndex();
            int idxEdicion = comboEdiciones.getSelectedIndex();
            int idxTipo = comboTipos.getSelectedIndex();
            txtDatos.setText("");
            if (idxEvento < 0 || idxEdicion < 0 || idxTipo < 0) return;
            txtDatos.setText(datosTipoRegistro[idxEdicion][idxTipo]);
        });
        // Inicializar combos
        if (eventos.length > 0) {
            comboEventos.setSelectedIndex(0);
        }
    }
}
