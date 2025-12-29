package prodly.admin;

import prodly.integration.InputWriter;
import prodly.integration.CppRunner;
import prodly.integration.OutputReader;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

public class UserManagementUI extends JFrame {
    private String username;
    private String role;
    private JTable usersTable;
    private DefaultTableModel usersModel;
    private JButton refreshButton;
    private JButton addUserButton;
    private JButton deleteUserButton;
    private JButton editUserButton;
    private JLabel statsLabel;
    
    public UserManagementUI(String username, String role) {
        this.username = username;
        this.role = role;
        
        setTitle("User Management - " + username);
        setSize(1000, 700);
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
        loadUsers();
    }
    
    private void initializeComponents() {
        String[] columns = {"Username", "Role", "Created"};
        usersModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        usersTable = new JTable(usersModel);
        usersTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        usersTable.setRowHeight(25);
        usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        usersTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        refreshButton = new JButton("Refresh");
        addUserButton = new JButton("Add User");
        deleteUserButton = new JButton("Delete User");
        editUserButton = new JButton("Edit User");
        
        styleButton(refreshButton, new Color(70, 130, 180));
        styleButton(addUserButton, new Color(60, 179, 113));
        styleButton(deleteUserButton, new Color(220, 20, 60));
        styleButton(editUserButton, new Color(255, 140, 0));
        
        statsLabel = new JLabel("Total Users: -");
        statsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }
    
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(120, 35));
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        // Top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 245));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("User Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(statsLabel, BorderLayout.EAST);
        
        // Center panel - Users table
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "All Users",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 14)
        ));
        centerPanel.add(new JScrollPane(usersTable), BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.add(refreshButton);
        buttonPanel.add(addUserButton);
        buttonPanel.add(editUserButton);
        buttonPanel.add(deleteUserButton);
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void attachListeners() {
        refreshButton.addActionListener(e -> loadUsers());
        
        addUserButton.addActionListener(e -> showAddUserDialog());
        
        editUserButton.addActionListener(e -> showEditUserDialog());
        
        deleteUserButton.addActionListener(e -> deleteSelectedUser());
    }
    
    private void loadUsers() {
        try {
            JSONObject request = new JSONObject();
            request.put("action", "list_users");
            
            InputWriter.write(request.toJSONString());
            CppRunner.runCore();
            
            String responseStr = OutputReader.read();
            JSONParser parser = new JSONParser();
            JSONObject response = (JSONObject) parser.parse(responseStr);
            
            String status = (String) response.get("status");
            
            if ("success".equals(status)) {
                String usersJson = (String) response.get("users");
                JSONArray users = (JSONArray) parser.parse(usersJson);
                
                usersModel.setRowCount(0);
                for (Object userObj : users) {
                    JSONObject user = (JSONObject) userObj;
                    String username = (String) user.get("username");
                    String role = (String) user.get("role");
                    String created = (String) user.get("created");
                    
                    usersModel.addRow(new Object[]{username, role, created});
                }
                
                long total = (Long) response.get("total");
                long employees = (Long) response.get("employees");
                long managers = (Long) response.get("managers");
                long admins = (Long) response.get("admins");
                
                statsLabel.setText(String.format("Total: %d | Employees: %d | Managers: %d | Admins: %d", 
                    total, employees, managers, admins));
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error loading users",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void showAddUserDialog() {
        JDialog dialog = new JDialog(this, "Add New User", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"employee", "manager", "admin"});
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        panel.add(roleCombo, gbc);
        
        JButton createButton = new JButton("Create User");
        createButton.addActionListener(e -> {
            String newUsername = usernameField.getText().trim();
            String newPassword = new String(passwordField.getPassword());
            String newRole = (String) roleCombo.getSelectedItem();
            
            if (newUsername.isEmpty() || newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                    "Please fill all fields",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                JSONObject request = new JSONObject();
                request.put("action", "signup");
                request.put("username", newUsername);
                request.put("password", newPassword);
                request.put("role", newRole);
                
                InputWriter.write(request.toJSONString());
                CppRunner.runCore();
                
                String responseStr = OutputReader.read();
                JSONParser parser = new JSONParser();
                JSONObject response = (JSONObject) parser.parse(responseStr);
                
                String status = (String) response.get("status");
                
                if ("success".equals(status)) {
                    JOptionPane.showMessageDialog(dialog,
                        "User created successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    loadUsers();
                } else {
                    String message = (String) response.get("message");
                    JOptionPane.showMessageDialog(dialog,
                        message != null ? message : "Failed to create user",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        panel.add(createButton, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void showEditUserDialog() {
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a user to edit",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String selectedUsername = (String) usersModel.getValueAt(selectedRow, 0);
        String currentRole = (String) usersModel.getValueAt(selectedRow, 1);
        
        if ("admin".equals(selectedUsername)) {
            JOptionPane.showMessageDialog(this,
                "Cannot edit admin user",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JDialog dialog = new JDialog(this, "Edit User: " + selectedUsername, true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        JLabel usernameLabel = new JLabel("Username: " + selectedUsername);
        JPasswordField passwordField = new JPasswordField(20);
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"employee", "manager", "admin"});
        roleCombo.setSelectedItem(currentRole);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(usernameLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("New Password (leave empty to keep current):"), gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        panel.add(roleCombo, gbc);
        
        JButton updateButton = new JButton("Update User");
        updateButton.addActionListener(e -> {
            String newPassword = new String(passwordField.getPassword());
            String newRole = (String) roleCombo.getSelectedItem();
            
            try {
                // Update role if changed
                if (!newRole.equals(currentRole)) {
                    JSONObject request = new JSONObject();
                    request.put("action", "update_user_role");
                    request.put("username", selectedUsername);
                    request.put("role", newRole);
                    
                    InputWriter.write(request.toJSONString());
                    CppRunner.runCore();
                    
                    String responseStr = OutputReader.read();
                    JSONParser parser = new JSONParser();
                    JSONObject response = (JSONObject) parser.parse(responseStr);
                    
                    if (!"success".equals(response.get("status"))) {
                        String message = (String) response.get("message");
                        JOptionPane.showMessageDialog(dialog,
                            message != null ? message : "Failed to update role",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                
                // Update password if provided
                if (!newPassword.isEmpty()) {
                    JSONObject request = new JSONObject();
                    request.put("action", "update_user_password");
                    request.put("username", selectedUsername);
                    request.put("password", newPassword);
                    
                    InputWriter.write(request.toJSONString());
                    CppRunner.runCore();
                    
                    String responseStr = OutputReader.read();
                    JSONParser parser = new JSONParser();
                    JSONObject response = (JSONObject) parser.parse(responseStr);
                    
                    if (!"success".equals(response.get("status"))) {
                        String message = (String) response.get("message");
                        JOptionPane.showMessageDialog(dialog,
                            message != null ? message : "Failed to update password",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                
                JOptionPane.showMessageDialog(dialog,
                    "User updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                loadUsers();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        panel.add(updateButton, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void deleteSelectedUser() {
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a user to delete",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String selectedUsername = (String) usersModel.getValueAt(selectedRow, 0);
        
        if ("admin".equals(selectedUsername)) {
            JOptionPane.showMessageDialog(this,
                "Cannot delete admin user",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete user: " + selectedUsername + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                JSONObject request = new JSONObject();
                request.put("action", "delete_user");
                request.put("username", selectedUsername);
                
                InputWriter.write(request.toJSONString());
                CppRunner.runCore();
                
                String responseStr = OutputReader.read();
                JSONParser parser = new JSONParser();
                JSONObject response = (JSONObject) parser.parse(responseStr);
                
                String status = (String) response.get("status");
                
                if ("success".equals(status)) {
                    JOptionPane.showMessageDialog(this,
                        "User deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    loadUsers();
                } else {
                    String message = (String) response.get("message");
                    JOptionPane.showMessageDialog(this,
                        message != null ? message : "Failed to delete user",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}

