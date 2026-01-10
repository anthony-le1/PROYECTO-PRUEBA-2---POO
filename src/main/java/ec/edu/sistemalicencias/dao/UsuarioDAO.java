package ec.edu.sistemalicencias.dao;

import ec.edu.sistemalicencias.config.DatabaseConfig;
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

    // ===================== CREAR USUARIO =====================
    public void crearUsuario(Usuario usuario) throws UsuarioException {

        String sql = """
            INSERT INTO usuarios(nombre, apellido, correo, password, estado, rol)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getCorreo());
            ps.setString(4, usuario.getPassword());
            ps.setBoolean(5, usuario.isEstado());
            ps.setString(6, usuario.getRol());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new UsuarioException("Error al crear usuario", e);
        }
    }

    // ===================== LISTAR USUARIOS =====================
    public List<Usuario> listarUsuarios() throws UsuarioException {

        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Usuario u = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("correo"),
                        rs.getString("password"),
                        rs.getBoolean("estado"),
                        rs.getString("rol")
                );
                lista.add(u);
            }

        } catch (SQLException e) {
            throw new UsuarioException("Error al listar usuarios", e);
        }

        return lista;
    }

    // ===================== BUSCAR POR CORREO =====================
    public Usuario buscarPorCorreo(String correo) throws UsuarioException {

        String sql = "SELECT * FROM usuarios WHERE correo = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, correo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("correo"),
                            rs.getString("password"),
                            rs.getBoolean("estado"),
                            rs.getString("rol")
                    );
                }
            }

        } catch (SQLException e) {
            throw new UsuarioException("Error al buscar usuario", e);
        }

        return null;
    }



    // ===================== ACTUALIZAR USUARIO =====================
    public void actualizarUsuario(Usuario usuario) throws UsuarioException {

        String sql = """
            UPDATE usuarios
            SET nombre = ?, apellido = ?, password = ?, estado = ?, rol = ?
            WHERE correo = ?
            """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getPassword());
            ps.setBoolean(4, usuario.isEstado());
            ps.setString(5, usuario.getRol());
            ps.setString(6, usuario.getCorreo());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new UsuarioException("Error al actualizar usuario", e);
        }
    }
    public void eliminarUsuarioPorCorreo(String correo) throws UsuarioException {

        String sql = "DELETE FROM usuarios WHERE correo = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new UsuarioException("Error al eliminar usuario", e);
        }
    }



    // ===================== LOGIN =====================
    public Usuario login(String correo, String password) throws UsuarioException {

        String sql = """
            SELECT * FROM usuarios
            WHERE correo = ? AND password = ? AND estado = true
            """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("correo"),
                            rs.getString("password"),
                            rs.getBoolean("estado"),
                            rs.getString("rol")
                    );
                }
            }

            throw new UsuarioException("Correo o contraseña incorrectos"); //Buena practica en ciberseguridad

        } catch (SQLException e) {
            throw new UsuarioException("Error al iniciar sesión", e);
        }
    }

    // ===================== EXISTE USUARIO =====================
    public boolean existeUsuario(String correo) throws UsuarioException {

        String sql = "SELECT 1 FROM usuarios WHERE correo = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new UsuarioException("Error al verificar usuario", e);
        }
    }
}






