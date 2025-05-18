package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        headerPanel.setBackground(new Color(245, 245, 245));
        
        JLabel titleLabel = new JLabel("Daftar Permintaan Peminjaman");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JLabel subtitleLabel = new JLabel("Menampilkan permintaan peminjaman yang perlu diproses");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(245, 245, 245));
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitleLabel);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        
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
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(100); // Username
        table.getColumnModel().getColumn(1).setPreferredWidth(180); // Nama Barang
        table.getColumnModel().getColumn(2).setPreferredWidth(60);  // Jumlah
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Status
        table.getColumnModel().getColumn(4).setPreferredWidth(120); // Tanggal
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        
        JPanel actionPanel = new JPanel();
        actionPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        actionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        
        JButton btnApprove = new JButton("Setujui");
        btnApprove.setFont(new Font("Arial", Font.BOLD, 12));
        btnApprove.setBackground(new Color(40, 167, 69));
        btnApprove.setForeground(Color.WHITE);
        btnApprove.setFocusPainted(false);
        btnApprove.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnApprove.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnApprove.addActionListener(e -> handleAction("Dipinjam"));
        
        JButton btnReject = new JButton("Tolak");
        btnReject.setFont(new Font("Arial", Font.BOLD, 12));
        btnReject.setBackground(new Color(220, 53, 69));
        btnReject.setForeground(Color.WHITE);
        btnReject.setFocusPainted(false);
        btnReject.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReject.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnReject.addActionListener(e -> handleAction("Ditolak"));
        
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setFont(new Font("Arial", Font.BOLD, 12));
        btnRefresh.setBackground(new Color(108, 117, 125));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnRefresh.addActionListener(e -> refreshTable());
        
        actionPanel.add(btnApprove);
        actionPanel.add(btnReject);
        actionPanel.add(btnRefresh);
        
        // Information panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JLabel infoLabel = new JLabel("* Pilih baris dalam tabel untuk memproses permintaan");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        infoLabel.setForeground(new Color(100, 100, 100));
        infoPanel.add(infoLabel);
        
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.add(infoPanel, BorderLayout.WEST);
        footerPanel.add(actionPanel, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
        
        refreshTable();
        
        setVisible(true);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        try {
            List<Transaction> list = TransactionDAO.getTransactionsByStatus("Request");
            if (list.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Tidak ada permintaan peminjaman baru.",
                    "Informasi",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
            for (Transaction t : list) {
                tableModel.addRow(new Object[] {
                    t.getUsername(),
                    t.getAssetName(),
                    t.getQuantity(),
                    t.getStatus(),
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

    private void handleAction(String newStatus) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                "Silakan pilih permintaan terlebih dahulu.",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String username = (String) tableModel.getValueAt(row, 0);
        String assetName = (String) tableModel.getValueAt(row, 1);
        int qty = (int) tableModel.getValueAt(row, 2);
        
        // Confirmation dialog
        String actionText = newStatus.equals("Dipinjam") ? "menyetujui" : "menolak";
        String message = String.format("Apakah Anda yakin ingin %s permintaan peminjaman '%s' oleh '%s'?", 
                                      actionText, assetName, username);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
                                                   message, 
                                                   "Konfirmasi", 
                                                   JOptionPane.YES_NO_OPTION,
                                                   JOptionPane.QUESTION_MESSAGE);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        try {
            boolean updated = TransactionDAO.updateTransactionStatus(username, assetName, qty, newStatus);
            if (updated) {
                if (newStatus.equals("Dipinjam")) {
                    AssetDAO.updateQuantity(assetName, AssetDAO.getAssetByName(assetName).getQuantity());
                } else if (newStatus.equals("Ditolak")) {
                    AssetDAO.updateQuantity(assetName, AssetDAO.getAssetByName(assetName).getQuantity() + qty);
                }
                
                LogDAO.insertLog("Admin mengubah status peminjaman " + assetName + " oleh " + username + " menjadi " + newStatus);
                
                String resultText = newStatus.equals("Dipinjam") ? "disetujui" : "ditolak";
                JOptionPane.showMessageDialog(this,
                    "Permintaan peminjaman berhasil " + resultText + ".",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Gagal memproses permintaan peminjaman.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
        
        refreshTable();
    }
}