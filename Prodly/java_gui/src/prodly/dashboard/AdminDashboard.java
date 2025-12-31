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
import prodly.ui.ModernTheme;

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
        welcomeLabel.setFont(ModernTheme.FONT_HEADING);
        welcomeLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        
        viewAuditButton = new JButton("View Audit Logs");
        systemSettingsButton = new JButton("System Settings");
        userManagementButton = new JButton("User Management");
        reportsButton = new JButton("Reports & Analytics");
        analyticsButton = new JButton("Analytics Dashboard");
        searchButton = new JButton("Search");
        backupButton = new JButton("Backup & Restore");
        
        ModernTheme.styleModernButton(viewAuditButton, ModernTheme.PRIMARY);
        ModernTheme.styleModernButton(systemSettingsButton, ModernTheme.ACCENT);
        ModernTheme.styleModernButton(userManagementButton, ModernTheme.ACCENT_ORANGE);
        ModernTheme.styleModernButton(reportsButton, ModernTheme.ACCENT_PURPLE);
        ModernTheme.styleModernButton(analyticsButton, ModernTheme.ACCENT_BLUE);
        ModernTheme.styleModernButton(searchButton, ModernTheme.PRIMARY_LIGHT);
        ModernTheme.styleModernButton(backupButton, ModernTheme.SUCCESS);
        
        // Set button sizes
        Dimension buttonSize = new Dimension(280, 50);
        viewAuditButton.setPreferredSize(buttonSize);
        systemSettingsButton.setPreferredSize(buttonSize);
        userManagementButton.setPreferredSize(buttonSize);
        reportsButton.setPreferredSize(buttonSize);
        analyticsButton.setPreferredSize(buttonSize);
        searchButton.setPreferredSize(buttonSize);
        backupButton.setPreferredSize(buttonSize);
    }
    
    private void layoutComponents() {
        getContentPane().setBackground(ModernTheme.BACKGROUND);
        setLayout(new BorderLayout());
        
        // Top panel with modern styling
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(ModernTheme.PRIMARY);
        topPanel.setBorder(BorderFactory.createEmptyBorder(ModernTheme.PADDING_LARGE, 
            ModernTheme.PADDING_XLARGE, ModernTheme.PADDING_LARGE, ModernTheme.PADDING_XLARGE));
        welcomeLabel.setForeground(Color.WHITE);
        topPanel.add(welcomeLabel);
        
        // Center panel - Admin options with card layout
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(ModernTheme.BACKGROUND);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(ModernTheme.PADDING_XLARGE, 
            ModernTheme.PADDING_XLARGE, ModernTheme.PADDING_XLARGE, ModernTheme.PADDING_XLARGE));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM, 
            ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM);
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

