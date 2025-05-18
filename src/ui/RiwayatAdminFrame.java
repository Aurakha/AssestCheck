package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import dao.TransactionDAO;
import model.Transaction;

public class RiwayatAdminFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public RiwayatAdminFrame() {
        setTitle("Riwayat Peminjaman - Semua User");
        setSize(600, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        tableModel = new DefaultTableModel(
            new String[] { "Username", "Nama Barang", "Jumlah", "Status", "Tanggal" }, 0
        );
        table = new JTable(tableModel);
        refreshTable();

        add(new JScrollPane(table), BorderLayout.CENTER);
        setVisible(true);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);  // reset isi tabel

        // Ambil semua transaksi dari database menggunakan DAO
        for (Transaction t : TransactionDAO.getAllTransactions()) {
            tableModel.addRow(new Object[] {
                t.getUsername(),
                t.getAssetName(),
                t.getQuantity(),
                t.getStatus(),
                t.getDate()
            });
        }
    }
}
