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
        welcomeLabel.setFont(ModernTheme.FONT_HEADING);
        welcomeLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        
        progressLabel = new JLabel("Progress: 0%");
        progressLabel.setFont(ModernTheme.FONT_BODY);
        progressLabel.setForeground(ModernTheme.TEXT_SECONDARY);
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setFont(ModernTheme.FONT_SMALL);
        progressBar.setForeground(ModernTheme.ACCENT);
        progressBar.setBackground(ModernTheme.BORDER);
        progressBar.setBorderPainted(false);
        
        stageLabel = new JLabel("Stage: Getting Started");
        stageLabel.setFont(ModernTheme.FONT_BODY);
        stageLabel.setForeground(ModernTheme.TEXT_SECONDARY);
        
        daysRemainingLabel = new JLabel("Estimated Days Remaining: -");
        daysRemainingLabel.setFont(ModernTheme.FONT_BODY);
        daysRemainingLabel.setForeground(ModernTheme.TEXT_SECONDARY);
        
        // Tasks table with modern styling
        String[] columns = {"ID", "Task Name", "Description", "Skill", "Hours", "Status"};
        tasksModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tasksTable = new JTable(tasksModel);
        tasksTable.setFont(ModernTheme.FONT_BODY);
        tasksTable.setRowHeight(30);
        tasksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tasksTable.setSelectionBackground(ModernTheme.ACCENT_BLUE);
        tasksTable.setSelectionForeground(Color.WHITE);
        tasksTable.setGridColor(ModernTheme.BORDER);
        tasksTable.getTableHeader().setFont(ModernTheme.FONT_BUTTON);
        tasksTable.getTableHeader().setBackground(ModernTheme.PRIMARY);
        tasksTable.getTableHeader().setForeground(Color.WHITE);
        tasksTable.setBackground(ModernTheme.SURFACE);
        
        refreshButton = new JButton("Refresh");
        completeTaskButton = new JButton("Complete Selected Task");
        viewOnboardingButton = new JButton("Onboarding Details");
        viewEvaluationButton = new JButton("Evaluation");
        viewLevelButton = new JButton("My Level");
        viewUpskillButton = new JButton("Upskill Recommendations");
        searchButton = new JButton("Search");
        
        ModernTheme.styleModernButton(refreshButton, ModernTheme.PRIMARY);
        ModernTheme.styleModernButton(completeTaskButton, ModernTheme.ACCENT);
        ModernTheme.styleModernButton(viewOnboardingButton, ModernTheme.ACCENT_ORANGE);
        ModernTheme.styleModernButton(viewEvaluationButton, ModernTheme.ACCENT_PURPLE);
        ModernTheme.styleModernButton(viewLevelButton, ModernTheme.ACCENT_BLUE);
        ModernTheme.styleModernButton(viewUpskillButton, ModernTheme.ERROR);
        ModernTheme.styleModernButton(searchButton, ModernTheme.PRIMARY_LIGHT);
    }
    
    private void layoutComponents() {
        getContentPane().setBackground(ModernTheme.BACKGROUND);
        setLayout(new BorderLayout());
        
        // Top panel - Welcome and stats with modern card
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(ModernTheme.PRIMARY);
        topPanel.setBorder(BorderFactory.createEmptyBorder(ModernTheme.PADDING_LARGE, 
            ModernTheme.PADDING_XLARGE, ModernTheme.PADDING_LARGE, ModernTheme.PADDING_XLARGE));
        
        JPanel leftTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftTop.setBackground(ModernTheme.PRIMARY);
        welcomeLabel.setForeground(Color.WHITE);
        leftTop.add(welcomeLabel);
        
        JPanel statsCard = ModernTheme.createCardPanel();
        statsCard.setLayout(new GridLayout(2, 2, ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_SMALL));
        statsCard.add(progressLabel);
        statsCard.add(stageLabel);
        statsCard.add(daysRemainingLabel);
        statsCard.add(new JLabel()); // Spacer
        
        topPanel.add(leftTop, BorderLayout.WEST);
        topPanel.add(statsCard, BorderLayout.EAST);
        
        // Progress bar panel
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBackground(ModernTheme.PRIMARY);
        progressPanel.setBorder(BorderFactory.createEmptyBorder(ModernTheme.PADDING_MEDIUM, 
            ModernTheme.PADDING_XLARGE, 0, ModernTheme.PADDING_XLARGE));
        progressPanel.add(progressBar, BorderLayout.CENTER);
        topPanel.add(progressPanel, BorderLayout.SOUTH);
        
        // Center panel - Tasks table with modern card
        JPanel centerPanel = ModernTheme.createCardPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(ModernTheme.BORDER, 1),
                "Available Tasks",
                0, 0,
                ModernTheme.FONT_HEADING
            ),
            BorderFactory.createEmptyBorder(ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM, 
                ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM)
        ));
        
        JScrollPane scrollPane = new JScrollPane(tasksTable);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        scrollPane.setBorder(BorderFactory.createLineBorder(ModernTheme.BORDER, 1));
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel with modern styling
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM));
        buttonPanel.setBackground(ModernTheme.BACKGROUND);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(ModernTheme.PADDING_MEDIUM, 
            ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM));
        buttonPanel.add(refreshButton);
        buttonPanel.add(completeTaskButton);
        
        JSeparator sep1 = new JSeparator(SwingConstants.VERTICAL);
        sep1.setPreferredSize(new Dimension(1, 30));
        buttonPanel.add(sep1);
        
        buttonPanel.add(viewOnboardingButton);
        buttonPanel.add(viewEvaluationButton);
        buttonPanel.add(viewLevelButton);
        buttonPanel.add(viewUpskillButton);
        
        JSeparator sep2 = new JSeparator(SwingConstants.VERTICAL);
        sep2.setPreferredSize(new Dimension(1, 30));
        buttonPanel.add(sep2);
        
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
