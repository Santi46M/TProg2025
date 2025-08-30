package presentacion;

import javax.swing.*;
import java.awt.*;

public class ConsultaRegistroFrame extends JInternalFrame {
    public ConsultaRegistroFrame(String[] usuarios, String[][] registrosPorUsuario, String[][] datosRegistro) {
        super("Consulta de Registro", true, true, true, true);
        setBounds(200, 200, 500, 300);
        setVisible(true);
        setLayout(new BorderLayout());

        JPanel panelSeleccion = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblUsuario = new JLabel("Usuario:");
        JComboBox<String> comboUsuarios = new JComboBox<>(usuarios);
        panelSeleccion.add(lblUsuario);
        panelSeleccion.add(comboUsuarios);
        JLabel lblRegistro = new JLabel("Registro:");
        JComboBox<String> comboRegistros = new JComboBox<>();
        panelSeleccion.add(lblRegistro);
        panelSeleccion.add(comboRegistros);
        add(panelSeleccion, BorderLayout.NORTH);

        JTextArea txtDatos = new JTextArea(8, 40);
        txtDatos.setEditable(false);
        add(new JScrollPane(txtDatos), BorderLayout.CENTER);

        // Actualizar registros al seleccionar usuario
        comboUsuarios.addActionListener(e -> {
            int idx = comboUsuarios.getSelectedIndex();
            comboRegistros.removeAllItems();
            txtDatos.setText("");
            if (idx < 0) return;
            for (String reg : registrosPorUsuario[idx]) {
                comboRegistros.addItem(reg);
            }
        });
        // Mostrar datos al seleccionar registro
        comboRegistros.addActionListener(e -> {
            int idxUsuario = comboUsuarios.getSelectedIndex();
            int idxRegistro = comboRegistros.getSelectedIndex();
            txtDatos.setText("");
            if (idxUsuario < 0 || idxRegistro < 0) return;
            txtDatos.setText(datosRegistro[idxUsuario][idxRegistro]);
        });
        // Inicializar combos
        if (usuarios.length > 0) {
            comboUsuarios.setSelectedIndex(0);
        }
    }
}
