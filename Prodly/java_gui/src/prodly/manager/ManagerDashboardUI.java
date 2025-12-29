package prodly.manager;

import prodly.integration.InputWriter;
import prodly.integration.CppRunner;
import prodly.integration.OutputReader;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ManagerDashboardUI extends JFrame {
    private String username;
    private String role;
    private JLabel totalEmployeesLabel;
    private JLabel avgLevelLabel;
    private JLabel avgProgressLabel;
    private JLabel atRiskLabel;
    private JTable teamTable;
    private DefaultTableModel teamModel;
    private JButton refreshButton;
    
    public ManagerDashboardUI(String username, String role) {
        this.username = username;
        this.role = role;
        
        setTitle("Detailed Manager Analytics - " + username);
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
        loadData();
    }
    
    private void initializeComponents() {
        totalEmployeesLabel = new JLabel("Total Employees: -");
        totalEmployeesLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        avgLevelLabel = new JLabel("Average Level: -");
        avgLevelLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        avgProgressLabel = new JLabel("Average Progress: -");
        avgProgressLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        atRiskLabel = new JLabel("At-Risk Employees: -");
        atRiskLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        atRiskLabel.setForeground(new Color(220, 20, 60));
        
        String[] columns = {"Employee ID", "Name", "Level", "Progress %", "Status", "Action Required"};
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
        
        refreshButton = new JButton("Refresh Data");
        refreshButton.setBackground(new Color(70, 130, 180));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        topPanel.setBackground(new Color(245, 245, 245));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        topPanel.add(totalEmployeesLabel);
        topPanel.add(avgLevelLabel);
        topPanel.add(avgProgressLabel);
        topPanel.add(atRiskLabel);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Team Performance Overview",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 14)
        ));
        centerPanel.add(new JScrollPane(teamTable), BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(refreshButton);
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void attachListeners() {
        refreshButton.addActionListener(e -> loadData());
    }
    
    private void loadData() {
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
                long total = (Long) response.get("totalEmployees");
                totalEmployeesLabel.setText("Total Employees: " + total);
                
                long avgLevel = (Long) response.get("averageLevel");
                avgLevelLabel.setText("Average Level: " + avgLevel);
                
                double avgProgress = Double.parseDouble((String) response.get("averageProgress"));
                avgProgressLabel.setText(String.format("Average Progress: %.1f%%", avgProgress));
                
                long atRisk = (Long) response.get("atRiskCount");
                atRiskLabel.setText("At-Risk Employees: " + atRisk);
                
                // Update team table
                teamModel.setRowCount(0);
                teamModel.addRow(new Object[]{"emp1", "John Doe", 3, "75.5%", "On Track", "None"});
                teamModel.addRow(new Object[]{"emp2", "Jane Smith", 2, "45.0%", "At Risk", "Needs Support"});
                teamModel.addRow(new Object[]{"emp3", "Bob Johnson", 4, "90.0%", "On Track", "None"});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error loading data: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
