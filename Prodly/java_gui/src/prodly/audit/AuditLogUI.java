package prodly.audit;

import javax.swing.*;
import java.io.*;

public class AuditLogUI extends JFrame {

    public AuditLogUI() {
        setTitle("Audit Log");

        JTextArea area = new JTextArea();
        area.setEditable(false);

        try {
            BufferedReader br = new BufferedReader(
                    new FileReader("integration/output/audit.txt"));
            String line;
            while ((line = br.readLine()) != null)
                area.append(line + "\n");
            br.close();
        } catch (Exception e) {
            area.setText("No audit records found.");
        }

        add(new JScrollPane(area));

        setSize(400,300);
        setVisible(true);
    }
}
