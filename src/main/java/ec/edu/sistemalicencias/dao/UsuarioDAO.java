package ec.edu.sistemalicencias.dao;

import ec.edu.sistemalicencias.model.entities.Usuario;
import ec.edu.sistemalicencias.model.exceptions.UsuarioException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private final Connection conexion;

    public UsuarioDAO(Connection conexion) {
        this.conexion = conexion;
    }

    // Crear usuario
    public void crearUsuario(Usuario usuario) throws UsuarioException {
        String sql = "INSERT INTO usuarios(usuario, contrasenia, rol) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, usuario.getUsuario());
            ps.setString(2, usuario.getContrasenia());
            ps.setString(3, usuario.getRol());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new UsuarioException("Error al crear usuario: " + e.getMessage());
        }
    }

    // Listar usuarios
    public List<Usuario> listarUsuarios() throws UsuarioException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT usuario, contrasenia, rol FROM usuarios";

        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Usuario(
                        rs.getString("usuario"),
                        rs.getString("contrasenia"),
                        rs.getString("rol")
                ));
            }

        } catch (SQLException e) {
            throw new UsuarioException("Error al listar usuarios: " + e.getMessage());
        }

        return lista;
    }

    // Actualizar usuario
    public void actualizarUsuario(Usuario usuario) throws UsuarioException {
        String sql = "UPDATE usuarios SET contrasenia = ?, rol = ? WHERE usuario = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, usuario.getContrasenia());
            ps.setString(2, usuario.getRol());
            ps.setString(3, usuario.getUsuario());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new UsuarioException("Error al actualizar usuario: " + e.getMessage());
        }
    }

    // Eliminar usuario
    public void eliminarUsuario(String usuario) throws UsuarioException {
        String sql = "DELETE FROM usuarios WHERE usuario = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new UsuarioException("Error al eliminar usuario: " + e.getMessage());
        }
    }

    // Login
    public Usuario login(String usuario, String contrasenia) throws UsuarioException {
        String sql = "SELECT usuario, contrasenia, rol FROM usuarios WHERE usuario = ? AND contrasenia = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ps.setString(2, contrasenia);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Usuario(
                        rs.getString("usuario"),
                        rs.getString("contrasenia"),
                        rs.getString("rol")
                );
            } else {
                throw new UsuarioException("Usuario o contraseña incorrectos");
            }
        } catch (SQLException e) {
            throw new UsuarioException("Error al iniciar sesión: " + e.getMessage());
        }
    }

    // ✅ Método para verificar existencia de usuario
    public boolean existeUsuario(String usuario) throws UsuarioException {
        String sql = "SELECT 1 FROM usuarios WHERE usuario = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // si hay resultado, el usuario existe
        } catch (SQLException e) {
            throw new UsuarioException("Error al verificar existencia de usuario: " + e.getMessage());
        }
    }
}





