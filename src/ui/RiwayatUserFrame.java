package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import dao.TransactionDAO;
import model.Transaction;

public class RiwayatUserFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private String currentUser;

    public RiwayatUserFrame(String username) {
        this.currentUser = username;

        setTitle("Riwayat Peminjaman Saya");
        setSize(600, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel with Border Layout
        JPanel mainPanel = new JPanel(new BorderLayout(0, 15)); // Add vertical gap between components
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around the frame
        
        // Table setup
        tableModel = new DefaultTableModel(
            new String[] { "Nama Barang", "Jumlah", "Status", "Tanggal" }, 0
        );
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        refreshTable();
        
        // Put table in a scroll pane with a titled border
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Riwayat Transaksi Peminjaman"));
        
        // Add components to main panel
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add main panel to frame
        add(mainPanel);

        setVisible(true);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);  // Kosongkan tabel dulu

        // Ambil transaksi berdasarkan user dari database menggunakan DAO
        for (Transaction t : TransactionDAO.getUserTransactions(currentUser)) {
            tableModel.addRow(new Object[] {
                t.getAssetName(),
                t.getQuantity(),
                t.getStatus(),
                t.getDate()
            });
        }
    }
}