package o;

import javax.swing.*;
import java.awt.*;

public class ConsultaEdicionEventoFrame extends JInternalFrame {
    public ConsultaEdicionEventoFrame(String[] eventos, String[][] edicionesPorEvento, String[][] detallesEdicion, String[][] tiposRegistro, String[][] registros, String[][] patrocinios) {
        super("Consulta de Edición de Evento", true, true, true, true);
        setBounds(100, 100, 700, 450);
        setVisible(true);
        setLayout(new BorderLayout());

        JPanel panelSeleccion = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblEvento = new JLabel("Evento:");
        JComboBox<String> comboEventos = new JComboBox<>(eventos);
        panelSeleccion.add(lblEvento);
        panelSeleccion.add(comboEventos);
        add(panelSeleccion, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new BorderLayout());
        JLabel lblEdiciones = new JLabel("Ediciones:");
        panelCentro.add(lblEdiciones, BorderLayout.NORTH);
        DefaultListModel<String> listModelEdiciones = new DefaultListModel<>();
        JList<String> listEdiciones = new JList<>(listModelEdiciones);
        JScrollPane scrollEdiciones = new JScrollPane(listEdiciones);
        panelCentro.add(scrollEdiciones, BorderLayout.CENTER);
        add(panelCentro, BorderLayout.WEST);

        JPanel panelDetalles = new JPanel(new GridLayout(0, 1));
        JTextArea txtDetalles = new JTextArea(8, 30);
        txtDetalles.setEditable(false);
        panelDetalles.add(new JScrollPane(txtDetalles));

        JLabel lblTipos = new JLabel("Tipos de Registro:");
        panelDetalles.add(lblTipos);
        DefaultListModel<String> listModelTipos = new DefaultListModel<>();
        JList<String> listTipos = new JList<>(listModelTipos);
        JScrollPane scrollTipos = new JScrollPane(listTipos);
        panelDetalles.add(scrollTipos);

        JLabel lblRegistros = new JLabel("Registros:");
        panelDetalles.add(lblRegistros);
        DefaultListModel<String> listModelRegistros = new DefaultListModel<>();
        JList<String> listRegistros = new JList<>(listModelRegistros);
        JScrollPane scrollRegistros = new JScrollPane(listRegistros);
        panelDetalles.add(scrollRegistros);

        JLabel lblPatrocinios = new JLabel("Patrocinios:");
        panelDetalles.add(lblPatrocinios);
        DefaultListModel<String> listModelPatrocinios = new DefaultListModel<>();
        JList<String> listPatrocinios = new JList<>(listModelPatrocinios);
        JScrollPane scrollPatrocinios = new JScrollPane(listPatrocinios);
        panelDetalles.add(scrollPatrocinios);

        add(panelDetalles, BorderLayout.CENTER);

        JButton btnDetalleTipo = new JButton("Ver Detalle Tipo de Registro");
        JButton btnDetallePatrocinio = new JButton("Ver Detalle Patrocinio");
        JPanel panelBotones = new JPanel();
        panelBotones.add(btnDetalleTipo);
        panelBotones.add(btnDetallePatrocinio);
        add(panelBotones, BorderLayout.SOUTH);
        btnDetalleTipo.setEnabled(false);
        btnDetallePatrocinio.setEnabled(false);

        comboEventos.addActionListener(e -> {
            int idx = comboEventos.getSelectedIndex();
            listModelEdiciones.clear();
            if (idx < 0) return;
            for (String ed : edicionesPorEvento[idx]) {
                listModelEdiciones.addElement(ed);
            }
            txtDetalles.setText("");
            listModelTipos.clear();
            listModelRegistros.clear();
            listModelPatrocinios.clear();
        });

        listEdiciones.addListSelectionListener(e -> {
            int idxEvento = comboEventos.getSelectedIndex();
            int idxEd = listEdiciones.getSelectedIndex();
            if (idxEvento < 0 || idxEd < 0) return;
            txtDetalles.setText(detallesEdicion[idxEvento][idxEd]);
            listModelTipos.clear();
            for (String tipo : tiposRegistro[idxEvento]) {
                listModelTipos.addElement(tipo);
            }
            listModelRegistros.clear();
            for (String reg : registros[idxEvento]) {
                listModelRegistros.addElement(reg);
            }
            listModelPatrocinios.clear();
            for (String pat : patrocinios[idxEvento]) {
                listModelPatrocinios.addElement(pat);
            }
            btnDetalleTipo.setEnabled(listModelTipos.size() > 0);
            btnDetallePatrocinio.setEnabled(listModelPatrocinios.size() > 0);
        });

        btnDetalleTipo.addActionListener(e -> {
            int idxEvento = comboEventos.getSelectedIndex();
            int idxTipo = listTipos.getSelectedIndex();
            if (idxEvento < 0 || idxTipo < 0) return;
            JOptionPane.showMessageDialog(this, "Detalle Tipo de Registro: " + tiposRegistro[idxEvento][idxTipo]);
        });

        btnDetallePatrocinio.addActionListener(e -> {
            int idxEvento = comboEventos.getSelectedIndex();
            int idxPat = listPatrocinios.getSelectedIndex();
            if (idxEvento < 0 || idxPat < 0) return;
            JOptionPane.showMessageDialog(this, "Detalle Patrocinio: " + patrocinios[idxEvento][idxPat]);
        });

        // Inicializar con el primer evento
        if (eventos.length > 0) {
            comboEventos.setSelectedIndex(0);
        }
    }
}