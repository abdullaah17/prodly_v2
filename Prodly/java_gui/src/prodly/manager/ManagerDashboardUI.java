package prodly.manager;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class ManagerDashboardUI extends VBox {

    public ManagerDashboardUI() {
        setPadding(new Insets(30));
        setSpacing(25);

        GridPane grid = new GridPane();
        grid.setHgap(20);

        grid.add(card("Utilization", "78%"), 0, 0);
        grid.add(card("At-Risk Hires", "2"), 1, 0);
        grid.add(card("Avg Ramp-Up", "21 days"), 2, 0);

        getChildren().add(grid);
    }

    private VBox card(String title, String value) {
        VBox c = new VBox(8);
        c.setPadding(new Insets(20));
        c.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 12;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1),12,0,0,4);
        """);

        Label t = new Label(title);
        Label v = new Label(value);
        v.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        c.getChildren().addAll(t, v);
        return c;
    }
}
