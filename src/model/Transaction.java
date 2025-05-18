package model;

public class Transaction {
    private String username;
    private String assetName;
    private int quantity;
    private String status; // "Pending", "Dipinjam", "Rejected", "Dikembalikan"
    private String date;

    public Transaction(String username, String assetName, int quantity, String status, String date) {
        this.username = username;
        this.assetName = assetName;
        this.quantity = quantity;
        this.status = status;
        this.date = date;
    }

    public String getUsername() { return username; }
    public String getAssetName() { return assetName; }
    public int getQuantity() { return quantity; }
    public String getStatus() { return status; }
    public String getDate() { return date; }

    public void setStatus(String status) { this.status = status; } // Tambahan setter supaya admin bisa update status
}
