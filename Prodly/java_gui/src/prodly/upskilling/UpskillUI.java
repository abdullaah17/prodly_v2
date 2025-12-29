package prodly.upskilling;

import javax.swing.*;

public class UpskillUI extends JFrame {

    public UpskillUI() {
        setTitle("Upskilling Plan");
        setSize(400, 300);

        JTextArea plan = new JTextArea();
        plan.setEditable(false);
        plan.setText(
            "Recommended Tasks:\n" +
            "• Advanced DSA\n" +
            "• Backend Project\n" +
            "• DB Optimization"
        );

        add(new JScrollPane(plan));
        setVisible(true);
    }
}
