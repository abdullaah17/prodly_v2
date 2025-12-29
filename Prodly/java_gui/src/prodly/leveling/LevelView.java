package prodly.leveling;

import prodly.integration.InputWriter;
import prodly.integration.CppRunner;
import prodly.integration.OutputReader;

import javax.swing.*;
import java.awt.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class LevelView extends JFrame {
    private String username;
    private String role;
    private JLabel levelLabel;
    private JProgressBar levelProgressBar;
    private JLabel levelDescriptionLabel;
    private JButton refreshButton;
    
    public LevelView(String username, String role) {
        this.username = username;
        this.role = role;
        
        setTitle("My Level - " + username);
        setSize(500, 400);
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
        loadLevel();
    }
    
    private void initializeComponents() {
        levelLabel = new JLabel("Level: -");
        levelLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        levelLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        levelProgressBar = new JProgressBar(1, 5);
        levelProgressBar.setStringPainted(true);
        levelProgressBar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        levelProgressBar.setForeground(new Color(60, 179, 113));
        
        levelDescriptionLabel = new JLabel("<html><center>Level information will appear here</center></html>");
        levelDescriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        levelDescriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        refreshButton = new JButton("Refresh Level");
        refreshButton.setBackground(new Color(70, 130, 180));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        centerPanel.add(levelLabel, BorderLayout.NORTH);
        centerPanel.add(levelProgressBar, BorderLayout.CENTER);
        centerPanel.add(levelDescriptionLabel, BorderLayout.SOUTH);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(refreshButton);
        
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void attachListeners() {
        refreshButton.addActionListener(e -> loadLevel());
    }
    
    private void loadLevel() {
        try {
            // Sample level assignment - would use actual evaluation scores
            JSONObject request = new JSONObject();
            request.put("action", "assign_level");
            request.put("skillScores", "{}"); // Simplified
            
            InputWriter.write(request.toJSONString());
            CppRunner.runCore();
            
            String responseStr = OutputReader.read();
            JSONParser parser = new JSONParser();
            JSONObject response = (JSONObject) parser.parse(responseStr);
            
            String status = (String) response.get("status");
            
            if ("success".equals(status)) {
                long level = (Long) response.get("level");
                levelLabel.setText("Level: " + level);
                levelProgressBar.setValue((int) level);
                
                String[] descriptions = {
                    "Level 1: Beginner - Just getting started",
                    "Level 2: Novice - Building foundational skills",
                    "Level 3: Intermediate - Solid understanding",
                    "Level 4: Advanced - Strong expertise",
                    "Level 5: Expert - Master level"
                };
                
                if (level >= 1 && level <= 5) {
                    levelDescriptionLabel.setText("<html><center>" + descriptions[(int)level - 1] + "</center></html>");
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error loading level: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
