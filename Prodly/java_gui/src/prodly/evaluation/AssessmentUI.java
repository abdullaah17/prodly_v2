package prodly.evaluation;

import javax.swing.*;
import java.awt.*;

public class AssessmentUI extends JFrame {

    private JTextField dsa = new JTextField();
    private JTextField oop = new JTextField();
    private JTextField db  = new JTextField();
    private JTextArea output = new JTextArea(6, 30);

    public AssessmentUI() {
        setTitle("Prodly – Evaluation");
        setSize(420, 320);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));
        form.add(new JLabel("DSA Score"));
        form.add(dsa);
        form.add(new JLabel("OOP Score"));
        form.add(oop);
        form.add(new JLabel("DB Score"));
        form.add(db);

        JButton eval = new JButton("Evaluate");
        form.add(eval);

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(output), BorderLayout.CENTER);

        eval.addActionListener(e -> evaluate());

        setVisible(true);
    }

    private void evaluate() {
        int dsaScore = Integer.parseInt(dsa.getText());
        int oopScore = Integer.parseInt(oop.getText());
        int dbScore  = Integer.parseInt(db.getText());

        int level = (dsaScore + oopScore + dbScore) / 30;

        output.setText(
            "Evaluation Complete\n" +
            "Assigned Level: L" + level
        );
    }
}
