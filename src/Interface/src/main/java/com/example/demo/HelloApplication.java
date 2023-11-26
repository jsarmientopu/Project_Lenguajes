package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        AnchorPane root = fxmlLoader.load();

        // Set the maximum width of the root pane to 1300
        root.setMinWidth(1300); // Optional: You can set the minimum width as well to maintain a fixed width.
        root.setMinHeight(900); // Optional: You can set the minimum height as well to maintain a fixed height.

        Scene scene = new Scene(root, 875, 620);
        stage.setTitle("Grammar Analyzer!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}