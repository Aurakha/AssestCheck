package ui;

import javax.swing.*;
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
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        tableModel = new DefaultTableModel(new String[] { "Nama Barang", "Jumlah", "Kategori" }, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        cbCategory = new JComboBox<>(new String[] { "Semua", "Elektronik", "Peralatan Kantor", "Peralatan IT", "Peralatan Ruang Meeting" });
        cbCategory.addActionListener(e -> refreshTable());

        tfSearch = new JTextField(15);
        tfSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                refreshTable();
            }
        });

        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Filter Kategori:"));
        topPanel.add(cbCategory);
        topPanel.add(new JLabel("Cari Barang:"));
        topPanel.add(tfSearch);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

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
}
