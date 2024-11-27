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

        // Add menus
        Menu fichier = new Menu("Fichier");
        Menu perspective = new Menu("Perspective");
        Menu aide = new Menu("Aide");

        // Add menu items to "Fichier"
        MenuItem nouveau = new MenuItem("Nouveau");
        MenuItem ouvrir = new MenuItem("Ouvrir"); // For uploading an image
        MenuItem sauvegarder = new MenuItem("Sauvegarder");
        MenuItem quitter = new MenuItem("Quitter");
        quitter.setOnAction(e -> System.exit(0));
        fichier.getItems().addAll(nouveau, ouvrir, sauvegarder, quitter);

        // Add menus to the menu bar
        menuBar.getMenus().addAll(fichier, perspective, aide);

        // Load the FXML layout
        BorderPane layout = new BorderPane();
        layout.setTop(menuBar);
        layout.setCenter(fxmlLoader.load());

        // Pass the "Ouvrir" menu item to the controller
        PhotoEditorController controller = fxmlLoader.getController();
        controller.setOuvrirMenuItem(ouvrir);

        // Create and set the scene
        Scene scene = new Scene(layout, 600, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
