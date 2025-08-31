package presentacion;

import javax.swing.*;

import excepciones.RegistroNoExiste;
import excepciones.UsuarioNoExisteException;
import logica.DTRegistro;
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
	
    public ConsultaRegistroFrame(IControladorUsuario icu) {
        super("Consulta de Registro", true, true, true, true);
        setBounds(200, 200, 500, 300);
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
        JComboBox<String> comboRegistros = new JComboBox<>();
        panelSeleccion.add(lblRegistro);
        panelSeleccion.add(comboRegistros);
        add(panelSeleccion, BorderLayout.NORTH);

        JTextArea txtDatos = new JTextArea(8, 40);
        txtDatos.setEditable(false);
        add(new JScrollPane(txtDatos), BorderLayout.CENTER);

        // Actualizar registros al seleccionar usuario
        comboUsuarios.addActionListener(e -> {
            String idx = (String) comboUsuarios.getSelectedItem();
            comboRegistros.removeAllItems();
            txtDatos.setText("");
            if (idx == null) return;
            
            // Obtenemos los registros del usuario
            if (controlUsr.esAsistente(idx)) {
            	System.out.println("es asistente");
                Set<DTRegistro> registrosPorUsuario = controlUsr.obtenerRegistrosAsistente(controlUsr.listarAsistentes().get(idx));
                for (DTRegistro reg : registrosPorUsuario) {
                    String idRegistro = reg.getId();
                	comboRegistros.addItem(idRegistro);
                }
             // Mostrar datos al seleccionar registro
                comboRegistros.addActionListener(ep -> {
//                    int idxUsuario = comboUsuarios.getSelectedIndex();
                    String idRegistro = (String) comboRegistros.getSelectedItem();
                    txtDatos.setText("");
//                    if (idxUsuario < 0 || idxRegistro < 0) return;
//                    txtDatos.setText(datosRegistro[idxUsuario][idxRegistro]);
                    // Obtenemos la info del registro
                    DTRegistro datos = controlUsr.obtenerDatosRegistros(idRegistro);
                    if (datos != null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Identificador: " + datos.getId() +"\n");
                        sb.append("Usuario: " + datos.getUsuario() +"\n");
                        sb.append("Edición: " + datos.getEdicion() +"\n");
                        sb.append("Tipo de registro: " + datos.getTipoRegistro() +"\n");
                        sb.append("Fecha de registro: " + datos.getFechaRegistro().toString() +"\n");
                        sb.append("Costo: " + datos.getCosto() +"\n");
                        sb.append("Fecha de inicio: " + datos.getFechaInicio().toString() +"\n");
                        txtDatos.setText(sb.toString());
                    }
                });
            }else {
            	System.out.println("es organizador");
            	// Manejo de error, se solicitó el registro de un organizador
            }
            panelSeleccion.revalidate();
            panelSeleccion.repaint();
//            datos.clear();
          

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
}
