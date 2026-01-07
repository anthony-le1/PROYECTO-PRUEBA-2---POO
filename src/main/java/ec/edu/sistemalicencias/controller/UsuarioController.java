package ec.edu.sistemalicencias.controller;

import ec.edu.sistemalicencias.model.entities.Usuario;
import ec.edu.sistemalicencias.model.exceptions.UsuarioException;
import ec.edu.sistemalicencias.service.UsuarioService;
import ec.edu.sistemalicencias.view.UserManagementView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * Controlador del módulo de usuarios.
 * Gestiona la comunicación entre la vista de usuarios y el servicio.
 * Permite CRUD y refresco de tabla.
 */
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UserManagementView userManagementView;

    public UsuarioController(UserManagementView view) {
        this.usuarioService = new UsuarioService();
        this.userManagementView = view;
        inicializarBotonesUsuarios();
        refrescarTablaUsuarios();
    }

    /**
     * Refresca la tabla de usuarios
     */
    public void refrescarTablaUsuarios() {
        try {
            List<Usuario> lista = usuarioService.listarUsuarios();
            String[] columnas = {"Usuario", "Contraseña", "Rol"};
            Object[][] datos = new Object[lista.size()][3];

            for (int i = 0; i < lista.size(); i++) {
                Usuario u = lista.get(i);
                datos[i][0] = u.getUsuario();
                datos[i][1] = u.getContrasenia();
                datos[i][2] = u.getRol();
            }

            userManagementView.mostrarUsuarios(
                    new DefaultTableModel(datos, columnas)
            );

        } catch (UsuarioException e) {
            mostrarError(e.getMessage());
        }
    }

    /**
     * Crea un usuario
     */
    public void crearUsuario() {
        String usuario = JOptionPane.showInputDialog("Ingrese el nombre de usuario:");
        String contrasenia = JOptionPane.showInputDialog("Ingrese la contraseña:");
        String rol = JOptionPane.showInputDialog("Ingrese el rol (Administrador / Analista):");

        if (usuario != null && contrasenia != null && rol != null) {
            try {
                usuarioService.crearUsuario(
                        new Usuario(usuario, contrasenia, rol)
                );
                mostrarExito("Usuario creado correctamente");
                refrescarTablaUsuarios();
            } catch (UsuarioException e) {
                mostrarError(e.getMessage());
            }
        }
    }

    /**
     * Actualiza un usuario
     */
    public void actualizarUsuario() {
        int fila = userManagementView.getTablaUsuarios().getSelectedRow();
        if (fila >= 0) {
            String usuario = (String) userManagementView.getTablaUsuarios().getValueAt(fila, 0);
            String contrasenia = JOptionPane.showInputDialog("Nueva contraseña:");
            String rol = JOptionPane.showInputDialog("Nuevo rol:");

            if (contrasenia != null && rol != null) {
                try {
                    usuarioService.actualizarUsuario(
                            new Usuario(usuario, contrasenia, rol)
                    );
                    mostrarExito("Usuario actualizado correctamente");
                    refrescarTablaUsuarios();
                } catch (UsuarioException e) {
                    mostrarError(e.getMessage());
                }
            }
        } else {
            mostrarError("Seleccione un usuario de la tabla");
        }
    }

    /**
     * Elimina un usuario
     */
    public void eliminarUsuario() {
        int fila = userManagementView.getTablaUsuarios().getSelectedRow();
        if (fila >= 0) {
            String usuario = (String) userManagementView.getTablaUsuarios().getValueAt(fila, 0);
            if (confirmar("¿Desea eliminar el usuario " + usuario + "?")) {
                try {
                    usuarioService.eliminarUsuario(usuario);
                    mostrarExito("Usuario eliminado correctamente");
                    refrescarTablaUsuarios();
                } catch (UsuarioException e) {
                    mostrarError(e.getMessage());
                }
            }
        } else {
            mostrarError("Seleccione un usuario de la tabla");
        }
    }

    private void inicializarBotonesUsuarios() {
        userManagementView.getBtnRefrescar().addActionListener(e -> refrescarTablaUsuarios());
        userManagementView.getBtnCrear().addActionListener(e -> crearUsuario());
        userManagementView.getBtnActualizar().addActionListener(e -> actualizarUsuario());
        userManagementView.getBtnEliminar().addActionListener(e -> eliminarUsuario());
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean confirmar(String mensaje) {
        return JOptionPane.showConfirmDialog(
                null,
                mensaje,
                "Confirmación",
                JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION;
    }
}

