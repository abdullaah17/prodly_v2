package prodly.rolegate;

import prodly.integration.InputWriter;
import prodly.integration.CppRunner;
import prodly.integration.OutputReader;
import prodly.dashboard.EmployeeDashboard;
import prodly.dashboard.ManagerDashboard;
import prodly.dashboard.AdminDashboard;
import prodly.ui.ModernTheme;
import prodly.ui.components.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class LoginScreen extends JFrame {
    private ValidatedTextField usernameField;
    private ValidatedPasswordField passwordField;
    private JComboBox<String> roleCombo;
    private ModernButton loginButton;
    private ModernButton signupButton;
    private LoadingOverlay loadingOverlay;
    private JPanel formPanel;
    
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
        // Validated fields with validation
        usernameField = new ValidatedTextField("Username", 
            ValidatedTextField.combine(
                ValidatedTextField.required(),
                ValidatedTextField.minLength(3)
            ));
        
        passwordField = new ValidatedPasswordField("Password",
            ValidatedTextField.combine(
                ValidatedTextField.required(),
                ValidatedTextField.minLength(6)
            ));
        
        // Role combo box
        roleCombo = new JComboBox<>(new String[]{"employee", "manager", "admin"});
        roleCombo.setFont(ModernTheme.FONT_BODY);
        roleCombo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ModernTheme.BORDER, 1),
            BorderFactory.createEmptyBorder(12, 14, 12, 14)
        ));
        roleCombo.setBackground(ModernTheme.SURFACE);
        
        // Modern buttons
        loginButton = ModernButton.primary("Login");
        signupButton = ModernButton.success("Sign Up");
        
        // Loading overlay
        loadingOverlay = new LoadingOverlay();
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
        formPanel = ModernTheme.createCardPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ModernTheme.BORDER, 1),
            BorderFactory.createEmptyBorder(ModernTheme.PADDING_XLARGE, ModernTheme.PADDING_XLARGE, 
                ModernTheme.PADDING_XLARGE, ModernTheme.PADDING_XLARGE)
        ));
        
        // Username field
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, usernameField.getPreferredSize().height));
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(ModernTheme.PADDING_MEDIUM));
        
        // Password field
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, passwordField.getPreferredSize().height));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(ModernTheme.PADDING_MEDIUM));
        
        // Role combo (for signup)
        JPanel rolePanel = new JPanel(new BorderLayout());
        rolePanel.setOpaque(false);
        JLabel roleLabel = new JLabel("Role:");
        ModernTheme.styleLabel(roleLabel, false);
        roleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, ModernTheme.PADDING_SMALL, 0));
        rolePanel.add(roleLabel, BorderLayout.NORTH);
        rolePanel.add(roleCombo, BorderLayout.CENTER);
        rolePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rolePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, rolePanel.getPreferredSize().height));
        formPanel.add(rolePanel);
        formPanel.add(Box.createVerticalStrut(ModernTheme.PADDING_LARGE));
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, ModernTheme.PADDING_SMALL, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(signupButton);
        buttonPanel.add(loginButton);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(buttonPanel);
        
        // Main container
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(ModernTheme.BACKGROUND);
        mainContainer.setBorder(BorderFactory.createEmptyBorder(ModernTheme.PADDING_LARGE, 
            ModernTheme.PADDING_LARGE, ModernTheme.PADDING_LARGE, ModernTheme.PADDING_LARGE));
        mainContainer.add(formPanel, BorderLayout.CENTER);
        
        // Use layered pane for loading overlay
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        
        // Create layered pane for overlay
        JLayeredPane layeredPane = getLayeredPane();
        if (layeredPane == null) {
            layeredPane = new JLayeredPane();
        }
        layeredPane.setLayout(new BorderLayout());
        layeredPane.add(mainContainer, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(loadingOverlay, JLayeredPane.MODAL_LAYER);
        loadingOverlay.setBounds(0, 0, getWidth(), getHeight());
        add(layeredPane, BorderLayout.CENTER);
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
        passwordField.getPasswordField().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
    }
    
    private void performLogin() {
        // Validate fields
        boolean usernameValid = usernameField.validateField();
        boolean passwordValid = passwordField.validateField();
        
        if (!usernameValid || !passwordValid) {
            ToastNotification.showError(this, "Please fix the errors in the form");
            return;
        }
        
        String username = usernameField.getText().trim();
        String password = passwordField.getPassword();
        
        // Show loading
        loadingOverlay.show("Logging in...");
        loginButton.setEnabled(false);
        signupButton.setEnabled(false);
        
        // Run in background thread to avoid blocking UI
        new Thread(() -> {
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
                
                SwingUtilities.invokeLater(() -> {
                    loadingOverlay.hide();
                    loginButton.setEnabled(true);
                    signupButton.setEnabled(true);
                    
                    if ("success".equals(status)) {
                        String role = (String) response.get("role");
                        ToastNotification.showSuccess(this, "Login successful!");
                        
                        // Small delay before navigation for better UX
                        Timer delayTimer = new Timer(500, e -> {
                            openDashboard(role, username);
                        });
                        delayTimer.setRepeats(false);
                        delayTimer.start();
                    } else {
                        String message = (String) response.get("message");
                        ToastNotification.showError(this, message != null ? message : "Login failed");
                    }
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    loadingOverlay.hide();
                    loginButton.setEnabled(true);
                    signupButton.setEnabled(true);
                    ToastNotification.showError(this, "Error: " + ex.getMessage());
                    ex.printStackTrace();
                });
            }
        }).start();
    }
    
    private void performSignup() {
        // Validate fields
        boolean usernameValid = usernameField.validateField();
        boolean passwordValid = passwordField.validateField();
        
        if (!usernameValid || !passwordValid) {
            ToastNotification.showError(this, "Please fix the errors in the form");
            return;
        }
        
        String username = usernameField.getText().trim();
        String password = passwordField.getPassword();
        String role = (String) roleCombo.getSelectedItem();
        
        // Show loading
        loadingOverlay.show("Creating account...");
        loginButton.setEnabled(false);
        signupButton.setEnabled(false);
        
        // Run in background thread
        new Thread(() -> {
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
                
                SwingUtilities.invokeLater(() -> {
                    loadingOverlay.hide();
                    loginButton.setEnabled(true);
                    signupButton.setEnabled(true);
                    
                    if ("success".equals(status)) {
                        ToastNotification.showSuccess(this, "Account created successfully! Logging in...");
                        
                        // Automatically log in the newly created user
                        Timer delayTimer = new Timer(800, e -> {
                            performLogin();
                        });
                        delayTimer.setRepeats(false);
                        delayTimer.start();
                    } else {
                        String message = (String) response.get("message");
                        ToastNotification.showError(this, message != null ? message : "Signup failed");
                    }
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    loadingOverlay.hide();
                    loginButton.setEnabled(true);
                    signupButton.setEnabled(true);
                    ToastNotification.showError(this, "Error: " + ex.getMessage());
                    ex.printStackTrace();
                });
            }
        }).start();
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

