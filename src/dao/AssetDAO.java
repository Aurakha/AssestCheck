package dao;

import model.Asset;
import data.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssetDAO {

    // Ambil semua asset beserta kategori
    public static List<Asset> getAllAssets() {
        List<Asset> assets = new ArrayList<>();
        String sql = "SELECT a.name, a.quantity, c.name AS category, a.qr_code " +
                     "FROM assets a " +
                     "JOIN categories c ON a.category_id = c.id";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                assets.add(new Asset(
                    rs.getString("name"),
                    rs.getInt("quantity"),
                    rs.getString("category"),
                    rs.getString("qr_code")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assets;
    }

    // Ambil asset berdasarkan nama
    public static Asset getAssetByName(String name) {
        String sql = "SELECT a.name, a.quantity, c.name AS category, a.qr_code " +
                     "FROM assets a " +
                     "JOIN categories c ON a.category_id = c.id " +
                     "WHERE a.name = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Asset(
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getString("category"),
                        rs.getString("qr_code")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Ambil asset berdasarkan QR Code
    public static Asset getAssetByQrCode(String qrCode) {
        String sql = "SELECT a.name, a.quantity, c.name AS category, a.qr_code " +
                     "FROM assets a " +
                     "JOIN categories c ON a.category_id = c.id " +
                     "WHERE a.qr_code = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, qrCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Asset(
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getString("category"),
                        rs.getString("qr_code")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Insert asset baru dengan kategori terasosiasi
    public static boolean insertAsset(Asset asset) {
        String sql = "INSERT INTO assets (name, quantity, category_id, qr_code) " +
                     "VALUES (?, ?, (SELECT id FROM categories WHERE name = ?), ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, asset.getName());
            ps.setInt(2, asset.getQuantity());
            ps.setString(3, asset.getCategory());
            ps.setString(4, asset.getQrCode());

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update quantity asset
    public static boolean updateQuantity(String assetName, int newQuantity) {
        String sql = "UPDATE assets SET quantity = ? WHERE name = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, newQuantity);     // Tetapkan nilai baru langsung
            ps.setString(2, assetName);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean increaseQuantity(String assetName, int quantityToAdd) {
        String sql = "UPDATE assets SET quantity = quantity + ? WHERE name = ?";
    
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
    
            ps.setInt(1, quantityToAdd);
            ps.setString(2, assetName);
    
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
