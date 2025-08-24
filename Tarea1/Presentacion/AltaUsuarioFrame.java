package Presentacion

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JDesktopPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JInternalFrame;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;

public class Eventos extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Eventos frame = new Eventos();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Eventos() {
		setTitle("Eventos.uy");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 803, 563);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Sistema");
		menuBar.add(mnNewMenu);
		
		JMenu mnNewMenu_1 = new JMenu("Usuario");
		menuBar.add(mnNewMenu_1);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Alta Usuario");
		mnNewMenu_1.add(mntmNewMenuItem);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Ver Informacion");
		mnNewMenu_1.add(mntmNewMenuItem_1);
		
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("ListarUsuario");
		mnNewMenu_1.add(mntmNewMenuItem_2);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JDesktopPane desktopPane = new JDesktopPane();
		contentPane.add(desktopPane);
		
		JInternalFrame internalFrame = new JInternalFrame("Alta de Usuario");
		internalFrame.setIconifiable(true);
		internalFrame.setMaximizable(true);
		internalFrame.setResizable(true);
		internalFrame.setNormalBounds(new Rectangle(0, 0, 30, 30));
		internalFrame.setClosable(true);
		contentPane.add(internalFrame);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		internalFrame.getContentPane().setLayout(gridBagLayout);
		
		JLabel lblNewLabel = new JLabel("Nick:");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		internalFrame.getContentPane().add(lblNewLabel, gbc_lblNewLabel);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		internalFrame.getContentPane().add(textField, gbc_textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Nombre:");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 1;
		internalFrame.getContentPane().add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		textField_1 = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 0);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 1;
		internalFrame.getContentPane().add(textField_1, gbc_textField_1);
		textField_1.setColumns(10);
		internalFrame.setVisible(true);
		
		JLabel lblNewLabel_2 = new JLabel("Correo");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 2;
		internalFrame.getContentPane().add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.insets = new Insets(0, 0, 5, 0);
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.gridx = 1;
		gbc_textField_2.gridy = 2;
		internalFrame.getContentPane().add(textField, gbc_textField_2);
		textField.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("Tipo de Usuario");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel_3.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_3.gridx = 0;
		gbc_lblNewLabel_3.gridy = 3;
		internalFrame.getContentPane().add(lblNewLabel_3, gbc_lblNewLabel_3);
		
		JPanel panelRadio = new JPanel(new GridLayout(1, 2, 5, 0)); // 1 fila, 2 columnas, 5px separación horizontal
		JRadioButton rbtnOrganizador = new JRadioButton("Organizador");
		JRadioButton rbtnAsistente = new JRadioButton("Asistente");

		// Agrupar los radio buttons
		ButtonGroup grupoTipoUsuario = new ButtonGroup();
		grupoTipoUsuario.add(rbtnOrganizador);
		grupoTipoUsuario.add(rbtnAsistente);

		// Agregar los botones al panel
		panelRadio.add(rbtnOrganizador);
		panelRadio.add(rbtnAsistente);

		// GridBagConstraints para colocar el panel en la columna 1, fila 3
		GridBagConstraints gbc_panelRadio = new GridBagConstraints();
		gbc_panelRadio.gridx = 1;
		gbc_panelRadio.gridy = 3;
		gbc_panelRadio.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelRadio.insets = new Insets(0, 0, 5, 0);
		internalFrame.getContentPane().add(panelRadio, gbc_panelRadio);
		
		JButton btnAceptar = new JButton("Aceptar");
		GridBagConstraints gbc_btnAceptar = new GridBagConstraints();
		gbc_btnAceptar.gridx = 1; // columna
		gbc_btnAceptar.gridy = 4; // fila debajo de los campos
		gbc_btnAceptar.insets = new Insets(10, 5, 5, 5);
		gbc_btnAceptar.anchor = GridBagConstraints.EAST;
		internalFrame.getContentPane().add(btnAceptar, gbc_btnAceptar);

		// Botón Cancelar
		JButton btnCancelar = new JButton("Cancelar");
		GridBagConstraints gbc_btnCancelar = new GridBagConstraints();
		gbc_btnCancelar.gridx = 2; // columna siguiente
		gbc_btnCancelar.gridy = 4; // misma fila
		gbc_btnCancelar.insets = new Insets(10, 5, 5, 0);
		gbc_btnCancelar.anchor = GridBagConstraints.WEST;
		internalFrame.getContentPane().add(btnCancelar, gbc_btnCancelar);

		// Acciones
		btnAceptar.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        javax.swing.JOptionPane.showMessageDialog(internalFrame, "Usuario dado de alta correctamente");
		    }
		});

		btnCancelar.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        internalFrame.dispose();
		    }
		    
		    
		});
		
		btnAceptar.addActionListener(e -> {
		    String nick = lblNewLabel.getText().trim();
		    String nombre = lblNewLabel_1.getText().trim();
		    String correo = lblNewLabel_2.getText().trim();
		    String tipo = null;

		    if (rbtnOrganizador.isSelected()) {
		        tipo = "Organizador";
		    } else if (rbtnAsistente.isSelected()) {
		        tipo = "Asistente";
		    }

		    // Validaciones
		    if (nick.isEmpty() || nombre.isEmpty() || correo.isEmpty()) {
		        JOptionPane.showMessageDialog(null,
		            "Todos los campos deben estar completos.",
		            "Error de validación",
		            JOptionPane.ERROR_MESSAGE);
		        return; // Sale sin continuar
		    }

		    if (tipo == null || tipo.equals("Seleccionar...")) {
		        JOptionPane.showMessageDialog(null,
		            "Debe seleccionar un tipo de usuario.",
		            "Error de validación",
		            JOptionPane.ERROR_MESSAGE);
		        return;
		    }

		    // Si todo está bien
		    JOptionPane.showMessageDialog(null,
		        "Usuario registrado con éxito:\n" +
		        "Nick: " + nick + "\nNombre: " + nombre + "\nCorreo: " + correo + "\nTipo: " + tipo,
		        "Éxito",
		        JOptionPane.INFORMATION_MESSAGE);
		});

}}