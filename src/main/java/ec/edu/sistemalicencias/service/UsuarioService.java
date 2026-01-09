package ec.edu.sistemalicencias.service;

import ec.edu.sistemalicencias.config.DatabaseConfig;
import ec.edu.sistemalicencias.dao.UsuarioDAO;
import ec.edu.sistemalicencias.model.entities.Usuario;
import ec.edu.sistemalicencias.model.exceptions.UsuarioException;

import java.sql.Connection;
import java.util.List;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioService() {
        try {
            Connection conexion = DatabaseConfig
                    .getInstance()
                    .obtenerConexion();

            this.usuarioDAO = new UsuarioDAO(conexion);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al inicializar UsuarioService", e
            );
        }
    }

    // Crear usuario
    public void crearUsuario(Usuario usuario) throws UsuarioException {
        usuarioDAO.crearUsuario(usuario);
    }

    // Listar usuarios
    public List<Usuario> listarUsuarios() throws UsuarioException {
        return usuarioDAO.listarUsuarios();
    }

    // Actualizar usuario
    public void actualizarUsuario(Usuario usuario) throws UsuarioException {
        usuarioDAO.actualizarUsuario(usuario);
    }

    // Eliminar usuario
    public void eliminarUsuario(String usuario) throws UsuarioException {
        usuarioDAO.eliminarUsuario(usuario);
    }

    // Login
    public Usuario login(String usuario, String contrasenia) throws UsuarioException {
        return usuarioDAO.login(usuario, contrasenia);
    }

    // Verificar si existe usuario
    public boolean existeUsuario(String usuario) throws UsuarioException {
        return usuarioDAO.existeUsuario(usuario);
    }
}




