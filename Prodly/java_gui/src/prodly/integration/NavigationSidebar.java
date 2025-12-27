package prodly.integration;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import prodly.audit.AuditLogUI;
import prodly.evaluation.AssessmentUI;
import prodly.manager.ManagerDashboardUI;
import prodly.onboarding.TaskBoardUI;

public class NavigationSidebar {

    public static VBox create() {
        VBox bar = new VBox(18);
        bar.setPrefWidth(240);
        bar.setStyle("""
            -fx-background-color: #0f172a;
            -fx-padding: 25;
        """);

        bar.getChildren().addAll(
            nav("Evaluation", () -> AppShell.setView(new AssessmentUI())),
            nav("Onboarding", () -> AppShell.setView(new TaskBoardUI())),
            nav("Manager", () -> AppShell.setView(new ManagerDashboardUI())),
            nav("Audit", () -> AppShell.setView(new AuditLogUI()))
        );

        return bar;
    }

    private static Button nav(String text, Runnable action) {
        Button b = new Button(text);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setStyle("""
            -fx-background-color: transparent;
            -fx-text-fill: #e5e7eb;
            -fx-font-size: 14px;
        """);
        b.setOnAction(e -> action.run());
        return b;
    }
}
