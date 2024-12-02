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
        // Load the FXML layout
        FXMLLoader fxmlLoader = new FXMLLoader(PhotoEditorApplication.class.getResource("photo-editor-view.fxml"));
        BorderPane layout = fxmlLoader.load();

        // Menu setup
        MenuBar menuBar = new MenuBar();
        Menu fichier = new Menu("Fichier");
        Menu perspective = new Menu("Perspective");
        Menu aide = new Menu("Aide");

        // Fichier Menu Items
        MenuItem ouvrir = new MenuItem("Ouvrir");
        MenuItem sauvegarder = new MenuItem("Sauvegarder");
        MenuItem quitter = new MenuItem("Quitter");
        quitter.setOnAction(e -> System.exit(0));

        fichier.getItems().addAll(ouvrir, sauvegarder, quitter);

        // Perspective Menu Items
        MenuItem newPerspective = new MenuItem("New Perspective");
        perspective.getItems().add(newPerspective);

        // Add Menus to MenuBar
        menuBar.getMenus().addAll(fichier, perspective, aide);
        layout.setTop(menuBar);

        // Get the controller and set the menu items
        PhotoEditorController controller = fxmlLoader.getController();
        controller.setMenuItems(ouvrir, sauvegarder, perspective);

        // Stage setup
        Scene scene = new Scene(layout, 800, 600);
        stage.setTitle("PhotoEditor");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
