package o;

import javax.swing.*;
import java.awt.*;

public class AltaTipoRegistroFrame extends JInternalFrame {
    public AltaTipoRegistroFrame(String[] eventos, String[][] edicionesPorEvento, String[][] tiposRegistroPorEdicion) {
        super("Alta de Tipo de Registro", true, true, true, true);
        setBounds(120, 120, 500, 350);
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
        add(panelSeleccion, BorderLayout.NORTH);

        // Panel datos tipo de registro
        JPanel panelDatos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField(15);
        panelDatos.add(lblNombre, gbc);
        gbc.gridx = 1;
        panelDatos.add(txtNombre, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel lblDescripcion = new JLabel("Descripción:");
        JTextField txtDescripcion = new JTextField(15);
        panelDatos.add(lblDescripcion, gbc);
        gbc.gridx = 1;
        panelDatos.add(txtDescripcion, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel lblCosto = new JLabel("Costo:");
        JTextField txtCosto = new JTextField(10);
        panelDatos.add(lblCosto, gbc);
        gbc.gridx = 1;
        panelDatos.add(txtCosto, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel lblCupo = new JLabel("Cupo:");
        JTextField txtCupo = new JTextField(10);
        panelDatos.add(lblCupo, gbc);
        gbc.gridx = 1;
        panelDatos.add(txtCupo, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        add(panelDatos, BorderLayout.CENTER);

        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");
        JPanel panelBotones = new JPanel();
        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);

        // Actualizar ediciones al seleccionar evento
        comboEventos.addActionListener(e -> {
            int idx = comboEventos.getSelectedIndex();
            comboEdiciones.removeAllItems();
            if (idx < 0) return;
            for (String ed : edicionesPorEvento[idx]) {
                comboEdiciones.addItem(ed);
            }
        });
        // Inicializar ediciones
        if (eventos.length > 0) {
            comboEventos.setSelectedIndex(0);
        }

        btnAceptar.addActionListener(e -> {
            int idxEvento = comboEventos.getSelectedIndex();
            int idxEdicion = comboEdiciones.getSelectedIndex();
            if (idxEvento < 0 || idxEdicion < 0) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un evento y una edición.");
                return;
            }
            String nombre = txtNombre.getText().trim();
            String descripcion = txtDescripcion.getText().trim();
            String costo = txtCosto.getText().trim();
            String cupo = txtCupo.getText().trim();
            if (nombre.isEmpty() || descripcion.isEmpty() || costo.isEmpty() || cupo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
                return;
            }
            // Validar si ya existe el nombre del tipo de registro para la edición
            for (String tipo : tiposRegistroPorEdicion[idxEdicion]) {
                if (tipo.equalsIgnoreCase(nombre)) {
                    JOptionPane.showMessageDialog(this, "Ya existe un tipo de registro con ese nombre para esta edición. Ingrese otro nombre o cancele.");
                    return;
                }
            }
            // Aquí se daría de alta el tipo de registro
            JOptionPane.showMessageDialog(this, "Tipo de registro creado con éxito para la edición seleccionada.");
            // Limpiar campos
            txtNombre.setText("");
            txtDescripcion.setText("");
            txtCosto.setText("");
            txtCupo.setText("");
        });

        btnCancelar.addActionListener(e -> this.dispose());
    }
}
