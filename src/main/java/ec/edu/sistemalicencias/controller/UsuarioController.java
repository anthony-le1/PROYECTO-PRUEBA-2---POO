package ec.edu.sistemalicencias.controller;


import ec.edu.sistemalicencias.service.UsuarioService;
import ec.edu.sistemalicencias.view.UserManagementView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * Controlador del módulo de usuarios.
 * Gestiona la comunicación entre la vista de usuarios y el servicio.
 * Permite CRUD y refresco de tabla.
 * @author Sistema Licencias Ecuador
 * @version 1.0
 */
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UserManagementView userManagementView;

    /**
     * Constructor que inicializa el servicio y la vista.
     * @param view Vista de gestión de usuarios
     */
    public UsuarioController(UserManagementView view) {
        this.usuarioService = new UsuarioService();
        this.userManagementView = view;
        inicializarBotonesUsuarios();
        refrescarTablaUsuarios();
    }

    /**
     * Refresca la tabla de usuarios en la vista
     */
    public void refrescarTablaUsuarios() {
        try {
            List<main.java.ec.edu.sistemalicencias.model.config.model.entities.Usuario> lista = usuarioService.listarUsuarios();
            String[] columnas = {"Usuario", "Contrasenia", "Rol"};
            Object[][] datos = new Object[lista.size()][3];

            for (int i = 0; i < lista.size(); i++) {
                main.java.ec.edu.sistemalicencias.model.config.model.entities.Usuario u = lista.get(i);
                datos[i][0] = u.getUsuario();
                datos[i][1] = u.getContrasenia();
                datos[i][2] = u.getRol();
            }

            userManagementView.mostrarUsuarios(new DefaultTableModel(datos, columnas));

        } catch (main.java.ec.edu.sistemalicencias.model.config.model.exceptions.UsuarioException e) {
            mostrarError(e.getMessage());
        }
    }

    /**
     * Crea un nuevo usuario mediante un diálogo
     */
    public void crearUsuario() {
        String usuario = JOptionPane.showInputDialog("Ingrese el nombre de usuario:");
        String contrasenia = JOptionPane.showInputDialog("Ingrese la contrasenia:");
        String rol = JOptionPane.showInputDialog("Ingrese el rol (Administrador / Analista):");

        if (usuario != null && contrasenia != null && rol != null) {
            try {
                usuarioService.crearUsuario(new main.java.ec.edu.sistemalicencias.model.config.model.entities.Usuario(usuario, contrasenia, rol));
                mostrarExito("Usuario creado correctamente");
                refrescarTablaUsuarios();
            } catch (main.java.ec.edu.sistemalicencias.model.config.model.exceptions.UsuarioException e) {
                mostrarError(e.getMessage());
            }
        }
    }

    /**
     * Actualiza un usuario seleccionado en la tabla
     */
    public void actualizarUsuario() {
        int fila = userManagementView.getTablaUsuarios().getSelectedRow();
        if (fila >= 0) {
            String usuario = (String) userManagementView.getTablaUsuarios().getValueAt(fila, 0);
            String contrasenia = JOptionPane.showInputDialog("Nueva contrasenia:");
            String rol = JOptionPane.showInputDialog("Nuevo rol (Administrador / Analista):");

            if (contrasenia != null && rol != null) {
                try {
                    usuarioService.actualizarUsuario(new main.java.ec.edu.sistemalicencias.model.config.model.entities.Usuario(usuario, contrasenia, rol));
                    mostrarExito("Usuario actualizado correctamente");
                    refrescarTablaUsuarios();
                } catch (main.java.ec.edu.sistemalicencias.model.config.model.exceptions.UsuarioException e) {
                    mostrarError(e.getMessage());
                }
            }
        } else {
            mostrarError("Seleccione un usuario de la tabla");
        }
    }

    /**
     * Elimina un usuario seleccionado en la tabla
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
                } catch (main.java.ec.edu.sistemalicencias.model.config.model.exceptions.UsuarioException e) {
                    mostrarError(e.getMessage());
                }
            }
        } else {
            mostrarError("Seleccione un usuario de la tabla");
        }
    }

    /**
     * Inicializa los botones de la vista para que llamen a los métodos CRUD
     */
    private void inicializarBotonesUsuarios() {
        userManagementView.getBtnRefrescar().addActionListener(e -> refrescarTablaUsuarios());
        userManagementView.getBtnCrear().addActionListener(e -> crearUsuario());
        userManagementView.getBtnActualizar().addActionListener(e -> actualizarUsuario());
        userManagementView.getBtnEliminar().addActionListener(e -> eliminarUsuario());
    }

    /**
     * Muestra un mensaje de error en la interfaz
     * @param mensaje Mensaje a mostrar
     */
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Muestra un mensaje de éxito en la interfaz
     * @param mensaje Mensaje a mostrar
     */
    public void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(null,
                mensaje,
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Muestra un mensaje de confirmación
     * @param mensaje Mensaje a mostrar
     * @return true si el usuario confirma
     */
    public boolean confirmar(String mensaje) {
        int respuesta = JOptionPane.showConfirmDialog(null,
                mensaje,
                "Confirmación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        return respuesta == JOptionPane.YES_OPTION;
    }
}
