package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import dao.AssetDAO;
import model.Asset;

public class AddInventoryFrame extends JFrame {
    public AddInventoryFrame() {
        setTitle("Tambah Barang");
        setSize(300, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel layout 5 baris x 2 kolom
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        JTextField nameField = new JTextField();
        JTextField quantityField = new JTextField();

        // Ubah jadi JComboBox untuk kategori
        String[] categories = {
            "Elektronik",
            "Peralatan Kantor",
            "Peralatan IT",
            "Peralatan Ruang Meeting"
        };
        JComboBox<String> categoryCombo = new JComboBox<>(categories);

        JTextField qrCodeField = new JTextField();
        JButton btnAdd = new JButton("Tambah");

        panel.add(new JLabel("Nama Barang:"));
        panel.add(nameField);
        panel.add(new JLabel("Jumlah:"));
        panel.add(quantityField);
        panel.add(new JLabel("Kategori:"));
        panel.add(categoryCombo);        // <-- dropdown di sini
        panel.add(new JLabel("QR Code:"));
        panel.add(qrCodeField);
        panel.add(new JLabel());
        panel.add(btnAdd);

        add(panel);

        btnAdd.addActionListener(e -> {
            String name = nameField.getText().trim();
            String qtyText = quantityField.getText().trim();
            String category = (String) categoryCombo.getSelectedItem();  // ambil kategori
            String qrCode = qrCodeField.getText().trim();

            if (name.isEmpty() || qtyText.isEmpty() || category == null) {
                JOptionPane.showMessageDialog(this, "Semua field wajib diisi.");
                return;
            }

            try {
                int quantity = Integer.parseInt(qtyText);
                Asset asset = AssetDAO.getAssetByName(name);
                if (asset != null) {
                    int newQty = asset.getQuantity() + quantity;
                    boolean updated = AssetDAO.updateQuantity(name, newQty);
                    JOptionPane.showMessageDialog(this,
                        updated ? "Jumlah barang diperbarui." : "Gagal memperbarui jumlah barang.");
                } else {
                    Asset newAsset = new Asset(name, quantity, category, qrCode);
                    boolean inserted = AssetDAO.insertAsset(newAsset);
                    JOptionPane.showMessageDialog(this,
                        inserted ? "Barang baru berhasil ditambahkan." : "Gagal menambahkan barang baru.");
                }

                // Reset form
                nameField.setText("");
                quantityField.setText("");
                categoryCombo.setSelectedIndex(0);
                qrCodeField.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka.");
            }
        });

        setVisible(true);
    }
}
