package prodly.dashboard;

import javax.swing.*;

public class EmployeeDashboard extends JFrame {

    public EmployeeDashboard() {
        setTitle("Employee Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        add(new JLabel("Welcome, Employee 👋", SwingConstants.CENTER));
    }
}
