package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import dao.TransactionDAO;
import model.Transaction;

public class RiwayatAdminFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public RiwayatAdminFrame() {
        setTitle("Riwayat Peminjaman - Semua User");
        setSize(750, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        headerPanel.setBackground(new Color(245, 245, 245));
        
        JLabel titleLabel = new JLabel("Riwayat Peminjaman Barang");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JLabel subtitleLabel = new JLabel("Menampilkan transaksi dari semua pengguna");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(245, 245, 245));
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitleLabel);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        
        // Create table with enhanced styling
        tableModel = new DefaultTableModel(
            new String[] { "Username", "Nama Barang", "Jumlah", "Status", "Tanggal" }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setGridColor(new Color(230, 230, 230));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        table.setSelectionBackground(new Color(210, 230, 250));
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        // Set preferred column widths for better readability
        table.getColumnModel().getColumn(0).setPreferredWidth(100); // Username
        table.getColumnModel().getColumn(1).setPreferredWidth(180); // Nama Barang
        table.getColumnModel().getColumn(2).setPreferredWidth(60);  // Jumlah
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Status
        table.getColumnModel().getColumn(4).setPreferredWidth(120); // Tanggal
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        
        // Footer panel with refresh button
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JButton btnRefresh = new JButton("Refresh Data");
        btnRefresh.setFont(new Font("Arial", Font.BOLD, 12));
        btnRefresh.setBackground(new Color(70, 130, 180));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnRefresh.addActionListener(e -> refreshTable());
        
        JButton btnExport = new JButton("Export ke Excel");
        btnExport.setFont(new Font("Arial", Font.BOLD, 12));
        btnExport.setBackground(new Color(40, 167, 69));
        btnExport.setForeground(Color.WHITE);
        btnExport.setFocusPainted(false);
        btnExport.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExport.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        // Export functionality would be implemented here
        
        footerPanel.add(btnExport);
        footerPanel.add(Box.createHorizontalStrut(10));
        footerPanel.add(btnRefresh);
        
        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        // Set content pane
        setContentPane(mainPanel);
        
        // Initialize table data
        refreshTable();
        
        setVisible(true);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);  // reset isi tabel
        
        try {
            // Ambil semua transaksi dari database menggunakan DAO
            for (Transaction t : TransactionDAO.getAllTransactions()) {
                tableModel.addRow(new Object[] {
                    t.getUsername(),
                    t.getAssetName(),
                    t.getQuantity(),
                    formatStatus(t.getStatus()),
                    t.getDate()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Gagal memuat data: " + ex.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Helper method to format status for better readability
    private String formatStatus(String status) {
        if (status == null) return "";
        
        switch (status.toLowerCase()) {
            case "approved":
                return "Disetujui";
            case "pending":
                return "Menunggu";
            case "rejected":
                return "Ditolak";
            case "returned":
                return "Dikembalikan";
            default:
                return status;
        }
    }
}