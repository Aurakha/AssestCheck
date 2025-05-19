package ui;

import javax.swing.*;
import java.awt.*;
import dao.AssetDAO;
import model.Asset;

public class ScanQRFrame extends JFrame {
    private JTextField qrCodeField;
    private JTextArea infoArea;

    public ScanQRFrame() {
        setTitle("Scan QR Barang");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel with Border Layout
        JPanel mainPanel = new JPanel(new BorderLayout(0, 15)); // Add vertical gap
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        
        // Input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Scan QR Code"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // QR Code field panel
        JPanel qrPanel = new JPanel(new BorderLayout(5, 5));
        JLabel qrLabel = new JLabel("Masukkan QR Code Barang:");
        qrCodeField = new JTextField();
        qrPanel.add(qrLabel, BorderLayout.NORTH);
        qrPanel.add(qrCodeField, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton scanButton = new JButton("Scan QR");
        scanButton.addActionListener(e -> handleScanQRCode());
        buttonPanel.add(scanButton);
        
        // Add to input panel
        inputPanel.add(qrPanel);
        inputPanel.add(buttonPanel);
        
        // Info panel
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Informasi Barang"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(infoArea);
        infoPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add components to main panel
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        
        // Add main panel to frame
        add(mainPanel);

        setVisible(true);
    }

    private void handleScanQRCode() {
        String qrCode = qrCodeField.getText().trim();

        if (qrCode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "QR Code tidak boleh kosong!");
            return;
        }

        // Ambil data barang berdasarkan QR Code menggunakan AssetDAO
        Asset asset = AssetDAO.getAssetByQrCode(qrCode);

        if (asset != null) {
            infoArea.setText("Nama Barang: " + asset.getName() + "\n" +
                             "Jumlah Tersedia: " + asset.getQuantity() + "\n" +
                             "Kategori: " + asset.getCategory());
        } else {
            infoArea.setText("Barang dengan QR Code tersebut tidak ditemukan.");
        }
    }
}