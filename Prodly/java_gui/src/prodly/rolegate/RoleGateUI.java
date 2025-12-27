package prodly.rolegate;

import prodly.onboarding.TaskBoardUI;

import javax.swing.*;

public class RoleGateUI extends JFrame {

    public RoleGateUI(int level) {
        setTitle("Role Eligibility");

        String[] roles = {"Backend", "Frontend", "Data"};
        JComboBox<String> roleBox = new JComboBox<>(roles);
        JLabel result = new JLabel(" ", SwingConstants.CENTER);
        JButton check = new JButton("Check Eligibility");

        check.addActionListener(e -> {
            String role = (String) roleBox.getSelectedItem();
            boolean eligible =
                    (role.equals("Backend") && level >= 3) ||
                    (role.equals("Frontend") && level >= 2) ||
                    (role.equals("Data") && level >= 4);

            result.setText(eligible ? "✔ Eligible" : "✖ Not Eligible");

            dispose();
            new TaskBoardUI(eligible);
        });

        setLayout(new java.awt.GridLayout(4,1));
        add(new JLabel("Select Role", SwingConstants.CENTER));
        add(roleBox);
        add(check);
        add(result);

        setSize(300,200);
        setVisible(true);
    }
}
