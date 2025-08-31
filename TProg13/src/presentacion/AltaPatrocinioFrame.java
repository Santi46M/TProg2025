package presentacion;

import javax.swing.*;

import logica.IControladorEvento;
import logica.IControladorUsuario;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;

public class AltaPatrocinioFrame extends JInternalFrame {
    private JComboBox<String> comboEventos;
    private JComboBox<String> comboEdiciones;
    private JComboBox<String> comboTipos;
    private JComboBox<String> comboInstituciones;
    private JComboBox<String> comboTipoGratuito;
    private JComboBox<String> comboNivel;
    private JTextField txtAporte;
    private JTextField txtCantidadGratuitos;
    private JTextField txtCodigo;
    private String[] eventos;
    private String[][] edicionesPorEvento;
    private String[][] tiposPorEdicion;
    private String[] instituciones;
    private double[] costosTipoRegistro;
    private Set<String> codigosPatrocinio = new HashSet<>();
    private Set<String> patrociniosInstitucionEdicion = new HashSet<>();

    public AltaPatrocinioFrame(IControladorUsuario iCU, IControladorEvento iCE) {
        super("Alta de Patrocinio", true, true, true, true);
        setBounds(250, 250, 600, 400);
        setVisible(false);
        setLayout(new BorderLayout());

        JPanel panelSeleccion = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel lblEvento = new JLabel("Evento:");
        comboEventos = new JComboBox<>();
        panelSeleccion.add(lblEvento, gbc);
        gbc.gridx = 1;
        panelSeleccion.add(comboEventos, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel lblEdicion = new JLabel("Edición:");
        comboEdiciones = new JComboBox<>();
        panelSeleccion.add(lblEdicion, gbc);
        gbc.gridx = 1;
        panelSeleccion.add(comboEdiciones, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel lblTipo = new JLabel("Tipo de Registro:");
        comboTipos = new JComboBox<>();
        panelSeleccion.add(lblTipo, gbc);
        gbc.gridx = 1;
        panelSeleccion.add(comboTipos, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel lblInstitucion = new JLabel("Institución:");
        comboInstituciones = new JComboBox<>();
        panelSeleccion.add(lblInstitucion, gbc);
        gbc.gridx = 1;
        panelSeleccion.add(comboInstituciones, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel lblNivel = new JLabel("Nivel de Patrocinio:");
        String[] niveles = {"Platino", "Oro", "Plata", "Bronce"};
        comboNivel = new JComboBox<>(niveles);
        panelSeleccion.add(lblNivel, gbc);
        gbc.gridx = 1;
        panelSeleccion.add(comboNivel, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel lblAporte = new JLabel("Aporte económico:");
        txtAporte = new JTextField(10);
        panelSeleccion.add(lblAporte, gbc);
        gbc.gridx = 1;
        panelSeleccion.add(txtAporte, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel lblTipoGratuito = new JLabel("Tipo de Registro Gratuito:");
        comboTipoGratuito = new JComboBox<>();
        panelSeleccion.add(lblTipoGratuito, gbc);
        gbc.gridx = 1;
        panelSeleccion.add(comboTipoGratuito, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel lblCantidadGratuitos = new JLabel("Cantidad Registros Gratuitos:");
        txtCantidadGratuitos = new JTextField(10);
        panelSeleccion.add(lblCantidadGratuitos, gbc);
        gbc.gridx = 1;
        panelSeleccion.add(txtCantidadGratuitos, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel lblCodigo = new JLabel("Código de Patrocinio:");
        txtCodigo = new JTextField(12);
        panelSeleccion.add(lblCodigo, gbc);
        gbc.gridx = 1;
        panelSeleccion.add(txtCodigo, gbc);

        add(panelSeleccion, BorderLayout.CENTER);

        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");
        JPanel panelBotones = new JPanel();
        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);

        comboEventos.addActionListener(e -> {
            cargarEdiciones();
            cargarTipos();
        });
        comboEdiciones.addActionListener(e -> {
            cargarTipos();
        });

        btnAceptar.addActionListener(e -> {
            int idxEvento = comboEventos.getSelectedIndex();
            int idxEdicion = comboEdiciones.getSelectedIndex();
            int idxTipo = comboTipos.getSelectedIndex();
            int idxTipoGratuito = comboTipoGratuito.getSelectedIndex();
            int idxInstitucion = comboInstituciones.getSelectedIndex();
            String nivel = (String) comboNivel.getSelectedItem();
            String aporteStr = txtAporte.getText().trim();
            String cantidadGratuitosStr = txtCantidadGratuitos.getText().trim();
            String codigo = txtCodigo.getText().trim();
            if (idxEvento < 0 || idxEdicion < 0 || idxTipo < 0 || idxTipoGratuito < 0 || idxInstitucion < 0 || nivel == null || aporteStr.isEmpty() || cantidadGratuitosStr.isEmpty() || codigo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
                return;
            }
            double aporte;
            int cantidadGratuitos;
            try {
                aporte = Double.parseDouble(aporteStr);
                cantidadGratuitos = Integer.parseInt(cantidadGratuitosStr);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Aporte y cantidad deben ser numéricos.");
                return;
            }
            if (codigosPatrocinio.contains(codigo.toLowerCase())) {
                JOptionPane.showMessageDialog(this, "Ya existe un patrocinio con ese código. Ingrese otro código o cancele.");
                return;
            }
            String clavePatrocinio = comboInstituciones.getItemAt(idxInstitucion).toLowerCase() + "-" + comboEdiciones.getItemAt(idxEdicion).toLowerCase();
            if (patrociniosInstitucionEdicion.contains(clavePatrocinio)) {
                JOptionPane.showMessageDialog(this, "Ya existe un patrocinio de esta institución para la edición seleccionada.");
                return;
            }
            double costoTipo = costosTipoRegistro.length > idxTipoGratuito ? costosTipoRegistro[idxTipoGratuito] : 0.0;
            double totalGratis = costoTipo * cantidadGratuitos;
            if (totalGratis > aporte * 0.2) {
                JOptionPane.showMessageDialog(this, "El costo de los registros gratuitos supera el 20% del aporte económico. Modifique los valores o cancele.");
                return;
            }
            // --- ALTA REAL DEL PATROCINIO EN LA LÓGICA ---
            try {
                logica.ControladorEvento controlador = new logica.ControladorEvento();
                String nombreEvento = comboEventos.getItemAt(idxEvento);
                String nombreEdicion = comboEdiciones.getItemAt(idxEdicion);
                String nombreInstitucion = comboInstituciones.getItemAt(idxInstitucion);
                String tipoRegistroGratuito = comboTipoGratuito.getItemAt(idxTipoGratuito);
                // Obtener objetos reales
                logica.Ediciones edicion = controlador.obtenerEdicion(nombreEvento, nombreEdicion);
                logica.Institucion institucion = logica.manejadorUsuario.getInstancia().findInstitucion(nombreInstitucion);
                logica.TipoRegistro tipoRegistro = edicion != null ? edicion.getTipoRegistro(tipoRegistroGratuito) : null;
                logica.DTNivel nivelEnum = logica.DTNivel.valueOf(nivel.toUpperCase());
                java.time.LocalDate fechaHoy = java.time.LocalDate.now();
                controlador.AltaPatrocinio(
                    edicion,
                    institucion,
                    nivelEnum,
                    tipoRegistro,
                    (int) aporte,
                    fechaHoy,
                    cantidadGratuitos,
                    codigo
                );
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al dar de alta el patrocinio: " + ex.getMessage());
                return;
            }
            codigosPatrocinio.add(codigo.toLowerCase());
            patrociniosInstitucionEdicion.add(clavePatrocinio);
            String fechaAlta = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            JOptionPane.showMessageDialog(this, "Patrocinio creado con éxito:\nEvento: " + comboEventos.getSelectedItem() +
                    "\nEdición: " + comboEdiciones.getSelectedItem() +
                    "\nInstitución: " + comboInstituciones.getSelectedItem() +
                    "\nNivel: " + nivel +
                    "\nAporte: " + aporte +
                    "\nTipo gratuito: " + comboTipoGratuito.getSelectedItem() +
                    "\nCantidad gratuitos: " + cantidadGratuitos +
                    "\nCódigo: " + codigo +
                    "\nFecha de alta: " + fechaAlta);
            txtAporte.setText("");
            txtCantidadGratuitos.setText("");
            txtCodigo.setText("");
            this.dispose();
        });
        btnCancelar.addActionListener(e -> this.dispose());
    }

    public void cargarDatos() {
        // Cargar eventos y datos auxiliares desde la lógica
        logica.ControladorEvento controlador = new logica.ControladorEvento();
        java.util.List<logica.DTEvento> listaEventos = controlador.listarEventos();
        eventos = new String[listaEventos.size()];
        edicionesPorEvento = new String[listaEventos.size()][];
        java.util.List<String[]> tiposList = new java.util.ArrayList<>();
        java.util.List<Double> costosList = new java.util.ArrayList<>();
        for (int i = 0; i < listaEventos.size(); i++) {
            logica.DTEvento ev = listaEventos.get(i);
            eventos[i] = ev.getNombre();
            java.util.List<String> eds = controlador.listarEdicionesEvento(ev.getNombre());
            edicionesPorEvento[i] = eds.toArray(new String[0]);
            for (String ed : eds) {
                logica.Ediciones edi = controlador.obtenerEdicion(ev.getNombre(), ed);
                java.util.List<String> tipos = new java.util.ArrayList<>();
                if (edi != null) {
                    for (logica.TipoRegistro tr : edi.getTiposRegistro()) {
                        tipos.add(tr.getNombre());
                        costosList.add((double) tr.getCosto());
                    }
                }
                tiposList.add(tipos.toArray(new String[0]));
            }
        }
        tiposPorEdicion = tiposList.toArray(new String[0][0]);
        costosTipoRegistro = costosList.stream().mapToDouble(Double::doubleValue).toArray();
        instituciones = logica.manejadorUsuario.getInstancia().getInstituciones().toArray(new String[0]);
        codigosPatrocinio = new HashSet<>();
        for (var p : logica.manejadorAuxiliar.getInstancia().listarPatrocinios()) {
            if (p != null && p.getCodigoPatrocinio() != null)
                codigosPatrocinio.add(p.getCodigoPatrocinio().toLowerCase());
        }
        patrociniosInstitucionEdicion = new HashSet<>();
        for (var p : logica.manejadorAuxiliar.getInstancia().listarPatrocinios()) {
            if (p != null && p.getInstitucion() != null && p.getEdicion() != null && p.getInstitucion().getNombre() != null && p.getEdicion().getNombre() != null)
                patrociniosInstitucionEdicion.add(p.getInstitucion().getNombre().toLowerCase() + "-" + p.getEdicion().getNombre().toLowerCase());
        }
        // Cargar combos
        comboEventos.removeAllItems();
        for (String ev : eventos) comboEventos.addItem(ev);
        comboInstituciones.removeAllItems();
        for (String inst : instituciones) comboInstituciones.addItem(inst);
        if (eventos.length > 0) {
            comboEventos.setSelectedIndex(0);
            cargarEdiciones();
            cargarTipos();
        } else {
            comboEdiciones.removeAllItems();
            comboTipos.removeAllItems();
            comboTipoGratuito.removeAllItems();
        }
    }

    private void cargarEdiciones() {
        comboEdiciones.removeAllItems();
        int idxEvento = comboEventos.getSelectedIndex();
        if (idxEvento < 0 || edicionesPorEvento == null || edicionesPorEvento.length <= idxEvento) return;
        for (String ed : edicionesPorEvento[idxEvento]) comboEdiciones.addItem(ed);
        if (comboEdiciones.getItemCount() > 0) comboEdiciones.setSelectedIndex(0);
    }
    private void cargarTipos() {
        comboTipos.removeAllItems();
        comboTipoGratuito.removeAllItems();
        int idxEvento = comboEventos.getSelectedIndex();
        int idxEdicion = comboEdiciones.getSelectedIndex();
        int idxTipo = 0;
        for (int i = 0; i < idxEvento; i++) {
            idxTipo += edicionesPorEvento[i].length;
        }
        idxTipo += idxEdicion;
        if (idxEvento < 0 || idxEdicion < 0 || tiposPorEdicion == null || tiposPorEdicion.length <= idxTipo) return;
        for (String tipo : tiposPorEdicion[idxTipo]) {
            comboTipos.addItem(tipo);
            comboTipoGratuito.addItem(tipo);
        }
        if (comboTipos.getItemCount() > 0) comboTipos.setSelectedIndex(0);
        if (comboTipoGratuito.getItemCount() > 0) comboTipoGratuito.setSelectedIndex(0);
    }
}