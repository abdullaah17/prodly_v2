package prodly.rolegate;

import javax.swing.*;

public class RoleGateUI extends JFrame {

    public RoleGateUI() {
        setTitle("Role Gate");
        setSize(400, 250);

        JTextArea result = new JTextArea();
        result.setEditable(false);
        result.setText("Role eligibility result shown here.");

        add(new JScrollPane(result));
        setVisible(true);
    }
}
