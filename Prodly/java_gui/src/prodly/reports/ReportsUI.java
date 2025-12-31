package prodly.reports;

import prodly.integration.InputWriter;
import prodly.integration.CppRunner;
import prodly.integration.OutputReader;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

public class ReportsUI extends JFrame {
    private String username;
    private String role;
    
    private JComboBox<String> reportTypeCombo;
    private JComboBox<String> timeRangeCombo;
    private JTable reportTable;
    private DefaultTableModel reportModel;
    private JTextArea summaryArea;
    private JButton generateButton;
    private JButton exportButton;
    
    public ReportsUI(String username, String role) {
        this.username = username;
        this.role = role;
        
        setTitle("Reports & Analytics - " + username);
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
    }
    
    private void initializeComponents() {
        reportTypeCombo = new JComboBox<>(new String[]{
            "Onboarding Progress",
            "Skill Evaluation",
            "Team Performance",
            "User Activity",
            "Completion Rates",
            "Level Distribution"
        });
        reportTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        timeRangeCombo = new JComboBox<>(new String[]{
            "Last 7 Days",
            "Last 30 Days",
            "Last 90 Days",
            "All Time"
        });
        timeRangeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        String[] columns = {"Metric", "Value", "Change", "Status"};
        reportModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reportTable = new JTable(reportModel);
        reportTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        reportTable.setRowHeight(25);
        reportTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        summaryArea = new JTextArea(8, 40);
        summaryArea.setEditable(false);
        summaryArea.setFont(new Font("Courier New", Font.PLAIN, 11));
        summaryArea.setBackground(new Color(245, 245, 245));
        
        generateButton = new JButton("Generate Report");
        exportButton = new JButton("Export to CSV");
        
        styleButton(generateButton, new Color(70, 130, 180));
        styleButton(exportButton, new Color(60, 179, 113));
    }
    
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(150, 35));
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        // Top panel - Controls
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(new Color(245, 245, 245));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        topPanel.add(new JLabel("Report Type:"), gbc);
        gbc.gridx = 1;
        topPanel.add(reportTypeCombo, gbc);
        
        gbc.gridx = 2;
        topPanel.add(new JLabel("Time Range:"), gbc);
        gbc.gridx = 3;
        topPanel.add(timeRangeCombo, gbc);
        
        gbc.gridx = 4;
        topPanel.add(generateButton, gbc);
        gbc.gridx = 5;
        topPanel.add(exportButton, gbc);
        
        // Center panel - Report table
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Report Data",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 14)
        ));
        centerPanel.add(new JScrollPane(reportTable), BorderLayout.CENTER);
        
        // Summary panel
        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Summary & Insights",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 14)
        ));
        summaryPanel.add(new JScrollPane(summaryArea), BorderLayout.CENTER);
        
        // Main layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(summaryPanel, BorderLayout.SOUTH);
        
        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void attachListeners() {
        generateButton.addActionListener(e -> generateReport());
        exportButton.addActionListener(e -> exportToCSV());
    }
    
    private void generateReport() {
        String reportType = (String) reportTypeCombo.getSelectedItem();
        String timeRange = (String) timeRangeCombo.getSelectedItem();
        
        try {
            JSONObject request = new JSONObject();
            request.put("action", "generate_report");
            request.put("reportType", reportType);
            request.put("timeRange", timeRange);
            
            InputWriter.write(request.toJSONString());
            CppRunner.runCore();
            
            String responseStr = OutputReader.read();
            JSONParser parser = new JSONParser();
            JSONObject response = (JSONObject) parser.parse(responseStr);
            
            String status = (String) response.get("status");
            
            if ("success".equals(status)) {
                String dataJson = (String) response.get("data");
                JSONArray data = (JSONArray) parser.parse(dataJson);
                
                reportModel.setRowCount(0);
                for (Object rowObj : data) {
                    JSONObject row = (JSONObject) rowObj;
                    String metric = (String) row.get("metric");
                    String value = (String) row.get("value");
                    String change = (String) row.get("change");
                    String statusText = (String) row.get("status");
                    
                    reportModel.addRow(new Object[]{metric, value, change, statusText});
                }
                
                String summary = (String) response.get("summary");
                summaryArea.setText(summary != null ? summary : "No summary available.");
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error generating report",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            // Generate sample data for demonstration
            generateSampleReport(reportType);
        }
    }
    
    private void generateSampleReport(String reportType) {
        reportModel.setRowCount(0);
        
        if ("Onboarding Progress".equals(reportType)) {
            reportModel.addRow(new Object[]{"Average Completion", "68%", "+5%", "Improving"});
            reportModel.addRow(new Object[]{"Tasks Completed", "142", "+12", "On Track"});
            reportModel.addRow(new Object[]{"Average Time", "12 days", "-2 days", "Faster"});
            reportModel.addRow(new Object[]{"Stuck Employees", "3", "-1", "Improving"});
            
            summaryArea.setText("Onboarding Progress Report\n" +
                "========================\n\n" +
                "• Overall completion rate has improved by 5% this period\n" +
                "• Average onboarding time decreased by 2 days\n" +
                "• 3 employees need additional support\n" +
                "• Most employees are progressing well through their paths");
                
        } else if ("Skill Evaluation".equals(reportType)) {
            reportModel.addRow(new Object[]{"Average Score", "78/100", "+3", "Good"});
            reportModel.addRow(new Object[]{"Top Skill", "DSA", "85%", "Excellent"});
            reportModel.addRow(new Object[]{"Needs Improvement", "Testing", "65%", "Attention"});
            reportModel.addRow(new Object[]{"Evaluations", "45", "+8", "Active"});
            
            summaryArea.setText("Skill Evaluation Report\n" +
                "=====================\n\n" +
                "• Average skill score is 78/100, showing good progress\n" +
                "• DSA is the strongest skill area across the team\n" +
                "• Testing skills need focused training\n" +
                "• 45 evaluations completed this period");
                
        } else if ("Team Performance".equals(reportType)) {
            reportModel.addRow(new Object[]{"Team Average Level", "3.2", "+0.3", "Growing"});
            reportModel.addRow(new Object[]{"High Performers", "12", "+2", "Excellent"});
            reportModel.addRow(new Object[]{"At Risk", "3", "-1", "Improving"});
            reportModel.addRow(new Object[]{"Productivity Score", "82%", "+4%", "Strong"});
            
            summaryArea.setText("Team Performance Report\n" +
                "======================\n\n" +
                "• Team average level increased to 3.2\n" +
                "• 12 high performers identified\n" +
                "• 3 employees need additional support\n" +
                "• Overall productivity is strong at 82%");
        }
    }
    
    private void exportToCSV() {
        if (reportModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "No data to export. Please generate a report first.",
                "No Data",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Report to CSV");
        fileChooser.setSelectedFile(new java.io.File("report.csv"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File file = fileChooser.getSelectedFile();
                java.io.FileWriter writer = new java.io.FileWriter(file);
                
                // Write headers
                for (int i = 0; i < reportModel.getColumnCount(); i++) {
                    writer.write(reportModel.getColumnName(i));
                    if (i < reportModel.getColumnCount() - 1) writer.write(",");
                }
                writer.write("\n");
                
                // Write data
                for (int i = 0; i < reportModel.getRowCount(); i++) {
                    for (int j = 0; j < reportModel.getColumnCount(); j++) {
                        Object value = reportModel.getValueAt(i, j);
                        writer.write(value != null ? value.toString() : "");
                        if (j < reportModel.getColumnCount() - 1) writer.write(",");
                    }
                    writer.write("\n");
                }
                
                writer.close();
                
                JOptionPane.showMessageDialog(this,
                    "Report exported successfully to:\n" + file.getAbsolutePath(),
                    "Export Successful",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Error exporting file: " + ex.getMessage(),
                    "Export Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}


