package prodly.dashboard;

import prodly.integration.InputWriter;
import prodly.integration.CppRunner;
import prodly.integration.OutputReader;
import prodly.audit.AuditLogUI;
import prodly.admin.UserManagementUI;
import prodly.admin.SystemSettingsUI;
import prodly.reports.ReportsUI;
import prodly.analytics.AnalyticsDashboard;
import prodly.search.SearchUI;
import prodly.backup.BackupRestoreUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class AdminDashboard extends JFrame {
    private String username;
    private String role;
    private JLabel welcomeLabel;
    private JButton viewAuditButton;
    private JButton systemSettingsButton;
    private JButton userManagementButton;
    private JButton reportsButton;
    private JButton analyticsButton;
    private JButton searchButton;
    private JButton backupButton;
    
    public AdminDashboard(String username, String role) {
        this.username = username;
        this.role = role;
        
        setTitle("Prodly - Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        initializeComponents();
        layoutComponents();
        attachListeners();
    }
    
    private void initializeComponents() {
        welcomeLabel = new JLabel("Admin Dashboard - " + username);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        viewAuditButton = new JButton("View Audit Logs");
        systemSettingsButton = new JButton("System Settings");
        userManagementButton = new JButton("User Management");
        reportsButton = new JButton("Reports & Analytics");
        analyticsButton = new JButton("Analytics Dashboard");
        searchButton = new JButton("Search");
        backupButton = new JButton("Backup & Restore");
        
        styleButton(viewAuditButton, new Color(70, 130, 180));
        styleButton(systemSettingsButton, new Color(60, 179, 113));
        styleButton(userManagementButton, new Color(255, 140, 0));
        styleButton(reportsButton, new Color(138, 43, 226));
        styleButton(analyticsButton, new Color(30, 144, 255));
        styleButton(searchButton, new Color(72, 61, 139));
        styleButton(backupButton, new Color(34, 139, 34));
    }
    
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(250, 60));
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        // Top panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(245, 245, 245));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        topPanel.add(welcomeLabel);
        
        // Center panel - Admin options
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.gridx = 0; gbc.gridy = 0;
        centerPanel.add(viewAuditButton, gbc);
        
        gbc.gridy = 1;
        centerPanel.add(systemSettingsButton, gbc);
        
        gbc.gridy = 2;
        centerPanel.add(userManagementButton, gbc);
        
        gbc.gridy = 3;
        centerPanel.add(reportsButton, gbc);
        
        gbc.gridy = 4;
        centerPanel.add(analyticsButton, gbc);
        
        gbc.gridy = 5;
        centerPanel.add(searchButton, gbc);
        
        gbc.gridy = 6;
        centerPanel.add(backupButton, gbc);
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private void attachListeners() {
        viewAuditButton.addActionListener(e -> {
            new AuditLogUI(username, role).setVisible(true);
        });
        
        systemSettingsButton.addActionListener(e -> {
            new SystemSettingsUI(username, role).setVisible(true);
        });
        
        userManagementButton.addActionListener(e -> {
            new UserManagementUI(username, role).setVisible(true);
        });
        
        reportsButton.addActionListener(e -> {
            new ReportsUI(username, role).setVisible(true);
        });
        
        analyticsButton.addActionListener(e -> {
            new AnalyticsDashboard(username, role).setVisible(true);
        });
        
        searchButton.addActionListener(e -> {
            new SearchUI(username, role).setVisible(true);
        });
        
        backupButton.addActionListener(e -> {
            new BackupRestoreUI(username, role).setVisible(true);
        });
    }
}

