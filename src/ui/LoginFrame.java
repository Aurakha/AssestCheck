package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import dao.UserDAO;
import dao.LogDAO;
import model.User;
import model.Role;

public class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("Login");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel utama - dua kolom: kiri (logo) dan kanan (form)
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));

        // ================= Panel kiri: Gambar/logo =================
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        try {
            ImageIcon logoIcon = new ImageIcon("src/image/logop.png"); // Ubah path sesuai lokasi gambar
            Image scaledImage = logoIcon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            leftPanel.add(logoLabel, BorderLayout.CENTER);
        } catch (Exception ex) {
            leftPanel.add(new JLabel("Logo gagal dimuat"), BorderLayout.CENTER);
        }

        // ================= Panel kanan: Form login =================
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        rightPanel.setBackground(new Color(245, 245, 250));

        // Kontainer form
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 220), 1, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        formContainer.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("AssetsCheck");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(41, 128, 185));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        formContainer.add(titleLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        JTextField usernameField = new JTextField(16);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        JPasswordField passwordField = new JPasswordField(16);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JButton loginBtn = new JButton("Login");
        styleButton(loginBtn, new Color(52, 152, 219));

        JButton btnRegister = new JButton("Register");
        styleButton(btnRegister, new Color(39, 174, 96));

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(userLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(passLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.add(loginBtn);
        btnPanel.add(Box.createHorizontalStrut(10));
        btnPanel.add(btnRegister);
        formPanel.add(btnPanel, gbc);

        formContainer.add(formPanel, BorderLayout.CENTER);
        rightPanel.add(formContainer, BorderLayout.CENTER);

        // Tambahkan ke main panel
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        add(mainPanel);
        setVisible(true);

        // Event Login
        loginBtn.addActionListener(e -> {
            String userInput = usernameField.getText().trim();
            String passInput = new String(passwordField.getPassword());

            User u = UserDAO.getUserByUsername(userInput);
            if (u != null && u.getPassword().equals(passInput)) {
                LogDAO.insertLog("User '" + userInput + "' berhasil login.");
                JOptionPane.showMessageDialog(this, "Login berhasil!");
                dispose();
                if (u.getRole() == Role.ADMIN) {
                    new MainMenuAdminFrame(u);
                } else {
                    new MainMenuUserFrame(u);
                }
            } else {
                LogDAO.insertLog("Login gagal untuk username '" + userInput + "'.");
                JOptionPane.showMessageDialog(this, "Username/password salah.");
            }
        });

        // Event Register
        btnRegister.addActionListener(e -> {
            dispose();
            new RegisterFrame();
        });
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
    }
}
