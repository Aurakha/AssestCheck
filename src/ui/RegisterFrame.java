package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import dao.UserDAO;
import dao.LogDAO;
import model.User;
import model.Role;

public class RegisterFrame extends JFrame {
    public RegisterFrame() {
        setTitle("Register");
        setSize(350, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JComboBox<Role> cbRole = new JComboBox<>(Role.values());
        JButton btnRegister = new JButton("Register");
        JButton btnBack = new JButton("Back to Login");

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Role:"));
        panel.add(cbRole);
        panel.add(btnRegister);
        panel.add(btnBack);

        add(panel);
        setVisible(true);

        // Aksi Register
        btnRegister.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            Role role = (Role) cbRole.getSelectedItem();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username dan password tidak boleh kosong.");
                return;
            }
            // Cek username sudah ada?
            if (UserDAO.getUserByUsername(username) != null) {
                JOptionPane.showMessageDialog(this, "Username sudah terdaftar.");
                return;
            }

            User newUser = new User(username, password, role);
            boolean success = UserDAO.insertUser(newUser);
            if (success) {
                LogDAO.insertLog("User baru terdaftar: '" + username + "' dengan role " + role);
                JOptionPane.showMessageDialog(this, "Registrasi berhasil! Silakan login.");
                dispose();
                new LoginFrame();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal registrasi. Coba lagi.");
            }
        });

        // Kembali ke Login
        btnBack.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });
    }
}
