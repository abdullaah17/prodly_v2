package prodly;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import prodly.integration.AppShell;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(AppShell.create(), 1350, 850);
        stage.setTitle("Prodly – B2B Onboarding Platform");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
