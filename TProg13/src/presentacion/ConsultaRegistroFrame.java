package presentacion;

import javax.swing.*;

import excepciones.RegistroNoExiste;
import excepciones.UsuarioNoExisteException;
import logica.DTRegistro;
import logica.IControladorEvento;
import logica.IControladorUsuario;

import java.awt.*;
import java.util.Set;
import java.util.Vector;

public class ConsultaRegistroFrame extends JInternalFrame {
	// Definimos algunos paneles como atributos
	private JPanel panelSeleccion;
	private JComboBox<String> comboUsuarios;
	private Vector<String> usuarios;
	private IControladorUsuario controlUsr;
	private DTRegistro datos;
	private JComboBox<String> comboRegistros;
	private JTextField txtId;
	private JTextField txtUsuario;
	private JTextField txtEdicion;
	private JTextField txtTipoRegistro;
	private JTextField txtFechaRegistro;
	private JTextField txtCosto;
	private JTextField txtFechaInicio;
	
    public ConsultaRegistroFrame(IControladorUsuario icu, IControladorEvento iCE) {
        super("Consulta de Registro", true, true, true, true);
        setBounds(200, 200, 500, 350);
        setVisible(true);
        setLayout(new BorderLayout());
        controlUsr = icu;

        panelSeleccion = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblUsuario = new JLabel("Usuario:");
        // Obtenemos los usuarios del sistema
        usuarios = new Vector<>();
        usuarios.addAll(controlUsr.listarUsuarios().keySet());
        comboUsuarios = new JComboBox<>(usuarios);
        panelSeleccion.add(lblUsuario);
        panelSeleccion.add(comboUsuarios);
        JLabel lblRegistro = new JLabel("Registro:");
        comboRegistros = new JComboBox<>();
        panelSeleccion.add(lblRegistro);
        panelSeleccion.add(comboRegistros);
        add(panelSeleccion, BorderLayout.NORTH);

        JPanel panelDatos = new JPanel(new GridLayout(0, 2, 10, 10));
        panelDatos.add(new JLabel("Identificador:"));
        txtId = new JTextField();
        txtId.setEditable(false);
        panelDatos.add(txtId);
        panelDatos.add(new JLabel("Usuario:"));
        txtUsuario = new JTextField();
        txtUsuario.setEditable(false);
        panelDatos.add(txtUsuario);
        panelDatos.add(new JLabel("Edición:"));
        txtEdicion = new JTextField();
        txtEdicion.setEditable(false);
        panelDatos.add(txtEdicion);
        panelDatos.add(new JLabel("Tipo de registro:"));
        txtTipoRegistro = new JTextField();
        txtTipoRegistro.setEditable(false);
        panelDatos.add(txtTipoRegistro);
        panelDatos.add(new JLabel("Fecha de registro:"));
        txtFechaRegistro = new JTextField();
        txtFechaRegistro.setEditable(false);
        panelDatos.add(txtFechaRegistro);
        panelDatos.add(new JLabel("Costo:"));
        txtCosto = new JTextField();
        txtCosto.setEditable(false);
        panelDatos.add(txtCosto);
        panelDatos.add(new JLabel("Fecha de inicio:"));
        txtFechaInicio = new JTextField();
        txtFechaInicio.setEditable(false);
        panelDatos.add(txtFechaInicio);
        add(panelDatos, BorderLayout.CENTER);

        // Actualizar registros al seleccionar usuario
        comboUsuarios.addActionListener(e -> {
            String idx = (String) comboUsuarios.getSelectedItem();
            comboRegistros.removeAllItems();
            limpiarCampos();
            if (idx == null) return;
            
            // Obtenemos los registros del usuario
            if (controlUsr.esAsistente(idx)) {
            	System.out.println("es asistente");
                Set<DTRegistro> registrosPorUsuario = controlUsr.obtenerRegistrosAsistente(controlUsr.listarAsistentes().get(idx));
                for (DTRegistro reg : registrosPorUsuario) {
                    String idRegistro = reg.getId();
                	comboRegistros.addItem(idRegistro);
                }
            }else {
            	System.out.println("es organizador");
            	// Manejo de error, se solicitó el registro de un organizador
            }
            panelSeleccion.revalidate();
            panelSeleccion.repaint();
//            datos.clear();
          

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
                txtFechaRegistro.setText(datos.getFechaRegistro().toString());
                txtCosto.setText(String.valueOf(datos.getCosto()));
                txtFechaInicio.setText(datos.getFechaInicio().toString());
            }
        });
    }
    
    public void cargarUsuarios() throws RegistroNoExiste {
    	// Este método se usa para obtener los usuarios del sistema cada vez que ingresemos al caso de uso, sino solo lo obtiene al inicio y nunca más
    	DefaultComboBoxModel<String> model;
    	usuarios.clear();
		usuarios.addAll(controlUsr.listarUsuarios().keySet());
		model = new DefaultComboBoxModel<String>(usuarios);
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