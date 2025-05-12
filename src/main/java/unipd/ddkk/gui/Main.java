package unipd.ddkk.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        Label label = new Label("DDKK");
        Scene scene = new Scene(label, 300, 300);
        stage.setScene(scene);
        stage.setTitle("NosenseGenerator");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

