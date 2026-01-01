package prodly.dashboard;

import prodly.integration.InputWriter;
import prodly.integration.CppRunner;
import prodly.integration.OutputReader;
import prodly.manager.ManagerDashboardUI;
import prodly.reports.ReportsUI;
import prodly.analytics.AnalyticsDashboard;
import prodly.search.SearchUI;
import prodly.ui.ModernTheme;

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
    private JButton reportsButton;
    private JButton analyticsButton;
    private JButton searchButton;
    
    public ManagerDashboard(String username, String role) {
        this.username = username;
        this.role = role;
        
        setTitle("Prodly - Manager Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
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
        welcomeLabel.setFont(ModernTheme.FONT_HEADING);
        welcomeLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        
        totalEmployeesLabel = new JLabel("Total Employees: -");
        totalEmployeesLabel.setFont(ModernTheme.FONT_BODY);
        totalEmployeesLabel.setForeground(ModernTheme.TEXT_SECONDARY);
        
        avgLevelLabel = new JLabel("Average Level: -");
        avgLevelLabel.setFont(ModernTheme.FONT_BODY);
        avgLevelLabel.setForeground(ModernTheme.TEXT_SECONDARY);
        
        avgProgressLabel = new JLabel("Average Progress: -");
        avgProgressLabel.setFont(ModernTheme.FONT_BODY);
        avgProgressLabel.setForeground(ModernTheme.TEXT_SECONDARY);
        
        atRiskLabel = new JLabel("At-Risk Employees: -");
        atRiskLabel.setFont(ModernTheme.FONT_BODY);
        atRiskLabel.setForeground(ModernTheme.ERROR);
        
        // Team table with modern styling
        String[] columns = {"Employee ID", "Name", "Level", "Progress %", "Status"};
        teamModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        teamTable = new JTable(teamModel);
        teamTable.setFont(ModernTheme.FONT_BODY);
        teamTable.setRowHeight(30);
        teamTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        teamTable.setSelectionBackground(ModernTheme.ACCENT_BLUE);
        teamTable.setSelectionForeground(Color.WHITE);
        teamTable.setGridColor(ModernTheme.BORDER);
        teamTable.getTableHeader().setFont(ModernTheme.FONT_BUTTON);
        teamTable.getTableHeader().setBackground(ModernTheme.PRIMARY);
        teamTable.getTableHeader().setForeground(Color.WHITE);
        teamTable.setBackground(ModernTheme.SURFACE);
        
        refreshButton = new JButton("Refresh");
        viewDetailsButton = new JButton("View Detailed Analytics");
        reportsButton = new JButton("Reports & Analytics");
        analyticsButton = new JButton("Analytics Dashboard");
        searchButton = new JButton("Search");
        
        ModernTheme.styleModernButton(refreshButton, ModernTheme.PRIMARY);
        ModernTheme.styleModernButton(viewDetailsButton, ModernTheme.ACCENT_ORANGE);
        ModernTheme.styleModernButton(reportsButton, ModernTheme.ACCENT_PURPLE);
        ModernTheme.styleModernButton(analyticsButton, ModernTheme.ACCENT_BLUE);
        ModernTheme.styleModernButton(searchButton, ModernTheme.PRIMARY_LIGHT);
    }
    
    private void layoutComponents() {
        getContentPane().setBackground(ModernTheme.BACKGROUND);
        setLayout(new BorderLayout());
        
        // Top panel - Stats with modern styling
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(ModernTheme.PRIMARY);
        topPanel.setBorder(BorderFactory.createEmptyBorder(ModernTheme.PADDING_LARGE, 
            ModernTheme.PADDING_XLARGE, ModernTheme.PADDING_LARGE, ModernTheme.PADDING_XLARGE));
        
        JPanel leftTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftTop.setBackground(ModernTheme.PRIMARY);
        welcomeLabel.setForeground(Color.WHITE);
        leftTop.add(welcomeLabel);
        
        JPanel statsPanel = ModernTheme.createCardPanel();
        statsPanel.setLayout(new GridLayout(2, 2, ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM));
        statsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(ModernTheme.BORDER, 1),
                "Team Statistics",
                0, 0,
                ModernTheme.FONT_HEADING
            ),
            BorderFactory.createEmptyBorder(ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM, 
                ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM)
        ));
        statsPanel.add(totalEmployeesLabel);
        statsPanel.add(avgLevelLabel);
        statsPanel.add(avgProgressLabel);
        statsPanel.add(atRiskLabel);
        
        topPanel.add(leftTop, BorderLayout.WEST);
        topPanel.add(statsPanel, BorderLayout.EAST);
        
        // Center panel - Team table with modern card
        JPanel centerPanel = ModernTheme.createCardPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(ModernTheme.BORDER, 1),
                "Team Members",
                0, 0,
                ModernTheme.FONT_HEADING
            ),
            BorderFactory.createEmptyBorder(ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM, 
                ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM)
        ));
        
        JScrollPane scrollPane = new JScrollPane(teamTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(ModernTheme.BORDER, 1));
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel with modern styling
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM));
        buttonPanel.setBackground(ModernTheme.BACKGROUND);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(ModernTheme.PADDING_MEDIUM, 
            ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM));
        buttonPanel.add(refreshButton);
        buttonPanel.add(viewDetailsButton);
        buttonPanel.add(reportsButton);
        buttonPanel.add(analyticsButton);
        buttonPanel.add(searchButton);
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void attachListeners() {
        refreshButton.addActionListener(e -> loadDashboardData());
        
        viewDetailsButton.addActionListener(e -> {
            new ManagerDashboardUI(username, role).setVisible(true);
        });
        
        reportsButton.addActionListener(e -> {
            new ReportsUI(username, role).setVisible(true);
        });
        
        analyticsButton.addActionListener(e -> {
            new AnalyticsDashboard(username, role).setVisible(true);
        });
        
        searchButton.addActionListener(e -> {
            new SearchUI(username, role).setVisible(true);
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

