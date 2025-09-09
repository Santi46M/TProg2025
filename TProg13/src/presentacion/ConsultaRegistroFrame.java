package presentacion;

import javax.swing.*;

import excepciones.RegistroNoExiste;
import excepciones.UsuarioNoExisteException;
import logica.Datatypes.DTRegistro;
import logica.Interfaces.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ConsultaRegistroFrame extends JInternalFrame {
	// Definimos algunos paneles como atributos
	private JPanel panelSeleccion;
	private JComboBox<String> comboUsuarios;
	private JComboBox<String> comboRegistros;
	private Vector<String> usuarios;
	private IControladorUsuario controlUsr;
	private JTextArea txtDatos;

	private boolean cargando = false;
	private List<DTRegistro> registrosActuales = new ArrayList<>();

    public ConsultaRegistroFrame(IControladorUsuario icu, IControladorEvento iCE) {
        super("Consulta de Registro", true, true, true, true);
        setBounds(200, 200, 600, 360);
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

        txtDatos = new JTextArea(10, 48);
        txtDatos.setEditable(false);
        add(new JScrollPane(txtDatos), BorderLayout.CENTER);

        comboUsuarios.addActionListener(e -> {
            if (cargando) return;
            String nick = (String) comboUsuarios.getSelectedItem();
            if (nick == null) return;
            cargarRegistros(nick, null);
        });

        comboRegistros.addActionListener(e -> {
            if (cargando) return;
            String id = (String) comboRegistros.getSelectedItem();
            if (id == null) return;
            mostrarDetalle(id);
        });

        inicializarPreseleccion();
        setVisible(true);
    }

    private void inicializarPreseleccion() {
        cargando = true;

        // usuario consultado previamente
        String nickSel = controlUsr.getUsuarioSeleccionadoNickname();
        if (nickSel != null) {
            seleccionarItemPorTexto(comboUsuarios, nickSel);
        }

        String nickActual = (String) comboUsuarios.getSelectedItem();
        if (nickActual != null) {
            // registro seleccionado previamente
            String regSel = controlUsr.getRegistroSeleccionadoId();
            cargarRegistros(nickActual, regSel);
            if (regSel != null) {
                seleccionarItemPorPrefijo(comboRegistros, regSel); // items = "id"
                mostrarDetalle(regSel);
            }
        }

        cargando = false;
    }

    private void cargarRegistros(String nickname, String preseleccionId) {
        cargando = true;
        comboRegistros.removeAllItems();
        txtDatos.setText("");
        registrosActuales.clear();

        if (controlUsr.esAsistente(nickname)) {
            Set<DTRegistro> regs = controlUsr.obtenerRegistrosAsistente(controlUsr.listarAsistentes().get(nickname));
            // ordenar opcionalmente por fecha de registro
            List<DTRegistro> lista = new ArrayList<>(regs);
            // Collections.sort(lista, Comparator.comparing(DTRegistro::getFechaRegistro));
            registrosActuales = lista;

            for (DTRegistro r : lista) {
                comboRegistros.addItem(r.getId());
            }
            if (preseleccionId != null) {
                seleccionarItemPorTexto(comboRegistros, preseleccionId);
            }
        } else {
            // organizador no tiene registros
        }

        cargando = false;
    }

    private void mostrarDetalle(String idRegistro) {
        DTRegistro datos = controlUsr.obtenerDatosRegistros(idRegistro);
        if (datos == null) {
            txtDatos.setText("");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Identificador: ").append(datos.getId()).append("\n");
        sb.append("Usuario: ").append(datos.getUsuario()).append("\n");
        sb.append("Edición: ").append(datos.getEdicion()).append("\n");
        sb.append("Tipo de registro: ").append(datos.getTipoRegistro()).append("\n");
        sb.append("Fecha de registro: ").append(String.valueOf(datos.getFechaRegistro())).append("\n");
        sb.append("Costo: ").append(String.valueOf(datos.getCosto())).append("\n");
        sb.append("Fecha de inicio: ").append(String.valueOf(datos.getFechaInicio())).append("\n");
        txtDatos.setText(sb.toString());
    }

    private void seleccionarItemPorTexto(JComboBox<String> combo, String texto) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            String it = combo.getItemAt(i);
            if (it != null && it.equals(texto)) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void seleccionarItemPorPrefijo(JComboBox<String> combo, String prefijo) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            String it = combo.getItemAt(i);
            if (it != null && it.startsWith(prefijo)) {
                combo.setSelectedIndex(i);
                return;
            }
        }
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
