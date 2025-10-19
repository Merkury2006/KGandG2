package org.example.kgandg2_1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("mainwindow.fxml"));

        GraphicsManager graphicsManager = new GraphicsManager();
        GraphicsController graphicsController = new GraphicsController(graphicsManager);

        fxmlLoader.setController(graphicsController);

        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Bezier Curves");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}