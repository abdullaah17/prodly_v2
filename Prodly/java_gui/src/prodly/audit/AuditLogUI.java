package prodly.audit;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AuditLogUI extends JFrame {
    private String username;
    private String role;
    private JTextArea logArea;
    private JButton refreshButton;
    private JButton clearButton;
    
    public AuditLogUI(String username, String role) {
        this.username = username;
        this.role = role;
        
        setTitle("Audit Logs - " + username);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        initializeComponents();
        layoutComponents();
        attachListeners();
        loadLogs();
    }
    
    private void initializeComponents() {
        logArea = new JTextArea(20, 50);
        logArea.setEditable(false);
        logArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        logArea.setBackground(new Color(245, 245, 245));
        
        refreshButton = new JButton("Refresh");
        refreshButton.setBackground(new Color(70, 130, 180));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        
        clearButton = new JButton("Clear");
        clearButton.setBackground(new Color(220, 20, 60));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(new JLabel("System Audit Logs"));
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Activity Log",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 14)
        ));
        centerPanel.add(new JScrollPane(logArea), BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.add(refreshButton);
        buttonPanel.add(clearButton);
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void attachListeners() {
        refreshButton.addActionListener(e -> loadLogs());
        clearButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to clear the log?",
                "Confirm Clear",
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                logArea.setText("");
            }
        });
    }
    
    private void loadLogs() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder logs = new StringBuilder();
        
        logs.append("[").append(sdf.format(new Date())).append("] System initialized\n");
        logs.append("[").append(sdf.format(new Date())).append("] User login: ").append(username).append("\n");
        logs.append("[").append(sdf.format(new Date())).append("] Role: ").append(role).append("\n");
        logs.append("[").append(sdf.format(new Date())).append("] Dashboard accessed\n");
        logs.append("[").append(sdf.format(new Date())).append("] Onboarding data loaded\n");
        logs.append("[").append(sdf.format(new Date())).append("] Task completion tracked\n");
        logs.append("[").append(sdf.format(new Date())).append("] Evaluation completed\n");
        logs.append("[").append(sdf.format(new Date())).append("] Level assigned\n");
        logs.append("[").append(sdf.format(new Date())).append("] Recommendations generated\n");
        
        logArea.setText(logs.toString());
    }
}
