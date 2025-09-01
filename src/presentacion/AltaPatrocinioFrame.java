package presentacion;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class AltaPatrocinioFrame extends JInternalFrame {
    public AltaPatrocinioFrame(String[] eventos, String[][] edicionesPorEvento, String[][] tiposPorEdicion, String[] instituciones, Set<String> codigosPatrocinio, Set<String> patrociniosInstitucionEdicion, double[] costosTipoRegistro) {
        super("Alta de Patrocinio", true, true, true, true);
        setBounds(250, 250, 600, 400);
        setVisible(true);
        setLayout(new BorderLayout());

        JPanel panelSeleccion = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Evento
        JLabel lblEvento = new JLabel("Evento:");
        JComboBox<String> comboEventos = new JComboBox<>(eventos);
        panelSeleccion.add(lblEvento, gbc);
        gbc.gridx = 1;
        panelSeleccion.add(comboEventos, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        // Edición
        JLabel lblEdicion = new JLabel("Edición:");
        JComboBox<String> comboEdiciones = new JComboBox<>();
        panelSeleccion.add(lblEdicion, gbc);
        gbc.gridx = 1;
        panelSeleccion.add(comboEdiciones, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        // Tipo de registro
        JLabel lblTipo = new JLabel("Tipo de Registro:");
        JComboBox<String> comboTipos = new JComboBox<>();
        panelSeleccion.add(lblTipo, gbc);
        gbc.gridx = 1;
        panelSeleccion.add(comboTipos, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        // Institución
        JLabel lblInstitucion = new JLabel("Institución:");
        JComboBox<String> comboInstituciones = new JComboBox<>(instituciones);
        panelSeleccion.add(lblInstitucion, gbc);
        gbc.gridx = 1;
        panelSeleccion.add(comboInstituciones, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        // Nivel de patrocinio
        JLabel lblNivel = new JLabel("Nivel de Patrocinio:");
        String[] niveles = {"Platino", "Oro", "Plata", "Bronce"};
        JComboBox<String> comboNivel = new JComboBox<>(niveles);
        panelSeleccion.add(lblNivel, gbc);
        gbc.gridx = 1;
        panelSeleccion.add(comboNivel, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        // Aporte económico
        JLabel lblAporte = new JLabel("Aporte económico:");
        JTextField txtAporte = new JTextField(10);
        panelSeleccion.add(lblAporte, gbc);
        gbc.gridx = 1;
        panelSeleccion.add(txtAporte, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        // Tipo de registro gratuito
        JLabel lblTipoGratuito = new JLabel("Tipo de Registro Gratuito:");
        JComboBox<String> comboTipoGratuito = new JComboBox<>();
        panelSeleccion.add(lblTipoGratuito, gbc);
        gbc.gridx = 1;
        panelSeleccion.add(comboTipoGratuito, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        // Cantidad de registros gratuitos
        JLabel lblCantidadGratuitos = new JLabel("Cantidad Registros Gratuitos:");
        JTextField txtCantidadGratuitos = new JTextField(10);
        panelSeleccion.add(lblCantidadGratuitos, gbc);
        gbc.gridx = 1;
        panelSeleccion.add(txtCantidadGratuitos, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        // Código de patrocinio
        JLabel lblCodigo = new JLabel("Código de Patrocinio:");
        JTextField txtCodigo = new JTextField(12);
        panelSeleccion.add(lblCodigo, gbc);
        gbc.gridx = 1;
        panelSeleccion.add(txtCodigo, gbc);

        add(panelSeleccion, BorderLayout.CENTER);

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
            comboTipos.removeAllItems();
            comboTipoGratuito.removeAllItems();
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
            comboTipoGratuito.removeAllItems();
            if (idxEvento < 0 || idxEdicion < 0) return;
            for (String tipo : tiposPorEdicion[idxEdicion]) {
                comboTipos.addItem(tipo);
                comboTipoGratuito.addItem(tipo);
            }
        });
        // Inicializar combos
        if (eventos.length > 0) {
            comboEventos.setSelectedIndex(0);
        }

        btnAceptar.addActionListener(e -> {
            int idxEvento = comboEventos.getSelectedIndex();
            int idxEdicion = comboEdiciones.getSelectedIndex();
            int idxTipo = comboTipos.getSelectedIndex();
            int idxTipoGratuito = comboTipoGratuito.getSelectedIndex();
            int idxInstitucion = comboInstituciones.getSelectedIndex();
            String nivel = (String) comboNivel.getSelectedItem();
            String aporteStr = txtAporte.getText().trim();
            String cantidadGratuitosStr = txtCantidadGratuitos.getText().trim();
            String codigo = txtCodigo.getText().trim();
            if (idxEvento < 0 || idxEdicion < 0 || idxTipo < 0 || idxTipoGratuito < 0 || idxInstitucion < 0 || nivel == null || aporteStr.isEmpty() || cantidadGratuitosStr.isEmpty() || codigo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
                return;
            }
            double aporte;
            int cantidadGratuitos;
            try {
                aporte = Double.parseDouble(aporteStr);
                cantidadGratuitos = Integer.parseInt(cantidadGratuitosStr);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Aporte y cantidad deben ser numéricos.");
                return;
            }
            // Validar código único
            if (codigosPatrocinio.contains(codigo.toLowerCase())) {
                JOptionPane.showMessageDialog(this, "Ya existe un patrocinio con ese código. Ingrese otro código o cancele.");
                return;
            }
            // Validar patrocinio único por institución y edición
            String clavePatrocinio = comboInstituciones.getItemAt(idxInstitucion).toLowerCase() + "-" + comboEdiciones.getItemAt(idxEdicion).toLowerCase();
            if (patrociniosInstitucionEdicion.contains(clavePatrocinio)) {
                JOptionPane.showMessageDialog(this, "Ya existe un patrocinio de esta institución para la edición seleccionada.");
                return;
            }
            // Validar 20% del aporte
            double costoTipo = costosTipoRegistro[idxTipoGratuito];
            double totalGratis = costoTipo * cantidadGratuitos;
            if (totalGratis > aporte * 0.2) {
                JOptionPane.showMessageDialog(this, "El costo de los registros gratuitos supera el 20% del aporte económico. Modifique los valores o cancele.");
                return;
            }
            // Simular alta
            codigosPatrocinio.add(codigo.toLowerCase());
            patrociniosInstitucionEdicion.add(clavePatrocinio);
            String fechaAlta = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            JOptionPane.showMessageDialog(this, "Patrocinio creado con éxito:\nEvento: " + eventos[idxEvento] +
                    "\nEdición: " + comboEdiciones.getItemAt(idxEdicion) +
                    "\nInstitución: " + comboInstituciones.getItemAt(idxInstitucion) +
                    "\nNivel: " + nivel +
                    "\nAporte: " + aporte +
                    "\nTipo gratuito: " + comboTipoGratuito.getItemAt(idxTipoGratuito) +
                    "\nCantidad gratuitos: " + cantidadGratuitos +
                    "\nCódigo: " + codigo +
                    "\nFecha de alta: " + fechaAlta);
            txtAporte.setText("");
            txtCantidadGratuitos.setText("");
            txtCodigo.setText("");
        });

        btnCancelar.addActionListener(e -> this.dispose());
    }
}
