package presentacion;

import javax.swing.*;

import com.toedter.calendar.JDateChooser;

import excepciones.UsuarioNoExisteException;

import java.util.*;
import logica.Interfaces.*;
import logica.*;
import java.util.Map;
import java.util.Vector;
import java.time.LocalDate;
import java.time.*;
import java.awt.*;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AltaUsuarioFrame extends JInternalFrame {
	private IControladorUsuario controlUsr;
	private JPanel panelAsistente;
	private JPanel panelOrganizador;
	private Vector<String> instituciones;
	private JComboBox<String> comboInstitucion;

	
	private JTextField textField;
    private JTextField textField_1;
    private JTextField textField_2;
    private JRadioButton rdbtnAsistente;
    private JRadioButton rdbtnOrganizador;
    private ButtonGroup grupoRol; // Para poder habilitar uno solo
    

    // Campos adicionales para Asistente
    private JTextField txtApellido;
 // private JTextField txtFechaNacimiento;
    private JDateChooser txtFechaNacimiento;

    // Campos adicionales para Organizador
    private JTextField txtDescripcion;
    private JTextField txtWeb;

    public AltaUsuarioFrame(IControladorUsuario icu, IControladorEvento iCE) {
        controlUsr = icu;
    	
    	// Ventana alta de usuario
    	setTitle("Alta de usuario");
        setResizable(true);
        setMaximizable(true);
        setIconifiable(true);
        setClosable(true);
        setBounds(100, 100, 500, 400);
        getContentPane().setLayout(null);

        // Campo nickname
        JLabel lblNickname = new JLabel("Nickname: ");
        lblNickname.setHorizontalAlignment(SwingConstants.LEFT);
        lblNickname.setBounds(34, 11, 120, 14);
        getContentPane().add(lblNickname);

        textField = new JTextField();
        textField.setBounds(160, 8, 206, 20);
        getContentPane().add(textField);

        
        //Campo nombre
        JLabel lblNombre = new JLabel("Nombre: ");
        lblNombre.setBounds(34, 59, 120, 14);
        getContentPane().add(lblNombre);

        textField_1 = new JTextField();
        textField_1.setBounds(160, 56, 206, 20);
        getContentPane().add(textField_1);

        
        //Campo correo
        JLabel lblCorreo = new JLabel("Correo electrónico: ");
        lblCorreo.setBounds(34, 108, 120, 14);
        getContentPane().add(lblCorreo);

        textField_2 = new JTextField();
        textField_2.setBounds(160, 105, 206, 20);
        getContentPane().add(textField_2);

        // Check de Asistente
        rdbtnAsistente = new JRadioButton("Asistente");
        rdbtnAsistente.setBounds(92, 154, 109, 23);
        getContentPane().add(rdbtnAsistente);

        //Check de Organizador
        rdbtnOrganizador = new JRadioButton("Organizador");
        rdbtnOrganizador.setBounds(236, 154, 109, 23);
        getContentPane().add(rdbtnOrganizador);

        // Los agrupamos para que solo se pueda seleccionar uno
        grupoRol = new ButtonGroup();
        grupoRol.add(rdbtnAsistente);
        grupoRol.add(rdbtnOrganizador);
        
        
        //Creamos un panel para asistente, en este irán los campos de apellido, fecha de nac e instituxion
        panelAsistente = new JPanel();
        panelAsistente.setLayout(null);
        panelAsistente.setBounds(20, 190, 450, 100);

        JLabel lblApellido = new JLabel("Apellido:");
        lblApellido.setBounds(10, 10, 100, 20);
        panelAsistente.add(lblApellido);

        txtApellido = new JTextField();
        txtApellido.setBounds(120, 10, 200, 20);
        panelAsistente.add(txtApellido);

        JLabel lblFechaNac = new JLabel("Fecha Nacimiento:");
        lblFechaNac.setBounds(10, 40, 120, 20);
        panelAsistente.add(lblFechaNac);

        txtFechaNacimiento = new JDateChooser();
        txtFechaNacimiento.setBounds(140, 40, 180, 20);
        panelAsistente.add(txtFechaNacimiento);
        
        JLabel lblInstitucion = new JLabel("Institución:");
        lblInstitucion.setBounds(10, 70, 100, 20);
        panelAsistente.add(lblInstitucion);

        
        //Forzadas
        
        instituciones = new Vector<>();
        //Agrego una opcion para que no sea necessario que tenga que elegir una institucion
        instituciones.add("Ninguna");
        instituciones.addAll(controlUsr.getInstituciones());
        comboInstitucion = new JComboBox<>(instituciones);
        comboInstitucion.setBounds(120, 70, 200, 20);
        panelAsistente.add(comboInstitucion);

        getContentPane().add(panelAsistente);
        panelAsistente.setVisible(false);

      //Creamos un panel para organizador, en este irán los campos de descripcion y link
        panelOrganizador = new JPanel();
        panelOrganizador.setLayout(null);
        panelOrganizador.setBounds(20, 190, 450, 80);

        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setBounds(10, 10, 100, 20);
        panelOrganizador.add(lblDescripcion);

        txtDescripcion = new JTextField();
        txtDescripcion.setBounds(120, 10, 200, 20);
        panelOrganizador.add(txtDescripcion);

        JLabel lblWeb = new JLabel("Sitio Web:");
        lblWeb.setBounds(10, 40, 100, 20);
        panelOrganizador.add(lblWeb);

        txtWeb = new JTextField();
        txtWeb.setBounds(120, 40, 200, 20);
        panelOrganizador.add(txtWeb);

        getContentPane().add(panelOrganizador);
        panelOrganizador.setVisible(false);

        //Deshabilitamos para la segunda vuelta
        toggleCamposAdicionales(false, false);
        
        //En caso de seleccionar asistente, se habilita los campos de asistente
        rdbtnAsistente.addActionListener(e -> {
            panelAsistente.setVisible(true);
            panelOrganizador.setVisible(false);
            toggleCamposAdicionales(true, false);
        });

      //En caso de seleccionar organizador, se habilita los campos de organizador
        rdbtnOrganizador.addActionListener(e -> {
            panelAsistente.setVisible(false);
            panelOrganizador.setVisible(true);
            toggleCamposAdicionales(false, true);
        });

        // Botón Aceptar
        JButton btnAceptar = new JButton("Aceptar");
       // btnAceptar.setBackground(Color.GREEN);
        btnAceptar.setBounds(92, 320, 100, 25);
        getContentPane().add(btnAceptar);
        btnAceptar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cmdRegistroUsuarioActionPerformed(e);
            }
        });

        // Botón Cancelar
        JButton btnCancelar = new JButton("Cancelar");
     //   btnCancelar.setBackground(Color.RED);
        btnCancelar.setBounds(236, 320, 100, 25);
        getContentPane().add(btnCancelar);
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                limpiarFormulario();
                setVisible(false);
            }
        });
    }

    // Cada vez que el frame se hace visible limpiamos el formulario para asegurar estado inicial
    @Override
    public void setVisible(boolean aFlag) {
        if (aFlag) {
            limpiarFormulario();
        }
        super.setVisible(aFlag);
    }
    
    private void toggleCamposAdicionales(boolean asistente, boolean organizador) {
        // Asistente
        txtApellido.setEnabled(asistente);
        txtFechaNacimiento.setEnabled(asistente);

        // Organizador
        txtDescripcion.setEnabled(organizador);
        txtWeb.setEnabled(organizador);
    }

    private boolean checkFormulario() {
        String nickname = this.textField.getText();
        String nombre = this.textField_1.getText();
        String correo = this.textField_2.getText();

        if (nickname.isEmpty() || nombre.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No puede haber campos vacíos", "Registrar Usuario",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!rdbtnAsistente.isSelected() && !rdbtnOrganizador.isSelected()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un rol (Asistente u Organizador)", "Registrar Usuario",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validación extra de campos según rol
        if (rdbtnAsistente.isSelected()) {
            if (txtApellido.getText().isEmpty() || txtFechaNacimiento.getDate()==null) {
                JOptionPane.showMessageDialog(this, "Debe completar apellido y fecha de nacimiento", "Registrar Usuario",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } else if (rdbtnOrganizador.isSelected()) {
            if (txtDescripcion.getText().isEmpty() || txtWeb.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe completar descripción y sitio web", "Registrar Usuario",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return true;
    }

    protected void cmdRegistroUsuarioActionPerformed(ActionEvent e) {
        if (checkFormulario()) {
            try {

                
            	if (rdbtnOrganizador.isSelected()) {
            		controlUsr.AltaUsuario(this.textField.getText(), this.textField_1.getText(), this.textField_2.getText(), txtDescripcion.getText(), txtWeb.getText(), null, null, null, true);
            	}else {
            	    // convertir Date -> LocalDate
            	    LocalDate fechaNac = txtFechaNacimiento.getDate()
            	                                           .toInstant()
            	                                           .atZone(ZoneId.systemDefault())
            	                                           .toLocalDate();
            		controlUsr.AltaUsuario(this.textField.getText(), this.textField_1.getText(), this.textField_2.getText(), "", "", txtApellido.getText(),fechaNac, null, false);
            	}
            	
//            	Map<String,Usuario> prueba = controlUsr.listarUsuarios();
//            	if(controlUsr.listarUsuarios().isEmpty()) {
//                	JOptionPane.showMessageDialog(this, "no hay nada",title, JOptionPane.INFORMATION_MESSAGE);
//            	}else {
//            		JOptionPane.showMessageDialog(this, "TIENE",title, JOptionPane.INFORMATION_MESSAGE);
//            	}
//            	Map<String, Usuario> prueba = controlUsr.listarUsuarios();
//
//            	for (Map.Entry<String, Usuario> entry : prueba.entrySet()) {
//            	    String clave = entry.getKey();      // el nickname o id
//
//            	    System.out.println("Clave: " + clave);
//
//            	    System.out.println("--------------");
//            	}

//            	
//            	for (Map.Entry<String, Usuario> entrada : prueba.entrySet()) {
//            	    String clave = entrada.getKey();
//            	    System.out.println("Clave: " + clave);
//            	}
            	JOptionPane.showMessageDialog(this, "El Usuario se ha creado con éxito", "Registrar Usuario",
                        JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                setVisible(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al registrar usuario: " + ex.getMessage(),
                        "Registrar Usuario", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

//    private void limpiarFormulario() {
//        textField.setText("");
//        textField_1.setText("");
//        textField_2.setText("");
//        rdbtnAsistente.setSelected(false);
//        rdbtnOrganizador.setSelected(false);
//        txtApellido.setText("");
//        txtFechaNacimiento.setDate(null);
//        txtDescripcion.setText("");
//        txtWeb.setText("");
//
//        toggleCamposAdicionales(false, false);
//    }
    
    private void limpiarFormulario() {
        textField.setText("");
        textField_1.setText("");
        textField_2.setText("");
        rdbtnAsistente.setSelected(false);
        rdbtnOrganizador.setSelected(false);
        txtApellido.setText("");
        txtFechaNacimiento.setDate(null);
        txtDescripcion.setText("");
        txtWeb.setText("");

        toggleCamposAdicionales(false, false);
        grupoRol.clearSelection();

//        // Ocultar paneles después de limpiar
//        for (Component comp : getContentPane().getComponents()) {
//            if (comp instanceof JPanel) {
//                comp.setVisible(false);
//            }
//        }
        
        panelAsistente.setVisible(false);
        panelOrganizador.setVisible(false);
    }
    
    public void cargarInstituciones() {
    	//Agregar bien la exception
    	DefaultComboBoxModel<String> model;
    	instituciones.clear();
    	instituciones.add("Ninguna");
    	instituciones.addAll(controlUsr.getInstituciones());
		model = new DefaultComboBoxModel<String>(instituciones);
		comboInstitucion.setModel(model);
    	}
}
