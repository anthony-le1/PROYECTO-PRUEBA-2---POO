package ec.edu.sistemalicencias.service;

import ec.edu.sistemalicencias.dao.UsuarioDAO;
import ec.edu.sistemalicencias.model.entities.Usuario;
import ec.edu.sistemalicencias.model.exceptions.UsuarioException;

import java.util.List;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
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
}


