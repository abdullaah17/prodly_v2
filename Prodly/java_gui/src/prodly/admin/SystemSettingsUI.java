package prodly.admin;

import prodly.integration.InputWriter;
import prodly.integration.CppRunner;
import prodly.integration.OutputReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class SystemSettingsUI extends JFrame {
    private String username;
    private String role;
    
    private JTextField minPasswordLengthField;
    private JTextField sessionTimeoutField;
    private JCheckBox requireStrongPasswordCheck;
    private JCheckBox enableAuditLogCheck;
    private JComboBox<String> defaultRoleCombo;
    private JTextArea systemInfoArea;
    
    private JButton saveButton;
    private JButton resetButton;
    private JButton refreshButton;
    
    public SystemSettingsUI(String username, String role) {
        this.username = username;
        this.role = role;
        
        setTitle("System Settings - " + username);
        setSize(700, 600);
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
        loadSettings();
    }
    
    private void initializeComponents() {
        minPasswordLengthField = new JTextField("8", 10);
        sessionTimeoutField = new JTextField("30", 10);
        requireStrongPasswordCheck = new JCheckBox("Require Strong Passwords");
        enableAuditLogCheck = new JCheckBox("Enable Audit Logging");
        defaultRoleCombo = new JComboBox<>(new String[]{"employee", "manager", "admin"});
        
        systemInfoArea = new JTextArea(8, 40);
        systemInfoArea.setEditable(false);
        systemInfoArea.setFont(new Font("Courier New", Font.PLAIN, 11));
        systemInfoArea.setBackground(new Color(245, 245, 245));
        
        saveButton = new JButton("Save Settings");
        resetButton = new JButton("Reset to Defaults");
        refreshButton = new JButton("Refresh");
        
        styleButton(saveButton, new Color(60, 179, 113));
        styleButton(resetButton, new Color(255, 140, 0));
        styleButton(refreshButton, new Color(70, 130, 180));
    }
    
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(150, 35));
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        // Top panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(245, 245, 245));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("System Settings");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        topPanel.add(titleLabel);
        
        // Center panel - Settings form
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel settingsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Security Settings
        JLabel securityLabel = new JLabel("Security Settings");
        securityLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        settingsPanel.add(securityLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        settingsPanel.add(new JLabel("Minimum Password Length:"), gbc);
        gbc.gridx = 1;
        settingsPanel.add(minPasswordLengthField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        settingsPanel.add(new JLabel("Session Timeout (minutes):"), gbc);
        gbc.gridx = 1;
        settingsPanel.add(sessionTimeoutField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        settingsPanel.add(requireStrongPasswordCheck, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        settingsPanel.add(enableAuditLogCheck, gbc);
        
        // User Settings
        JLabel userLabel = new JLabel("User Settings");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        settingsPanel.add(userLabel, gbc);
        
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 6;
        settingsPanel.add(new JLabel("Default Role for New Users:"), gbc);
        gbc.gridx = 1;
        settingsPanel.add(defaultRoleCombo, gbc);
        
        centerPanel.add(settingsPanel, BorderLayout.NORTH);
        
        // System Info
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "System Information",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 14)
        ));
        infoPanel.add(new JScrollPane(systemInfoArea), BorderLayout.CENTER);
        
        centerPanel.add(infoPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.add(refreshButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(saveButton);
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void attachListeners() {
        saveButton.addActionListener(e -> saveSettings());
        resetButton.addActionListener(e -> resetSettings());
        refreshButton.addActionListener(e -> loadSettings());
    }
    
    private void loadSettings() {
        try {
            JSONObject request = new JSONObject();
            request.put("action", "get_system_settings");
            
            InputWriter.write(request.toJSONString());
            CppRunner.runCore();
            
            String responseStr = OutputReader.read();
            JSONParser parser = new JSONParser();
            JSONObject response = (JSONObject) parser.parse(responseStr);
            
            String status = (String) response.get("status");
            
            if ("success".equals(status)) {
                long minPassword = (Long) response.get("minPasswordLength");
                long sessionTimeout = (Long) response.get("sessionTimeout");
                boolean strongPassword = (Boolean) response.get("requireStrongPassword");
                boolean auditLog = (Boolean) response.get("enableAuditLog");
                String defaultRole = (String) response.get("defaultRole");
                
                minPasswordLengthField.setText(String.valueOf(minPassword));
                sessionTimeoutField.setText(String.valueOf(sessionTimeout));
                requireStrongPasswordCheck.setSelected(strongPassword);
                enableAuditLogCheck.setSelected(auditLog);
                defaultRoleCombo.setSelectedItem(defaultRole);
                
                // System info
                long totalUsers = (Long) response.get("totalUsers");
                String version = (String) response.get("version");
                String lastBackup = (String) response.get("lastBackup");
                
                StringBuilder info = new StringBuilder();
                info.append("System Version: ").append(version).append("\n");
                info.append("Total Users: ").append(totalUsers).append("\n");
                info.append("Last Backup: ").append(lastBackup).append("\n");
                info.append("System Status: Operational\n");
                info.append("Database: In-Memory\n");
                info.append("Platform: Java + C++ Integration\n");
                
                systemInfoArea.setText(info.toString());
            }
        } catch (Exception ex) {
            // Use default values if loading fails
            systemInfoArea.setText("System Information:\nVersion: 1.0.0\nStatus: Operational");
        }
    }
    
    private void saveSettings() {
        try {
            int minPassword = Integer.parseInt(minPasswordLengthField.getText());
            int sessionTimeout = Integer.parseInt(sessionTimeoutField.getText());
            
            if (minPassword < 4 || minPassword > 20) {
                JOptionPane.showMessageDialog(this,
                    "Password length must be between 4 and 20",
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (sessionTimeout < 5 || sessionTimeout > 480) {
                JOptionPane.showMessageDialog(this,
                    "Session timeout must be between 5 and 480 minutes",
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            JSONObject request = new JSONObject();
            request.put("action", "update_system_settings");
            request.put("minPasswordLength", minPassword);
            request.put("sessionTimeout", sessionTimeout);
            request.put("requireStrongPassword", requireStrongPasswordCheck.isSelected());
            request.put("enableAuditLog", enableAuditLogCheck.isSelected());
            request.put("defaultRole", defaultRoleCombo.getSelectedItem());
            
            InputWriter.write(request.toJSONString());
            CppRunner.runCore();
            
            String responseStr = OutputReader.read();
            JSONParser parser = new JSONParser();
            JSONObject response = (JSONObject) parser.parse(responseStr);
            
            String status = (String) response.get("status");
            
            if ("success".equals(status)) {
                JOptionPane.showMessageDialog(this,
                    "Settings saved successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                loadSettings();
            } else {
                String message = (String) response.get("message");
                JOptionPane.showMessageDialog(this,
                    message != null ? message : "Failed to save settings",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Please enter valid numbers for password length and session timeout",
                "Invalid Input",
                JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void resetSettings() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Reset all settings to defaults?",
            "Confirm Reset",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            minPasswordLengthField.setText("8");
            sessionTimeoutField.setText("30");
            requireStrongPasswordCheck.setSelected(false);
            enableAuditLogCheck.setSelected(true);
            defaultRoleCombo.setSelectedItem("employee");
            
            JOptionPane.showMessageDialog(this,
                "Settings reset to defaults. Click 'Save Settings' to apply.",
                "Settings Reset",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

