package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import dao.TransactionDAO;
import dao.AssetDAO;
import dao.LogDAO;
import model.Transaction;
import java.util.List;

public class RequestPeminjamanFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public RequestPeminjamanFrame() {
        setTitle("Request Peminjaman");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        tableModel = new DefaultTableModel(new String[] { "Username", "Nama Barang", "Jumlah", "Status", "Tanggal" }, 0);
        table = new JTable(tableModel);
        refreshTable();

        JButton btnApprove = new JButton("Approve");
        JButton btnReject = new JButton("Reject");
        JPanel actionPanel = new JPanel();
        actionPanel.add(btnApprove);
        actionPanel.add(btnReject);

        btnApprove.addActionListener(e -> handleAction("Dipinjam"));
        btnReject.addActionListener(e -> handleAction("Ditolak"));

        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Transaction> list = TransactionDAO.getTransactionsByStatus("Request");
        for (Transaction t : list) {
            tableModel.addRow(new Object[] {
                t.getUsername(),
                t.getAssetName(),
                t.getQuantity(),
                t.getStatus(),
                t.getDate()
            });
        }
    }

    private void handleAction(String newStatus) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih request terlebih dahulu.");
            return;
        }

        String username = (String) tableModel.getValueAt(row, 0);
        String assetName = (String) tableModel.getValueAt(row, 1);
        int qty = (int) tableModel.getValueAt(row, 2);

        boolean updated = TransactionDAO.updateTransactionStatus(username, assetName, qty, newStatus);
        if (updated) {
            if (newStatus.equals("Dipinjam")) {
                AssetDAO.updateQuantity(assetName, AssetDAO.getAssetByName(assetName).getQuantity());
            } else if (newStatus.equals("Ditolak")) {
                // rollback stok (di DAO logic sebelumnya, stok tidak dikurangi permanen)
                AssetDAO.updateQuantity(assetName, AssetDAO.getAssetByName(assetName).getQuantity() + qty);
            }
            LogDAO.insertLog("Admin mengubah status peminjaman " + assetName + " oleh " + username + " menjadi " + newStatus);
            JOptionPane.showMessageDialog(this, "Request telah " + (newStatus.equals("Dipinjam") ? "disetujui." : "ditolak."));
        } else {
            JOptionPane.showMessageDialog(this, "Gagal memproses request.");
        }
        refreshTable();
    }
}
