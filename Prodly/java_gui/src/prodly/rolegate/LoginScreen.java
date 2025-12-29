package prodly.rolegate;

import prodly.integration.InputWriter;
import prodly.integration.CppRunner;
import prodly.integration.OutputReader;
import prodly.dashboard.EmployeeDashboard;
import prodly.dashboard.ManagerDashboard;
import prodly.dashboard.AdminDashboard;

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
        setSize(500, 600);
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
        statusLabel.setForeground(Color.RED);
        
        // Style buttons
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        signupButton.setBackground(new Color(60, 179, 113));
        signupButton.setForeground(Color.WHITE);
        signupButton.setFocusPainted(false);
        signupButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(25, 25, 112));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        
        JLabel titleLabel = new JLabel("PRODLY");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        JLabel subtitleLabel = new JLabel("Role-Based Onboarding Engine");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(200, 200, 200));
        headerPanel.add(subtitleLabel);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(userLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        formPanel.add(usernameField, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(passLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        formPanel.add(passwordField, gbc);
        
        // Role (for signup)
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(roleLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        roleCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(roleCombo, gbc);
        
        // Status label
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(statusLabel, gbc);
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        signupButton.setPreferredSize(new Dimension(0, 40));
        formPanel.add(signupButton, gbc);
        
        gbc.gridx = 1;
        loginButton.setPreferredSize(new Dimension(0, 40));
        formPanel.add(loginButton, gbc);
        
        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
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
                statusLabel.setForeground(new Color(60, 179, 113));
                
                // Navigate to appropriate dashboard
                SwingUtilities.invokeLater(() -> {
                    openDashboard(role, username);
                });
            } else {
                String message = (String) response.get("message");
                statusLabel.setText(message != null ? message : "Login failed");
                statusLabel.setForeground(Color.RED);
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
                statusLabel.setForeground(new Color(60, 179, 113));
                
                // Automatically log in the newly created user
                SwingUtilities.invokeLater(() -> {
                    // Use the role from signup and login automatically
                    openDashboard(role, username);
                });
            } else {
                String message = (String) response.get("message");
                statusLabel.setText(message != null ? message : "Signup failed");
                statusLabel.setForeground(Color.RED);
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

