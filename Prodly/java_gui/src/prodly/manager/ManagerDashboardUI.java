package prodly.manager;

import javax.swing.*;

public class ManagerDashboardUI extends JFrame {

    public ManagerDashboardUI() {
        setTitle("Manager Dashboard");
        setSize(400, 300);

        JTextArea summary = new JTextArea();
        summary.setEditable(false);
        summary.setText(
            "Total Employees: 12\n" +
            "At Risk: 3\n" +
            "Ready for Role: 6"
        );

        add(new JScrollPane(summary));
        setVisible(true);
    }
}
