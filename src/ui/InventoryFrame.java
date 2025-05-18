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
}