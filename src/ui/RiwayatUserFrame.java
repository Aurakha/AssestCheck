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

        tableModel = new DefaultTableModel(
            new String[] { "Nama Barang", "Jumlah", "Status", "Tanggal" }, 0
        );
        table = new JTable(tableModel);
        refreshTable();

        add(new JScrollPane(table), BorderLayout.CENTER);
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
