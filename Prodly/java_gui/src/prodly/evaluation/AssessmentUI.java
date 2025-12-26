package prodly.evaluation;

import prodly.integration.CppBridge;
import prodly.leveling.LevelSummaryUI;

import javax.swing.*;

public class AssessmentUI extends JFrame {

    public AssessmentUI() {
        setTitle("Skill Evaluation");

        JTextField dsa = new JTextField();
        JTextField oop = new JTextField();
        JTextField db  = new JTextField();

        JButton evaluate = new JButton("Evaluate");

        evaluate.addActionListener(e -> {
            try {
                int level = CppBridge.runEvaluation(
                        Integer.parseInt(dsa.getText()),
                        Integer.parseInt(oop.getText()),
                        Integer.parseInt(db.getText())
                );
                dispose();
                new LevelSummaryUI(level);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input");
            }
        });

        setLayout(new java.awt.GridLayout(4,2));
        add(new JLabel("DSA Score"));
        add(dsa);
        add(new JLabel("OOP Score"));
        add(oop);
        add(new JLabel("DB Score"));
        add(db);
        add(new JLabel());
        add(evaluate);

        setSize(300,200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}
