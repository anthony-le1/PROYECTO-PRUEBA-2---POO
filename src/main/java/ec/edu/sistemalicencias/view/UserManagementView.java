package ec.edu.sistemalicencias.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UserManagementView extends JFrame {

    private JTable tablaUsuarios;
    private JButton btnCrear, btnActualizar, btnEliminar, btnRefrescar;
    private JPanel panel1;

    public UserManagementView() {
        setTitle("Gestion de Usuarios");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        tablaUsuarios = new JTable();
        JScrollPane scroll = new JScrollPane(tablaUsuarios);
        scroll.setBounds(20, 20, 550, 200);
        add(scroll);

        btnCrear = new JButton("Crear");
        btnCrear.setBounds(20, 250, 100, 30);
        add(btnCrear);

        btnActualizar = new JButton("Actualizar");
        btnActualizar.setBounds(140, 250, 100, 30);
        add(btnActualizar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(260, 250, 100, 30);
        add(btnEliminar);

        btnRefrescar = new JButton("Refrescar");
        btnRefrescar.setBounds(380, 250, 100, 30);
        add(btnRefrescar);

        setLocationRelativeTo(null);
    }

    public JTable getTablaUsuarios() { return tablaUsuarios; }
    public JButton getBtnCrear() { return btnCrear; }
    public JButton getBtnActualizar() { return btnActualizar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnRefrescar() { return btnRefrescar; }

    public void mostrarUsuarios(DefaultTableModel modelo) {
        tablaUsuarios.setModel(modelo);
    }
}

