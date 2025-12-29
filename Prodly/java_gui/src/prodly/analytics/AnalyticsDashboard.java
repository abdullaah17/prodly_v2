package prodly.analytics;

import prodly.integration.InputWriter;
import prodly.integration.CppRunner;
import prodly.integration.OutputReader;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class AnalyticsDashboard extends JFrame {
    private String username;
    private String role;
    
    private JComboBox<String> chartTypeCombo;
    private ChartPanel chartPanel;
    private JTextArea insightsArea;
    
    public AnalyticsDashboard(String username, String role) {
        this.username = username;
        this.role = role;
        
        setTitle("Advanced Analytics Dashboard - " + username);
        setSize(1200, 800);
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
        loadAnalytics();
    }
    
    private void initializeComponents() {
        chartTypeCombo = new JComboBox<>(new String[]{
            "Progress Over Time",
            "Skill Distribution",
            "Level Distribution",
            "Team Performance",
            "Completion Rates"
        });
        chartTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        chartPanel = new ChartPanel();
        
        insightsArea = new JTextArea(6, 40);
        insightsArea.setEditable(false);
        insightsArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        insightsArea.setBackground(new Color(245, 245, 245));
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        // Top panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setBackground(new Color(245, 245, 245));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        topPanel.add(new JLabel("Chart Type:"));
        topPanel.add(chartTypeCombo);
        
        // Center panel - Chart
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Visual Analytics",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 14)
        ));
        centerPanel.add(chartPanel, BorderLayout.CENTER);
        
        // Insights panel
        JPanel insightsPanel = new JPanel(new BorderLayout());
        insightsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Key Insights",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 14)
        ));
        insightsPanel.add(new JScrollPane(insightsArea), BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(insightsPanel, BorderLayout.SOUTH);
    }
    
    private void attachListeners() {
        chartTypeCombo.addActionListener(e -> loadAnalytics());
    }
    
    private void loadAnalytics() {
        String chartType = (String) chartTypeCombo.getSelectedItem();
        chartPanel.setChartType(chartType);
        chartPanel.repaint();
        
        // Generate insights
        StringBuilder insights = new StringBuilder();
        insights.append("Analytics Insights for: ").append(chartType).append("\n");
        insights.append("==========================================\n\n");
        
        if ("Progress Over Time".equals(chartType)) {
            insights.append("• Overall progress shows steady improvement\n");
            insights.append("• 68% average completion rate across all users\n");
            insights.append("• Peak activity during first 2 weeks\n");
            insights.append("• Completion rate increased 5% this month");
        } else if ("Skill Distribution".equals(chartType)) {
            insights.append("• DSA skills are strongest (85% average)\n");
            insights.append("• Testing skills need improvement (65%)\n");
            insights.append("• OOP skills are well distributed\n");
            insights.append("• System Design shows growing interest");
        } else if ("Level Distribution".equals(chartType)) {
            insights.append("• Most users at Level 3 (40%)\n");
            insights.append("• Level 4 users increased by 15%\n");
            insights.append("• Only 5% at Level 1 (new users)\n");
            insights.append("• Strong upward progression trend");
        }
        
        insightsArea.setText(insights.toString());
    }
    
    // Custom chart panel
    class ChartPanel extends JPanel {
        private String chartType = "Progress Over Time";
        
        public void setChartType(String type) {
            this.chartType = type;
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int width = getWidth();
            int height = getHeight();
            int padding = 50;
            
            // Draw background
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, width, height);
            
            // Draw title
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
            FontMetrics fm = g2.getFontMetrics();
            String title = chartType;
            int titleWidth = fm.stringWidth(title);
            g2.drawString(title, (width - titleWidth) / 2, 30);
            
            // Draw chart based on type
            if ("Progress Over Time".equals(chartType)) {
                drawLineChart(g2, width, height, padding);
            } else if ("Skill Distribution".equals(chartType)) {
                drawBarChart(g2, width, height, padding);
            } else if ("Level Distribution".equals(chartType)) {
                drawPieChart(g2, width, height, padding);
            } else {
                drawBarChart(g2, width, height, padding);
            }
        }
        
        private void drawLineChart(Graphics2D g2, int width, int height, int padding) {
            // Draw axes
            g2.setColor(Color.GRAY);
            g2.drawLine(padding, height - padding, width - padding, height - padding);
            g2.drawLine(padding, padding, padding, height - padding);
            
            // Draw data points and line
            int[] data = {45, 52, 58, 63, 68, 72, 68};
            String[] labels = {"Week 1", "Week 2", "Week 3", "Week 4", "Week 5", "Week 6", "Week 7"};
            
            int chartWidth = width - 2 * padding;
            int chartHeight = height - 2 * padding;
            int pointRadius = 5;
            
            g2.setColor(new Color(70, 130, 180));
            g2.setStroke(new BasicStroke(2));
            
            for (int i = 0; i < data.length - 1; i++) {
                int x1 = padding + (i * chartWidth / (data.length - 1));
                int y1 = height - padding - (data[i] * chartHeight / 100);
                int x2 = padding + ((i + 1) * chartWidth / (data.length - 1));
                int y2 = height - padding - (data[i + 1] * chartHeight / 100);
                
                g2.drawLine(x1, y1, x2, y2);
                g2.fillOval(x1 - pointRadius, y1 - pointRadius, pointRadius * 2, pointRadius * 2);
            }
            
            // Draw last point
            int lastX = padding + ((data.length - 1) * chartWidth / (data.length - 1));
            int lastY = height - padding - (data[data.length - 1] * chartHeight / 100);
            g2.fillOval(lastX - pointRadius, lastY - pointRadius, pointRadius * 2, pointRadius * 2);
            
            // Draw labels
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            for (int i = 0; i < labels.length; i++) {
                int x = padding + (i * chartWidth / (labels.length - 1));
                String label = labels[i];
                int labelWidth = g2.getFontMetrics().stringWidth(label);
                g2.drawString(label, x - labelWidth / 2, height - padding + 20);
            }
        }
        
        private void drawBarChart(Graphics2D g2, int width, int height, int padding) {
            String[] categories = {"DSA", "OOP", "DB", "Design", "Testing"};
            int[] values = {85, 78, 72, 68, 65};
            Color[] colors = {
                new Color(70, 130, 180),
                new Color(60, 179, 113),
                new Color(255, 140, 0),
                new Color(138, 43, 226),
                new Color(220, 20, 60)
            };
            
            int chartWidth = width - 2 * padding;
            int chartHeight = height - 2 * padding;
            int barWidth = chartWidth / categories.length - 20;
            int maxValue = 100;
            
            for (int i = 0; i < categories.length; i++) {
                int barHeight = (values[i] * chartHeight) / maxValue;
                int x = padding + (i * (chartWidth / categories.length)) + 10;
                int y = height - padding - barHeight;
                
                g2.setColor(colors[i]);
                g2.fillRect(x, y, barWidth, barHeight);
                
                // Draw value on bar
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                String valueStr = String.valueOf(values[i]);
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(valueStr);
                g2.drawString(valueStr, x + (barWidth - textWidth) / 2, y + barHeight / 2 + 5);
                
                // Draw category label
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                int labelWidth = g2.getFontMetrics().stringWidth(categories[i]);
                g2.drawString(categories[i], x + (barWidth - labelWidth) / 2, height - padding + 20);
            }
        }
        
        private void drawPieChart(Graphics2D g2, int width, int height, int padding) {
            int[] values = {5, 15, 40, 25, 15};
            String[] labels = {"Level 1", "Level 2", "Level 3", "Level 4", "Level 5"};
            Color[] colors = {
                new Color(220, 20, 60),
                new Color(255, 140, 0),
                new Color(255, 215, 0),
                new Color(60, 179, 113),
                new Color(70, 130, 180)
            };
            
            int centerX = width / 2;
            int centerY = height / 2;
            int radius = Math.min(width, height) / 3;
            
            int total = 0;
            for (int v : values) total += v;
            
            int startAngle = 0;
            for (int i = 0; i < values.length; i++) {
                int arcAngle = (values[i] * 360) / total;
                
                g2.setColor(colors[i]);
                g2.fillArc(centerX - radius, centerY - radius, radius * 2, radius * 2, 
                          startAngle, arcAngle);
                
                // Draw label
                double labelAngle = Math.toRadians(startAngle + arcAngle / 2);
                int labelX = (int)(centerX + (radius + 30) * Math.cos(labelAngle));
                int labelY = (int)(centerY + (radius + 30) * Math.sin(labelAngle));
                
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                String label = labels[i] + " (" + values[i] + "%)";
                int labelWidth = g2.getFontMetrics().stringWidth(label);
                g2.drawString(label, labelX - labelWidth / 2, labelY);
                
                startAngle += arcAngle;
            }
        }
    }
}

