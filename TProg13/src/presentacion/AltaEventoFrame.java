package presentacion;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import excepciones.EventoYaExisteException;
import logica.ControladorEvento;
import logica.DTCategorias;

public class AltaEventoFrame extends JInternalFrame {
    private Runnable abrirConsultaRunnable;
    public void setAbrirConsultaRunnable(Runnable r) { this.abrirConsultaRunnable = r; }
    private JDesktopPane desktopPane;
    //private ConsultaEventoFrame[] consultaEventoFrameRef;
    public AltaEventoFrame(JDesktopPane desktopPane) {//, ConsultaEventoFrame[] consultaEventoFrameRef) {
        super("Alta de Evento", true, true, true, true);
        this.desktopPane = desktopPane;
       // this.consultaEventoFrameRef = consultaEventoFrameRef;
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

        // Panel dinámico de categorías
        JPanel panelCategorias = new JPanel();
        panelCategorias.setLayout(new BoxLayout(panelCategorias, BoxLayout.Y_AXIS));
        JScrollPane scrollCategorias = new JScrollPane(panelCategorias);
        scrollCategorias.setPreferredSize(new Dimension(200, 80));
        GridBagConstraints gbc_panelCategorias = new GridBagConstraints();
        gbc_panelCategorias.gridx = 1;
        gbc_panelCategorias.gridy = 4;
        gbc_panelCategorias.fill = GridBagConstraints.BOTH;
        getContentPane().add(scrollCategorias, gbc_panelCategorias);

        // Cargar categorías dinámicamente
        java.util.List<JCheckBox> checkBoxesCategorias = new java.util.ArrayList<>();
        try {
            logica.ControladorEvento controlador = new logica.ControladorEvento();
            java.util.List<String> categorias = new java.util.ArrayList<>(logica.manejadorAuxiliar.getInstancia().listarCategorias());
            for (String cat : categorias) {
                JCheckBox check = new JCheckBox(cat);
                checkBoxesCategorias.add(check);
                panelCategorias.add(check);
            }
        } catch (Exception ex) {
            panelCategorias.add(new JLabel("No se pudieron cargar las categorías."));
        }

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
            java.util.List<String> categoriasSeleccionadas = new java.util.ArrayList<>();
            for (JCheckBox check : checkBoxesCategorias) {
                if (check.isSelected()) {
                    categoriasSeleccionadas.add(check.getText());
                }
            }
            if (nombre.isEmpty() || descripcion.isEmpty() || fechaStr.isEmpty() || sigla.isEmpty() || categoriasSeleccionadas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos deben estar completos y debe seleccionar al menos una Categoría.");
                return;
            }

            // Validación de formato de fecha y conversión a LocalDate
            int anio, mes, dia;
            try {
                String[] partes = fechaStr.split("-");
                if (partes.length != 3) throw new Exception();
                anio = Integer.parseInt(partes[0]);
                mes = Integer.parseInt(partes[1]);
                dia = Integer.parseInt(partes[2]);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "La fecha debe tener el formato YYYY-MM-DD.");
                return;
            }

            try {
                java.time.LocalDate fechaAlta = java.time.LocalDate.of(anio, mes, dia);
                ControladorEvento controlador = new ControladorEvento();
                DTCategorias dtCategorias = new DTCategorias(categoriasSeleccionadas);
                controlador.AltaEvento(nombre, descripcion, fechaAlta, sigla, dtCategorias);
                // Mostrar eventos actuales
                java.util.List<logica.DTEvento> eventos = controlador.listarEventos();
                StringBuilder sb = new StringBuilder();
                sb.append("Evento registrado con éxito:\n");
                for (logica.DTEvento e : eventos) {
                    sb.append("- ").append(e.getNombre()).append(" (Sigla: ").append(e.getSigla()).append(")\n");
                }
                JOptionPane.showMessageDialog(this, sb.toString());
                if (abrirConsultaRunnable != null) abrirConsultaRunnable.run();
            } catch (EventoYaExisteException ex) {
                JOptionPane.showMessageDialog(this,
                    "Ya existe un evento con el nombre: '" + nombre + "'.\nPor favor, elija otro nombre o cancele la operación.",
                    "Error - Evento ya existente", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al registrar el evento: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(ev -> this.dispose());
    }
}