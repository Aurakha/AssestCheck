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

        // Main panel with Border Layout
        JPanel mainPanel = new JPanel(new BorderLayout(0, 15)); // Add vertical gap between components
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around the frame
        
        // Table setup
        tableModel = new DefaultTableModel(new String[] { "Nama Barang", "Jumlah Tersedia" }, 0);
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        refreshTable();
        
        // Put table in a scroll pane with a titled border
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Daftar Barang"));

        // Form panel for input
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Form Peminjaman"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Input components
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tfJumlah = new JTextField(10);
        inputPanel.add(new JLabel("Jumlah yang ingin dipinjam:"));
        inputPanel.add(tfJumlah);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnRequest = new JButton("Request Pinjam");
        buttonPanel.add(btnRequest);
        
        // Add input and button panels to form panel
        formPanel.add(inputPanel);
        formPanel.add(buttonPanel);
        
        // Add action listener to button
        btnRequest.addActionListener(e -> handleRequestPinjam());
        
        // Add components to main panel
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(formPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);

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