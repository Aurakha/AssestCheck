package dao;

import model.Transaction;
import data.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    // Tambahkan transaksi baru (Request peminjaman)
    public static boolean insertTransaction(Transaction tx) {
        String sql = "INSERT INTO transactions (user_id, asset_id, quantity, status, date) " +
                "VALUES ((SELECT id FROM users WHERE username = ?), (SELECT id FROM assets WHERE name = ?), ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tx.getUsername());
            ps.setString(2, tx.getAssetName());
            ps.setInt(3, tx.getQuantity());
            ps.setString(4, tx.getStatus());
            ps.setDate(5, Date.valueOf(tx.getDate()));

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Ambil semua transaksi (untuk admin melihat semua riwayat)
    public static List<Transaction> getAllTransactions() {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT u.username, a.name, t.quantity, t.status, t.date FROM transactions t " +
                "JOIN users u ON t.user_id = u.id " +
                "JOIN assets a ON t.asset_id = a.id ORDER BY t.id DESC";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Transaction(
                        rs.getString("username"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getString("status"),
                        rs.getDate("date").toString()));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Ambil transaksi milik user tertentu
    public static List<Transaction> getUserTransactions(String username) {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT a.name, t.quantity, t.status, t.date FROM transactions t " +
                "JOIN users u ON t.user_id = u.id " +
                "JOIN assets a ON t.asset_id = a.id " +
                "WHERE u.username = ? ORDER BY t.id DESC";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Transaction(
                        username,
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getString("status"),
                        rs.getDate("date").toString()));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Ambil semua transaksi dengan status tertentu (misal: Request)
    public static List<Transaction> getTransactionsByStatus(String status) {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT u.username, a.name, t.quantity, t.status, t.date FROM transactions t " +
                "JOIN users u ON t.user_id = u.id " +
                "JOIN assets a ON t.asset_id = a.id " +
                "WHERE t.status = ? ORDER BY t.id DESC";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Transaction(
                        rs.getString("username"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getString("status"),
                        rs.getDate("date").toString()));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Update status transaksi
    public static boolean updateTransactionStatus(String username, String assetName, int quantity, String newStatus) {
        String sql = "UPDATE transactions t " +
                "JOIN users u ON t.user_id = u.id " +
                "JOIN assets a ON t.asset_id = a.id " +
                "SET t.status = ? " +
                "WHERE u.username = ? AND a.name = ? AND t.quantity = ?";
        // (hilangkan AND status = 'Request')
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setString(2, username);
            ps.setString(3, assetName);
            ps.setInt(4, quantity);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // TransactionDAO.java
    public static List<Transaction> getTransactionsByUserAndStatus(String username, String status) {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT a.name, t.quantity, t.status, t.date "
                + "FROM transactions t "
                + "JOIN users u ON t.user_id = u.id "
                + "JOIN assets a ON t.asset_id = a.id "
                + "WHERE u.username = ? AND t.status = ? "
                + "ORDER BY t.id DESC";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Transaction(
                        username,
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getString("status"),
                        rs.getDate("date").toString()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
