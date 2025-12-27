package prodly.integration;

import javafx.scene.layout.BorderPane;
import prodly.evaluation.AssessmentUI;

public class AppShell {

    private static BorderPane root = new BorderPane();

    public static BorderPane create() {
        root.setLeft(NavigationSidebar.create());
        root.setCenter(new AssessmentUI());
        return root;
    }

    public static void setView(javafx.scene.Node node) {
        root.setCenter(node);
    }
}
