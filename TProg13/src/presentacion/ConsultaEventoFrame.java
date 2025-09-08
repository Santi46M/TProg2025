package presentacion;

import javax.swing.*;
import java.awt.*;
import logica.Interfaces.*;


public class ConsultaEventoFrame extends JInternalFrame {
    private IControladorEvento controladorEvento;
    private JComboBox<String> comboEventos;
    private DefaultListModel<String> listModel;
    private JTextArea txtDatos;
    private JLabel lblCategorias;
    private JList<String> listEdiciones;
    private String[][] datosEventos;
    private String[][] categoriasEventos;
    private String[][] edicionesEventos;

    public ConsultaEventoFrame(IControladorUsuario iCU, IControladorEvento controladorEvento) {
        super("Consulta de Evento", true, true, true, true);
        this.controladorEvento = controladorEvento;
        setBounds(100, 100, 600, 400);
        setLayout(new BorderLayout());

        JPanel panelSeleccion = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblEvento = new JLabel("Evento:");
        comboEventos = new JComboBox<>();
        panelSeleccion.add(lblEvento);
        panelSeleccion.add(comboEventos);
        add(panelSeleccion, BorderLayout.NORTH);

        JPanel panelDatos = new JPanel(new GridLayout(0, 1));
        txtDatos = new JTextArea(6, 40);
        txtDatos.setEditable(false);
        panelDatos.add(new JScrollPane(txtDatos));

        lblCategorias = new JLabel();
        panelDatos.add(lblCategorias);

        listModel = new DefaultListModel<>();
        listEdiciones = new JList<>(listModel);
        JScrollPane scrollEdiciones = new JScrollPane(listEdiciones);
        panelDatos.add(scrollEdiciones);
        
        // Agrega un nuevo JTextArea para mostrar los detalles de la edición
        JTextArea txtEdicion = new JTextArea(6, 40);
        txtEdicion.setEditable(false);
        panelDatos.add(new JScrollPane(txtEdicion));
        
        add(panelDatos, BorderLayout.CENTER);

        comboEventos.addActionListener(e -> mostrarDatosEvento());
        listEdiciones.addListSelectionListener(e -> mostrarDetalleEdicionCompleta(txtEdicion));
    }

    public void cargarEventos() {
        try {
            java.util.List<logica.Datatypes.DTEvento> eventos = controladorEvento.listarEventos();
            String[] eventosArr = new String[eventos.size()];
            datosEventos = new String[eventos.size()][1];
            categoriasEventos = new String[eventos.size()][];
            edicionesEventos = new String[eventos.size()][];
            for (int i = 0; i < eventos.size(); i++) {
                logica.Datatypes.DTEvento ev = eventos.get(i);
                eventosArr[i] = ev.getNombre();
                datosEventos[i][0] = ev.getDescripcion();
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
                txtDatos.setText("");
                lblCategorias.setText("");
                listModel.clear();
            }
        } catch (Exception ex) {
            comboEventos.setModel(new DefaultComboBoxModel<>(new String[]{"No hay eventos"}));
            comboEventos.revalidate();
            comboEventos.repaint();
            txtDatos.setText("");
            lblCategorias.setText("");
            listModel.clear();
        }
    }

    private void mostrarDatosEvento() {
        int idx = comboEventos.getSelectedIndex();
        if (idx < 0 || datosEventos == null || idx >= datosEventos.length) {
            txtDatos.setText("");
            lblCategorias.setText("");
            listModel.clear();
            return;
        }
        txtDatos.setText(datosEventos[idx][0]);
        StringBuilder cats = new StringBuilder("Categorías: ");
        for (String cat : categoriasEventos[idx]) {
            cats.append(cat).append(", ");
        }
        if (cats.length() > 12) cats.setLength(cats.length() - 2); // quitar última coma
        lblCategorias.setText(cats.toString());
        listModel.clear();
        for (String ed : edicionesEventos[idx]) {
            listModel.addElement(ed);
        }
    }

    private void mostrarDetalleEdicionCompleta(JTextArea txtEdicion) {
        int idxEvento = comboEventos.getSelectedIndex();
        int idxEd = listEdiciones.getSelectedIndex();
        if (idxEvento < 0 || idxEd < 0 || edicionesEventos == null || idxEvento >= edicionesEventos.length || idxEd >= edicionesEventos[idxEvento].length) {
            txtEdicion.setText("");
            return;
        }
        String nombreEvento = comboEventos.getItemAt(idxEvento);
        String nombreEdicion = edicionesEventos[idxEvento][idxEd];
        logica.Clases.Ediciones edi = controladorEvento.obtenerEdicion(nombreEvento, nombreEdicion);
        if (edi == null) {
            txtEdicion.setText("No se encontró la edición.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Nombre: ").append(edi.getNombre()).append("\n");
        sb.append("Sigla: ").append(edi.getSigla()).append("\n");
        sb.append("Fechas: ").append(edi.getFechaInicio()).append(" a ").append(edi.getFechaFin()).append("\n");
        sb.append("Fecha alta: ").append(edi.getFechaAlta()).append("\n");
        sb.append("Ciudad: ").append(edi.getCiudad()).append("\n");
        sb.append("País: ").append(edi.getPais()).append("\n");
        sb.append("Organizador: ").append(edi.getOrganizador() != null ? edi.getOrganizador().getNickname() : "").append("\n");
        txtEdicion.setText(sb.toString());
    }
}