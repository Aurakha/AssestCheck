package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import dao.AssetDAO;
import dao.TransactionDAO;
import dao.LogDAO;
import model.Asset;
import model.Transaction;

import java.time.LocalDate;
import java.util.List;

public class BorrowFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField tfJumlah;
    private JButton btnRequest;
    private String currentUser;

    public BorrowFrame(String username) {
        this.currentUser = username;

        setTitle("Peminjaman Barang");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        tableModel = new DefaultTableModel(new String[] { "Nama Barang", "Jumlah Tersedia" }, 0);
        table = new JTable(tableModel);
        refreshTable();

        JPanel inputPanel = new JPanel(new FlowLayout());
        tfJumlah = new JTextField(5);
        btnRequest = new JButton("Request Pinjam");

        inputPanel.add(new JLabel("Jumlah yang ingin dipinjam:"));
        inputPanel.add(tfJumlah);
        inputPanel.add(btnRequest);

        btnRequest.addActionListener(e -> handleRequestPinjam());

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Asset> assets = AssetDAO.getAllAssets();
        for (Asset a : assets) {
            tableModel.addRow(new Object[] { a.getName(), a.getQuantity() });
        }
    }

    private void handleRequestPinjam() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih barang terlebih dahulu.");
            return;
        }

        String assetName = (String) tableModel.getValueAt(selectedRow, 0);
        int availableQty = (int) tableModel.getValueAt(selectedRow, 1);
        String jumlahText = tfJumlah.getText().trim();

        if (jumlahText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan jumlah yang ingin dipinjam.");
            return;
        }

        try {
            int qtyToRequest = Integer.parseInt(jumlahText);
            if (qtyToRequest <= 0) {
                JOptionPane.showMessageDialog(this, "Jumlah harus lebih dari 0.");
                return;
            }

            if (qtyToRequest > availableQty) {
                JOptionPane.showMessageDialog(this, "Jumlah melebihi stok tersedia.");
                return;
            }

            // Kurangi stok barang sementara
            int newQty = availableQty - qtyToRequest;
            AssetDAO.updateQuantity(assetName, newQty);

            // Buat permintaan peminjaman (status "Request")
            String date = LocalDate.now().toString();
            Transaction t = new Transaction(currentUser, assetName, qtyToRequest, "Request", date);
            TransactionDAO.insertTransaction(t);

            // Tambahkan log
            LogDAO.insertLog("User '" + currentUser + "' mengajukan permintaan pinjam " + qtyToRequest + " " + assetName);

            JOptionPane.showMessageDialog(this, "Permintaan pinjam berhasil diajukan. Menunggu persetujuan.");
            tfJumlah.setText("");
            refreshTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka.");
        }
    }
}
