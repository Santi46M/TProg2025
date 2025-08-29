package o;

import javax.swing.*;
import java.awt.*;

public class AltaUsuarioFrame extends JInternalFrame {
    public AltaUsuarioFrame() {
        super("Alta de Usuario", true, true, true, true);
        setBounds(new Rectangle(50, 50, 450, 350));
        setVisible(true);

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[3];
        gridBagLayout.rowHeights = new int[3];
        gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        getContentPane().setLayout(gridBagLayout);

        JLabel lblNick = new JLabel("Nick:");
        GridBagConstraints gbc_lblNick = new GridBagConstraints();
        gbc_lblNick.insets = new Insets(0, 0, 5, 5);
        gbc_lblNick.anchor = GridBagConstraints.WEST;
        gbc_lblNick.gridx = 0;
        gbc_lblNick.gridy = 0;
        getContentPane().add(lblNick, gbc_lblNick);

        JTextField txtNick = new JTextField();
        GridBagConstraints gbc_txtNick = new GridBagConstraints();
        gbc_txtNick.insets = new Insets(0, 0, 5, 0);
        gbc_txtNick.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtNick.gridx = 1;
        gbc_txtNick.gridy = 0;
        getContentPane().add(txtNick, gbc_txtNick);
        txtNick.setColumns(10);

        JLabel lblNombre = new JLabel("Nombre:");
        GridBagConstraints gbc_lblNombre = new GridBagConstraints();
        gbc_lblNombre.anchor = GridBagConstraints.WEST;
        gbc_lblNombre.insets = new Insets(0, 0, 5, 5);
        gbc_lblNombre.gridx = 0;
        gbc_lblNombre.gridy = 1;
        getContentPane().add(lblNombre, gbc_lblNombre);

        JTextField txtNombre = new JTextField();
        GridBagConstraints gbc_txtNombre = new GridBagConstraints();
        gbc_txtNombre.insets = new Insets(0, 0, 5, 0);
        gbc_txtNombre.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtNombre.gridx = 1;
        gbc_txtNombre.gridy = 1;
        getContentPane().add(txtNombre, gbc_txtNombre);
        txtNombre.setColumns(10);

        JLabel lblCorreo = new JLabel("Correo:");
        GridBagConstraints gbc_lblCorreo = new GridBagConstraints();
        gbc_lblCorreo.insets = new Insets(0, 0, 5, 5);
        gbc_lblCorreo.anchor = GridBagConstraints.WEST;
        gbc_lblCorreo.gridx = 0;
        gbc_lblCorreo.gridy = 2;
        getContentPane().add(lblCorreo, gbc_lblCorreo);

        JTextField txtCorreo = new JTextField();
        GridBagConstraints gbc_txtCorreo = new GridBagConstraints();
        gbc_txtCorreo.insets = new Insets(0, 0, 5, 0);
        gbc_txtCorreo.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtCorreo.gridx = 1;
        gbc_txtCorreo.gridy = 2;
        getContentPane().add(txtCorreo, gbc_txtCorreo);
        txtCorreo.setColumns(10);

        JLabel lblTipo = new JLabel("Tipo de Usuario:");
        GridBagConstraints gbc_lblTipo = new GridBagConstraints();
        gbc_lblTipo.insets = new Insets(0, 0, 0, 5);
        gbc_lblTipo.anchor = GridBagConstraints.WEST;
        gbc_lblTipo.gridx = 0;
        gbc_lblTipo.gridy = 3;
        getContentPane().add(lblTipo, gbc_lblTipo);

        JPanel panelRadio = new JPanel(new GridLayout(1, 2, 5, 0));
        JRadioButton rbtnOrganizador = new JRadioButton("Organizador");
        JRadioButton rbtnAsistente = new JRadioButton("Asistente");
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbtnOrganizador);
        grupo.add(rbtnAsistente);
        panelRadio.add(rbtnOrganizador);
        panelRadio.add(rbtnAsistente);

        GridBagConstraints gbc_panelRadio = new GridBagConstraints();
        gbc_panelRadio.gridx = 1;
        gbc_panelRadio.gridy = 3;
        gbc_panelRadio.fill = GridBagConstraints.HORIZONTAL;
        gbc_panelRadio.insets = new Insets(0, 0, 5, 0);
        getContentPane().add(panelRadio, gbc_panelRadio);

        JLabel lblDescripcion = new JLabel("Descripción:");
        JTextField txtDescripcion = new JTextField();
        JLabel lblSitioWeb = new JLabel("Sitio web (opcional):");
        JTextField txtSitioWeb = new JTextField();

