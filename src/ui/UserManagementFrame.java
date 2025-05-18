package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import dao.UserDAO;
import model.User;

public class UserManagementFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public UserManagementFrame() {
        setTitle("Manajemen User");
        setSize(650, 450);
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
        
        JLabel titleLabel = new JLabel("Daftar Pengguna");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Create table
        tableModel = new DefaultTableModel(new String[] { "Username", "Role" }, 0) {
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
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JButton btnAdd = new JButton("Tambah User");
        btnAdd.setFont(new Font("Arial", Font.BOLD, 12));
        btnAdd.setBackground(new Color(70, 130, 180));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        JButton btnDelete = new JButton("Hapus User");
        btnDelete.setFont(new Font("Arial", Font.BOLD, 12));
        btnDelete.setBackground(new Color(220, 53, 69));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDelete.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnDelete.addActionListener(e -> deleteSelectedUser());
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(Box.createHorizontalStrut(10)); // Add spacing between buttons
        buttonPanel.add(btnDelete);
        
        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Set content pane
        setContentPane(mainPanel);
        
        // Initialize table data
        refreshTable();
        
        setVisible(true);
    }

    private void refreshTable() {
        tableModel.setRowCount(0); // Bersihkan tabel
        for (User u : UserDAO.getAllUsers()) {
            tableModel.addRow(new Object[] { u.getUsername(), u.getRole().toString() });
        }
    }

    private void deleteSelectedUser() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String username = (String) tableModel.getValueAt(selectedRow, 0);

            // Jangan izinkan hapus user admin
            if (username.equals("admin")) {
                JOptionPane.showMessageDialog(this, 
                    "User admin tidak bisa dihapus.", 
                    "Operasi Dibatasi",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Apakah Anda yakin ingin menghapus user \"" + username + "\"?",
                    "Konfirmasi Hapus",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = UserDAO.deleteUser(username);
                if (success) {
                    JOptionPane.showMessageDialog(this, 
                        "User berhasil dihapus.", 
                        "Sukses", 
                        JOptionPane.INFORMATION_MESSAGE);
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Gagal menghapus user.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Pilih user yang ingin dihapus.", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
        }
    }
}