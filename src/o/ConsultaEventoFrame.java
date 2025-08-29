package o;

import javax.swing.*;
import java.awt.*;

public class ConsultaEventoFrame extends JInternalFrame {
    public ConsultaEventoFrame(String[] eventos, String[][] datosEventos, String[][] categoriasEventos, String[][] edicionesEventos) {
        super("Consulta de Evento", true, true, true, true);
        setBounds(100, 100, 600, 400);
        setVisible(true);
        setLayout(new BorderLayout());

        JPanel panelSeleccion = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblEvento = new JLabel("Evento:");
        JComboBox<String> comboEventos = new JComboBox<>(eventos);
        panelSeleccion.add(lblEvento);
        panelSeleccion.add(comboEventos);
        add(panelSeleccion, BorderLayout.NORTH);

        JPanel panelDatos = new JPanel(new GridLayout(0, 1));
        JTextArea txtDatos = new JTextArea(6, 40);
        txtDatos.setEditable(false);
        panelDatos.add(new JScrollPane(txtDatos));

        JLabel lblCategorias = new JLabel();
        panelDatos.add(lblCategorias);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> listEdiciones = new JList<>(listModel);
        JScrollPane scrollEdiciones = new JScrollPane(listEdiciones);
        panelDatos.add(scrollEdiciones);
        add(panelDatos, BorderLayout.CENTER);

        JButton btnVerEdicion = new JButton("Ver Detalle Edición");
        btnVerEdicion.setEnabled(false);
        panelDatos.add(btnVerEdicion);

        comboEventos.addActionListener(e -> {
            int idx = comboEventos.getSelectedIndex();
            if (idx < 0) return;
            txtDatos.setText(datosEventos[idx][0]);
            StringBuilder cats = new StringBuilder("Categorías: ");
            for (String cat : categoriasEventos[idx]) {
                cats.append(cat).append(", ");
            }
            if (cats.length() > 12) cats.setLength(cats.length() - 2); // quitar última coma
            lblCategorias.setText(cats.toString());
            listModel.clear();
            for (String ed : edicionesEventos[idx]) {
                listModel.addElement(ed);
            }
            btnVerEdicion.setEnabled(listModel.size() > 0);
        });

        btnVerEdicion.addActionListener(e -> {
            int idxEvento = comboEventos.getSelectedIndex();
            int idxEd = listEdiciones.getSelectedIndex();
            if (idxEvento < 0 || idxEd < 0) return;
            JOptionPane.showMessageDialog(this, "Detalle de Edición: " + edicionesEventos[idxEvento][idxEd]);
        });

        // Inicializar con el primer evento
        if (eventos.length > 0) {
            comboEventos.setSelectedIndex(0);
        }
    }
}
