package prodly.manager;

import prodly.audit.AuditLogUI;

import javax.swing.*;

public class ManagerDashboardUI extends JFrame {

    public ManagerDashboardUI() {
        setTitle("Manager Dashboard");

        String[][] data = {
                {"Ahsan", "L3", "Low"},
                {"Ali", "L2", "High"}
        };

        String[] cols = {"Employee", "Level", "Risk"};

        JTable table = new JTable(data, cols);
        JButton audit = new JButton("View Audit Log");

        audit.addActionListener(e -> {
            dispose();
            new AuditLogUI();
        });

        add(new JScrollPane(table), "Center");
        add(audit, "South");

        setSize(400,250);
        setVisible(true);
    }
}
