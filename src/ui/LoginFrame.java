package ui;

import javax.swing.*;
import java.awt.*;
import dao.UserDAO;
import dao.LogDAO;
import model.User;
import model.Role;

public class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("Login");
        setSize(380, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel utama dengan padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(new Color(245, 245, 250));

        // Panel judul/logo
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("AssetsCheck");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(titleLabel);

        // Panel form login
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        JTextField usernameField = new JTextField(16);

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        JPasswordField passwordField = new JPasswordField(16);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(52, 152, 219));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JButton btnRegister = new JButton("Register");
        btnRegister.setBackground(new Color(46, 204, 113));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 15));

        // Tambahkan komponen ke formPanel
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

        // Gabungkan panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);

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

        btnRegister.addActionListener(e -> {
            dispose();
            new RegisterFrame();
        });
    }
}