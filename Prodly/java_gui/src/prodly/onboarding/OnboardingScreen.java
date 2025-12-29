package prodly.onboarding;

import prodly.integration.InputWriter;
import prodly.integration.CppRunner;
import prodly.integration.OutputReader;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

public class OnboardingScreen extends JFrame {
    private String username;
    private String role;
    private JProgressBar progressBar;
    private JLabel stageLabel;
    private JLabel daysLabel;
    private JTable tasksTable;
    private DefaultTableModel tasksModel;
    private JButton refreshButton;
    
    public OnboardingScreen(String username, String role) {
        this.username = username;
        this.role = role;
        
        setTitle("Onboarding Journey - " + username);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        initializeComponents();
        layoutComponents();
        attachListeners();
        loadData();
    }
    
    private void initializeComponents() {
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        progressBar.setForeground(new Color(60, 179, 113));
        
        stageLabel = new JLabel("Stage: -");
        stageLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        daysLabel = new JLabel("Days Remaining: -");
        daysLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        String[] columns = {"ID", "Task Name", "Description", "Skill", "Estimated Hours"};
        tasksModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tasksTable = new JTable(tasksModel);
        tasksTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tasksTable.setRowHeight(25);
        tasksTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        refreshButton = new JButton("Refresh");
        refreshButton.setBackground(new Color(70, 130, 180));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 245));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        infoPanel.setBackground(new Color(245, 245, 245));
        infoPanel.add(stageLabel);
        infoPanel.add(daysLabel);
        
        topPanel.add(new JLabel("Your Onboarding Progress"), BorderLayout.NORTH);
        topPanel.add(infoPanel, BorderLayout.CENTER);
        topPanel.add(progressBar, BorderLayout.SOUTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Onboarding Tasks",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 14)
        ));
        centerPanel.add(new JScrollPane(tasksTable), BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
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
                long completion = (Long) response.get("completion");
                progressBar.setValue((int) completion);
                
                String stage = (String) response.get("stage");
                stageLabel.setText("Stage: " + stage);
                
                long days = (Long) response.get("daysRemaining");
                daysLabel.setText("Days Remaining: " + days);
                
                String tasksJson = (String) response.get("tasks");
                JSONArray tasks = (JSONArray) parser.parse(tasksJson);
                
                tasksModel.setRowCount(0);
                for (Object taskObj : tasks) {
                    JSONObject task = (JSONObject) taskObj;
                    tasksModel.addRow(new Object[]{
                        task.get("id"),
                        task.get("name"),
                        task.get("description"),
                        task.get("skill"),
                        task.get("estimatedHours")
                    });
                }
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
