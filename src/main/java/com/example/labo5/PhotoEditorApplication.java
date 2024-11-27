package com.example.labo5;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PhotoEditorApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(PhotoEditorApplication.class.getResource("photo-editor-view.fxml"));

        stage.setTitle("PhotoEditor");

        // Create MenuBar
        MenuBar menuBar = new MenuBar();

        // Add menu options
        Menu fichier = new Menu("Fichier");
        MenuItem ouvrir = new MenuItem("Ouvrir");
        MenuItem quitter = new MenuItem("Quitter");
        quitter.setOnAction(e -> System.exit(0));
        fichier.getItems().addAll(ouvrir, quitter);

        menuBar.getMenus().addAll(fichier);

        // Load the FXML layout
        BorderPane layout = new BorderPane();
        layout.setTop(menuBar);
        layout.setCenter(fxmlLoader.load());

        Scene scene = new Scene(layout, 600, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
