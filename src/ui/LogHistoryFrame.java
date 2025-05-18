package ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import dao.LogDAO;
import model.Log;

public class LogHistoryFrame extends JFrame {
    private JTextArea textArea;

    public LogHistoryFrame() {
        setTitle("Log Aktivitas Sistem");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        add(scrollPane, BorderLayout.CENTER);
        loadLogs();
        setVisible(true);
    }

    private void loadLogs() {
        List<Log> logs = LogDAO.getAllLogs();
        textArea.setText(""); // Bersihkan dulu
        for (Log log : logs) {
            textArea.append("[" + log.getTimestamp() + "] " + log.getMessage() + "\n");
        }
    }
}
