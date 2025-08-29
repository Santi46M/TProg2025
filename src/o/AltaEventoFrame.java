package o;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AltaEventoFrame extends JInternalFrame {
    public AltaEventoFrame(JDesktopPane desktopPane) {
        super("Alta de Evento", true, true, true, true);
        setBounds(new Rectangle(50, 50, 450, 300));
        setVisible(true);

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[3];
        gridBagLayout.rowHeights = new int[6];
        gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        getContentPane().setLayout(gridBagLayout);

        // Nombre
        JLabel lblNombre = new JLabel("Nombre:");
        GridBagConstraints gbc_lblNombre = new GridBagConstraints();
        gbc_lblNombre.insets = new Insets(0, 0, 5, 5);
        gbc_lblNombre.anchor = GridBagConstraints.WEST;
        gbc_lblNombre.gridx = 0;
        gbc_lblNombre.gridy = 0;
        getContentPane().add(lblNombre, gbc_lblNombre);

        JTextField txtNombre = new JTextField();
        GridBagConstraints gbc_txtNombre = new GridBagConstraints();
        gbc_txtNombre.insets = new Insets(0, 0, 5, 0);
        gbc_txtNombre.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtNombre.gridx = 1;
        gbc_txtNombre.gridy = 0;
        getContentPane().add(txtNombre, gbc_txtNombre);
        txtNombre.setColumns(10);

        // Descripcion
        JLabel lblDescripcion = new JLabel("Descripcion:");
        GridBagConstraints gbc_lblDescripcion = new GridBagConstraints();
        gbc_lblDescripcion.insets = new Insets(0, 0, 5, 5);
        gbc_lblDescripcion.anchor = GridBagConstraints.WEST;
        gbc_lblDescripcion.gridx = 0;
        gbc_lblDescripcion.gridy = 1;
        getContentPane().add(lblDescripcion, gbc_lblDescripcion);

        JTextField txtDescripcion = new JTextField();
        GridBagConstraints gbc_txtDescripcion = new GridBagConstraints();
        gbc_txtDescripcion.insets = new Insets(0, 0, 5, 0);
        gbc_txtDescripcion.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtDescripcion.gridx = 1;
        gbc_txtDescripcion.gridy = 1;
        getContentPane().add(txtDescripcion, gbc_txtDescripcion);
        txtDescripcion.setColumns(10);

        // Fecha
        JLabel lblFecha = new JLabel("Fecha (YYYY-MM-DD):");
        GridBagConstraints gbc_lblFecha = new GridBagConstraints();
        gbc_lblFecha.insets = new Insets(0, 0, 5, 5);
        gbc_lblFecha.anchor = GridBagConstraints.WEST;
        gbc_lblFecha.gridx = 0;
        gbc_lblFecha.gridy = 2;
        getContentPane().add(lblFecha, gbc_lblFecha);

        JTextField txtFecha = new JTextField();
        GridBagConstraints gbc_txtFecha = new GridBagConstraints();
        gbc_txtFecha.insets = new Insets(0, 0, 5, 0);
        gbc_txtFecha.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtFecha.gridx = 1;
        gbc_txtFecha.gridy = 2;
        getContentPane().add(txtFecha, gbc_txtFecha);
        txtFecha.setColumns(10);

        // Sigla
        JLabel lblSigla = new JLabel("Sigla:");
        GridBagConstraints gbc_lblSigla = new GridBagConstraints();
        gbc_lblSigla.insets = new Insets(0, 0, 5, 5);
        gbc_lblSigla.anchor = GridBagConstraints.WEST;
        gbc_lblSigla.gridx = 0;
        gbc_lblSigla.gridy = 3;
        getContentPane().add(lblSigla, gbc_lblSigla);

        JTextField txtSigla = new JTextField();
        GridBagConstraints gbc_txtSigla = new GridBagConstraints();
        gbc_txtSigla.insets = new Insets(0, 0, 5, 0);
        gbc_txtSigla.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtSigla.gridx = 1;
        gbc_txtSigla.gridy = 3;
        getContentPane().add(txtSigla, gbc_txtSigla);
        txtSigla.setColumns(10);

        // Categoria
        JLabel lblCategoria = new JLabel("Categorias:");
        GridBagConstraints gbc_lblCategoria = new GridBagConstraints();
        gbc_lblCategoria.insets = new Insets(0, 0, 0, 5);
        gbc_lblCategoria.anchor = GridBagConstraints.WEST;
        gbc_lblCategoria.gridx = 0;
        gbc_lblCategoria.gridy = 4;
        getContentPane().add(lblCategoria, gbc_lblCategoria);

        JPanel panelRadio = new JPanel(new GridLayout(1, 2, 5, 0));
        JRadioButton rbtnFull = new JRadioButton("Full Experience");
        JRadioButton rbtnVip = new JRadioButton("Vip");
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbtnFull);
        grupo.add(rbtnVip);
        panelRadio.add(rbtnFull);
        panelRadio.add(rbtnVip);

        GridBagConstraints gbc_panelRadio = new GridBagConstraints();
        gbc_panelRadio.gridx = 1;
        gbc_panelRadio.gridy = 4;
        gbc_panelRadio.fill = GridBagConstraints.HORIZONTAL;
        getContentPane().add(panelRadio, gbc_panelRadio);

        // Botones
        JButton btnAceptar = new JButton("Aceptar");
        GridBagConstraints gbc_btnAceptar = new GridBagConstraints();
        gbc_btnAceptar.gridx = 1;
        gbc_btnAceptar.gridy = 5;
        gbc_btnAceptar.insets = new Insets(10, 5, 5, 5);
        getContentPane().add(btnAceptar, gbc_btnAceptar);

        JButton btnCancelar = new JButton("Cancelar");
        GridBagConstraints gbc_btnCancelar = new GridBagConstraints();
        gbc_btnCancelar.gridx = 2;
        gbc_btnCancelar.gridy = 5;
        gbc_btnCancelar.insets = new Insets(10, 5, 5, 0);
        getContentPane().add(btnCancelar, gbc_btnCancelar);

        btnAceptar.addActionListener(ev -> {
            String nombre = txtNombre.getText().trim();
            String descripcion = txtDescripcion.getText().trim();
            String fechaStr = txtFecha.getText().trim();
            String sigla = txtSigla.getText().trim();
            String categoria = rbtnFull.isSelected() ? "Full Experience" : rbtnVip.isSelected() ? "Vip" : null;

            if (nombre.isEmpty() || descripcion.isEmpty() || fechaStr.isEmpty() || sigla.isEmpty() || categoria == null) {
                JOptionPane.showMessageDialog(this, "Todos los campos deben estar completos y debe seleccionar una Categoria.");
                return;
            }

            // Validación de formato de fecha
            String fechaFormateada = fechaStr;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date fecha = sdf.parse(fechaStr);
                fechaFormateada = sdf.format(fecha);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "La fecha debe tener el formato YYYY-MM-DD.");
                return;
            }

            JOptionPane.showMessageDialog(this,
                    "Evento registrado con éxito:\nNombre: " + nombre +
                            "\nDescripcion: " + descripcion +
                            "\nFecha: " + fechaFormateada +
                            "\nSigla: " + sigla +
                            "\nCategoria: " + categoria);
        });

        btnCancelar.addActionListener(ev -> this.dispose());

        desktopPane.add(this);
        toFront();
    }
}
