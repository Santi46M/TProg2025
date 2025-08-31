package presentacion;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AltaEdicionEvento extends JInternalFrame {
	private JComboBox<String> comboEvento;
	private JComboBox<String> comboOrganizador;
	private logica.IControladorUsuario controladorUsuario;
	
    public AltaEdicionEvento(logica.IControladorUsuario controladorUsuario) {
		super("Alta de Edición de Evento", true, true, true, true);
		this.controladorUsuario = controladorUsuario;
		setBounds(new Rectangle(60, 60, 550, 400));
		setVisible(true);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[3];
		gridBagLayout.rowHeights = new int[10];
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);

		JLabel lblEvento = new JLabel("Evento:");
		GridBagConstraints gbc_lblEvento = new GridBagConstraints();
		gbc_lblEvento.insets = new Insets(0, 0, 5, 5);
		gbc_lblEvento.anchor = GridBagConstraints.WEST;
		gbc_lblEvento.gridx = 0;
		gbc_lblEvento.gridy = 0;
		getContentPane().add(lblEvento, gbc_lblEvento);

		comboEvento = new JComboBox<>();
		GridBagConstraints gbc_comboEvento = new GridBagConstraints();
		gbc_comboEvento.insets = new Insets(0, 0, 5, 0);
		gbc_comboEvento.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboEvento.gridx = 1;
		gbc_comboEvento.gridy = 0;
		getContentPane().add(comboEvento, gbc_comboEvento);

		JLabel lblOrganizador = new JLabel("Organizador:");
		GridBagConstraints gbc_lblOrganizador = new GridBagConstraints();
		gbc_lblOrganizador.insets = new Insets(0, 0, 5, 5);
		gbc_lblOrganizador.anchor = GridBagConstraints.WEST;
		gbc_lblOrganizador.gridx = 0;
		gbc_lblOrganizador.gridy = 1;
		getContentPane().add(lblOrganizador, gbc_lblOrganizador);

		comboOrganizador = new JComboBox<>();
		GridBagConstraints gbc_comboOrganizador = new GridBagConstraints();
		gbc_comboOrganizador.insets = new Insets(0, 0, 5, 0);
		gbc_comboOrganizador.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboOrganizador.gridx = 1;
		gbc_comboOrganizador.gridy = 1;
		getContentPane().add(comboOrganizador, gbc_comboOrganizador);

		JLabel lblNombre = new JLabel("Nombre de Edición:");
		GridBagConstraints gbc_lblNombre = new GridBagConstraints();
		gbc_lblNombre.insets = new Insets(0, 0, 5, 5);
		gbc_lblNombre.anchor = GridBagConstraints.WEST;
		gbc_lblNombre.gridx = 0;
		gbc_lblNombre.gridy = 2;
		getContentPane().add(lblNombre, gbc_lblNombre);

		JTextField txtNombre = new JTextField();
		GridBagConstraints gbc_txtNombre = new GridBagConstraints();
		gbc_txtNombre.insets = new Insets(0, 0, 5, 0);
		gbc_txtNombre.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtNombre.gridx = 1;
		gbc_txtNombre.gridy = 2;
		getContentPane().add(txtNombre, gbc_txtNombre);
		txtNombre.setColumns(15);

		JLabel lblSigla = new JLabel("Sigla:");
		GridBagConstraints gbc_lblSigla = new GridBagConstraints();
		gbc_lblSigla.insets = new Insets(0, 0, 5, 5);
		gbc_lblSigla.anchor = GridBagConstraints.WEST;
		gbc_lblSigla.gridx = 0;
		gbc_lblSigla.gridy = 3;
		getContentPane().add(lblSigla, gbc_lblSigla);

		JTextField txtSigla = new JTextField();
		GridBagConstraints gbc_txtSigla = new GridBagConstraints();
		gbc_txtSigla.insets = new Insets(0, 0, 5, 0);
		gbc_txtSigla.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSigla.gridx = 1;
		gbc_txtSigla.gridy = 3;
		getContentPane().add(txtSigla, gbc_txtSigla);
		txtSigla.setColumns(10);

		JLabel lblCiudad = new JLabel("Ciudad:");
		GridBagConstraints gbc_lblCiudad = new GridBagConstraints();
		gbc_lblCiudad.insets = new Insets(0, 0, 5, 5);
		gbc_lblCiudad.anchor = GridBagConstraints.WEST;
		gbc_lblCiudad.gridx = 0;
		gbc_lblCiudad.gridy = 4;
		getContentPane().add(lblCiudad, gbc_lblCiudad);

		JTextField txtCiudad = new JTextField();
		GridBagConstraints gbc_txtCiudad = new GridBagConstraints();
		gbc_txtCiudad.insets = new Insets(0, 0, 5, 0);
		gbc_txtCiudad.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtCiudad.gridx = 1;
		gbc_txtCiudad.gridy = 4;
		getContentPane().add(txtCiudad, gbc_txtCiudad);
		txtCiudad.setColumns(10);

		JLabel lblPais = new JLabel("País:");
		GridBagConstraints gbc_lblPais = new GridBagConstraints();
		gbc_lblPais.insets = new Insets(0, 0, 5, 5);
		gbc_lblPais.anchor = GridBagConstraints.WEST;
		gbc_lblPais.gridx = 0;
		gbc_lblPais.gridy = 5;
		getContentPane().add(lblPais, gbc_lblPais);

		JTextField txtPais = new JTextField();
		GridBagConstraints gbc_txtPais = new GridBagConstraints();
		gbc_txtPais.insets = new Insets(0, 0, 5, 0);
		gbc_txtPais.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPais.gridx = 1;
		gbc_txtPais.gridy = 5;
		getContentPane().add(txtPais, gbc_txtPais);
		txtPais.setColumns(10);

		JLabel lblFechaInicio = new JLabel("Fecha de inicio (YYYY-MM-DD):");
		GridBagConstraints gbc_lblFechaInicio = new GridBagConstraints();
		gbc_lblFechaInicio.insets = new Insets(0, 0, 5, 5);
		gbc_lblFechaInicio.anchor = GridBagConstraints.WEST;
		gbc_lblFechaInicio.gridx = 0;
		gbc_lblFechaInicio.gridy = 6;
		getContentPane().add(lblFechaInicio, gbc_lblFechaInicio);

		JTextField txtFechaInicio = new JTextField();
		GridBagConstraints gbc_txtFechaInicio = new GridBagConstraints();
		gbc_txtFechaInicio.insets = new Insets(0, 0, 5, 0);
		gbc_txtFechaInicio.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFechaInicio.gridx = 1;
		gbc_txtFechaInicio.gridy = 6;
		getContentPane().add(txtFechaInicio, gbc_txtFechaInicio);
		txtFechaInicio.setColumns(10);

		JLabel lblFechaFin = new JLabel("Fecha de fin (YYYY-MM-DD):");
		GridBagConstraints gbc_lblFechaFin = new GridBagConstraints();
		gbc_lblFechaFin.insets = new Insets(0, 0, 5, 5);
		gbc_lblFechaFin.anchor = GridBagConstraints.WEST;
		gbc_lblFechaFin.gridx = 0;
		gbc_lblFechaFin.gridy = 7;
		getContentPane().add(lblFechaFin, gbc_lblFechaFin);

		JTextField txtFechaFin = new JTextField();
		GridBagConstraints gbc_txtFechaFin = new GridBagConstraints();
		gbc_txtFechaFin.insets = new Insets(0, 0, 5, 0);
		gbc_txtFechaFin.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFechaFin.gridx = 1;
		gbc_txtFechaFin.gridy = 7;
		getContentPane().add(txtFechaFin, gbc_txtFechaFin);
		txtFechaFin.setColumns(10);

		JLabel lblFechaAlta = new JLabel("Fecha de alta (YYYY-MM-DD):");
		GridBagConstraints gbc_lblFechaAlta = new GridBagConstraints();
		gbc_lblFechaAlta.insets = new Insets(0, 0, 0, 5);
		gbc_lblFechaAlta.anchor = GridBagConstraints.WEST;
		gbc_lblFechaAlta.gridx = 0;
		gbc_lblFechaAlta.gridy = 8;
		getContentPane().add(lblFechaAlta, gbc_lblFechaAlta);

		JTextField txtFechaAlta = new JTextField();
		GridBagConstraints gbc_txtFechaAlta = new GridBagConstraints();
		gbc_txtFechaAlta.insets = new Insets(0, 0, 0, 0);
		gbc_txtFechaAlta.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFechaAlta.gridx = 1;
		gbc_txtFechaAlta.gridy = 8;
		getContentPane().add(txtFechaAlta, gbc_txtFechaAlta);
		txtFechaAlta.setColumns(10);

		JButton btnAceptar = new JButton("Aceptar");
		GridBagConstraints gbc_btnAceptar = new GridBagConstraints();
		gbc_btnAceptar.gridx = 1;
		gbc_btnAceptar.gridy = 9;
		gbc_btnAceptar.insets = new Insets(10, 5, 5, 5);
		gbc_btnAceptar.anchor = GridBagConstraints.WEST;
		getContentPane().add(btnAceptar, gbc_btnAceptar);

		JButton btnCancelar = new JButton("Cancelar");
		GridBagConstraints gbc_btnCancelar = new GridBagConstraints();
		gbc_btnCancelar.gridx = 2;
		gbc_btnCancelar.gridy = 9;
		gbc_btnCancelar.insets = new Insets(10, 5, 5, 0);
		gbc_btnCancelar.anchor = GridBagConstraints.EAST;
		getContentPane().add(btnCancelar, gbc_btnCancelar);

		btnAceptar.addActionListener(ev -> {
            String eventoNombre = (String) comboEvento.getSelectedItem();
            String organizadorNick = (String) comboOrganizador.getSelectedItem();
            String nombre = txtNombre.getText().trim();
            String sigla = txtSigla.getText().trim();
            String ciudad = txtCiudad.getText().trim();
            String pais = txtPais.getText().trim();
            String fechaInicio = txtFechaInicio.getText().trim();
            String fechaFin = txtFechaFin.getText().trim();
            String fechaAlta = txtFechaAlta.getText().trim();
            if (eventoNombre == null || organizadorNick == null || nombre.isEmpty() || sigla.isEmpty() || ciudad.isEmpty() || pais.isEmpty() || fechaInicio.isEmpty() || fechaFin.isEmpty() || fechaAlta.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos deben estar completos.");
                return;
            }
            try {
                DateTimeFormatter flexibleFormatter = DateTimeFormatter.ofPattern("yyyy-M-d");
                LocalDate fInicio = LocalDate.parse(fechaInicio, flexibleFormatter);
                LocalDate fFin = LocalDate.parse(fechaFin, flexibleFormatter);
                LocalDate fAlta = LocalDate.parse(fechaAlta, flexibleFormatter);
                logica.ControladorEvento controladorEvento = new logica.ControladorEvento();
                logica.ManejadorEvento manejadorEvento = logica.ManejadorEvento.getInstancia();
                logica.Eventos evento = manejadorEvento.obtenerEvento(eventoNombre);
                logica.Organizador organizador = null;
                if (controladorUsuario != null) {
                    java.util.Map<String, logica.Organizador> orgs = controladorUsuario.listarOrganizadores();
                    organizador = orgs.get(organizadorNick);
                }
                if (evento == null || organizador == null) {
                    JOptionPane.showMessageDialog(this, "No se pudo encontrar el evento u organizador seleccionado.");
                    return;
                }
                controladorEvento.AltaEdicionEvento(evento, organizador, nombre, sigla, "", fInicio, fFin, fAlta, ciudad, pais);
                JOptionPane.showMessageDialog(this, "Edición registrada con éxito.");
                this.dispose();
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use AAAA-MM-DD, por ejemplo: 2000-02-02 o 2000-2-2");
            } catch (excepciones.EdicionYaExisteException ex) {
                JOptionPane.showMessageDialog(this, "Ya existe una edición con ese nombre.");
            } catch (excepciones.EventoYaExisteException ex) {
                JOptionPane.showMessageDialog(this, "El evento no existe.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al registrar la edición: " + ex.getMessage());
            }
        });

        btnCancelar.addActionListener(ev -> this.dispose());
    }

	public void cargarEventos() {
		try {
			logica.ControladorEvento controlador = new logica.ControladorEvento();
			java.util.List<logica.DTEvento> eventos = controlador.listarEventos();
			comboEvento.removeAllItems();
			for (logica.DTEvento ev : eventos) {
				comboEvento.addItem(ev.getNombre());
			}
			if (comboEvento.getItemCount() > 0) {
				comboEvento.setSelectedIndex(0);
			}
		} catch (Exception ex) {
			comboEvento.setModel(new DefaultComboBoxModel<>(new String[]{"No hay eventos"}));
		}
	}

	public void cargarOrganizadores() {
		try {
			comboOrganizador.removeAllItems();
			if (controladorUsuario != null) {
				java.util.Map<String, logica.Organizador> orgs = controladorUsuario.listarOrganizadores();
				for (String nick : orgs.keySet()) {
					comboOrganizador.addItem(nick);
				}
			}
			if (comboOrganizador.getItemCount() > 0) {
				comboOrganizador.setSelectedIndex(0);
			}
		} catch (Exception ex) {
			comboOrganizador.setModel(new DefaultComboBoxModel<>(new String[]{"No hay organizadores"}));
		}
	}
}