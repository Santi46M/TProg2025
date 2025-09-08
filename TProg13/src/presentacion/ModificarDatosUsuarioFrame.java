package presentacion;

import javax.swing.*;
import java.awt.*;

public class ModificarDatosUsuarioFrame extends JInternalFrame {
    public ModificarDatosUsuarioFrame(String[] usuarios, String[][] datosUsuarios) {
        super("Modificar Datos de Usuario", true, true, true, true);
        setBounds(80, 80, 500, 400);
        setVisible(true);
        setLayout(new BorderLayout());

        JPanel panelSeleccion = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblUsuario = new JLabel("Usuario:");
        JComboBox<String> comboUsuarios = new JComboBox<>(usuarios);
        panelSeleccion.add(lblUsuario);
        panelSeleccion.add(comboUsuarios);
        add(panelSeleccion, BorderLayout.NORTH);

        JPanel panelDatos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Nickname (no editable)
        JLabel lblNick = new JLabel("Nick:");
        JTextField txtNick = new JTextField(15);
        txtNick.setEditable(false);
        panelDatos.add(lblNick, gbc);
        gbc.gridx = 1;
        panelDatos.add(txtNick, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        // Correo (no editable)
        JLabel lblCorreo = new JLabel("Correo:");
        JTextField txtCorreo = new JTextField(15);
        txtCorreo.setEditable(false);
        panelDatos.add(lblCorreo, gbc);
        gbc.gridx = 1;
        panelDatos.add(txtCorreo, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        // Nombre
        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField(15);
        panelDatos.add(lblNombre, gbc);
        gbc.gridx = 1;
        panelDatos.add(txtNombre, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        // Apellido
        JLabel lblApellido = new JLabel("Apellido:");
        JTextField txtApellido = new JTextField(15);
        panelDatos.add(lblApellido, gbc);
        gbc.gridx = 1;
        panelDatos.add(txtApellido, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        // Fecha de nacimiento
        JLabel lblFechaNac = new JLabel("Fecha de nacimiento (YYYY-MM-DD):");
        JTextField txtFechaNac = new JTextField(15);
        panelDatos.add(lblFechaNac, gbc);
        gbc.gridx = 1;
        panelDatos.add(txtFechaNac, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        // Descripción
        JLabel lblDescripcion = new JLabel("Descripción:");
        JTextField txtDescripcion = new JTextField(15);
        panelDatos.add(lblDescripcion, gbc);
        gbc.gridx = 1;
        panelDatos.add(txtDescripcion, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        // Sitio web
        JLabel lblSitioWeb = new JLabel("Sitio web:");
        JTextField txtSitioWeb = new JTextField(15);
        panelDatos.add(lblSitioWeb, gbc);
        gbc.gridx = 1;
        panelDatos.add(txtSitioWeb, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        add(panelDatos, BorderLayout.CENTER);

        JButton btnGuardar = new JButton("Guardar Cambios");
        JPanel panelBoton = new JPanel();
        panelBoton.add(btnGuardar);
        add(panelBoton, BorderLayout.SOUTH);

        // Cargar datos del usuario seleccionado
        comboUsuarios.addActionListener(e -> {
            int idx = comboUsuarios.getSelectedIndex();
            if (idx < 0) return;
            txtNick.setText(datosUsuarios[idx][0]);
            txtCorreo.setText(datosUsuarios[idx][1]);
            txtNombre.setText(datosUsuarios[idx][2]);
            txtApellido.setText(datosUsuarios[idx][3]);
            txtFechaNac.setText(datosUsuarios[idx][4]);
            txtDescripcion.setText(datosUsuarios[idx][5]);
            txtSitioWeb.setText(datosUsuarios[idx][6]);
        });

        // Inicializar con el primer usuario
        if (usuarios.length > 0) {
            comboUsuarios.setSelectedIndex(0);
        }

        btnGuardar.addActionListener(e -> {
            int idx = comboUsuarios.getSelectedIndex();
            if (idx < 0) return;
            // Aquí deberías actualizar los datos en tu sistema
            datosUsuarios[idx][2] = txtNombre.getText().trim();
            datosUsuarios[idx][3] = txtApellido.getText().trim();
            datosUsuarios[idx][4] = txtFechaNac.getText().trim();
            datosUsuarios[idx][5] = txtDescripcion.getText().trim();
            datosUsuarios[idx][6] = txtSitioWeb.getText().trim();
            JOptionPane.showMessageDialog(this, "Datos actualizados correctamente para " + txtNick.getText());
        });
    }
}
