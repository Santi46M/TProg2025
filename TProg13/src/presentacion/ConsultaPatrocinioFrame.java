package presentacion;

import javax.swing.*;
import java.awt.*;

public class ConsultaPatrocinioFrame extends JInternalFrame {
    public ConsultaPatrocinioFrame(String[] eventos, String[][] edicionesPorEvento, String[][] patrociniosPorEdicion, String[][] datosPatrocinio) {
        super("Consulta de Patrocinio", true, true, true, true);
        setBounds(220, 220, 500, 320);
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
        JLabel lblPatrocinio = new JLabel("Patrocinio:");
        JComboBox<String> comboPatrocinios = new JComboBox<>();
        panelSeleccion.add(lblPatrocinio);
        panelSeleccion.add(comboPatrocinios);
        add(panelSeleccion, BorderLayout.NORTH);

        JTextArea txtDatos = new JTextArea(8, 40);
        txtDatos.setEditable(false);
        add(new JScrollPane(txtDatos), BorderLayout.CENTER);

        // Actualizar ediciones al seleccionar evento
        comboEventos.addActionListener(e -> {
            int idx = comboEventos.getSelectedIndex();
            comboEdiciones.removeAllItems();
            comboPatrocinios.removeAllItems();
            txtDatos.setText("");
            if (idx < 0) return;
            for (String ed : edicionesPorEvento[idx]) {
                comboEdiciones.addItem(ed);
            }
        });
        // Actualizar patrocinios al seleccionar edición
        comboEdiciones.addActionListener(e -> {
            int idxEvento = comboEventos.getSelectedIndex();
            int idxEdicion = comboEdiciones.getSelectedIndex();
            comboPatrocinios.removeAllItems();
            txtDatos.setText("");
            if (idxEvento < 0 || idxEdicion < 0) return;
            for (String pat : patrociniosPorEdicion[idxEdicion]) {
                comboPatrocinios.addItem(pat);
            }
        });
        // Mostrar datos al seleccionar patrocinio
        comboPatrocinios.addActionListener(e -> {
            int idxEvento = comboEventos.getSelectedIndex();
            int idxEdicion = comboEdiciones.getSelectedIndex();
            int idxPatrocinio = comboPatrocinios.getSelectedIndex();
            txtDatos.setText("");
            if (idxEvento < 0 || idxEdicion < 0 || idxPatrocinio < 0) return;
            txtDatos.setText(datosPatrocinio[idxEdicion][idxPatrocinio]);
        });
        // Inicializar combos
        if (eventos.length > 0) {
            comboEventos.setSelectedIndex(0);
        }
    }
}
