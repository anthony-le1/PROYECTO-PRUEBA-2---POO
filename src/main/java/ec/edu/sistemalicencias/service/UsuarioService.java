package ec.edu.sistemalicencias.service;



import ec.edu.sistemalicencias.dao.UsuarioDAO;
import ec.edu.sistemalicencias.model.exceptions.UsuarioException;

import java.util.List;

public class UsuarioService {

    private UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new usuarioDAO;
    }

    // Crear usuario
    public void crearUsuario(main.java.ec.edu.sistemalicencias.model.config.model.entities.Usuario usuario) throws main.java.ec.edu.sistemalicencias.model.config.model.exceptions.UsuarioException {
        usuarioDAO.crearUsuario(usuario);
    }

    // Listar usuarios
    public List<main.java.ec.edu.sistemalicencias.model.config.model.entities.Usuario> listarUsuarios() throws main.java.ec.edu.sistemalicencias.model.config.model.exceptions.UsuarioException {
        return usuarioDAO.listarUsuarios();
    }

    // Actualizar usuario
    public void actualizarUsuario( usuario) throws main.java.ec.edu.sistemalicencias.model.config.model.exceptions.UsuarioException {
        usuarioDAO.actualizarUsuario(usuario);
    }

    // Eliminar usuario
    public void eliminarUsuario(String usuario) throws main.java.ec.edu.sistemalicencias.model.config.model.exceptions.UsuarioException {
        usuarioDAO.eliminarUsuario(usuario);
    }

    // Login
    public Usuario login(String usuario, String contrasenia) throws UsuarioException {
        return usuarioDAO.login(usuario, contrasenia);
    }
}

