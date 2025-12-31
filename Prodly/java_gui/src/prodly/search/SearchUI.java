package prodly.search;

import prodly.integration.InputWriter;
import prodly.integration.CppRunner;
import prodly.integration.OutputReader;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

public class SearchUI extends JFrame {
    private String username;
    private String role;
    
    private JTextField searchField;
    private JComboBox<String> searchTypeCombo;
    private JTable resultsTable;
    private DefaultTableModel resultsModel;
    private JLabel resultsCountLabel;
    
    public SearchUI(String username, String role) {
        this.username = username;
        this.role = role;
        
        setTitle("Search - " + username);
        setSize(900, 600);
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
        searchField = new JTextField(30);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        searchTypeCombo = new JComboBox<>(new String[]{
            "All",
            "Users",
            "Tasks",
            "Skills",
            "Activities"
        });
        searchTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        String[] columns = {"Type", "Name", "Details", "Status"};
        resultsModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultsTable = new JTable(resultsModel);
        resultsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        resultsTable.setRowHeight(25);
        resultsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        resultsCountLabel = new JLabel("No results");
        resultsCountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        // Top panel - Search controls
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(new Color(245, 245, 245));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        topPanel.add(new JLabel("Search:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        topPanel.add(searchField, gbc);
        
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        topPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 3;
        topPanel.add(searchTypeCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 4;
        topPanel.add(resultsCountLabel, gbc);
        
        // Center panel - Results table
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Search Results",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 14)
        ));
        centerPanel.add(new JScrollPane(resultsTable), BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private void attachListeners() {
        searchField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performSearch();
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                // Real-time search as user types
                if (searchField.getText().length() >= 2) {
                    performSearch();
                } else if (searchField.getText().isEmpty()) {
                    resultsModel.setRowCount(0);
                    resultsCountLabel.setText("No results");
                }
            }
        });
        
        searchTypeCombo.addActionListener(e -> {
            if (!searchField.getText().isEmpty()) {
                performSearch();
            }
        });
    }
    
    private void performSearch() {
        String query = searchField.getText().trim();
        String searchType = (String) searchTypeCombo.getSelectedItem();
        
        if (query.isEmpty()) {
            resultsModel.setRowCount(0);
            resultsCountLabel.setText("No results");
            return;
        }
        
        try {
            JSONObject request = new JSONObject();
            request.put("action", "search");
            request.put("query", query);
            request.put("type", searchType);
            
            InputWriter.write(request.toJSONString());
            CppRunner.runCore();
            
            String responseStr = OutputReader.read();
            JSONParser parser = new JSONParser();
            JSONObject response = (JSONObject) parser.parse(responseStr);
            
            String status = (String) response.get("status");
            
            if ("success".equals(status)) {
                String resultsJson = (String) response.get("results");
                JSONArray results = (JSONArray) parser.parse(resultsJson);
                
                resultsModel.setRowCount(0);
                for (Object resultObj : results) {
                    JSONObject result = (JSONObject) resultObj;
                    String type = (String) result.get("type");
                    String name = (String) result.get("name");
                    String details = (String) result.get("details");
                    String statusText = (String) result.get("status");
                    
                    resultsModel.addRow(new Object[]{type, name, details, statusText});
                }
                
                long count = (Long) response.get("count");
                resultsCountLabel.setText(count + " result(s) found");
            } else {
                // Fallback to sample search results
                performSampleSearch(query, searchType);
            }
        } catch (Exception ex) {
            // Use sample search for demonstration
            performSampleSearch(query, searchType);
        }
    }
    
    private void performSampleSearch(String query, String searchType) {
        resultsModel.setRowCount(0);
        String lowerQuery = query.toLowerCase();
        
        if ("All".equals(searchType) || "Users".equals(searchType)) {
            if (lowerQuery.contains("admin") || lowerQuery.contains("user")) {
                resultsModel.addRow(new Object[]{"User", "admin", "Administrator", "Active"});
                resultsModel.addRow(new Object[]{"User", "john_doe", "Employee", "Active"});
                resultsModel.addRow(new Object[]{"User", "jane_smith", "Manager", "Active"});
            }
        }
        
        if ("All".equals(searchType) || "Tasks".equals(searchType)) {
            if (lowerQuery.contains("task") || lowerQuery.contains("onboard") || lowerQuery.contains("setup")) {
                resultsModel.addRow(new Object[]{"Task", "Company Introduction", "Orientation task", "Available"});
                resultsModel.addRow(new Object[]{"Task", "Tool Setup", "Technical setup", "In Progress"});
                resultsModel.addRow(new Object[]{"Task", "First Assignment", "Coding task", "Available"});
            }
        }
        
        if ("All".equals(searchType) || "Skills".equals(searchType)) {
            if (lowerQuery.contains("dsa") || lowerQuery.contains("skill") || lowerQuery.contains("oop")) {
                resultsModel.addRow(new Object[]{"Skill", "DSA", "Data Structures & Algorithms", "85%"});
                resultsModel.addRow(new Object[]{"Skill", "OOP", "Object-Oriented Programming", "78%"});
                resultsModel.addRow(new Object[]{"Skill", "Database", "Database Management", "72%"});
            }
        }
        
        resultsCountLabel.setText(resultsModel.getRowCount() + " result(s) found");
    }
}


