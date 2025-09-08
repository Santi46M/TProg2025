package presentacion;

import javax.swing.*;
import java.awt.*;
import logica.IControladorEvento;
import logica.IControladorUsuario;

public class ConsultaEventoFrame extends JInternalFrame {
    private IControladorEvento controladorEvento;
    private JComboBox<String> comboEventos;
    private DefaultListModel<String> listModel;
    private JTextField txtNombre;
    private JTextArea txtDescripcion;
    private JTextField lblCategorias;
    private JComboBox<String> comboEdiciones;
    private IControladorUsuario controladorUsuario;
    private String[][] datosEventos;
    private String[][] categoriasEventos;
    private String[][] edicionesEventos;

    public ConsultaEventoFrame(IControladorUsuario iCU, IControladorEvento controladorEvento) {
        super("Consulta de Evento", true, true, true, true);
        this.controladorEvento = controladorEvento;
        this.controladorUsuario = iCU;
        setBounds(100, 100, 600, 400);
        setLayout(new BorderLayout());

        JPanel panelSeleccion = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblEvento = new JLabel("Evento:");
        comboEventos = new JComboBox<>();
        panelSeleccion.add(lblEvento);
        panelSeleccion.add(comboEventos);
        add(panelSeleccion, BorderLayout.NORTH);

        JPanel panelDatos = new JPanel();
        panelDatos.setLayout(new BoxLayout(panelDatos, BoxLayout.Y_AXIS));

        JPanel panelCampos = new JPanel(new GridLayout(0, 2, 10, 10));
        panelDatos.add(panelCampos);

        panelCampos.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        txtNombre.setEditable(false);
        panelCampos.add(txtNombre);

        panelCampos.add(new JLabel("Descripción:"));
        txtDescripcion = new JTextArea(3, 40);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setEditable(false);
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        panelCampos.add(scrollDesc);

        panelCampos.add(new JLabel("Categorías:"));
        JTextField txtCategorias = new JTextField();
        txtCategorias.setEditable(false);
        panelCampos.add(txtCategorias);
        this.lblCategorias = txtCategorias;

        panelDatos.add(Box.createVerticalStrut(10));
        JPanel panelEdiciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblEdiciones = new JLabel("Ediciones:");
        comboEdiciones = new JComboBox<>();
        panelEdiciones.add(lblEdiciones);
        panelEdiciones.add(comboEdiciones);
        panelDatos.add(panelEdiciones);

        add(panelDatos, BorderLayout.CENTER);

        comboEventos.addActionListener(e -> mostrarDatosEvento());
        comboEdiciones.addActionListener(e -> abrirConsultaEdicion());
    }

    public void cargarEventos() {
        try {
            java.util.List<logica.DTEvento> eventos = controladorEvento.listarEventos();
            String[] eventosArr = new String[eventos.size()];
            datosEventos = new String[eventos.size()][2];
            categoriasEventos = new String[eventos.size()][];
            edicionesEventos = new String[eventos.size()][];
            for (int i = 0; i < eventos.size(); i++) {
                logica.DTEvento ev = eventos.get(i);
                eventosArr[i] = ev.getNombre();
                datosEventos[i][0] = ev.getNombre();
                datosEventos[i][1] = ev.getDescripcion();
                categoriasEventos[i] = ev.getCategorias().toArray(new String[0]);
                edicionesEventos[i] = ev.getEdiciones().toArray(new String[0]);
            }
            comboEventos.setModel(new DefaultComboBoxModel<>(eventosArr));
            comboEventos.revalidate();
            comboEventos.repaint();
            if (eventosArr.length > 0) {
                comboEventos.setSelectedIndex(0);
                mostrarDatosEvento();
            } else {
                txtNombre.setText("");
                txtDescripcion.setText("");
                lblCategorias.setText("");
                comboEdiciones.setModel(new DefaultComboBoxModel<>(new String[]{}));
            }
        } catch (Exception ex) {
            comboEventos.setModel(new DefaultComboBoxModel<>(new String[]{"No hay eventos"}));
            comboEventos.revalidate();
            comboEventos.repaint();
            txtNombre.setText("");
            txtDescripcion.setText("");
            lblCategorias.setText("");
            comboEdiciones.setModel(new DefaultComboBoxModel<>(new String[]{}));
        }
    }

    private void mostrarDatosEvento() {
        int idx = comboEventos.getSelectedIndex();
        if (idx < 0 || datosEventos == null || idx >= datosEventos.length) {
            txtNombre.setText("");
            txtDescripcion.setText("");
            lblCategorias.setText("");
            comboEdiciones.setModel(new DefaultComboBoxModel<>(new String[]{}));
            return;
        }
        txtNombre.setText(datosEventos[idx][0]);
        txtDescripcion.setText(datosEventos[idx][1]);
        StringBuilder cats = new StringBuilder();
        for (String cat : categoriasEventos[idx]) {
            cats.append(cat).append(", ");
        }
        if (cats.length() > 2) cats.setLength(cats.length() - 2); // quitar última coma
        lblCategorias.setText(cats.toString());
        comboEdiciones.setModel(new DefaultComboBoxModel<>(edicionesEventos[idx]));
        comboEdiciones.revalidate();
        comboEdiciones.repaint();
    }

    private void abrirConsultaEdicion() {
        int idxEvento = comboEventos.getSelectedIndex();
        int idxEd = comboEdiciones.getSelectedIndex();
        if (idxEvento < 0 || idxEd < 0 || edicionesEventos == null || idxEvento >= edicionesEventos.length || idxEd >= edicionesEventos[idxEvento].length) {
            return;
        }
        String nombreEvento = comboEventos.getItemAt(idxEvento);
        String nombreEdicion = edicionesEventos[idxEvento][idxEd];
        ConsultaEdicionEventoFrame frameEdicion = new ConsultaEdicionEventoFrame(controladorUsuario, controladorEvento, nombreEvento, nombreEdicion);
        JDesktopPane desktop = getDesktopPane();
        if (desktop != null) {
            desktop.add(frameEdicion);
            frameEdicion.setVisible(true);
            frameEdicion.toFront();
        } else {
            frameEdicion.setVisible(true);
        }
    }
}