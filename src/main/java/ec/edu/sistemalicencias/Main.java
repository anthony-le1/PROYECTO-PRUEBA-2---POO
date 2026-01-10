package ec.edu.sistemalicencias;

import ec.edu.sistemalicencias.config.DatabaseConfig;
import ec.edu.sistemalicencias.model.exceptions.BaseDatosException;
import ec.edu.sistemalicencias.view.LoginView;

import javax.swing.*;
import java.sql.Connection;

public class Main {

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo establecer el Look and Feel");
        }

        DatabaseConfig dbConfig = DatabaseConfig.getInstance();

        SwingUtilities.invokeLater(() -> {

            mostrarPantallaInicio();

            if (!dbConfig.checkConnection()) {
                mostrarErrorConexion();
                return;
            }

            // 🔹 Obtener conexión
            Connection conexion = null;
            try {
                conexion = dbConfig.getConnection();
            } catch (BaseDatosException e) {
                throw new RuntimeException(e);
            }

            // 🔹 Crear ventana LOGIN
            JFrame frameLogin = new JFrame("Inicio de Sesión");
            LoginView loginView = new LoginView(conexion);

            frameLogin.setContentPane(loginView.getPanel1());
            frameLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frameLogin.pack();
            frameLogin.setLocationRelativeTo(null);
            frameLogin.setVisible(true);
        });
    }

    private static void mostrarPantallaInicio() {
        JOptionPane.showMessageDialog(
                null,
                "SISTEMA DE LICENCIAS DE CONDUCIR - ECUADOR\n\n" +
                        "Agencia Nacional de Tránsito\n" +
                        "Versión 1.0\n\n" +
                        "Iniciando sistema...",
                "Bienvenido",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private static void mostrarErrorConexion() {
        JOptionPane.showMessageDialog(
                null,
                "No se pudo conectar a la base de datos.\nLa aplicación se cerrará.",
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
        System.exit(1);
    }
}



