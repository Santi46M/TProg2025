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
    public ConsultaUsuario(IControladorUsuario icu, IControladorEvento iCE) {
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
                // En caso de cancelar, destruimos el frame para forzar su recreación
                dispose();
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

                JPanel panelDatosUsuario = new JPanel();
                panelDatosUsuario.setLayout(new BoxLayout(panelDatosUsuario, BoxLayout.Y_AXIS)); // <-- CAMBIO
                panelContenedor.add(panelDatosUsuario, BorderLayout.CENTER);



                JPanel panelInfoBasica = new JPanel(new GridLayout(0, 2, 10, 10)); // <-- CAMBIO, antes usabas setBounds
                panelDatosUsuario.add(panelInfoBasica);
                
                JLabel lblNick = new JLabel("Nickname:");
                lblNick.setBounds(101, 11, 94, 14);
                panelInfoBasica.add(lblNick);

                JTextArea txtNick = new JTextArea();
                txtNick.setBounds(248, 6, 161, 22);
                txtNick.setEditable(false);
                panelInfoBasica.add(txtNick);

                JLabel lblNombre = new JLabel("Nombre:");
                lblNombre.setBounds(101, 36, 94, 14);
                panelInfoBasica.add(lblNombre);

                JTextArea txtNombre = new JTextArea();
                txtNombre.setBounds(248, 31, 161, 22);
                txtNombre.setEditable(false);
                panelInfoBasica.add(txtNombre);

                JLabel lblCorreo = new JLabel("Correo electrónico:");
                lblCorreo.setBounds(101, 61, 137, 14);
                panelInfoBasica.add(lblCorreo);

                JTextArea txtCorreo = new JTextArea();
                txtCorreo.setBounds(248, 56, 161, 22);
                txtCorreo.setEditable(false);
                panelInfoBasica.add(txtCorreo);
                
                // Agregamos un boton para cancelar la sekeccion
                JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JButton btnCerrar = new JButton("Cerrar");
                btnCerrar.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
//                        usuarios.clear();
//                        setVisible(false);
                        // Volvemos al panel de seleccion
                        cardLayout.show(getContentPane(), "SELECCION");
                        setTitle("Listar Usuarios");
                    }
                });
                panelSur.add(btnCerrar);
                panelContenedor.add(panelSur, BorderLayout.SOUTH);
                System.out.println("llega 0");
                
                
                Map<String, Usuario> prueba = controlUsr.listarUsuarios();
                
                            	for (Map.Entry<String, Usuario> entry : prueba.entrySet()) {
                            	    String clave = entry.getKey();      // el nickname o id
                
                            	    System.out.println("Clave: " + clave);
                
                            	    System.out.println("--------------");
                            	}
                // Separamos en casos, si es asistente o es organizador
                if ((controlUsr.listarAsistentes() != null) && controlUsr.listarAsistentes().containsKey(usuarioSeleccionado)) {
                	System.out.println("llega 1");
                	DTDatosUsuario datos = null;
					try {
						datos = controlUsr.obtenerDatosUsuario(usuarioSeleccionado);
						System.out.println("llega 2");
					} catch (UsuarioNoExisteException e1) {
						// TODO Auto-generated catch block
						// Este caso no pasa, ya que chequee antes que existiera en la lista
						e1.printStackTrace();
					}
					System.out.println("llega 3");

					System.out.println("Nick: "+datos.getNickname());
					txtNick.setText(datos.getNickname());
					System.out.println("nombre: "+datos.getNombre());
                    txtNombre.setText(datos.getNombre());
                    System.out.println("email: "+datos.getEmail());
                    txtCorreo.setText(datos.getEmail());

                    
                    JPanel panelAsistente = new JPanel(new GridLayout(0, 2, 10, 10));

                    panelDatosUsuario.add(panelAsistente);

                    JLabel lblApellido = new JLabel("Apellido:");
                    lblApellido.setBounds(94, 11, 89, 14);
                    panelAsistente.add(lblApellido);

                    JTextArea txtApellido = new JTextArea(datos.getApellido());
                    txtApellido.setBounds(237, 6, 161, 22);
                    txtApellido.setEditable(false);
                    panelAsistente.add(txtApellido);

                    JLabel lblFechaNac = new JLabel("Fecha de nacimiento:");
                    lblFechaNac.setBounds(94, 36, 121, 14);
                    panelAsistente.add(lblFechaNac);

                    JTextArea txtFecha = new JTextArea(datos.getFechaNac().toString());
                    txtFecha.setBounds(237, 31, 161, 22);
                    txtFecha.setEditable(false);
                    panelAsistente.add(txtFecha);
                    
                 // Supongamos que ya obtuviste el Set<DTRegistro> registros

                    
                    // Defino las columnas que quiero mostrar
                    String[] columnNames = {"ID", "Edición", "Tipo", "Fecha Registro", "Costo", "Fecha Inicio"};

                    // Cargo los datos en una matriz de Object
                    Object[][] dataRegistro = new Object[datos.getRegistros().size()][columnNames.length];
                    int i = 0;
                    for (DTRegistro r : datos.getRegistros()) {
                    	dataRegistro[i][0] = r.getId();
                    	dataRegistro[i][1] = r.getEdicion();
                    	dataRegistro[i][2] = r.getTipoRegistro();
                    	dataRegistro[i][3] = r.getFechaRegistro();
                        dataRegistro[i][4] = r.getCosto();
                        dataRegistro[i][5] = r.getFechaInicio();
                        i++;
                    }

                    // Creo la JTable
                    JTable table = new JTable(dataRegistro, columnNames);
                    JScrollPane scrollPane = new JScrollPane(table);
                    panelDatosUsuario.add(scrollPane);
                    panelDatosUsuario.revalidate();
                    panelDatosUsuario.repaint();


                
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

//					JOptionPane.showMessageDialog(this,datos.getNickname() ,title, JOptionPane.INFORMATION_MESSAGE);
					System.out.println(datos.getNickname());
					txtNick.setText(datos.getNickname());
					System.out.println(datos.getNombre());
                    txtNombre.setText(datos.getNombre());
                    System.out.println(datos.getEmail());
                    txtCorreo.setText(datos.getEmail());


                    JPanel panelOrganizador = new JPanel(new GridLayout(0, 2, 10, 10)); 
                    panelDatosUsuario.add(panelOrganizador);

                    JLabel lblDesc = new JLabel("Descripción:");
                    lblDesc.setBounds(93, 11, 89, 14);
                    panelOrganizador.add(lblDesc);

                    JTextArea txtDesc = new JTextArea(datos.getDesc());
                    txtDesc.setBounds(238, 6, 161, 22);
                    txtDesc.setEditable(false);
                    panelOrganizador.add(txtDesc);

                    JLabel lblLink = new JLabel("Link:");
                    lblLink.setBounds(93, 36, 121, 14);
                    panelOrganizador.add(lblLink);
                    
                    JTextArea txtLink = new JTextArea(datos.getLink());
                    txtLink.setBounds(237, 31, 161, 22);
                    txtLink.setEditable(false);
                    panelOrganizador.add(txtLink);
                    

                    String[] columnNames = {"Nombre", "Sigla", "Fecha Inicio", "Fecha Fin", "Fecha Alta", "Ciudad", "País"};

                    Object[][] datosEdicion = new Object[datos.getEdiciones().size()][columnNames.length];
                    int i = 0;
                    for (DTEdicion ed : datos.getEdiciones()) {
                    	System.out.println("Entra" + ed.getNombre());
                    	datosEdicion[i][0] = ed.getNombre();
                    	datosEdicion[i][1] = ed.getSigla();
                    	datosEdicion[i][2] = ed.getFechaInicio();
                    	datosEdicion[i][3] = ed.getFechaFin();
                    	datosEdicion[i][4] = ed.getFechaAlta();
                    	datosEdicion[i][5] = ed.getCiudad();
                    	datosEdicion[i][6] = ed.getPais();
                        i++;
                    }

                    JTable table = new JTable(datosEdicion, columnNames);
                    JScrollPane scrollPane = new JScrollPane(table);
                    panelDatosUsuario.add(scrollPane);
                    panelDatosUsuario.revalidate();
                    panelDatosUsuario.repaint();


                    
                } else {
                    // Obtenemos el usuario desde el controlador
                    Usuario usuario = controlUsr.listarUsuarios().get(usuarioSeleccionado);
                    JPanel panelContenedor1 = new JPanel(new BorderLayout());
                    panelDatos.add(panelContenedor1, BorderLayout.CENTER);
                    JTextArea txtDatos = new JTextArea(6, 40);
                    txtDatos.setEditable(false);
                    panelContenedor1.add(new JScrollPane(txtDatos), BorderLayout.NORTH);
                    DefaultListModel<String> listModel = new DefaultListModel<>();
                    JList<String> listExtra = new JList<>(listModel);
                    JScrollPane scrollExtra = new JScrollPane(listExtra);
                    panelContenedor1.add(scrollExtra, BorderLayout.CENTER);
                    JButton btnVerDetalle = new JButton("Ver Detalle");
                    panelContenedor1.add(btnVerDetalle, BorderLayout.SOUTH);
                    JLabel lblExtra = new JLabel();
                    panelContenedor1.add(lblExtra, BorderLayout.WEST);
                    // Mostrar datos básicos
                    txtDatos.setText(usuario.toString());
                    // Si es asistente, mostrar registros
                    if (usuario instanceof Asistente) {
                        lblExtra.setText("Registros a ediciones:");
                        Asistente asistente = (Asistente) usuario;
                        java.util.Map<String, Registro> registros = asistente.getRegistros();
                        java.util.List<Registro> listaRegistros = new java.util.ArrayList<>(registros.values());
                        for (Registro reg : listaRegistros) {
                            listModel.addElement(reg.getId() + " - " + reg.getEdicion().getNombre());
                        }
                        btnVerDetalle.setEnabled(listModel.size() > 0);
                        listExtra.addListSelectionListener(ev2 -> {
                            int idx = listExtra.getSelectedIndex();
                            if (idx < 0) return;
                            Registro reg = listaRegistros.get(idx);
                            StringBuilder sb = new StringBuilder();
                            sb.append("Registro ID: ").append(reg.getId()).append("\n");
                            sb.append("Evento: ").append(reg.getEdicion().getEvento().getNombre()).append("\n");
                            sb.append("Edición: ").append(reg.getEdicion().getNombre()).append("\n");
                            sb.append("Tipo de Registro: ").append(reg.getTipoRegistro().getNombre()).append("\n");
                            sb.append("Fecha: ").append(reg.getFechaInicio()).append("\n");
                            sb.append("Costo: ").append(reg.getTipoRegistro().getCosto()).append("\n");
                            sb.append("Cupo: ").append(reg.getTipoRegistro().getCupo()).append("\n");
                            txtDatos.setText(sb.toString());
                        });
                        btnVerDetalle.addActionListener(ev2 -> {
                            int idx = listExtra.getSelectedIndex();
                            if (idx < 0) return;
                            Registro reg = listaRegistros.get(idx);
                            Ediciones ed = reg.getEdicion();
                            StringBuilder sb = new StringBuilder();
                            sb.append("Edición: ").append(ed.getNombre()).append("\n");
                            sb.append("Sigla: ").append(ed.getSigla()).append("\n");
                            sb.append("Fechas: ").append(ed.getFechaInicio()).append(" a ").append(ed.getFechaFin()).append("\n");
                            sb.append("Fecha alta: ").append(ed.getFechaAlta()).append("\n");
                            sb.append("Ciudad: ").append(ed.getCiudad()).append("\n");
                            sb.append("País: ").append(ed.getPais()).append("\n");
                            sb.append("Organizador: ").append(ed.getOrganizador() != null ? ed.getOrganizador().getNickname() : "").append("\n");
                            txtDatos.setText(sb.toString());
                        });
                    } else {
                        // En caso de que sea organizador es similar la lógica, pero obtenemos otros campos
                    	DTDatosUsuario datos = null;
    					try {
    						datos = controlUsr.obtenerDatosUsuario(usuarioSeleccionado);
    					} catch (UsuarioNoExisteException e1) {
    						// TODO Auto-generated catch block
    						// Este caso no pasa, ya que chequee antes que existiera en la lista
    						e1.printStackTrace();
    					}

//    					JOptionPane.showMessageDialog(this,datos.getNickname() ,title, JOptionPane.INFORMATION_MESSAGE);
    					System.out.println(datos.getNickname());
    					txtNick.setText(datos.getNickname());
    					System.out.println(datos.getNombre());
                        txtNombre.setText(datos.getNombre());
                        System.out.println(datos.getEmail());
                        txtCorreo.setText(datos.getEmail());


                        JPanel panelOrganizador = new JPanel(new GridLayout(0, 2, 10, 10)); 
                        panelDatosUsuario.add(panelOrganizador);

                        JLabel lblDesc = new JLabel("Descripción:");
                        lblDesc.setBounds(93, 11, 89, 14);
                        panelOrganizador.add(lblDesc);

                        JTextArea txtDesc = new JTextArea(datos.getDesc());
                        txtDesc.setBounds(238, 6, 161, 22);
                        txtDesc.setEditable(false);
                        panelOrganizador.add(txtDesc);

                        JLabel lblLink = new JLabel("Link:");
                        lblLink.setBounds(93, 36, 121, 14);
                        panelOrganizador.add(lblLink);
                        
                        JTextArea txtLink = new JTextArea(datos.getLink());
                        txtLink.setBounds(237, 31, 161, 22);
                        txtLink.setEditable(false);
                        panelOrganizador.add(txtLink);
                        

                        String[] columnNames = {"Nombre", "Sigla", "Fecha Inicio", "Fecha Fin", "Fecha Alta", "Ciudad", "País"};

                        Object[][] datosEdicion = new Object[datos.getEdiciones().size()][columnNames.length];
                        int i = 0;
                        for (DTEdicion ed : datos.getEdiciones()) {
                        	System.out.println("Entra" + ed.getNombre());
                        	datosEdicion[i][0] = ed.getNombre();
                        	datosEdicion[i][1] = ed.getSigla();
                        	datosEdicion[i][2] = ed.getFechaInicio();
                        	datosEdicion[i][3] = ed.getFechaFin();
                        	datosEdicion[i][4] = ed.getFechaAlta();
                        	datosEdicion[i][5] = ed.getCiudad();
                        	datosEdicion[i][6] = ed.getPais();
                            i++;
                        }

                        JTable table = new JTable(datosEdicion, columnNames);
                        JScrollPane scrollPane = new JScrollPane(table);
                        panelDatosUsuario.add(scrollPane);
                        panelDatosUsuario.revalidate();
                        panelDatosUsuario.repaint();
                    }
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