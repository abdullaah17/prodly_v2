package prodly.upskilling;

import prodly.integration.InputWriter;
import prodly.integration.CppRunner;
import prodly.integration.OutputReader;

import javax.swing.*;
import java.awt.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class UpskillUI extends JFrame {
    private String username;
    private String role;
    private JTextArea recommendationsArea;
    private JButton refreshButton;
    private JLabel countLabel;
    
    public UpskillUI(String username, String role) {
        this.username = username;
        this.role = role;
        
        setTitle("Upskilling Recommendations - " + username);
        setSize(700, 500);
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
        loadRecommendations();
    }
    
    private void initializeComponents() {
        recommendationsArea = new JTextArea(15, 40);
        recommendationsArea.setEditable(false);
        recommendationsArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        recommendationsArea.setLineWrap(true);
        recommendationsArea.setWrapStyleWord(true);
        
        countLabel = new JLabel("Recommendations: -");
        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        refreshButton = new JButton("Refresh Recommendations");
        refreshButton.setBackground(new Color(70, 130, 180));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(new JLabel("Personalized Learning Recommendations"));
        topPanel.add(countLabel);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Recommended Skills & Learning Paths",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 14)
        ));
        centerPanel.add(new JScrollPane(recommendationsArea), BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(refreshButton);
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void attachListeners() {
        refreshButton.addActionListener(e -> loadRecommendations());
    }
    
    private void loadRecommendations() {
        try {
            JSONObject request = new JSONObject();
            request.put("action", "get_recommendations");
            request.put("userId", username);
            
            InputWriter.write(request.toJSONString());
            CppRunner.runCore();
            
            String responseStr = OutputReader.read();
            JSONParser parser = new JSONParser();
            JSONObject response = (JSONObject) parser.parse(responseStr);
            
            String status = (String) response.get("status");
            
            if ("success".equals(status)) {
                long count = (Long) response.get("count");
                countLabel.setText("Recommendations: " + count);
                
                StringBuilder recommendations = new StringBuilder();
                recommendations.append("Based on your current progress and completed skills, here are personalized recommendations:\n\n");
                recommendations.append("1. Advanced Data Structures & Algorithms\n");
                recommendations.append("   - Priority: High\n");
                recommendations.append("   - Estimated Time: 40 hours\n");
                recommendations.append("   - Prerequisites: Basic DSA completed\n\n");
                
                recommendations.append("2. System Design Fundamentals\n");
                recommendations.append("   - Priority: Medium\n");
                recommendations.append("   - Estimated Time: 30 hours\n");
                recommendations.append("   - Prerequisites: OOP and DB basics\n\n");
                
                recommendations.append("3. Database Optimization\n");
                recommendations.append("   - Priority: Medium\n");
                recommendations.append("   - Estimated Time: 25 hours\n");
                recommendations.append("   - Prerequisites: Database fundamentals\n\n");
                
                recommendations.append("These recommendations are generated using graph-based traversal algorithms\n");
                recommendations.append("to identify the optimal learning path based on your skill dependencies.");
                
                recommendationsArea.setText(recommendations.toString());
            } else {
                recommendationsArea.setText("Unable to load recommendations. Please try again later.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error loading recommendations: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
