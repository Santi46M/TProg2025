package presentacion;

import javax.swing.*;

import excepciones.UsuarioNoExisteException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import logica.*;
import java.time.LocalDate;

public class ConsultaUsuario extends JInternalFrame {
	private IControladorUsuario controlUsr;
	private Vector<String> usuarios;
	private JComboBox<String> comboUsuarios;
    private JButton btnCerrar;
    private JPanel panelSeleccion;
    private JPanel panelDatos;
    private CardLayout cardLayout;
    public ConsultaUsuario(IControladorUsuario icu) {
    	controlUsr = icu;
        cardLayout = new CardLayout();
        getContentPane().setLayout(cardLayout);

        // Divido en dos paneles, uno para seleccionar el usuario y otro para mostrar los datos
        // Panel para seleccionar el usuario
        panelSeleccion = new JPanel(null);
        getContentPane().add(panelSeleccion, "SELECCION");
        
        // Panel con los datos del usuario
        panelDatos = new JPanel(new BorderLayout());
        getContentPane().add(panelDatos, "DATOS");
        
        // Configuración básica
        setTitle("Listar Usuarios");
        setResizable(true);
        setMaximizable(true);
        setIconifiable(true);
        setClosable(true);
        setBounds(new Rectangle(300, 300, 500, 200));
//        getContentPane().setLayout(null);

        JLabel label = new JLabel("Seleccione un usuario:");
        label.setBounds(89, 51, 150, 20);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panelSeleccion.add(label);
  
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
        panelSeleccion.add(comboUsuarios);
        
        JButton btnSeleccionar = new JButton("Seleccionar");
        btnSeleccionar.setBounds(118, 109, 120, 30);
        panelSeleccion.add(btnSeleccionar);
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(284, 109, 120, 30);
        panelSeleccion.add(btnCancelar);
        
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // En caso de cancelar, limpiamos el vector de usuarios y escondemos el iframe
            	usuarios.clear();
                setVisible(false);
            }
        });

        // Si seleccionamos un usuario
        btnSeleccionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtenemos el usuario seleccionado
            	cardLayout.show(getContentPane(), "DATOS");
            	String usuarioSeleccionado = (String) comboUsuarios.getSelectedItem();

                if (usuarioSeleccionado == null || usuarioSeleccionado.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un usuario.");
                    return;
                }
                // Limpiamos el panel (por si hay datos viejos)
                panelDatos.removeAll();
                setTitle("Datos del usuario");

                
                JPanel panelContenedor = new JPanel(new BorderLayout());
                panelDatos.add(panelContenedor, BorderLayout.CENTER);

                // Panel donde van los datos (con absolute)
                JPanel panelDatosUsuario = new JPanel(null);
                panelContenedor.add(panelDatosUsuario, BorderLayout.CENTER);

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
                
                // Agregamos un boton para cancelar la sekeccion
                JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JButton btnCerrar = new JButton("Cerrar");
                btnCerrar.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        usuarios.clear();
//                        setVisible(false);
                        // Volvemos al panel de seleccion
                        cardLayout.show(getContentPane(), "SELECCION");
                        setTitle("Listar Usuarios");
                    }
                });
                panelSur.add(btnCerrar);
                panelContenedor.add(panelSur, BorderLayout.SOUTH);
                
               
                // Separamos en casos, si es asistente o es organizador
                if ((controlUsr.listarAsistentes() != null) && controlUsr.listarAsistentes().containsKey(usuarioSeleccionado)) {
                	DTDatosUsuario datos = null;
					try {
						datos = controlUsr.obtenerDatosUsuario(usuarioSeleccionado);
					} catch (UsuarioNoExisteException e1) {
						// TODO Auto-generated catch block
						// Este caso no pasa, ya que chequee antes que existiera en la lista
						e1.printStackTrace();
					}

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

 


                } else if ((controlUsr.listarOrganizadores() != null) && controlUsr.listarOrganizadores().containsKey(usuarioSeleccionado)) {
                	// En caso de que sea organizador es similar la lógica, pero obtenemos otros campos
                	DTDatosUsuario datos = null;
					try {
						datos = controlUsr.obtenerDatosUsuario(usuarioSeleccionado);
					} catch (UsuarioNoExisteException e1) {
						// TODO Auto-generated catch block
						// Este caso no pasa, ya que chequee antes que existiera en la lista
						e1.printStackTrace();
					}


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

                    
                }
                // Mostramos el panel que tiene los datos
                cardLayout.show(getContentPane(), "DATOS");
                panelDatos.revalidate();
                panelDatos.repaint();

            }
        });

    }

    
    public void cargarUsuarios() throws UsuarioNoExisteException {
    	// Este método se usa para obtener los usuarios del sistema cada vez que ingresemos al caso de uso, sino solo lo obtiene al inicio y nunca más
    	DefaultComboBoxModel<String> model;
    	usuarios.clear();
		usuarios.addAll(controlUsr.listarUsuarios().keySet());
		model = new DefaultComboBoxModel<String>(usuarios);
		comboUsuarios.setModel(model);
    	}
}
