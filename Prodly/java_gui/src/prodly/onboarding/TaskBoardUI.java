package prodly.onboarding;

import javax.swing.*;
import java.awt.*;

public class TaskBoardUI extends JFrame {

    public TaskBoardUI() {
        setTitle("Onboarding Tasks");
        setSize(500, 300);

        JTextArea tasks = new JTextArea();
        tasks.setEditable(false);
        tasks.setText(
            "• Company Orientation\n" +
            "• Tool Setup\n" +
            "• Role Training"
        );

        add(new JScrollPane(tasks), BorderLayout.CENTER);
        setVisible(true);
    }
}
