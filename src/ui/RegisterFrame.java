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
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel utama dengan padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(new Color(245, 245, 250));

        // Judul
        JLabel lblTitle = new JLabel("Register Akun Baru", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(52, 73, 94));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Panel form
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setOpaque(false);

        JLabel lblUsername = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel lblPassword = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JLabel lblRole = new JLabel("Role:");
        JComboBox<Role> cbRole = new JComboBox<>(Role.values());

        formPanel.add(lblUsername);
        formPanel.add(usernameField);
        formPanel.add(lblPassword);
        formPanel.add(passwordField);
        formPanel.add(lblRole);
        formPanel.add(cbRole);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Tambahkan jarak antara form dan tombol
        mainPanel.add(Box.createVerticalStrut(30), BorderLayout.SOUTH);

        // Panel tombol
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        JButton btnRegister = new JButton("Register");
        JButton btnBack = new JButton("Back to Login");

        btnRegister.setBackground(new Color(52, 152, 219));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegister.setFocusPainted(false);

        btnBack.setBackground(new Color(231, 76, 60));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBack.setFocusPainted(false);

        buttonPanel.add(btnRegister);
        buttonPanel.add(btnBack);

        // Bungkus buttonPanel dengan panel kosong agar ada padding bawah
        JPanel buttonWrapper = new JPanel(new BorderLayout());
        buttonWrapper.setOpaque(false);
        buttonWrapper.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // Tambah jarak atas tombol
        buttonWrapper.add(buttonPanel, BorderLayout.CENTER);

        mainPanel.add(buttonWrapper, BorderLayout.PAGE_END);

        add(mainPanel);
        setVisible(true);

        // Aksi Register
        btnRegister.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            Role role = (Role) cbRole.getSelectedItem();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username dan password tidak boleh kosong.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Cek username sudah ada?
            if (UserDAO.getUserByUsername(username) != null) {
                JOptionPane.showMessageDialog(this, "Username sudah terdaftar.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            User newUser = new User(username, password, role);
            boolean success = UserDAO.insertUser(newUser);
            if (success) {
                LogDAO.insertLog("User baru terdaftar: '" + username + "' dengan role " + role);
                JOptionPane.showMessageDialog(this, "Registrasi berhasil! Silakan login.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new LoginFrame();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal registrasi. Coba lagi.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Kembali ke Login
        btnBack.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });
    }
}
