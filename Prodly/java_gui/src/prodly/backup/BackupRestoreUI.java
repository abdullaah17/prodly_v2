package prodly.backup;

import prodly.integration.InputWriter;
import prodly.integration.CppRunner;
import prodly.integration.OutputReader;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class BackupRestoreUI extends JFrame {
    private String username;
    private String role;
    
    private JTextArea statusArea;
    private JButton backupButton;
    private JButton restoreButton;
    private JButton browseButton;
    private JTextField filePathField;
    
    public BackupRestoreUI(String username, String role) {
        this.username = username;
        this.role = role;
        
        setTitle("Backup & Restore - " + username);
        setSize(700, 500);
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
    }
    
    private void initializeComponents() {
        statusArea = new JTextArea(12, 50);
        statusArea.setEditable(false);
        statusArea.setFont(new Font("Courier New", Font.PLAIN, 11));
        statusArea.setBackground(new Color(245, 245, 245));
        
        backupButton = new JButton("Create Backup");
        restoreButton = new JButton("Restore from Backup");
        browseButton = new JButton("Browse...");
        
        filePathField = new JTextField(40);
        filePathField.setEditable(false);
        
        styleButton(backupButton, new Color(60, 179, 113));
        styleButton(restoreButton, new Color(255, 140, 0));
        styleButton(browseButton, new Color(70, 130, 180));
    }
    
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(180, 35));
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        // Top panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(245, 245, 245));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        topPanel.add(new JLabel("Backup & Restore"));
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(backupButton);
        
        // File selection panel
        JPanel filePanel = new JPanel(new BorderLayout());
        filePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Restore from File",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 14)
        ));
        
        JPanel fileInputPanel = new JPanel(new BorderLayout());
        fileInputPanel.add(new JLabel("Backup File:"), BorderLayout.WEST);
        fileInputPanel.add(filePathField, BorderLayout.CENTER);
        fileInputPanel.add(browseButton, BorderLayout.EAST);
        fileInputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        filePanel.add(fileInputPanel, BorderLayout.NORTH);
        filePanel.add(restoreButton, BorderLayout.SOUTH);
        
        // Status panel
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Status",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 14)
        ));
        statusPanel.add(new JScrollPane(statusArea), BorderLayout.CENTER);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(filePanel, BorderLayout.NORTH);
        centerPanel.add(statusPanel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        
        updateStatus("Ready. Click 'Create Backup' to backup all data.");
    }
    
    private void attachListeners() {
        backupButton.addActionListener(e -> createBackup());
        restoreButton.addActionListener(e -> restoreBackup());
        browseButton.addActionListener(e -> browseForFile());
    }
    
    private void createBackup() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Backup As");
        fileChooser.setSelectedFile(new File("prodly_backup_" + 
            System.currentTimeMillis() + ".dat"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                String backupPath = file.getAbsolutePath();
                
                JSONObject request = new JSONObject();
                request.put("action", "create_backup");
                request.put("backupPath", backupPath);
                
                InputWriter.write(request.toJSONString());
                CppRunner.runCore();
                
                String responseStr = OutputReader.read();
                JSONParser parser = new JSONParser();
                JSONObject response = (JSONObject) parser.parse(responseStr);
                
                String status = (String) response.get("status");
                
                if ("success".equals(status)) {
                    updateStatus("✓ Backup created successfully!\n" +
                        "Location: " + backupPath + "\n" +
                        "Time: " + new java.util.Date());
                    JOptionPane.showMessageDialog(this,
                        "Backup created successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    String message = (String) response.get("message");
                    updateStatus("✗ Backup failed: " + message);
                    JOptionPane.showMessageDialog(this,
                        "Backup failed: " + message,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                updateStatus("✗ Error: " + ex.getMessage());
                JOptionPane.showMessageDialog(this,
                    "Error creating backup: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void restoreBackup() {
        if (filePathField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please select a backup file first",
                "No File Selected",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Restoring from backup will overwrite current data.\n" +
            "Are you sure you want to continue?",
            "Confirm Restore",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String backupPath = filePathField.getText();
                
                JSONObject request = new JSONObject();
                request.put("action", "restore_backup");
                request.put("backupPath", backupPath);
                
                InputWriter.write(request.toJSONString());
                CppRunner.runCore();
                
                String responseStr = OutputReader.read();
                JSONParser parser = new JSONParser();
                JSONObject response = (JSONObject) parser.parse(responseStr);
                
                String status = (String) response.get("status");
                
                if ("success".equals(status)) {
                    updateStatus("✓ Restore completed successfully!\n" +
                        "Source: " + backupPath + "\n" +
                        "Time: " + new java.util.Date() + "\n" +
                        "Please restart the application for changes to take effect.");
                    JOptionPane.showMessageDialog(this,
                        "Restore completed successfully!\n" +
                        "Please restart the application.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    String message = (String) response.get("message");
                    updateStatus("✗ Restore failed: " + message);
                    JOptionPane.showMessageDialog(this,
                        "Restore failed: " + message,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                updateStatus("✗ Error: " + ex.getMessage());
                JOptionPane.showMessageDialog(this,
                    "Error restoring backup: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void browseForFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Backup File");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".dat");
            }
            
            @Override
            public String getDescription() {
                return "Backup Files (*.dat)";
            }
        });
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            filePathField.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }
    
    private void updateStatus(String message) {
        statusArea.setText(message);
        statusArea.setCaretPosition(0);
    }
}

