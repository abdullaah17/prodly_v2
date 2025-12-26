package prodly.onboarding;

import prodly.upskilling.ReEvaluationUI;

import javax.swing.*;

public class TaskBoardUI extends JFrame {

    public TaskBoardUI(boolean eligible) {
        setTitle("Task Board");

        DefaultListModel<String> model = new DefaultListModel<>();

        if (eligible) {
            model.addElement("Company Introduction");
            model.addElement("Tool Setup");
        } else {
            model.addElement("DSA Practice");
            model.addElement("OOP Revision");
            model.addElement("Re-Test");
        }

        JList<String> list = new JList<>(model);
        JButton complete = new JButton("Complete Tasks");

        complete.addActionListener(e -> {
            dispose();
            new ReEvaluationUI();
        });

        add(new JScrollPane(list), "Center");
        add(complete, "South");

        setSize(300,250);
        setVisible(true);
    }
}
