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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        // Panel utama dengan padding dan background
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(new Color(245, 245, 250));

        // Panel kiri: label welcome + tombol
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setOpaque(false);

        // Welcome label rata kiri
        JLabel welcomeLabel = new JLabel("Selamat datang, " + user.getUsername() + "!", SwingConstants.LEFT);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        welcomeLabel.setForeground(new Color(44, 62, 80));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        leftPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Panel tombol
        JPanel buttonPanel = new JPanel(new GridLayout(6, 1, 12, 12)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(255, 255, 255, 220));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 30));

        int iconSize = 32;
        JButton btnLihatBarang = new JButton("Lihat Daftar Barang", resizeIcon("src/icon/lihat.png", iconSize, iconSize));
        JButton btnPinjam = new JButton("Pinjam Barang", resizeIcon("src/icon/pinjam.png", iconSize, iconSize));
        JButton btnKembali = new JButton("Kembalikan Barang", resizeIcon("src/icon/kembali.png", iconSize, iconSize));
        JButton btnRiwayat = new JButton("Riwayat Peminjaman Saya", resizeIcon("src/icon/riwayat.png", iconSize, iconSize));
        JButton btnScanQR = new JButton("Scan QR Barang", resizeIcon("src/icon/qr.png", iconSize, iconSize));
        JButton btnLogout = new JButton("Logout", resizeIcon("src/icon/logout.png", iconSize, iconSize));

        JButton[] buttons = {btnLihatBarang, btnPinjam, btnKembali, btnRiwayat, btnScanQR, btnLogout};
        Color btnColor = new Color(52, 152, 219);
        Color btnLogoutColor = new Color(231, 76, 60);
        Font btnFont = new Font("Segoe UI", Font.BOLD, 15);

        for (JButton btn : buttons) {
            btn.setFocusPainted(false);
            btn.setFont(btnFont);
            btn.setBackground(btn == btnLogout ? btnLogoutColor : btnColor);
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setIconTextGap(16);
        }

        buttonPanel.add(btnLihatBarang);
        buttonPanel.add(btnPinjam);
        buttonPanel.add(btnKembali);
        buttonPanel.add(btnRiwayat);
        buttonPanel.add(btnScanQR);
        buttonPanel.add(btnLogout);

        leftPanel.add(buttonPanel, BorderLayout.CENTER);

        // Panel kanan: jam digital + tanggal
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);

        // Label jam & tanggal
        JLabel clockLabel = new JLabel();
        clockLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        clockLabel.setForeground(new Color(52, 73, 94));
        clockLabel.setHorizontalAlignment(SwingConstants.CENTER);
        clockLabel.setVerticalAlignment(SwingConstants.CENTER);
        rightPanel.add(clockLabel, BorderLayout.CENTER);

        // Timer untuk update jam dan tanggal setiap detik
        Timer timer = new Timer(1000, e -> {
            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            String dateTimeStr = now.format(java.time.format.DateTimeFormatter.ofPattern("dd MMMM yyyy | HH:mm:ss"));
            clockLabel.setText(dateTimeStr);
        });
        timer.setInitialDelay(0);
        timer.start();

        // Panel utama menggunakan GridLayout 1 baris 2 kolom
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        contentPanel.setOpaque(false);
        contentPanel.add(leftPanel);
        contentPanel.add(rightPanel);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);

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

    // Utility untuk resize icon
    private ImageIcon resizeIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}
