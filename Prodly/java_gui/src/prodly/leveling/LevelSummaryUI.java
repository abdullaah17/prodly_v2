package prodly.leveling;

import javax.swing.*;
import java.awt.*;

/**
 * Displays a summary of the employee's current level
 * and readiness status.
 */
public class LevelSummaryUI extends JFrame {

    public LevelSummaryUI(int level, boolean eligible) {
        setTitle("Prodly – Level Summary");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JTextArea summary = new JTextArea();
        summary.setEditable(false);

        summary.setText(
            "Level Evaluation Summary\n" +
            "------------------------\n" +
            "Assigned Level: L" + level + "\n" +
            "Eligibility Status: " + (eligible ? "ELIGIBLE" : "NOT ELIGIBLE") + "\n"
        );

        add(new JScrollPane(summary), BorderLayout.CENTER);

        setVisible(true);
    }
}