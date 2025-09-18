package presentacion;

import javax.swing.*;
import java.awt.*;
import excepciones.EventoYaExisteException;
import logica.Controladores.ControladorEvento;
import logica.Interfaces.*;
import logica.Datatypes.DTCategorias;
import com.toedter.calendar.JDateChooser;

public class AltaEventoFrame extends JInternalFrame {
    private Runnable abrirConsultaRunnable;
    public void setAbrirConsultaRunnable(Runnable r) { this.abrirConsultaRunnable = r; }
    private JList<String> listCategorias;
    private DefaultListModel<String> listModelCategorias;
    private java.util.List<Boolean> categoriasSeleccionadasFlags;
    
    public void cargarCategorias() {
        listModelCategorias.clear();
        categoriasSeleccionadasFlags = new java.util.ArrayList<>();
        try {
            java.util.List<String> categorias = new java.util.ArrayList<>(logica.Manejadores.manejadorAuxiliar.getInstancia().listarCategorias());
            for (String cat : categorias) {
                listModelCategorias.addElement(cat);
                categoriasSeleccionadasFlags.add(false);
            }
        } catch (Exception ex) {
            listModelCategorias.addElement("No se pudieron cargar las categorías.");
            categoriasSeleccionadasFlags.add(false);
        }
    }
    
    public AltaEventoFrame(IControladorUsuario iCU, IControladorEvento iCE) {
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
        JLabel lblFecha = new JLabel("Fecha:");
        GridBagConstraints gbc_lblFecha = new GridBagConstraints();
        gbc_lblFecha.insets = new Insets(0, 0, 5, 5);
        gbc_lblFecha.anchor = GridBagConstraints.WEST;
        gbc_lblFecha.gridx = 0;
        gbc_lblFecha.gridy = 2;
        getContentPane().add(lblFecha, gbc_lblFecha);

        JDateChooser dateChooserFecha = new JDateChooser();
        GridBagConstraints gbc_dateChooserFecha = new GridBagConstraints();
        gbc_dateChooserFecha.insets = new Insets(0, 0, 5, 0);
        gbc_dateChooserFecha.fill = GridBagConstraints.HORIZONTAL;
        gbc_dateChooserFecha.gridx = 1;
        gbc_dateChooserFecha.gridy = 2;
        getContentPane().add(dateChooserFecha, gbc_dateChooserFecha);

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

        listModelCategorias = new DefaultListModel<>();
        listCategorias = new JList<>(listModelCategorias);
        listCategorias.setCellRenderer(new ListCellRenderer<String>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
                JCheckBox checkBox = new JCheckBox(value, categoriasSeleccionadasFlags.get(index));
                checkBox.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
                checkBox.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
                return checkBox;
            }
        });
        listCategorias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listCategorias.setVisibleRowCount(5);
        listCategorias.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int index = listCategorias.locationToIndex(e.getPoint());
                if (index >= 0 && index < categoriasSeleccionadasFlags.size()) {
                    categoriasSeleccionadasFlags.set(index, !categoriasSeleccionadasFlags.get(index));
                    listCategorias.repaint();
                }
            }
        });
        JScrollPane scrollCategorias = new JScrollPane(listCategorias);
        scrollCategorias.setPreferredSize(new Dimension(200, 80));
        GridBagConstraints gbc_listCategorias = new GridBagConstraints();
        gbc_listCategorias.gridx = 1;
        gbc_listCategorias.gridy = 4;
        gbc_listCategorias.fill = GridBagConstraints.HORIZONTAL;
        getContentPane().add(scrollCategorias, gbc_listCategorias);

        cargarCategorias();

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
            java.util.Date fechaDate = dateChooserFecha.getDate();
            String sigla = txtSigla.getText().trim();
            java.util.List<String> categoriasSeleccionadas = new java.util.ArrayList<>();
            for (int i = 0; i < listModelCategorias.size(); i++) {
                if (categoriasSeleccionadasFlags.get(i)) {
                    categoriasSeleccionadas.add(listModelCategorias.get(i));
                }
            }
            if (nombre.isEmpty() || descripcion.isEmpty() || fechaDate == null || sigla.isEmpty() || categoriasSeleccionadas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos deben estar completos y debe seleccionar al menos una Categoría.");
                return;
            }
            try {
                java.time.LocalDate fechaAlta = fechaDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                ControladorEvento controlador = new ControladorEvento();
                DTCategorias dtCategorias = new DTCategorias(categoriasSeleccionadas);
                controlador.AltaEvento(nombre, descripcion, fechaAlta, sigla, dtCategorias);
                JOptionPane.showMessageDialog(this, "Evento registrado con éxito.");
                if (abrirConsultaRunnable != null) abrirConsultaRunnable.run();
                this.dispose();
            } catch (EventoYaExisteException ex) {
                JOptionPane.showMessageDialog(this,
                    "Ya existe un evento con el nombre: '" + nombre + "'.",
                    "Error - Evento ya existente", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al registrar el evento: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(ev -> this.dispose());
    }
}