        // Panel para campos extra de organizador
        JPanel panelExtra = new JPanel(new GridBagLayout());
        GridBagConstraints gbc_lblDescripcion = new GridBagConstraints();
        gbc_lblDescripcion.insets = new Insets(0, 0, 5, 5);
        gbc_lblDescripcion.anchor = GridBagConstraints.WEST;
        gbc_lblDescripcion.gridx = 0;
        gbc_lblDescripcion.gridy = 0;
        panelExtra.add(lblDescripcion, gbc_lblDescripcion);

        GridBagConstraints gbc_txtDescripcion = new GridBagConstraints();
        gbc_txtDescripcion.insets = new Insets(0, 0, 5, 0);
        gbc_txtDescripcion.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtDescripcion.gridx = 1;
        gbc_txtDescripcion.gridy = 0;
        gbc_txtDescripcion.weightx = 1.0;
        panelExtra.add(txtDescripcion, gbc_txtDescripcion);
        txtDescripcion.setColumns(10);

        GridBagConstraints gbc_lblSitioWeb = new GridBagConstraints();
        gbc_lblSitioWeb.insets = new Insets(0, 0, 0, 5);
        gbc_lblSitioWeb.anchor = GridBagConstraints.WEST;
        gbc_lblSitioWeb.gridx = 0;
        gbc_lblSitioWeb.gridy = 1;
        panelExtra.add(lblSitioWeb, gbc_lblSitioWeb);

        GridBagConstraints gbc_txtSitioWeb = new GridBagConstraints();
        gbc_txtSitioWeb.insets = new Insets(0, 0, 0, 0);
        gbc_txtSitioWeb.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtSitioWeb.gridx = 1;
        gbc_txtSitioWeb.gridy = 1;
        gbc_txtSitioWeb.weightx = 1.0;
        panelExtra.add(txtSitioWeb, gbc_txtSitioWeb);
        txtSitioWeb.setColumns(10);
        panelExtra.setVisible(false);

        GridBagConstraints gbc_panelExtra = new GridBagConstraints();
        gbc_panelExtra.gridx = 0;
        gbc_panelExtra.gridy = 5;
        gbc_panelExtra.gridwidth = 2;
        gbc_panelExtra.fill = GridBagConstraints.HORIZONTAL;
        gbc_panelExtra.insets = new Insets(0, 0, 5, 0);
        getContentPane().add(panelExtra, gbc_panelExtra);

