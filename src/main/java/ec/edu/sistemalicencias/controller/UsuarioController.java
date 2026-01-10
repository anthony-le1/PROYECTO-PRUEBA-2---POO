package ec.edu.sistemalicencias.controller;

import ec.edu.sistemalicencias.model.entities.Usuario;
import ec.edu.sistemalicencias.model.exceptions.UsuarioException;
import ec.edu.sistemalicencias.service.UsuarioService;
import ec.edu.sistemalicencias.view.UserManagementView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UserManagementView userManagementView;

    // 🔐 Usuario en sesión
    private Usuario usuarioLogueado;

    public UsuarioController(UserManagementView view) {
        this.usuarioService = new UsuarioService();
        this.userManagementView = view;

        if (view != null) {
            inicializarBotonesUsuarios();
            refrescarTablaUsuarios();
        }
    }

    // ===================== LOGIN =====================
    public Usuario login(String correo, String password, String rol) throws UsuarioException {

        // Login contra BD (correo + password)
        Usuario u = usuarioService.login(correo, password);

        // Verificación de rol (seguridad)
        if (!u.getRol().equalsIgnoreCase(rol)) {
            throw new UsuarioException("Credenciales inválidas");
        }

        // Guardamos sesión
        usuarioLogueado = u;

        // Aplicar control de acceso
        aplicarPermisosPorRol();

        return u;
    }

    // ===================== CONTROL DE ACCESO =====================
    private void aplicarPermisosPorRol() {

        if (userManagementView == null) return;

        if (!esAdmin()) {
            userManagementView.getBtnCrear().setEnabled(false);
            userManagementView.getBtnActualizar().setEnabled(false);
            userManagementView.getBtnEliminar().setEnabled(false);
        }
    }

    private boolean esAdmin() {
        return usuarioLogueado != null &&
                "Administrador".equalsIgnoreCase(usuarioLogueado.getRol());
    }

    // ===================== REFRESCAR TABLA =====================
    public void refrescarTablaUsuarios() {
        try {
            List<Usuario> lista = usuarioService.listarUsuarios();

            String[] columnas = {"Nombre", "Apellido", "Correo", "Rol", "Estado"};
            Object[][] datos = new Object[lista.size()][5];

            for (int i = 0; i < lista.size(); i++) {
                Usuario u = lista.get(i);
                datos[i][0] = u.getNombre();
                datos[i][1] = u.getApellido();
                datos[i][2] = u.getCorreo();
                datos[i][3] = u.getRol();
                datos[i][4] = u.isEstado() ? "Activo" : "Inactivo";
            }

            userManagementView.mostrarUsuarios(
                    new DefaultTableModel(datos, columnas)
            );

        } catch (UsuarioException e) {
            mostrarError(e.getMessage());
        }
    }

    // ===================== CREAR USUARIO =====================
    public void crearUsuario() {

        if (!esAdmin()) {
            mostrarError("No tiene permisos para crear usuarios");
            return;
        }

        String nombre = JOptionPane.showInputDialog("Nombre:");
        String apellido = JOptionPane.showInputDialog("Apellido:");
        String correo = JOptionPane.showInputDialog("Correo:");
        String password = JOptionPane.showInputDialog("Contraseña:");
        String rol = JOptionPane.showInputDialog("Rol (Administrador / Analista):");

        if (nombre == null || apellido == null || correo == null ||
                password == null || rol == null) {
            mostrarError("Todos los campos son obligatorios");
            return;
        }

        try {
            Usuario usuario = new Usuario(
                    0,
                    nombre,
                    apellido,
                    correo,
                    password,
                    true,
                    rol
            );

            usuarioService.crearUsuario(usuario);
            mostrarExito("Usuario creado correctamente");
            refrescarTablaUsuarios();

        } catch (UsuarioException e) {
            mostrarError(e.getMessage());
        }
    }

    // ===================== ACTUALIZAR USUARIO =====================
    public void actualizarUsuario() {

        if (!esAdmin()) {
            mostrarError("No tiene permisos para actualizar usuarios");
            return;
        }

        int fila = userManagementView.getTablaUsuarios().getSelectedRow();
        if (fila < 0) {
            mostrarError("Seleccione un usuario");
            return;
        }

        String correo = (String) userManagementView.getTablaUsuarios().getValueAt(fila, 2);
        String nuevoPassword = JOptionPane.showInputDialog("Nueva contraseña:");
        String nuevoRol = JOptionPane.showInputDialog("Nuevo rol:");

        if (nuevoPassword == null || nuevoRol == null) return;

        try {
            Usuario u = usuarioService.buscarPorCorreo(correo);
            u.setPassword(nuevoPassword);
            u.setRol(nuevoRol);

            usuarioService.actualizarUsuario(u);
            mostrarExito("Usuario actualizado correctamente");
            refrescarTablaUsuarios();

        } catch (UsuarioException e) {
            mostrarError(e.getMessage());
        }
    }

    // ===================== ELIMINAR USUARIO =====================
    public void eliminarUsuario() {

        if (!esAdmin()) {
            mostrarError("No tiene permisos para eliminar usuarios");
            return;
        }

        int fila = userManagementView.getTablaUsuarios().getSelectedRow();
        if (fila < 0) {
            mostrarError("Seleccione un usuario");
            return;
        }

        String correo = (String) userManagementView.getTablaUsuarios().getValueAt(fila, 2);

        if (confirmar("¿Desea eliminar el usuario con correo " + correo + "?")) {
            try {
                usuarioService.eliminarUsuarioPorCorreo(correo);
                mostrarExito("Usuario eliminado correctamente");
                refrescarTablaUsuarios();
            } catch (UsuarioException e) {
                mostrarError(e.getMessage());
            }
        }
    }

    // ===================== BOTONES =====================
    private void inicializarBotonesUsuarios() {
        userManagementView.getBtnRefrescar().addActionListener(e -> refrescarTablaUsuarios());
        userManagementView.getBtnCrear().addActionListener(e -> crearUsuario());
        userManagementView.getBtnActualizar().addActionListener(e -> actualizarUsuario());
        userManagementView.getBtnEliminar().addActionListener(e -> eliminarUsuario());
    }

    // ===================== MENSAJES =====================
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





