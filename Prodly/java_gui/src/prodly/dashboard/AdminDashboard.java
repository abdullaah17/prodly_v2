package prodly.dashboard;

import prodly.integration.InputWriter;
import prodly.integration.CppRunner;
import prodly.integration.OutputReader;
import prodly.audit.AuditLogUI;
import prodly.admin.UserManagementUI;
import prodly.admin.SystemSettingsUI;

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
        
        styleButton(viewAuditButton, new Color(70, 130, 180));
        styleButton(systemSettingsButton, new Color(60, 179, 113));
        styleButton(userManagementButton, new Color(255, 140, 0));
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
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0; gbc.gridy = 0;
        centerPanel.add(viewAuditButton, gbc);
        
        gbc.gridy = 1;
        centerPanel.add(systemSettingsButton, gbc);
        
        gbc.gridy = 2;
        centerPanel.add(userManagementButton, gbc);
        
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
    }
}

