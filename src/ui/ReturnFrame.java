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

        // Main panel with Border Layout
        JPanel mainPanel = new JPanel(new BorderLayout(0, 15)); // Add vertical gap between components
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around the frame
        
        // Table setup
        tableModel = new DefaultTableModel(
                new String[] { "Nama Barang", "Jumlah", "Tanggal Pinjam" }, 0);
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        refreshTable();
        
        // Put table in a scroll pane with a titled border
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Daftar Barang Dipinjam"));

        // Form panel for the button
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Form Pengembalian"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnKembalikan = new JButton("Kembalikan Barang");
        btnKembalikan.addActionListener(e -> handlePengembalian());
        buttonPanel.add(btnKembalikan);
        
        // Add button panel to form panel
        formPanel.add(buttonPanel);
        
        // Add components to main panel
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(formPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);

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