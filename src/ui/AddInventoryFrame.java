package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import dao.AssetDAO;
import model.Asset;

public class AddInventoryFrame extends JFrame {
    public AddInventoryFrame() {
        setTitle("Tambah Barang");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Create main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Create form panel with light background and border
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        formPanel.setBackground(new Color(245, 245, 245));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // Form components
        JLabel titleLabel = new JLabel("Tambah Data Barang");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel nameLabel = new JLabel("Nama Barang:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        JTextField nameField = new JTextField(15);
        nameField.setPreferredSize(new Dimension(200, 30));
        
        JLabel quantityLabel = new JLabel("Jumlah:");
        quantityLabel.setFont(new Font("Arial", Font.BOLD, 12));
        JTextField quantityField = new JTextField(15);
        quantityField.setPreferredSize(new Dimension(200, 30));
        
        JLabel categoryLabel = new JLabel("Kategori:");
        categoryLabel.setFont(new Font("Arial", Font.BOLD, 12));
        String[] categories = {
            "Elektronik",
            "Peralatan Kantor",
            "Peralatan IT",
            "Peralatan Ruang Meeting"
        };
        JComboBox<String> categoryCombo = new JComboBox<>(categories);
        categoryCombo.setPreferredSize(new Dimension(200, 30));
        
        JLabel qrCodeLabel = new JLabel("QR Code:");
        qrCodeLabel.setFont(new Font("Arial", Font.BOLD, 12));
        JTextField qrCodeField = new JTextField(15);
        qrCodeField.setPreferredSize(new Dimension(200, 30));
        
        JButton btnAdd = new JButton("Tambah");
        btnAdd.setBackground(new Color(70, 130, 180));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Arial", Font.BOLD, 12));
        btnAdd.setFocusPainted(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(titleLabel, gbc);
        
        // Add name field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 8, 8, 8);
        formPanel.add(nameLabel, gbc);
        
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);
        
        // Add quantity field
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(quantityLabel, gbc);
        
        gbc.gridx = 1;
        formPanel.add(quantityField, gbc);
        
        // Add category dropdown
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(categoryLabel, gbc);
        
        gbc.gridx = 1;
        formPanel.add(categoryCombo, gbc);
        
        // Add QR code field
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(qrCodeLabel, gbc);
        
        gbc.gridx = 1;
        formPanel.add(qrCodeField, gbc);
        
        // Add button
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 0, 0);
        formPanel.add(btnAdd, gbc);
        
        // Add form panel to main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Set content pane
        setContentPane(mainPanel);
        
        // Button action
        btnAdd.addActionListener(e -> {
            String name = nameField.getText().trim();
            String qtyText = quantityField.getText().trim();
            String category = (String) categoryCombo.getSelectedItem();
            String qrCode = qrCodeField.getText().trim();

            if (name.isEmpty() || qtyText.isEmpty() || category == null) {
                JOptionPane.showMessageDialog(this, "Semua field wajib diisi.", "Validasi Gagal", 
                                              JOptionPane.WARNING_MESSAGE);
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
                JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka.", "Validasi Gagal", 
                                              JOptionPane.ERROR_MESSAGE);
            }
        });

        pack();
        setVisible(true);
    }
}