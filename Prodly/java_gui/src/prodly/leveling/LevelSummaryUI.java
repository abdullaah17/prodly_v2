package prodly.leveling;

import prodly.rolegate.RoleGateUI;

import javax.swing.*;

public class LevelSummaryUI extends JFrame {

    public LevelSummaryUI(int level) {
        setTitle("Level Summary");

        JLabel label = new JLabel("Assigned Level: L" + level, SwingConstants.CENTER);
        JButton next = new JButton("Continue to Role Selection");

        next.addActionListener(e -> {
            dispose();
            new RoleGateUI(level);
        });

        add(label, "Center");
        add(next, "South");

        setSize(300,150);
        setVisible(true);
    }
}
