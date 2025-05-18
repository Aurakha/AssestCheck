package data;

import java.util.ArrayList;
import java.util.List;
import model.*;

public class Database {
    public static ArrayList<User> users = new ArrayList<>();
    public static ArrayList<Asset> assets = new ArrayList<>();
    public static ArrayList<Transaction> transactions = new ArrayList<>();
    public static List<String> logs = new ArrayList<>();

    static {
        users.add(new User("admin", "admin123", Role.ADMIN));
        users.add(new User("user", "user123", Role.USER));
        
        assets.add(new Asset("Laptop Lenovo", 5, "Elektronik", "QR001"));
        assets.add(new Asset("Proyektor Epson", 2, "Elektronik", "QR002"));
        assets.add(new Asset("HDMI Cable 2m", 10, "Aksesoris", "QR003"));
        assets.add(new Asset("Meja Kerja", 15, "Peralatan Kantor", "QR004"));
        assets.add(new Asset("Kursi Kantor", 20, "Peralatan Kantor", "QR005"));
        assets.add(new Asset("Proyektor Panasonic", 3, "Elektronik", "QR006"));
        assets.add(new Asset("Router Wi-Fi", 8, "Peralatan IT", "QR007"));
        assets.add(new Asset("Kabel Ethernet 10m", 12, "Aksesoris", "QR008"));
        assets.add(new Asset("Whiteboard", 5, "Peralatan Ruang Meeting", "QR009"));
        assets.add(new Asset("Papan Tulis", 7, "Peralatan Ruang Meeting", "QR010"));
    }

    public static void addLog(String message) {
        logs.add("[LOG] " + message);
    }
}
