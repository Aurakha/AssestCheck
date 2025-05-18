import ui.LoginFrame;

import javax.swing.UIManager;

import data.Database;
import model.User;
import model.Role;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            if (Database.users.isEmpty()) {
                Database.users.add(new User("admin", "admin", Role.ADMIN));
                System.out.println("User admin default ditambahkan.");
            }

            // Start aplikasi dengan Login Frame
            new LoginFrame();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Inisialisasi data awal (jika belum ada)
        
    }
}
