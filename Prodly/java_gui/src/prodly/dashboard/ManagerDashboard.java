package prodly.dashboard;

import prodly.integration.InputWriter;
import prodly.integration.CppRunner;
import prodly.integration.OutputReader;
import prodly.manager.ManagerDashboardUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ManagerDashboard extends JFrame {
    private String username;
    private String role;
    private JLabel welcomeLabel;
    private JLabel totalEmployeesLabel;
    private JLabel avgLevelLabel;
    private JLabel avgProgressLabel;
    private JLabel atRiskLabel;
    private JTable teamTable;
    private DefaultTableModel teamModel;
    private JButton refreshButton;
    private JButton viewDetailsButton;
    
    public ManagerDashboard(String username, String role) {
        this.username = username;
        this.role = role;
        
        setTitle("Prodly - Manager Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        initializeComponents();
        layoutComponents();
        attachListeners();
        loadDashboardData();
    }
    
    private void initializeComponents() {
        welcomeLabel = new JLabel("Manager Dashboard - " + username);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        totalEmployeesLabel = new JLabel("Total Employees: -");
        totalEmployeesLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        avgLevelLabel = new JLabel("Average Level: -");
        avgLevelLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        avgProgressLabel = new JLabel("Average Progress: -");
        avgProgressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        atRiskLabel = new JLabel("At-Risk Employees: -");
        atRiskLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        atRiskLabel.setForeground(new Color(220, 20, 60));
        
        // Team table
        String[] columns = {"Employee ID", "Name", "Level", "Progress %", "Status"};
        teamModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        teamTable = new JTable(teamModel);
        teamTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        teamTable.setRowHeight(25);
        teamTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        teamTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        refreshButton = new JButton("Refresh");
        viewDetailsButton = new JButton("View Detailed Analytics");
        
        styleButton(refreshButton, new Color(70, 130, 180));
        styleButton(viewDetailsButton, new Color(255, 140, 0));
    }
    
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(200, 35));
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        // Top panel - Stats
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 245));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel leftTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftTop.setBackground(new Color(245, 245, 245));
        leftTop.add(welcomeLabel);
        
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 15, 10));
        statsPanel.setBackground(new Color(245, 245, 245));
        statsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Team Statistics",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 14)
        ));
        statsPanel.add(totalEmployeesLabel);
        statsPanel.add(avgLevelLabel);
        statsPanel.add(avgProgressLabel);
        statsPanel.add(atRiskLabel);
        
        topPanel.add(leftTop, BorderLayout.WEST);
        topPanel.add(statsPanel, BorderLayout.EAST);
        
        // Center panel - Team table
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Team Members",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 14)
        ));
        
        JScrollPane scrollPane = new JScrollPane(teamTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.add(refreshButton);
        buttonPanel.add(viewDetailsButton);
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void attachListeners() {
        refreshButton.addActionListener(e -> loadDashboardData());
        
        viewDetailsButton.addActionListener(e -> {
            new ManagerDashboardUI(username, role).setVisible(true);
        });
    }
    
    private void loadDashboardData() {
        try {
            JSONObject request = new JSONObject();
            request.put("action", "get_team_stats");
            
            InputWriter.write(request.toJSONString());
            CppRunner.runCore();
            
            String responseStr = OutputReader.read();
            JSONParser parser = new JSONParser();
            JSONObject response = (JSONObject) parser.parse(responseStr);
            
            String status = (String) response.get("status");
            
            if ("success".equals(status)) {
                // Update stats
                long total = (Long) response.get("totalEmployees");
                totalEmployeesLabel.setText("Total Employees: " + total);
                
                long avgLevel = (Long) response.get("averageLevel");
                avgLevelLabel.setText("Average Level: " + avgLevel);
                
                double avgProgress = Double.parseDouble((String) response.get("averageProgress"));
                avgProgressLabel.setText(String.format("Average Progress: %.1f%%", avgProgress));
                
                long atRisk = (Long) response.get("atRiskCount");
                atRiskLabel.setText("At-Risk Employees: " + atRisk);
                
                // Update team table (sample data - would come from actual employee list)
                teamModel.setRowCount(0);
                teamModel.addRow(new Object[]{"emp1", "John Doe", 3, "75.5%", "On Track"});
                teamModel.addRow(new Object[]{"emp2", "Jane Smith", 2, "45.0%", "At Risk"});
                teamModel.addRow(new Object[]{"emp3", "Bob Johnson", 4, "90.0%", "On Track"});
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error loading team data",
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

