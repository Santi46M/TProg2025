import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class Eventos extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_1;
    private JDesktopPane desktopPane;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Eventos frame = new Eventos();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Eventos() {
        setTitle("Eventos.uy");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 803, 563);

        desktopPane = new JDesktopPane();
        setContentPane(desktopPane);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menuSistema = new JMenu("Sistema");
        menuBar.add(menuSistema);

        JMenu menuUsuario = new JMenu("Usuario");
        menuBar.add(menuUsuario);

        JMenuItem menuItemUsuario = new JMenuItem("Alta Usuario");
        menuUsuario.add(menuItemUsuario);
        menuItemUsuario.addActionListener(e -> abrirAltaUsuario());

        JMenuItem menuItemListarUsuario = new JMenuItem("ListarUsuario");
        menuUsuario.add(menuItemListarUsuario);
        menuItemListarUsuario.addActionListener(e -> abrirListarUsuarios());

        JMenu menuEvento = new JMenu("Evento");
        menuBar.add(menuEvento);

        JMenuItem menuItemEvento = new JMenuItem("Alta Evento");
        menuEvento.add(menuItemEvento);
        menuItemEvento.addActionListener(e -> abrirAltaEvento());

        JMenuItem menuItemRegistro = new JMenuItem("Registrarse a Edicion de Evento");
        menuEvento.add(menuItemRegistro);
        menuItemRegistro.addActionListener(e -> abrirRegistroEvento());

        abrirBienvenida();
    }

    private void abrirAltaUsuario() {
        JInternalFrame internalFrame = new JInternalFrame("Alta de Usuario");
        internalFrame.setIconifiable(true);
        internalFrame.setMaximizable(true);
        internalFrame.setResizable(true);
        internalFrame.setClosable(true);
        internalFrame.setBounds(new Rectangle(50, 50, 450, 300));
        internalFrame.setVisible(true);

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[3];
        gridBagLayout.rowHeights = new int[3];
        gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        internalFrame.getContentPane().setLayout(gridBagLayout);

        JLabel lblNick = new JLabel("Nick:");
        GridBagConstraints gbc_lblNick = new GridBagConstraints();
        gbc_lblNick.insets = new Insets(0, 0, 5, 5);
        gbc_lblNick.anchor = GridBagConstraints.WEST;
        gbc_lblNick.gridx = 0;
        gbc_lblNick.gridy = 0;
        internalFrame.getContentPane().add(lblNick, gbc_lblNick);

        JTextField txtNick = new JTextField();
        GridBagConstraints gbc_txtNick = new GridBagConstraints();
        gbc_txtNick.insets = new Insets(0, 0, 5, 0);
        gbc_txtNick.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtNick.gridx = 1;
        gbc_txtNick.gridy = 0;
        internalFrame.getContentPane().add(txtNick, gbc_txtNick);
        txtNick.setColumns(10);

        JLabel lblNombre = new JLabel("Nombre:");
        GridBagConstraints gbc_lblNombre = new GridBagConstraints();
        gbc_lblNombre.anchor = GridBagConstraints.WEST;
        gbc_lblNombre.insets = new Insets(0, 0, 5, 5);
        gbc_lblNombre.gridx = 0;
        gbc_lblNombre.gridy = 1;
        internalFrame.getContentPane().add(lblNombre, gbc_lblNombre);

        JTextField txtNombre = new JTextField();
        GridBagConstraints gbc_txtNombre = new GridBagConstraints();
        gbc_txtNombre.insets = new Insets(0, 0, 5, 0);
        gbc_txtNombre.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtNombre.gridx = 1;
        gbc_txtNombre.gridy = 1;
        internalFrame.getContentPane().add(txtNombre, gbc_txtNombre);
        txtNombre.setColumns(10);

        JLabel lblCorreo = new JLabel("Correo:");
        GridBagConstraints gbc_lblCorreo = new GridBagConstraints();
        gbc_lblCorreo.insets = new Insets(0, 0, 5, 5);
        gbc_lblCorreo.anchor = GridBagConstraints.WEST;
        gbc_lblCorreo.gridx = 0;
        gbc_lblCorreo.gridy = 2;
        internalFrame.getContentPane().add(lblCorreo, gbc_lblCorreo);

        JTextField txtCorreo = new JTextField();
        GridBagConstraints gbc_txtCorreo = new GridBagConstraints();
        gbc_txtCorreo.insets = new Insets(0, 0, 5, 0);
        gbc_txtCorreo.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtCorreo.gridx = 1;
        gbc_txtCorreo.gridy = 2;
        internalFrame.getContentPane().add(txtCorreo, gbc_txtCorreo);
        txtCorreo.setColumns(10);

        JLabel lblTipo = new JLabel("Tipo de Usuario:");
        GridBagConstraints gbc_lblTipo = new GridBagConstraints();
        gbc_lblTipo.insets = new Insets(0, 0, 0, 5);
        gbc_lblTipo.anchor = GridBagConstraints.WEST;
        gbc_lblTipo.gridx = 0;
        gbc_lblTipo.gridy = 3;
        internalFrame.getContentPane().add(lblTipo, gbc_lblTipo);

        JPanel panelRadio = new JPanel(new GridLayout(1, 2, 5, 0));
        JRadioButton rbtnOrganizador = new JRadioButton("Organizador");
        JRadioButton rbtnAsistente = new JRadioButton("Asistente");
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbtnOrganizador);
        grupo.add(rbtnAsistente);
        panelRadio.add(rbtnOrganizador);
        panelRadio.add(rbtnAsistente);

        GridBagConstraints gbc_panelRadio = new GridBagConstraints();
        gbc_panelRadio.gridx = 1;
        gbc_panelRadio.gridy = 3;
        gbc_panelRadio.fill = GridBagConstraints.HORIZONTAL;
        gbc_panelRadio.insets = new Insets(0, 0, 5, 0);
        internalFrame.getContentPane().add(panelRadio, gbc_panelRadio);

        JButton btnAceptar = new JButton("Aceptar");
        GridBagConstraints gbc_btnAceptar = new GridBagConstraints();
        gbc_btnAceptar.gridx = 1;
        gbc_btnAceptar.gridy = 4;
        gbc_btnAceptar.insets = new Insets(10, 5, 5, 5);
        gbc_btnAceptar.anchor = GridBagConstraints.WEST;
        internalFrame.getContentPane().add(btnAceptar, gbc_btnAceptar);

        JButton btnCancelar = new JButton("Cancelar");
        GridBagConstraints gbc_btnCancelar = new GridBagConstraints();
        gbc_btnCancelar.gridx = 2;
        gbc_btnCancelar.gridy = 4;
        gbc_btnCancelar.insets = new Insets(10, 5, 5, 0);
        gbc_btnCancelar.anchor = GridBagConstraints.EAST;
        internalFrame.getContentPane().add(btnCancelar, gbc_btnCancelar);

        btnAceptar.addActionListener(ev -> {
            String nick = txtNick.getText().trim();
            String nombre = txtNombre.getText().trim();
            String correo = txtCorreo.getText().trim();
            String tipo = rbtnOrganizador.isSelected() ? "Organizador" : rbtnAsistente.isSelected() ? "Asistente" : null;

            if (nick.isEmpty() || nombre.isEmpty() || correo.isEmpty() || tipo == null) {
                JOptionPane.showMessageDialog(internalFrame, "Todos los campos deben estar completos y debe seleccionar un tipo.");
                return;
            }

            JOptionPane.showMessageDialog(internalFrame, "Usuario registrado con éxito:\nNick: " + nick + "\nNombre: " + nombre + "\nCorreo: " + correo + "\nTipo: " + tipo);
        });

        btnCancelar.addActionListener(ev -> internalFrame.dispose());

        desktopPane.add(internalFrame);
        internalFrame.toFront();
    }

    private void abrirListarUsuarios() {
        JInternalFrame internalFrameListar = new JInternalFrame("Listar Usuarios");
        internalFrameListar.setClosable(true);
        internalFrameListar.setIconifiable(true);
        internalFrameListar.setMaximizable(true);
        internalFrameListar.setResizable(true);
        internalFrameListar.setBounds(new Rectangle(50, 50, 400, 150));
        internalFrameListar.setVisible(true);

        JPanel panel = new JPanel();
        JLabel label = new JLabel("Seleccione un usuario:");
        panel.add(label);

        String[] usuarios = {"Juan", "Ana", "Carlos", "Lucía"};
        JComboBox<String> comboUsuarios = new JComboBox<>(usuarios);
        panel.add(comboUsuarios);

        internalFrameListar.getContentPane().add(panel);
        desktopPane.add(internalFrameListar);
        internalFrameListar.toFront();
    }

    private void abrirAltaEvento() {
        JInternalFrame internalFrame = new JInternalFrame("Alta de Evento");
        internalFrame.setIconifiable(true);
        internalFrame.setMaximizable(true);
        internalFrame.setResizable(true);
        internalFrame.setClosable(true);
        internalFrame.setBounds(new Rectangle(50, 50, 450, 300));
        internalFrame.setVisible(true);

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[3];
        gridBagLayout.rowHeights = new int[5];
        gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        internalFrame.getContentPane().setLayout(gridBagLayout);

        // Nombre
        JLabel lblNombre = new JLabel("Nombre:");
        GridBagConstraints gbc_lblNombre = new GridBagConstraints();
        gbc_lblNombre.insets = new Insets(0, 0, 5, 5);
        gbc_lblNombre.anchor = GridBagConstraints.WEST;
        gbc_lblNombre.gridx = 0;
        gbc_lblNombre.gridy = 0;
        internalFrame.getContentPane().add(lblNombre, gbc_lblNombre);

        JTextField txtNombre = new JTextField();
        GridBagConstraints gbc_txtNombre = new GridBagConstraints();
        gbc_txtNombre.insets = new Insets(0, 0, 5, 0);
        gbc_txtNombre.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtNombre.gridx = 1;
        gbc_txtNombre.gridy = 0;
        internalFrame.getContentPane().add(txtNombre, gbc_txtNombre);
        txtNombre.setColumns(10);

        // Descripcion
        JLabel lblDescripcion = new JLabel("Descripcion:");
        GridBagConstraints gbc_lblDescripcion = new GridBagConstraints();
        gbc_lblDescripcion.insets = new Insets(0, 0, 5, 5);
        gbc_lblDescripcion.anchor = GridBagConstraints.WEST;
        gbc_lblDescripcion.gridx = 0;
        gbc_lblDescripcion.gridy = 1;
        internalFrame.getContentPane().add(lblDescripcion, gbc_lblDescripcion);

        JTextField txtDescripcion = new JTextField();
        GridBagConstraints gbc_txtDescripcion = new GridBagConstraints();
        gbc_txtDescripcion.insets = new Insets(0, 0, 5, 0);
        gbc_txtDescripcion.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtDescripcion.gridx = 1;
        gbc_txtDescripcion.gridy = 1;
        internalFrame.getContentPane().add(txtDescripcion, gbc_txtDescripcion);
        txtDescripcion.setColumns(10);

        // Fecha
        JLabel lblFecha = new JLabel("Fecha (YYYY-MM-DD):");
        GridBagConstraints gbc_lblFecha = new GridBagConstraints();
        gbc_lblFecha.insets = new Insets(0, 0, 5, 5);
        gbc_lblFecha.anchor = GridBagConstraints.WEST;
        gbc_lblFecha.gridx = 0;
        gbc_lblFecha.gridy = 2;
        internalFrame.getContentPane().add(lblFecha, gbc_lblFecha);

        JDateChooser dateChooser = new JDateChooser();
        GridBagConstraints gbc_dateChooser = new GridBagConstraints();
        gbc_dateChooser.insets = new Insets(0, 0, 5, 0);
        gbc_dateChooser.fill = GridBagConstraints.HORIZONTAL;
        gbc_dateChooser.gridx = 1;
        gbc_dateChooser.gridy = 2;
        internalFrame.getContentPane().add(dateChooser, gbc_dateChooser);

        // Sigla
        JLabel lblSigla = new JLabel("Sigla:");
        GridBagConstraints gbc_lblSigla = new GridBagConstraints();
        gbc_lblSigla.insets = new Insets(0, 0, 5, 5);
        gbc_lblSigla.anchor = GridBagConstraints.WEST;
        gbc_lblSigla.gridx = 0;
        gbc_lblSigla.gridy = 3;
        internalFrame.getContentPane().add(lblSigla, gbc_lblSigla);

        JTextField txtSigla = new JTextField();
        GridBagConstraints gbc_txtSigla = new GridBagConstraints();
        gbc_txtSigla.insets = new Insets(0, 0, 5, 0);
        gbc_txtSigla.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtSigla.gridx = 1;
        gbc_txtSigla.gridy = 3;
        internalFrame.getContentPane().add(txtSigla, gbc_txtSigla);
        txtSigla.setColumns(10);

        // Categoria
        JLabel lblCategoria = new JLabel("Categorias:");
        GridBagConstraints gbc_lblCategoria = new GridBagConstraints();
        gbc_lblCategoria.insets = new Insets(0, 0, 0, 5);
        gbc_lblCategoria.anchor = GridBagConstraints.WEST;
        gbc_lblCategoria.gridx = 0;
        gbc_lblCategoria.gridy = 4;
        internalFrame.getContentPane().add(lblCategoria, gbc_lblCategoria);

        JPanel panelRadio = new JPanel(new GridLayout(1, 2, 5, 0));
        JRadioButton rbtnFull = new JRadioButton("Full Experience");
        JRadioButton rbtnVip = new JRadioButton("Vip");
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbtnFull);
        grupo.add(rbtnVip);
        panelRadio.add(rbtnFull);
        panelRadio.add(rbtnVip);

        GridBagConstraints gbc_panelRadio = new GridBagConstraints();
        gbc_panelRadio.gridx = 1;
        gbc_panelRadio.gridy = 4;
        gbc_panelRadio.fill = GridBagConstraints.HORIZONTAL;
        internalFrame.getContentPane().add(panelRadio, gbc_panelRadio);

        // Botones
        JButton btnAceptar = new JButton("Aceptar");
        GridBagConstraints gbc_btnAceptar = new GridBagConstraints();
        gbc_btnAceptar.gridx = 1;
        gbc_btnAceptar.gridy = 5;
        gbc_btnAceptar.insets = new Insets(10, 5, 5, 5);
        internalFrame.getContentPane().add(btnAceptar, gbc_btnAceptar);

        JButton btnCancelar = new JButton("Cancelar");
        GridBagConstraints gbc_btnCancelar = new GridBagConstraints();
        gbc_btnCancelar.gridx = 2;
        gbc_btnCancelar.gridy = 5;
        gbc_btnCancelar.insets = new Insets(10, 5, 5, 0);
        internalFrame.getContentPane().add(btnCancelar, gbc_btnCancelar);

        btnAceptar.addActionListener(ev -> {
            String nombre = txtNombre.getText().trim();
            String descripcion = txtDescripcion.getText().trim();
            Date fecha = dateChooser.getDate();
            String sigla = txtSigla.getText().trim();
            String categoria = rbtnFull.isSelected() ? "Full Experience" : rbtnVip.isSelected() ? "Vip" : null;

            if (nombre.isEmpty() || descripcion.isEmpty() || fecha == null || sigla.isEmpty() || categoria == null) {
                JOptionPane.showMessageDialog(internalFrame, "Todos los campos deben estar completos y debe seleccionar una Categoria.");
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fechaFormateada = sdf.format(fecha);

            JOptionPane.showMessageDialog(internalFrame,
                    "Evento registrado con éxito:\nNombre: " + nombre +
                            "\nDescripcion: " + descripcion +
                            "\nFecha: " + fechaFormateada +
                            "\nSigla: " + sigla +
                            "\nCategoria: " + categoria);
        });

        btnCancelar.addActionListener(ev -> internalFrame.dispose());

        desktopPane.add(internalFrame);
        internalFrame.toFront();
    }

    private void abrirRegistroEvento() {
        JInternalFrame internalFrameListar = new JInternalFrame("Registro a Edicion de Evento");
        internalFrameListar.setClosable(true);
        internalFrameListar.setIconifiable(true);
        internalFrameListar.setMaximizable(true);
        internalFrameListar.setResizable(true);
        internalFrameListar.setBounds(new Rectangle(50, 50, 400, 150));
        internalFrameListar.setVisible(true);

        JPanel panel = new JPanel();
        JLabel label = new JLabel("Seleccione un Evento:");
        panel.add(label);

        String[] eventos = {"Elija el Evento", "Ingenieria de Muestra", "Campus Party", "Cuti", "Coloquios de Logica"};
        JComboBox<String> comboEventos = new JComboBox<>(eventos);
        panel.add(comboEventos);

        String[] edicionesMuestra = {"Verano 2025", "Invierno 2025"};
        JComboBox<String> comboEdicionesMuestra = new JComboBox<>(edicionesMuestra);
        panel.add(comboEdicionesMuestra);
        comboEdicionesMuestra.setVisible(false);

        String[] edicionesCampus = {"Edicion 2025"};
        JComboBox<String> comboEdicionesCampus = new JComboBox<>(edicionesCampus);
        panel.add(comboEdicionesCampus);
        comboEdicionesCampus.setVisible(false);

        String[] edicionesCuti = {"Julio 2025", "Agosto 2025"};
        JComboBox<String> comboEdicionesCuti = new JComboBox<>(edicionesCuti);
        panel.add(comboEdicionesCuti);
        comboEdicionesCuti.setVisible(false);

        String[] edicionesColoquios = {"Edicion 2024", "Edicion 2025"};
        JComboBox<String> comboEdicionesColoquios = new JComboBox<>(edicionesColoquios);
        panel.add(comboEdicionesColoquios);
        comboEdicionesColoquios.setVisible(false);

        comboEventos.addActionListener(ev -> {
            String seleccionado = (String) comboEventos.getSelectedItem();
            comboEdicionesMuestra.setVisible("Ingenieria de Muestra".equals(seleccionado));
            comboEdicionesCampus.setVisible("Campus Party".equals(seleccionado));
            comboEdicionesCuti.setVisible("Cuti".equals(seleccionado));
            comboEdicionesColoquios.setVisible("Coloquios de Logica".equals(seleccionado));
            panel.revalidate();
            panel.repaint();
        });

        internalFrameListar.getContentPane().add(panel);
        desktopPane.add(internalFrameListar);
        internalFrameListar.toFront();
    }

    private void abrirBienvenida() {
        JInternalFrame frameBienvenida = new JInternalFrame("Eventos.uy");
        frameBienvenida.setClosable(true);
        frameBienvenida.setIconifiable(true);
        frameBienvenida.setMaximizable(true);
        frameBienvenida.setResizable(true);
        frameBienvenida.setBounds(50, 50, 300, 200);
        frameBienvenida.setVisible(true);

        GridBagLayout layout = new GridBagLayout();
        frameBienvenida.getContentPane().setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel lblBienvenida = new JLabel("¡Bienvenido a Eventos.uy!");
        frameBienvenida.getContentPane().add(lblBienvenida, gbc);

        JButton btnAceptar = new JButton("Aceptar");
        gbc.gridy = 1;
        frameBienvenida.getContentPane().add(btnAceptar, gbc);

        btnAceptar.addActionListener(e -> frameBienvenida.dispose());

        desktopPane.add(frameBienvenida);
        frameBienvenida.toFront();
    }
}
