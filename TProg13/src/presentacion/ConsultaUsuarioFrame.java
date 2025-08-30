package presentacion;

import javax.swing.*;
import java.awt.*;

public class ConsultaUsuarioFrame extends JInternalFrame {
    public ConsultaUsuarioFrame(String[] usuarios, String[][] datosUsuarios, String[][] edicionesOrganizador, String[][] registrosAsistente) {
        super("Consulta de Perfil de Usuario", true, true, true, true);
        setBounds(60, 60, 600, 400);
        setVisible(true);
        setLayout(new BorderLayout());

        JPanel panelSeleccion = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblUsuario = new JLabel("Usuario:");
        JComboBox<String> comboUsuarios = new JComboBox<>(usuarios);
        panelSeleccion.add(lblUsuario);
        panelSeleccion.add(comboUsuarios);
        add(panelSeleccion, BorderLayout.NORTH);

        JPanel panelDatos = new JPanel(new GridLayout(0, 1));
        JTextArea txtDatos = new JTextArea(6, 40);
        txtDatos.setEditable(false);
        panelDatos.add(new JScrollPane(txtDatos));

        JLabel lblExtra = new JLabel();
        panelDatos.add(lblExtra);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> listExtra = new JList<>(listModel);
        JScrollPane scrollExtra = new JScrollPane(listExtra);
        panelDatos.add(scrollExtra);
        add(panelDatos, BorderLayout.CENTER);

        JButton btnVerDetalle = new JButton("Ver Detalle");
        btnVerDetalle.setEnabled(false);
        panelDatos.add(btnVerDetalle);

        comboUsuarios.addActionListener(e -> {
            int idx = comboUsuarios.getSelectedIndex();
            if (idx < 0) return;
            txtDatos.setText(datosUsuarios[idx][0]);
            String tipo = datosUsuarios[idx][1];
            listModel.clear();
            if (tipo.equals("Organizador")) {
                lblExtra.setText("Ediciones asociadas:");
                for (String ed : edicionesOrganizador[idx]) {
                    listModel.addElement(ed);
                }
            } else if (tipo.equals("Asistente")) {
                lblExtra.setText("Registros a ediciones:");
                for (String reg : registrosAsistente[idx]) {
                    listModel.addElement(reg);
                }
            } else {
                lblExtra.setText("");
            }
            btnVerDetalle.setEnabled(listModel.size() > 0);
        });

        btnVerDetalle.addActionListener(e -> {
            int idxUsuario = comboUsuarios.getSelectedIndex();
            int idxSel = listExtra.getSelectedIndex();
            if (idxUsuario < 0 || idxSel < 0) return;
            String tipo = datosUsuarios[idxUsuario][1];
            if (tipo.equals("Organizador")) {
                JOptionPane.showMessageDialog(this, "Detalle de Edición: " + edicionesOrganizador[idxUsuario][idxSel]);
            } else if (tipo.equals("Asistente")) {
                JOptionPane.showMessageDialog(this, "Detalle de Registro: " + registrosAsistente[idxUsuario][idxSel]);
            }
        });

        // Inicializar con el primer usuario
        if (usuarios.length > 0) {
            comboUsuarios.setSelectedIndex(0);
        }
    }
}
