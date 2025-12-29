package prodly.upskilling;

import prodly.manager.ManagerDashboardUI;

import javax.swing.*;

public class ReEvaluationUI extends JFrame {

    public ReEvaluationUI() {
        setTitle("Re-Evaluation");

        JLabel label = new JLabel("Upskilling Complete. Level Improved!", SwingConstants.CENTER);
        JButton next = new JButton("Proceed");

        next.addActionListener(e -> {
            dispose();
            // Note: Would need username and role in real implementation
            // For now, just close the window
        });

        add(label, "Center");
        add(next, "South");

        setSize(350,150);
        setVisible(true);
    }
}
