package presentacion;

import javax.swing.*;

import excepciones.RegistroNoExiste;
import logica.Datatypes.DTRegistro;
import logica.Interfaces.*;

import java.awt.*;
import java.util.Set;
import java.util.Vector;

public class ConsultaRegistroFrame extends JInternalFrame {
    // Definimos algunos paneles como atributos
    private JPanel panelSeleccion;
    private JComboBox<String> comboUsuarios;
    private JComboBox<String> comboRegistros;
    private Vector<String> usuarios;
    private IControladorUsuario controlUsr;

    // Detalle
    private JTextField txtId;
    private JTextField txtUsuario;
    private JTextField txtEdicion;
    private JTextField txtTipoRegistro;
    private JTextField txtFechaRegistro;
    private JTextField txtCosto;
    private JTextField txtFechaInicio;

    // --- Constructor normal
    public ConsultaRegistroFrame(IControladorUsuario icu, IControladorEvento iCE) {
        super("Consulta de Registro", true, true, true, true);
        setBounds(200, 200, 500, 350);
        setLayout(new BorderLayout());
        controlUsr = icu;

        // Barra superior
        panelSeleccion = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblUsuario = new JLabel("Usuario:");
        usuarios = new Vector<>();
        usuarios.addAll(controlUsr.listarUsuarios().keySet());
        comboUsuarios = new JComboBox<>(usuarios);

        JLabel lblRegistro = new JLabel("Registro:");
        comboRegistros = new JComboBox<>();

        panelSeleccion.add(lblUsuario);
        panelSeleccion.add(comboUsuarios);
        panelSeleccion.add(lblRegistro);
        panelSeleccion.add(comboRegistros);
        add(panelSeleccion, BorderLayout.NORTH);

        // Form de detalle
        JPanel panelDatos = new JPanel(new GridLayout(0, 2, 10, 10));
        panelDatos.add(new JLabel("Identificador:"));
        txtId = new JTextField(); txtId.setEditable(false); panelDatos.add(txtId);

        panelDatos.add(new JLabel("Usuario:"));
        txtUsuario = new JTextField(); txtUsuario.setEditable(false); panelDatos.add(txtUsuario);

        panelDatos.add(new JLabel("Edición:"));
        txtEdicion = new JTextField(); txtEdicion.setEditable(false); panelDatos.add(txtEdicion);

        panelDatos.add(new JLabel("Tipo de registro:"));
        txtTipoRegistro = new JTextField(); txtTipoRegistro.setEditable(false); panelDatos.add(txtTipoRegistro);

        panelDatos.add(new JLabel("Fecha de registro:"));
        txtFechaRegistro = new JTextField(); txtFechaRegistro.setEditable(false); panelDatos.add(txtFechaRegistro);

        panelDatos.add(new JLabel("Costo:"));
        txtCosto = new JTextField(); txtCosto.setEditable(false); panelDatos.add(txtCosto);

        panelDatos.add(new JLabel("Fecha de inicio:"));
        txtFechaInicio = new JTextField(); txtFechaInicio.setEditable(false); panelDatos.add(txtFechaInicio);

        add(panelDatos, BorderLayout.CENTER);

        // Listeners
        comboUsuarios.addActionListener(e -> {
            String nick = (String) comboUsuarios.getSelectedItem();
            comboRegistros.removeAllItems();
            limpiarCampos();
            if (nick == null) return;

            if (controlUsr.esAsistente(nick)) {
                Set<DTRegistro> registrosPorUsuario =
                        controlUsr.obtenerRegistrosAsistente(controlUsr.listarAsistentes().get(nick));
                for (DTRegistro reg : registrosPorUsuario) {
                    comboRegistros.addItem(reg.getId());
                }
            } else {
                // organizador no tiene registros
            }
            panelSeleccion.revalidate();
            panelSeleccion.repaint();
        });

        comboRegistros.addActionListener(e -> {
            String idRegistro = (String) comboRegistros.getSelectedItem();
            limpiarCampos();
            if (idRegistro == null) return;
            DTRegistro datos = controlUsr.obtenerDatosRegistros(idRegistro);
            if (datos != null) {
                txtId.setText(datos.getId());
                txtUsuario.setText(datos.getUsuario());
                txtEdicion.setText(datos.getEdicion());
                txtTipoRegistro.setText(datos.getTipoRegistro());
                txtFechaRegistro.setText(String.valueOf(datos.getFechaRegistro()));
                txtCosto.setText(String.valueOf(datos.getCosto()));
                txtFechaInicio.setText(String.valueOf(datos.getFechaInicio()));
            }
        });

        setVisible(true);
    }

    // --- Constructor con PRESELECCIÓN (nick + idRegistro)
    public ConsultaRegistroFrame(IControladorUsuario icu,
                                 IControladorEvento iCE,
                                 String nickPreseleccionado,
                                 String idRegistroPreseleccionado) {
        this(icu, iCE); // construye UI y listeners

        // Seteamos el usuario (esto dispara el listener y carga los registros)
        if (nickPreseleccionado != null) {
            comboUsuarios.setSelectedItem(nickPreseleccionado);
        }

        // Después que el combo de registros se cargue, seleccionamos el registro
        if (idRegistroPreseleccionado != null) {
            SwingUtilities.invokeLater(() -> comboRegistros.setSelectedItem(idRegistroPreseleccionado));
        }
    }

    public void cargarUsuarios() throws RegistroNoExiste {
        // Este método se usa para refrescar los usuarios al entrar al caso de uso
        DefaultComboBoxModel<String> model;
        usuarios.clear();
        usuarios.addAll(controlUsr.listarUsuarios().keySet());
        model = new DefaultComboBoxModel<>(usuarios);
        comboUsuarios.setModel(model);
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtUsuario.setText("");
        txtEdicion.setText("");
        txtTipoRegistro.setText("");
        txtFechaRegistro.setText("");
        txtCosto.setText("");
        txtFechaInicio.setText("");
    }
}