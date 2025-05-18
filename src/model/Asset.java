package model;

public class Asset {
    private String name;
    private int quantity;
    private String category; // Tambahan
    private String qrCode;    // Tambahan (bisa berupa string sederhana untuk sekarang)

    public Asset(String name, int quantity, String category, String qrCode) {
        this.name = name;
        this.quantity = quantity;
        this.category = category;
        this.qrCode = qrCode;
    }

    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public String getCategory() { return category; }
    public String getQrCode() { return qrCode; }

    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setCategory(String category) { this.category = category; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }
}
