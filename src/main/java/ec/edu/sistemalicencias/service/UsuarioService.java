package ec.edu.sistemalicencias.service;

import ec.edu.sistemalicencias.config.DatabaseConfig;
import ec.edu.sistemalicencias.dao.UsuarioDAO;
import ec.edu.sistemalicencias.model.entities.Usuario;
import ec.edu.sistemalicencias.model.exceptions.UsuarioException;

import java.sql.Connection;
import java.util.List;
import java.util.regex.Pattern;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioService() {
        try {
            Connection conexion = DatabaseConfig
                    .getInstance()
                    .getConnection();

            this.usuarioDAO = new UsuarioDAO(conexion);

        } catch (Exception e) {
            throw new RuntimeException("Error al inicializar UsuarioService", e);
        }
    }

    // ===================== LOGIN =====================
    public Usuario login(String correo, String password) throws UsuarioException {

        validarCorreo(correo);
        validarPassword(password);

        return usuarioDAO.login(correo, password);
    }

    // ===================== CRUD =====================
    public void crearUsuario(Usuario usuario) throws UsuarioException {

        validarCorreo(usuario.getCorreo());
        validarPassword(usuario.getPassword());

        if (usuarioDAO.existeUsuario(usuario.getCorreo())) {
            throw new UsuarioException("El usuario ya existe");
        }

        usuarioDAO.crearUsuario(usuario);
    }

    public List<Usuario> listarUsuarios() throws UsuarioException {
        return usuarioDAO.listarUsuarios();
    }

    public Usuario buscarPorCorreo(String correo) throws UsuarioException {
        return usuarioDAO.buscarPorCorreo(correo);
    }

    public void actualizarUsuario(Usuario usuario) throws UsuarioException {
        usuarioDAO.actualizarUsuario(usuario);
    }

    public void eliminarUsuarioPorCorreo(String correo) throws UsuarioException {
        usuarioDAO.eliminarUsuarioPorCorreo(correo);
    }

    // ===================== VALIDACIONES =====================
    private void validarCorreo(String correo) throws UsuarioException {
        if (correo == null || correo.isBlank()) {
            throw new UsuarioException("Correo inválido");
        }

        String regexCorreo = "^[\\w.-]+@[\\w.-]+\\.(com|ec|es|net|org)$";
        if (!Pattern.matches(regexCorreo, correo)) {
            throw new UsuarioException("Formato de correo inválido");
        }
    }

    private void validarPassword(String password) throws UsuarioException {

        if (password.length() < 8)
            throw new UsuarioException("La contraseña debe tener mínimo 8 caracteres");

        if (!password.matches(".*[A-Z].*"))
            throw new UsuarioException("La contraseña debe tener al menos una mayúscula");

        if (!password.matches(".*[a-z].*"))
            throw new UsuarioException("La contraseña debe tener al menos una minúscula");

        if (!password.matches(".*\\d.*"))
            throw new UsuarioException("La contraseña debe tener al menos un número");

        if (!password.matches(".*[@#$%!].*"))
            throw new UsuarioException("La contraseña debe tener un carácter especial (@#$%!)");
    }
}






