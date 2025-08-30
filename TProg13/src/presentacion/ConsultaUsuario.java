package presentacion;

import javax.swing.*;

import excepciones.UsuarioNoExisteException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import logica.*;
import java.time.LocalDate;
import javax.swing.DefaultComboBoxModel;

public class ConsultaUsuario extends JInternalFrame {
	private IControladorUsuario controlUsr;
	private Vector<String> usuarios;
	private JComboBox<String> comboUsuarios;
    public ConsultaUsuario(IControladorUsuario icu) {
    	controlUsr = icu;

        // Configuración básica
        setTitle("Listar Usuarios");
        setResizable(true);
        setMaximizable(true);
        setIconifiable(true);
        setClosable(true);
        setBounds(new Rectangle(300, 300, 500, 200));
        getContentPane().setLayout(null);

        JLabel label = new JLabel("Seleccione un usuario:");
        label.setBounds(89, 51, 150, 20);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(label);

        // Agrego todos los usuarios a un vector
        usuarios = new Vector<>();
        usuarios.addAll(controlUsr.listarUsuarios().keySet());
        
//        if(usuarios.isEmpty()) {
//        	JOptionPane.showMessageDialog(this, "no hay nada",title, JOptionPane.INFORMATION_MESSAGE);
//
//        
//        }else {
//        	JOptionPane.showMessageDialog(this, " hay ",title, JOptionPane.INFORMATION_MESSAGE);
//        }
        
        // Agrego ese vector a un combobox para que seleccione que usuario quiere
        comboUsuarios = new JComboBox<>(usuarios);
        comboUsuarios.setBounds(284, 49, 120, 25);
        getContentPane().add(comboUsuarios);
        
        JButton btnSeleccionar = new JButton("Seleccionar");
        btnSeleccionar.setBounds(183, 108, 120, 30);
        getContentPane().add(btnSeleccionar);

        // Acción al hacer clic en el botón
        btnSeleccionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtenemos el usuario seleccionado
            	String usuarioSeleccionado = (String) comboUsuarios.getSelectedItem();

                if (usuarioSeleccionado == null || usuarioSeleccionado.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un usuario.");
                    return;
                }

                getContentPane().removeAll();
                repaint();
                revalidate();

                setTitle("Datos del usuario");

                JPanel panelDatosUsuario = new JPanel();
                panelDatosUsuario.setBounds(10, 10, 474, 322);
                getContentPane().add(panelDatosUsuario);
                panelDatosUsuario.setLayout(null);

                JLabel lblNick = new JLabel("Nickname:");
                lblNick.setBounds(101, 11, 94, 14);
                panelDatosUsuario.add(lblNick);

                JTextArea txtNick = new JTextArea();
                txtNick.setBounds(248, 6, 161, 22);
                txtNick.setEditable(false);
                panelDatosUsuario.add(txtNick);

                JLabel lblNombre = new JLabel("Nombre:");
                lblNombre.setBounds(101, 36, 94, 14);
                panelDatosUsuario.add(lblNombre);

                JTextArea txtNombre = new JTextArea();
                txtNombre.setBounds(248, 31, 161, 22);
                txtNombre.setEditable(false);
                panelDatosUsuario.add(txtNombre);

                JLabel lblCorreo = new JLabel("Correo electrónico:");
                lblCorreo.setBounds(101, 61, 137, 14);
                panelDatosUsuario.add(lblCorreo);

                JTextArea txtCorreo = new JTextArea();
                txtCorreo.setBounds(248, 56, 161, 22);
                txtCorreo.setEditable(false);
                panelDatosUsuario.add(txtCorreo);

                // Cargar datos según tipo de usuario
                
                if ((controlUsr.listarAsistentes() != null) && controlUsr.listarAsistentes().containsKey(usuarioSeleccionado)) {
                	DTDatosUsuario datos = null;
					try {
						datos = controlUsr.obtenerDatosUsuario(usuarioSeleccionado);
					} catch (UsuarioNoExisteException e1) {
						// TODO Auto-generated catch block
						// Este caso no pasa, ya que chequee antes que existiera en la lista
						e1.printStackTrace();
					}
                	//Asistente usuario = controlUsr.listarUsuarios().get(usuarioSeleccionado);

                    txtNick.setText(datos.getNickname());
                    txtNombre.setText(datos.getNombre());
                    txtCorreo.setText(datos.getEmail());

                    JPanel panelDatosAsistente = new JPanel();
                    panelDatosAsistente.setBounds(10, 100, 414, 172);
                    panelDatosUsuario.add(panelDatosAsistente);
                    panelDatosAsistente.setLayout(null);

                    JLabel lblApellido = new JLabel("Apellido:");
                    lblApellido.setBounds(94, 11, 89, 14);
                    panelDatosAsistente.add(lblApellido);

                    JTextArea txtApellido = new JTextArea(datos.getApellido());
                    txtApellido.setBounds(237, 6, 161, 22);
                    txtApellido.setEditable(false);
                    panelDatosAsistente.add(txtApellido);

                    JLabel lblFechaNac = new JLabel("Fecha de nacimiento:");
                    lblFechaNac.setBounds(94, 36, 121, 14);
                    panelDatosAsistente.add(lblFechaNac);

                    JTextArea txtFecha = new JTextArea(datos.getFechaNac().toString());
                    txtFecha.setBounds(237, 31, 161, 22);
                    txtFecha.setEditable(false);
                    panelDatosAsistente.add(txtFecha);

                    
                    JLabel lblRegistros = new JLabel("Registros:");
                    lblRegistros.setBounds(94, 70, 121, 14);
                    panelDatosAsistente.add(lblRegistros);

                    DefaultListModel<String> modelRegistros = new DefaultListModel<>();
                    for (DTRegistro r : datos.getRegistros()) {
                        modelRegistros.addElement(r.toString()); 
                    }

                    JList<String> listRegistros = new JList<>(modelRegistros);
                    JScrollPane scrollRegistros = new JScrollPane(listRegistros);
                    scrollRegistros.setBounds(237, 66, 161, 60);
                    panelDatosAsistente.add(scrollRegistros);


                } else if ((controlUsr.listarOrganizadores() != null) && controlUsr.listarOrganizadores().containsKey(usuarioSeleccionado)) {
                	DTDatosUsuario datos = null;
					try {
						datos = controlUsr.obtenerDatosUsuario(usuarioSeleccionado);
					} catch (UsuarioNoExisteException e1) {
						// TODO Auto-generated catch block
						// Este caso no pasa, ya que chequee antes que existiera en la lista
						e1.printStackTrace();
					}
//                	Organizador usuario = organizadores.get(usuarioSeleccionado);

                    txtNick.setText(datos.getNickname());
                    txtNombre.setText(datos.getNombre());
                    txtCorreo.setText(datos.getEmail());

                    JPanel panelDatosOrganizador = new JPanel();
                    panelDatosOrganizador.setBounds(10, 100, 414, 172);
                    panelDatosUsuario.add(panelDatosOrganizador);
                    panelDatosOrganizador.setLayout(null);

                    JLabel lblDesc = new JLabel("Descripción:");
                    lblDesc.setBounds(93, 11, 89, 14);
                    panelDatosOrganizador.add(lblDesc);

                    JTextArea txtDesc = new JTextArea(datos.getDesc());
                    txtDesc.setBounds(238, 6, 161, 22);
                    txtDesc.setEditable(false);
                    panelDatosOrganizador.add(txtDesc);

                    JLabel lblLink = new JLabel("Link:");
                    lblLink.setBounds(93, 36, 121, 14);
                    panelDatosOrganizador.add(lblLink);

                    JTextArea txtLink = new JTextArea(datos.getLink());
                    txtLink.setBounds(238, 31, 161, 22);
                    txtLink.setEditable(false);
                    panelDatosOrganizador.add(txtLink);
                    
                    
                    JLabel lblEdiciones = new JLabel("Ediciones:");
                    lblEdiciones.setBounds(93, 70, 121, 14);
                    panelDatosOrganizador.add(lblEdiciones);

                    DefaultListModel<String> modelEdiciones = new DefaultListModel<>();
                    for (DTEdicionEvento ediEvento : datos.getEdiciones()) {
                        modelEdiciones.addElement(ediEvento.toString());
                    }

                    JList<String> listEdiciones = new JList<>(modelEdiciones);
                    JScrollPane scrollEdiciones = new JScrollPane(listEdiciones);
                    scrollEdiciones.setBounds(238, 66, 161, 60);
                    panelDatosOrganizador.add(scrollEdiciones);

                    
                }

                repaint();
                revalidate();
            }
        });
        
        setVisible(false);
    }

    
    public void cargarUsuarios() throws UsuarioNoExisteException {
    	DefaultComboBoxModel<String> model;
    	usuarios.clear();
		usuarios.addAll(controlUsr.listarUsuarios().keySet());
		model = new DefaultComboBoxModel<String>(usuarios);
		comboUsuarios.setModel(model);
    	}

}
