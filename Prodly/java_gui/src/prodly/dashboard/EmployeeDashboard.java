package prodly.dashboard;

import prodly.integration.InputWriter;
import prodly.integration.CppRunner;
import prodly.integration.OutputReader;
import prodly.onboarding.OnboardingScreen;
import prodly.evaluation.AssessmentUI;
import prodly.leveling.LevelView;
import prodly.upskilling.UpskillUI;
import prodly.search.SearchUI;
import prodly.ui.ModernTheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

public class EmployeeDashboard extends JFrame {
    private String username;
    private String role;
    private JLabel welcomeLabel;
    private JLabel progressLabel;
    private JProgressBar progressBar;
    private JLabel stageLabel;
    private JLabel daysRemainingLabel;
    private JTable tasksTable;
    private DefaultTableModel tasksModel;
    private JButton refreshButton;
    private JButton completeTaskButton;
    private JButton viewOnboardingButton;
    private JButton viewEvaluationButton;
    private JButton viewLevelButton;
    private JButton viewUpskillButton;
    private JButton searchButton;
    
    public EmployeeDashboard(String username, String role) {
        this.username = username;
        this.role = role;
        
        setTitle("Prodly - Employee Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
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
        welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        progressLabel = new JLabel("Progress: 0%");
        progressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        progressBar.setForeground(new Color(60, 179, 113));
        
        stageLabel = new JLabel("Stage: Getting Started");
        stageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        daysRemainingLabel = new JLabel("Estimated Days Remaining: -");
        daysRemainingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Tasks table
        String[] columns = {"ID", "Task Name", "Description", "Skill", "Hours", "Status"};
        tasksModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tasksTable = new JTable(tasksModel);
        tasksTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tasksTable.setRowHeight(25);
        tasksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tasksTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        refreshButton = new JButton("Refresh");
        completeTaskButton = new JButton("Complete Selected Task");
        viewOnboardingButton = new JButton("Onboarding Details");
        viewEvaluationButton = new JButton("Evaluation");
        viewLevelButton = new JButton("My Level");
        viewUpskillButton = new JButton("Upskill Recommendations");
        searchButton = new JButton("Search");
        
        styleButton(refreshButton, new Color(70, 130, 180));
        styleButton(completeTaskButton, new Color(60, 179, 113));
        styleButton(viewOnboardingButton, new Color(255, 140, 0));
        styleButton(viewEvaluationButton, new Color(138, 43, 226));
        styleButton(viewLevelButton, new Color(30, 144, 255));
        styleButton(viewUpskillButton, new Color(220, 20, 60));
        styleButton(searchButton, new Color(72, 61, 139));
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
        
        // Top panel - Welcome and stats
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 245));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel leftTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftTop.setBackground(new Color(245, 245, 245));
        leftTop.add(welcomeLabel);
        
        JPanel rightTop = new JPanel(new GridLayout(2, 2, 10, 5));
        rightTop.setBackground(new Color(245, 245, 245));
        rightTop.add(progressLabel);
        rightTop.add(stageLabel);
        rightTop.add(daysRemainingLabel);
        rightTop.add(new JLabel()); // Spacer
        
        topPanel.add(leftTop, BorderLayout.WEST);
        topPanel.add(rightTop, BorderLayout.EAST);
        topPanel.add(progressBar, BorderLayout.SOUTH);
        
        // Center panel - Tasks table
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Available Tasks",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 14)
        ));
        
        JScrollPane scrollPane = new JScrollPane(tasksTable);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.add(refreshButton);
        buttonPanel.add(completeTaskButton);
        buttonPanel.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPanel.add(viewOnboardingButton);
        buttonPanel.add(viewEvaluationButton);
        buttonPanel.add(viewLevelButton);
        buttonPanel.add(viewUpskillButton);
        buttonPanel.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPanel.add(searchButton);
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void attachListeners() {
        refreshButton.addActionListener(e -> loadDashboardData());
        
        completeTaskButton.addActionListener(e -> completeSelectedTask());
        
        viewOnboardingButton.addActionListener(e -> {
            new OnboardingScreen(username, role).setVisible(true);
        });
        
        viewEvaluationButton.addActionListener(e -> {
            new AssessmentUI(username, role).setVisible(true);
        });
        
        viewLevelButton.addActionListener(e -> {
            new LevelView(username, role).setVisible(true);
        });
        
        viewUpskillButton.addActionListener(e -> {
            new UpskillUI(username, role).setVisible(true);
        });
        
        searchButton.addActionListener(e -> {
            new SearchUI(username, role).setVisible(true);
        });
    }
    
    private void loadDashboardData() {
        try {
            JSONObject request = new JSONObject();
            request.put("action", "get_onboarding");
            request.put("role", role);
            request.put("username", username);
            
            InputWriter.write(request.toJSONString());
            CppRunner.runCore();
            
            String responseStr = OutputReader.read();
            JSONParser parser = new JSONParser();
            JSONObject response = (JSONObject) parser.parse(responseStr);
            
            String status = (String) response.get("status");
            
            if ("success".equals(status)) {
                // Update progress
                long completion = (Long) response.get("completion");
                progressBar.setValue((int) completion);
                progressLabel.setText("Progress: " + completion + "%");
                
                String stage = (String) response.get("stage");
                stageLabel.setText("Stage: " + stage);
                
                long days = (Long) response.get("daysRemaining");
                daysRemainingLabel.setText("Estimated Days Remaining: " + days);
                
                // Update tasks table
                String tasksJson = (String) response.get("tasks");
                JSONArray tasks = (JSONArray) parser.parse(tasksJson);
                
                tasksModel.setRowCount(0);
                for (Object taskObj : tasks) {
                    JSONObject task = (JSONObject) taskObj;
                    String id = (String) task.get("id");
                    String name = (String) task.get("name");
                    String desc = (String) task.get("description");
                    String skill = (String) task.get("skill");
                    long hours = (Long) task.get("estimatedHours");
                    
                    tasksModel.addRow(new Object[]{
                        id, name, desc, skill, hours, "Available"
                    });
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error loading dashboard data",
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
    
    private void completeSelectedTask() {
        int selectedRow = tasksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a task to complete",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String taskId = (String) tasksModel.getValueAt(selectedRow, 0);
        
        try {
            JSONObject request = new JSONObject();
            request.put("action", "complete_task");
            request.put("role", role);
            request.put("taskId", taskId);
            
            InputWriter.write(request.toJSONString());
            CppRunner.runCore();
            
            String responseStr = OutputReader.read();
            JSONParser parser = new JSONParser();
            JSONObject response = (JSONObject) parser.parse(responseStr);
            
            String status = (String) response.get("status");
            
            if ("success".equals(status)) {
                JOptionPane.showMessageDialog(this,
                    "Task completed successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                loadDashboardData(); // Refresh
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error completing task",
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
