package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import dao.AssetDAO;
import dao.TransactionDAO;
import dao.LogDAO;
import model.Transaction;
import data.DBConnection;

import java.sql.*;

public class ReturnFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private String currentUser;

    public ReturnFrame(String username) {
        this.currentUser = username;

        setTitle("Pengembalian Barang");
        setSize(600, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        tableModel = new DefaultTableModel(
                new String[] { "Nama Barang", "Jumlah", "Tanggal Pinjam" }, 0);
        table = new JTable(tableModel);
        refreshTable();

        JButton btnKembalikan = new JButton("Kembalikan Barang");
        btnKembalikan.addActionListener(e -> handlePengembalian());

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(btnKembalikan, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Transaction t : TransactionDAO.getTransactionsByUserAndStatus(currentUser, "Dipinjam")) {
            tableModel.addRow(new Object[] {
                    t.getAssetName(), t.getQuantity(), t.getDate()
            });
        }
    }

    private void handlePengembalian() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi yang ingin dikembalikan.");
            return;
        }

        String assetName = (String) tableModel.getValueAt(selectedRow, 0);
        int quantity = (int) tableModel.getValueAt(selectedRow, 1);

        // Update status transaksi menjadi "Dikembalikan"
        boolean success = TransactionDAO.updateTransactionStatus(currentUser, assetName, quantity, "Dikembalikan");

        if (success) {
            // Update stok barang: menambahkan jumlah yang dikembalikan
            boolean updated = AssetDAO.updateQuantity(assetName, quantity);

            // Log
            LogDAO.insertLog("User '" + currentUser + "' mengembalikan " + quantity + " " + assetName);

            if (updated) {
                JOptionPane.showMessageDialog(this, "Barang berhasil dikembalikan.");
            } else {
                JOptionPane.showMessageDialog(this, "Gagal memperbarui stok.");
            }

            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan. Coba lagi.");
        }
    }

}
