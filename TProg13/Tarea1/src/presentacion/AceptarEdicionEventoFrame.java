package presentacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import logica.Interfaces.IControladorEvento;

public class AceptarEdicionEventoFrame extends JInternalFrame {
    private IControladorEvento ICE;

    private JComboBox<String> comboEventos;
    private JComboBox<String> comboEdiciones;
    private JButton btnAceptar;
    private JButton btnRechazar;

    public AceptarEdicionEventoFrame(IControladorEvento ICE) {
        super("Aceptar/Rechazar Edición de Evento", true, true, true, true);
        this.ICE = ICE;
        setSize(500, 250);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        comboEventos = new JComboBox<>();
        comboEdiciones = new JComboBox<>();
        btnAceptar = new JButton("Aceptar Edición");
        btnRechazar = new JButton("Rechazar Edición");

        panel.add(new JLabel("Evento:"));
        panel.add(comboEventos);
        panel.add(new JLabel("Edición Ingresada:"));
        panel.add(comboEdiciones);
        panel.add(btnAceptar);
        panel.add(btnRechazar);

        add(panel, BorderLayout.CENTER);

        // Eventos
        comboEventos.addActionListener(e -> cargarEdiciones());

        btnAceptar.addActionListener(e -> cambiarEstado(true));
        btnRechazar.addActionListener(e -> cambiarEstado(false));
    }

    public void cargarEventos() {
        comboEventos.removeAllItems();
        List<String> eventos = ICE.listarEventosConEdicionesIngresadas();
        for (String nombre : eventos) {
            comboEventos.addItem(nombre);
        }
        comboEdiciones.removeAllItems();
    }

    private void cargarEdiciones() {
        comboEdiciones.removeAllItems();
        String evento = (String) comboEventos.getSelectedItem();
        if (evento != null) {
            List<String> ediciones = ICE.listarEdicionesIngresadasDeEvento(evento);
            for (String ed : ediciones) {
                comboEdiciones.addItem(ed);
            }
        }
    }

    private void cambiarEstado(boolean aceptar) {
        String evento = (String) comboEventos.getSelectedItem();
        String edicion = (String) comboEdiciones.getSelectedItem();
        if (evento == null || edicion == null) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar un evento y una edición", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        ICE.cambiarEstadoEdicion(evento, edicion, aceptar);
        JOptionPane.showMessageDialog(this, "Edición " + (aceptar ? "confirmada" : "rechazada") + " correctamente.");
        cargarEdiciones(); // recarga ediciones en caso de que ya no haya más ingresadas
        
    }
}