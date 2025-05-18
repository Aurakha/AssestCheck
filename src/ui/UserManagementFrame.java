package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import dao.UserDAO;
import model.User;

public class UserManagementFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public UserManagementFrame() {
        setTitle("Manajemen User");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        tableModel = new DefaultTableModel(new String[] { "Username", "Role" }, 0);
        table = new JTable(tableModel);
        refreshTable();

        JButton btnDelete = new JButton("Hapus User");
        btnDelete.addActionListener(e -> deleteSelectedUser());

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(btnDelete, BorderLayout.SOUTH);

        add(panel);
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
                JOptionPane.showMessageDialog(this, "User admin tidak bisa dihapus.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Apakah Anda yakin ingin menghapus user \"" + username + "\"?",
                    "Konfirmasi",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = UserDAO.deleteUser(username);
                if (success) {
                    JOptionPane.showMessageDialog(this, "User berhasil dihapus.");
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus user.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih user yang ingin dihapus.");
        }
    }
}
