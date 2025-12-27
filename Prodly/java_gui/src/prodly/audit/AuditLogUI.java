package prodly.audit;

import javafx.geometry.Insets;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class AuditLogUI extends VBox {

    public AuditLogUI() {
        setPadding(new Insets(30));

        TextArea log = new TextArea();
        log.setEditable(false);
        log.setText("""
        [10:02] Evaluation completed
        [10:04] Level assigned: L3
        [10:06] Role approved
        [10:20] Task completed: Orientation
        """);

        getChildren().add(log);
    }
}
