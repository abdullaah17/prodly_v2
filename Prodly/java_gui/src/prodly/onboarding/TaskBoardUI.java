package prodly.onboarding;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TaskBoardUI extends HBox {

    public TaskBoardUI() {
        setPadding(new Insets(25));
        setSpacing(20);

        getChildren().addAll(
            column("Pre-boarding"),
            column("First Day"),
            column("First Week"),
            column("First Month")
        );
    }

    private VBox column(String title) {
        VBox col = new VBox(12);
        col.setPrefWidth(260);
        col.setPadding(new Insets(15));
        col.setStyle("""
            -fx-background-color: #f1f5f9;
            -fx-background-radius: 12;
        """);

        col.getChildren().addAll(
            new Label(title),
            task("Offer Acceptance"),
            task("Orientation"),
            task("Role Training")
        );

        return col;
    }

    private Label task(String text) {
        Label t = new Label(text);
        t.setStyle("""
            -fx-background-color: white;
            -fx-padding: 10;
            -fx-background-radius: 8;
            -fx-border-color: #e5e7eb;
        """);
        return t;
    }
}
