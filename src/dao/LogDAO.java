package dao;

import model.Log;
import data.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogDAO {

    // Tambahkan log baru
    public static void insertLog(String message) {
        String sql = "INSERT INTO logs (message) VALUES (?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, message);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ambil semua log
    public static List<Log> getAllLogs() {
        List<Log> logs = new ArrayList<>();
        String sql = "SELECT message, timestamp FROM logs ORDER BY id DESC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String msg = rs.getString("message");
                String ts = rs.getString("timestamp");
                logs.add(new Log(msg, ts));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }
}
