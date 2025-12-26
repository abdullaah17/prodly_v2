package prodly.evaluation;

import prodly.integration.CppBridge;
import javax.swing.*;

public class AssessmentUI extends JFrame {

    public AssessmentUI() {
        JTextField dsa = new JTextField();
        JTextField oop = new JTextField();
        JTextField db  = new JTextField();
        JButton submit = new JButton("Evaluate");

        submit.addActionListener(e -> {
            try {
                int level = CppBridge.runEvaluation(
                        Integer.parseInt(dsa.getText()),
                        Integer.parseInt(oop.getText()),
                        Integer.parseInt(db.getText())
                );
                JOptionPane.showMessageDialog(this, "Assigned Level: L" + level);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        setLayout(new java.awt.GridLayout(4,2));
        add(new JLabel("DSA"));
        add(dsa);
        add(new JLabel("OOP"));
        add(oop);
        add(new JLabel("DB"));
        add(db);
        add(submit);

        setSize(300,200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}