        // Campos extra para asistentes
        JLabel lblApellido = new JLabel("Apellido:");
        JTextField txtApellido = new JTextField();
        JLabel lblFechaNac = new JLabel("Fecha de nacimiento:");
        JFormattedTextField txtFechaNac = new JFormattedTextField(java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT));
        JLabel lblInstitucion = new JLabel("Institución:");
        JComboBox<String> comboInstitucion = new JComboBox<>();
        // Ejemplo de instituciones, reemplazar por carga dinámica si es necesario
        comboInstitucion.addItem("Ninguna");
        comboInstitucion.addItem("Universidad A");
        comboInstitucion.addItem("Instituto B");
        comboInstitucion.addItem("Escuela C");
        // Panel para campos extra de asistente
        JPanel panelAsistente = new JPanel(new GridBagLayout());
        GridBagConstraints gbc_lblApellido = new GridBagConstraints();
        gbc_lblApellido.insets = new Insets(0, 0, 5, 5);
        gbc_lblApellido.anchor = GridBagConstraints.WEST;
        gbc_lblApellido.gridx = 0;
        gbc_lblApellido.gridy = 0;
        panelAsistente.add(lblApellido, gbc_lblApellido);
        GridBagConstraints gbc_txtApellido = new GridBagConstraints();
        gbc_txtApellido.insets = new Insets(0, 0, 5, 0);
        gbc_txtApellido.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtApellido.gridx = 1;
        gbc_txtApellido.gridy = 0;
        gbc_txtApellido.weightx = 1.0;
        panelAsistente.add(txtApellido, gbc_txtApellido);
        txtApellido.setColumns(10);
        GridBagConstraints gbc_lblFechaNac = new GridBagConstraints();
        gbc_lblFechaNac.insets = new Insets(0, 0, 5, 5);
        gbc_lblFechaNac.anchor = GridBagConstraints.WEST;
        gbc_lblFechaNac.gridx = 0;
        gbc_lblFechaNac.gridy = 1;
        panelAsistente.add(lblFechaNac, gbc_lblFechaNac);
        GridBagConstraints gbc_txtFechaNac = new GridBagConstraints();
        gbc_txtFechaNac.insets = new Insets(0, 0, 5, 0);
        gbc_txtFechaNac.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtFechaNac.gridx = 1;
        gbc_txtFechaNac.gridy = 1;
        gbc_txtFechaNac.weightx = 1.0;
        panelAsistente.add(txtFechaNac, gbc_txtFechaNac);
        txtFechaNac.setColumns(10);
        GridBagConstraints gbc_lblInstitucion = new GridBagConstraints();
        gbc_lblInstitucion.insets = new Insets(0, 0, 0, 5);
        gbc_lblInstitucion.anchor = GridBagConstraints.WEST;
        gbc_lblInstitucion.gridx = 0;
        gbc_lblInstitucion.gridy = 2;
        panelAsistente.add(lblInstitucion, gbc_lblInstitucion);
        GridBagConstraints gbc_comboInstitucion = new GridBagConstraints();
        gbc_comboInstitucion.insets = new Insets(0, 0, 0, 0);
        gbc_comboInstitucion.fill = GridBagConstraints.HORIZONTAL;
        gbc_comboInstitucion.gridx = 1;
        gbc_comboInstitucion.gridy = 2;
        gbc_comboInstitucion.weightx = 1.0;
        panelAsistente.add(comboInstitucion, gbc_comboInstitucion);
        panelAsistente.setVisible(false);

        GridBagConstraints gbc_panelAsistente = new GridBagConstraints();
        gbc_panelAsistente.gridx = 0;
        gbc_panelAsistente.gridy = 7;
        gbc_panelAsistente.gridwidth = 2;
        gbc_panelAsistente.fill = GridBagConstraints.HORIZONTAL;
        gbc_panelAsistente.insets = new Insets(0, 0, 5, 0);
        getContentPane().add(panelAsistente, gbc_panelAsistente);

        // Mostrar/ocultar paneles según tipo
        rbtnOrganizador.addActionListener(e -> {
            panelExtra.setVisible(true);
            panelAsistente.setVisible(false);
        });
        rbtnAsistente.addActionListener(e -> {
            panelExtra.setVisible(false);
            panelAsistente.setVisible(true);
        });

        // Botones
        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);
        GridBagConstraints gbc_panelBotones = new GridBagConstraints();
        gbc_panelBotones.gridx = 0;
        gbc_panelBotones.gridy = 8;
        gbc_panelBotones.gridwidth = 3;
        gbc_panelBotones.insets = new Insets(10, 0, 0, 0);
        gbc_panelBotones.anchor = GridBagConstraints.SOUTHEAST;
        gbc_panelBotones.fill = GridBagConstraints.HORIZONTAL;
        getContentPane().add(panelBotones, gbc_panelBotones);

        btnAceptar.addActionListener(ev -> {
            String nick = txtNick.getText().trim();
            String nombre = txtNombre.getText().trim();
            String correo = txtCorreo.getText().trim();
            String tipo = rbtnOrganizador.isSelected() ? "Organizador" : rbtnAsistente.isSelected() ? "Asistente" : null;
            String descripcion = txtDescripcion.getText().trim();
            String sitioWeb = txtSitioWeb.getText().trim();
            String apellido = txtApellido.getText().trim();
            String fechaNac = txtFechaNac.getText().trim();
            String institucion = (String) comboInstitucion.getSelectedItem();

            if (nick.isEmpty() || nombre.isEmpty() || correo.isEmpty() || tipo == null) {
                JOptionPane.showMessageDialog(this, "Todos los campos deben estar completos y debe seleccionar un tipo.");
                return;
            }
            if (tipo.equals("Organizador") && descripcion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "La descripción es obligatoria para organizadores.");
                return;
            }
            if (tipo.equals("Asistente") && (apellido.isEmpty() || fechaNac.isEmpty())) {
                JOptionPane.showMessageDialog(this, "El apellido y la fecha de nacimiento son obligatorios para asistentes.");
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Usuario registrado con éxito:\n");
            sb.append("Nick: ").append(nick).append("\n");
            sb.append("Nombre: ").append(nombre).append("\n");
            sb.append("Correo: ").append(correo).append("\n");
            sb.append("Tipo: ").append(tipo).append("\n");
            if (tipo.equals("Organizador")) {
                sb.append("Descripción: ").append(descripcion).append("\n");
                if (!sitioWeb.isEmpty()) {
                    sb.append("Sitio web: ").append(sitioWeb).append("\n");
                }
            } else if (tipo.equals("Asistente")) {
                sb.append("Apellido: ").append(apellido).append("\n");
                sb.append("Fecha de nacimiento: ").append(fechaNac).append("\n");
                if (institucion != null && !institucion.equals("Ninguna")) {
                    sb.append("Institución: ").append(institucion).append("\n");
                }
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        });

        btnCancelar.addActionListener(ev -> this.dispose());
    }
}