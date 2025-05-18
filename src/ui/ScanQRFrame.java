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

        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
        qrCodeField = new JTextField();
        infoArea = new JTextArea();
        infoArea.setEditable(false);
        
        JButton scanButton = new JButton("Scan QR");
        scanButton.addActionListener(e -> handleScanQRCode());

        panel.add(new JLabel("Masukkan QR Code Barang:"));
        panel.add(qrCodeField);
        panel.add(scanButton);
        panel.add(new JScrollPane(infoArea));

        add(panel, BorderLayout.CENTER);

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
