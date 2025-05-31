package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import dao.AssetDAO;
import model.Asset;
import java.util.List;

public class InventoryFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> cbCategory;
    private JTextField tfSearch;

    public InventoryFrame() {
        setTitle("Daftar Barang");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create a main panel with some padding
        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Create form panel with light background and border
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        formPanel.setBackground(new Color(245, 245, 245));
        
        // Category filter panel
        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        categoryPanel.setBackground(new Color(245, 245, 245));
        JLabel lblCategory = new JLabel("Filter Kategori:");
        lblCategory.setFont(new Font("Arial", Font.BOLD, 12));
        cbCategory = new JComboBox<>(new String[] { "Semua", "Elektronik", "Peralatan Kantor", "Peralatan IT", "Peralatan Ruang Meeting" });
        cbCategory.setPreferredSize(new Dimension(200, 25));
        cbCategory.addActionListener(e -> refreshTable());
        categoryPanel.add(lblCategory);
        categoryPanel.add(cbCategory);
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(245, 245, 245));
        JLabel lblSearch = new JLabel("Cari Barang:");
        lblSearch.setFont(new Font("Arial", Font.BOLD, 12));
        tfSearch = new JTextField(20);
        tfSearch.setPreferredSize(new Dimension(200, 25));
        tfSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                refreshTable();
            }
        });
        searchPanel.add(lblSearch);
        searchPanel.add(tfSearch);
        
        // Add components to form panel
        formPanel.add(categoryPanel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(searchPanel);
        
        // Table setup
        tableModel = new DefaultTableModel(new String[] { "Nama Barang", "Jumlah", "Kategori" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setGridColor(new Color(230, 230, 230));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        table.setSelectionBackground(new Color(210, 230, 250));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        
        // Add components to main panel
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Tombol hapus
        JButton btnHapus = new JButton("Hapus Barang");
        btnHapus.setBackground(new Color(231, 76, 60));
        btnHapus.setForeground(Color.WHITE);
        btnHapus.setFocusPainted(false);
        btnHapus.setFont(new Font("Arial", Font.BOLD, 13));
        btnHapus.addActionListener(e -> hapusBarang());

        // Tambahkan tombol Edit di bawah tombol Hapus
        JButton btnEdit = new JButton("Edit Barang");
        btnEdit.setBackground(new Color(52, 152, 219));
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setFocusPainted(false);
        btnEdit.setFont(new Font("Arial", Font.BOLD, 13));
        btnEdit.addActionListener(e -> editBarang());

        // Panel bawah untuk tombol hapus dan edit
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(245, 245, 245));
        bottomPanel.add(btnEdit);
        bottomPanel.add(btnHapus);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Set content pane
        setContentPane(mainPanel);
        
        refreshTable();
        setVisible(true);
    }

    private void refreshTable() {
        String searchQuery = tfSearch.getText().trim().toLowerCase();
        String selectedCategory = (String) cbCategory.getSelectedItem();

        tableModel.setRowCount(0);
        List<Asset> assets = AssetDAO.getAllAssets();
        for (Asset asset : assets) {
            boolean matchesCategory = selectedCategory.equals("Semua") || asset.getCategory().equalsIgnoreCase(selectedCategory);
            boolean matchesSearch = searchQuery.isEmpty() || asset.getName().toLowerCase().contains(searchQuery);

            if (matchesCategory && matchesSearch) {
                tableModel.addRow(new Object[] { asset.getName(), asset.getQuantity(), asset.getCategory() });
            }
        }
    }

    // Tambahkan method hapusBarang
    private void hapusBarang() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih barang yang ingin dihapus.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String namaBarang = (String) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus barang \"" + namaBarang + "\"?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = AssetDAO.deleteAssetByName(namaBarang);
            if (success) {
                JOptionPane.showMessageDialog(this, "Barang berhasil dihapus.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus barang.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Tambahkan method editBarang
    private void editBarang() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih barang yang ingin diedit.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String namaBarang = (String) tableModel.getValueAt(selectedRow, 0);
        int jumlah = (int) tableModel.getValueAt(selectedRow, 1);
        String kategori = (String) tableModel.getValueAt(selectedRow, 2);

        JTextField tfNama = new JTextField(namaBarang);
        JTextField tfJumlah = new JTextField(String.valueOf(jumlah));
        JComboBox<String> cbKategori = new JComboBox<>(new String[] { "Elektronik", "Peralatan Kantor", "Peralatan IT", "Peralatan Ruang Meeting" });
        cbKategori.setSelectedItem(kategori);

        JPanel panel = new JPanel(new GridLayout(3, 2, 8, 8));
        panel.add(new JLabel("Nama Barang:"));
        panel.add(tfNama);
        panel.add(new JLabel("Jumlah:"));
        panel.add(tfJumlah);
        panel.add(new JLabel("Kategori:"));
        panel.add(cbKategori);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Barang", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String newNama = tfNama.getText().trim();
            String jumlahStr = tfJumlah.getText().trim();
            String newKategori = (String) cbKategori.getSelectedItem();

            if (newNama.isEmpty() || jumlahStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama dan jumlah tidak boleh kosong.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int newJumlah;
            try {
                newJumlah = Integer.parseInt(jumlahStr);
                if (newJumlah < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka positif.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean success = AssetDAO.updateAsset(namaBarang, newNama, newJumlah, newKategori);
            if (success) {
                JOptionPane.showMessageDialog(this, "Barang berhasil diedit.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal mengedit barang.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}