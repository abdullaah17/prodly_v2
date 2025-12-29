package prodly.audit;

import javax.swing.*;
import java.awt.*;

public class AuditLogUI extends JFrame {

    public AuditLogUI() {
        setTitle("Prodly – Audit Logs");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JTextArea log = new JTextArea();
        log.setEditable(false);
        log.setText(
            "[10:02] Evaluation completed\n" +
            "[10:04] Level assigned\n" +
            "[10:06] Role approved\n" +
            "[10:20] Task completed: Orientation\n"
        );

        JScrollPane scrollPane = new JScrollPane(log);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }
}
