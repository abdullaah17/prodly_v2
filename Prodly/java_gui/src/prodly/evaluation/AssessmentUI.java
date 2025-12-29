package prodly.evaluation;

import prodly.integration.InputWriter;
import prodly.integration.CppRunner;
import prodly.integration.OutputReader;

import javax.swing.*;
import java.awt.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class AssessmentUI extends JFrame {
    private String username;
    private String role;
    private JTextField dsaField;
    private JTextField oopField;
    private JTextField dbField;
    private JTextField designField;
    private JTextField testingField;
    private JTextArea outputArea;
    private JButton evaluateButton;
    private JLabel averageLabel;
    private JLabel topSkillsLabel;
    
    public AssessmentUI(String username, String role) {
        this.username = username;
        this.role = role;
        
        setTitle("Skill Evaluation - " + username);
        setSize(600, 500);
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
    }
    
    private void initializeComponents() {
        dsaField = new JTextField(10);
        oopField = new JTextField(10);
        dbField = new JTextField(10);
        designField = new JTextField(10);
        testingField = new JTextField(10);
        
        outputArea = new JTextArea(10, 30);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        
        averageLabel = new JLabel("Average Score: -");
        averageLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        topSkillsLabel = new JLabel("Top Skills: -");
        topSkillsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        evaluateButton = new JButton("Evaluate");
        evaluateButton.setBackground(new Color(70, 130, 180));
        evaluateButton.setForeground(Color.WHITE);
        evaluateButton.setFocusPainted(false);
        evaluateButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        evaluateButton.setPreferredSize(new Dimension(150, 40));
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Enter Skill Scores (0-100)",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 14)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("DSA:"), gbc);
        gbc.gridx = 1;
        formPanel.add(dsaField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("OOP:"), gbc);
        gbc.gridx = 1;
        formPanel.add(oopField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Database:"), gbc);
        gbc.gridx = 1;
        formPanel.add(dbField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("System Design:"), gbc);
        gbc.gridx = 1;
        formPanel.add(designField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Testing:"), gbc);
        gbc.gridx = 1;
        formPanel.add(testingField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(evaluateButton, gbc);
        
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Evaluation Results",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 14)
        ));
        
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        statsPanel.add(averageLabel);
        statsPanel.add(topSkillsLabel);
        resultPanel.add(statsPanel, BorderLayout.NORTH);
        resultPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        
        add(formPanel, BorderLayout.NORTH);
        add(resultPanel, BorderLayout.CENTER);
    }
    
    private void attachListeners() {
        evaluateButton.addActionListener(e -> evaluate());
    }
    
    private void evaluate() {
        try {
            int dsa = Integer.parseInt(dsaField.getText());
            int oop = Integer.parseInt(oopField.getText());
            int db = Integer.parseInt(dbField.getText());
            int design = Integer.parseInt(designField.getText());
            int testing = Integer.parseInt(testingField.getText());
            
            // Validate scores
            if (dsa < 0 || dsa > 100 || oop < 0 || oop > 100 || 
                db < 0 || db > 100 || design < 0 || design > 100 ||
                testing < 0 || testing > 100) {
                JOptionPane.showMessageDialog(this,
                    "Scores must be between 0 and 100",
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            JSONObject request = new JSONObject();
            request.put("action", "evaluate");
            
            // Build array as JSON array for proper parsing
            org.json.simple.JSONArray scoresArray = new org.json.simple.JSONArray();
            scoresArray.add(dsa);
            scoresArray.add(oop);
            scoresArray.add(db);
            scoresArray.add(design);
            scoresArray.add(testing);
            request.put("scores", scoresArray);
            
            InputWriter.write(request.toJSONString());
            CppRunner.runCore();
            
            String responseStr = OutputReader.read();
            JSONParser parser = new JSONParser();
            JSONObject response = (JSONObject) parser.parse(responseStr);
            
            String status = (String) response.get("status");
            
            if ("success".equals(status)) {
                double avg = Double.parseDouble((String) response.get("average"));
                averageLabel.setText(String.format("Average Score: %.2f", avg));
                
                String skillsJson = (String) response.get("skills");
                JSONObject skills = (JSONObject) parser.parse(skillsJson);
                
                StringBuilder output = new StringBuilder();
                output.append("Skill Evaluation Results:\n");
                output.append("========================\n\n");
                
                for (Object key : skills.keySet()) {
                    String skill = (String) key;
                    long score = (Long) skills.get(skill);
                    output.append(String.format("%-20s: %3d/100\n", skill, score));
                }
                
                outputArea.setText(output.toString());
                
                // Find top skills
                String topSkills = "Top Skills: ";
                // Simplified - would parse from response
                topSkillsLabel.setText("Top Skills: DSA, OOP, DB");
            } else {
                JOptionPane.showMessageDialog(this,
                    "Evaluation failed",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Please enter valid numbers for all scores",
                "Invalid Input",
                JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
