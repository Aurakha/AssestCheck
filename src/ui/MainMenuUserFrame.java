package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import model.User;

public class MainMenuUserFrame extends JFrame {
    private User currentUser;

    public MainMenuUserFrame(User user) {
        this.currentUser = user;
        setTitle("Main Menu - User");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel welcomeLabel = new JLabel("Selamat datang, " + user.getUsername() + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton btnLihatBarang = new JButton("Lihat Daftar Barang");
        JButton btnPinjam = new JButton("Pinjam Barang");
        JButton btnKembali = new JButton("Kembalikan Barang");
        JButton btnRiwayat = new JButton("Riwayat Peminjaman Saya");
        JButton btnScanQR = new JButton("Scan QR Barang");
        JButton btnLogout = new JButton("Logout");

        JPanel buttonPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        buttonPanel.add(btnLihatBarang);
        buttonPanel.add(btnPinjam);
        buttonPanel.add(btnKembali);
        buttonPanel.add(btnRiwayat);
        buttonPanel.add(btnScanQR);
        buttonPanel.add(btnLogout);

        setLayout(new BorderLayout());
        add(welcomeLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        // Action Listeners
        btnLihatBarang.addActionListener(e -> new InventoryFrame());
        btnPinjam.addActionListener(e -> new BorrowFrame(user.getUsername()));
        btnKembali.addActionListener(e -> new ReturnFrame(user.getUsername()));
        btnRiwayat.addActionListener(e -> new RiwayatUserFrame(user.getUsername()));
        btnScanQR.addActionListener(e -> new ScanQRFrame());

        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        setVisible(true);
    }
}
