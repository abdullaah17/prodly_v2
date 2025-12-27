package prodly.evaluation;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class AssessmentUI extends VBox {

    public AssessmentUI() {
        setPadding(new Insets(30));
        setSpacing(20);

        Label title = new Label("Employee Skill Evaluation");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        GridPane form = new GridPane();
        form.setHgap(20);
        form.setVgap(15);

        TextField dsa = new TextField();
        TextField oop = new TextField();
        TextField db  = new TextField();

        form.addRow(0, new Label("DSA Score"), dsa);
        form.addRow(1, new Label("OOP Score"), oop);
        form.addRow(2, new Label("DB Score"), db);

        Button evaluate = new Button("Evaluate");
        Label result = new Label();

        evaluate.setOnAction(e ->
            result.setText("Assigned Level: L3  |  Status: Eligible")
        );

        getChildren().addAll(title, form, evaluate, result);
    }
}
