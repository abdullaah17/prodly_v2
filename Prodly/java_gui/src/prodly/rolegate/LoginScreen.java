package prodly.rolegate;

import prodly.integration.InputWriter;
import prodly.integration.CppRunner;
import prodly.integration.OutputReader;
import prodly.dashboard.EmployeeDashboard;
import prodly.dashboard.ManagerDashboard;
import prodly.dashboard.AdminDashboard;
import prodly.ui.ModernTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class LoginScreen extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;
    private JButton loginButton;
    private JButton signupButton;
    private JLabel statusLabel;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    public LoginScreen() {
        setTitle("Prodly - Role-Based Onboarding System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 650);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Modern look and feel
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
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        roleCombo = new JComboBox<>(new String[]{"employee", "manager", "admin"});
        loginButton = new JButton("Login");
        signupButton = new JButton("Sign Up");
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(ModernTheme.ERROR);
        statusLabel.setFont(ModernTheme.FONT_BODY);
        
        // Apply modern styling
        ModernTheme.styleTextField(usernameField);
        ModernTheme.stylePasswordField(passwordField);
        ModernTheme.styleModernButton(loginButton, ModernTheme.PRIMARY);
        ModernTheme.styleModernButton(signupButton, ModernTheme.ACCENT);
        
        // Style combo box
        roleCombo.setFont(ModernTheme.FONT_BODY);
        roleCombo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ModernTheme.BORDER, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        roleCombo.setBackground(ModernTheme.SURFACE);
    }
    
    private void layoutComponents() {
        getContentPane().setBackground(ModernTheme.BACKGROUND);
        setLayout(new BorderLayout());
        
        // Header with modern gradient effect
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(ModernTheme.PRIMARY);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(ModernTheme.PADDING_XLARGE, 
            ModernTheme.PADDING_LARGE, ModernTheme.PADDING_XLARGE, ModernTheme.PADDING_LARGE));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        
        JLabel titleLabel = new JLabel("PRODLY");
        titleLabel.setFont(ModernTheme.FONT_TITLE);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);
        
        headerPanel.add(Box.createVerticalStrut(8));
        
        JLabel subtitleLabel = new JLabel("Role-Based Onboarding Engine");
        subtitleLabel.setFont(ModernTheme.FONT_SUBTITLE);
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(subtitleLabel);
        
        // Form panel with card style
        JPanel formPanel = ModernTheme.createCardPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ModernTheme.BORDER, 1),
            BorderFactory.createEmptyBorder(ModernTheme.PADDING_XLARGE, ModernTheme.PADDING_XLARGE, 
                ModernTheme.PADDING_XLARGE, ModernTheme.PADDING_XLARGE)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM, 
            ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Username
        gbc.gridx = 0; gbc.gridy = 0; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel userLabel = new JLabel("Username:");
        ModernTheme.styleLabel(userLabel, false);
        formPanel.add(userLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        usernameField.setPreferredSize(new Dimension(250, 0));
        formPanel.add(usernameField, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel passLabel = new JLabel("Password:");
        ModernTheme.styleLabel(passLabel, false);
        formPanel.add(passLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        passwordField.setPreferredSize(new Dimension(250, 0));
        formPanel.add(passwordField, gbc);
        
        // Role (for signup)
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel roleLabel = new JLabel("Role:");
        ModernTheme.styleLabel(roleLabel, false);
        formPanel.add(roleLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        roleCombo.setPreferredSize(new Dimension(250, 0));
        formPanel.add(roleCombo, gbc);
        
        // Status label
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(ModernTheme.PADDING_MEDIUM, 0, ModernTheme.PADDING_SMALL, 0);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(statusLabel, gbc);
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5; gbc.insets = new Insets(ModernTheme.PADDING_MEDIUM, 
            ModernTheme.PADDING_SMALL, 0, ModernTheme.PADDING_SMALL);
        formPanel.add(signupButton, gbc);
        
        gbc.gridx = 1;
        formPanel.add(loginButton, gbc);
        
        // Main container
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(ModernTheme.BACKGROUND);
        mainContainer.setBorder(BorderFactory.createEmptyBorder(ModernTheme.PADDING_LARGE, 
            ModernTheme.PADDING_LARGE, ModernTheme.PADDING_LARGE, ModernTheme.PADDING_LARGE));
        mainContainer.add(formPanel, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        add(mainContainer, BorderLayout.CENTER);
    }
    
    private void attachListeners() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
        
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSignup();
            }
        });
        
        // Enter key support
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
    }
    
    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter username and password");
            return;
        }
        
        try {
            // Build JSON request
            JSONObject request = new JSONObject();
            request.put("action", "login");
            request.put("username", username);
            request.put("password", password);
            
            // Write to input.json
            InputWriter.write(request.toJSONString());
            
            // Run C++ core
            CppRunner.runCore();
            
            // Read response
            String responseStr = OutputReader.read();
            JSONParser parser = new JSONParser();
            JSONObject response = (JSONObject) parser.parse(responseStr);
            
            String status = (String) response.get("status");
            
            if ("success".equals(status)) {
                String role = (String) response.get("role");
                statusLabel.setText("Login successful!");
                statusLabel.setForeground(ModernTheme.SUCCESS);
                
                // Navigate to appropriate dashboard
                SwingUtilities.invokeLater(() -> {
                    openDashboard(role, username);
                });
            } else {
                String message = (String) response.get("message");
                statusLabel.setText(message != null ? message : "Login failed");
                statusLabel.setForeground(ModernTheme.ERROR);
            }
        } catch (Exception ex) {
            statusLabel.setText("Error: " + ex.getMessage());
            statusLabel.setForeground(Color.RED);
            ex.printStackTrace();
        }
    }
    
    private void performSignup() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String role = (String) roleCombo.getSelectedItem();
        
        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter username and password");
            return;
        }
        
        try {
            JSONObject request = new JSONObject();
            request.put("action", "signup");
            request.put("username", username);
            request.put("password", password);
            request.put("role", role);
            
            InputWriter.write(request.toJSONString());
            CppRunner.runCore();
            
            String responseStr = OutputReader.read();
            JSONParser parser = new JSONParser();
            JSONObject response = (JSONObject) parser.parse(responseStr);
            
            String status = (String) response.get("status");
            
            if ("success".equals(status)) {
                statusLabel.setText("Account created successfully! Logging in...");
                statusLabel.setForeground(ModernTheme.SUCCESS);
                
                // Automatically log in the newly created user
                SwingUtilities.invokeLater(() -> {
                    // Use the role from signup and login automatically
                    openDashboard(role, username);
                });
            } else {
                String message = (String) response.get("message");
                statusLabel.setText(message != null ? message : "Signup failed");
                statusLabel.setForeground(ModernTheme.ERROR);
            }
        } catch (Exception ex) {
            statusLabel.setText("Error: " + ex.getMessage());
            statusLabel.setForeground(Color.RED);
            ex.printStackTrace();
        }
    }
    
    private void openDashboard(String role, String username) {
        this.setVisible(false);
        
        JFrame dashboard = null;
        if ("employee".equals(role)) {
            dashboard = new EmployeeDashboard(username, role);
        } else if ("manager".equals(role)) {
            dashboard = new ManagerDashboard(username, role);
        } else if ("admin".equals(role)) {
            dashboard = new AdminDashboard(username, role);
        }
        
        if (dashboard != null) {
            dashboard.setVisible(true);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginScreen().setVisible(true);
        });
    }
}

