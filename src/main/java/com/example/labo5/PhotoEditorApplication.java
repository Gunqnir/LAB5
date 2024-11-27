package com.example.labo5;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PhotoEditorApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Load the FXML layout
        FXMLLoader fxmlLoader = new FXMLLoader(PhotoEditorApplication.class.getResource("photo-editor-view.fxml"));
        BorderPane layout = new BorderPane(fxmlLoader.load());

        // Menu setup
        MenuBar menuBar = new MenuBar();
        Menu fichier = new Menu("Fichier");
        Menu perspective = new Menu("Perspective");
        Menu aide = new Menu("Aide");


        MenuItem nouveau = new MenuItem("Nouveau");
        MenuItem ouvrir = new MenuItem("Ouvrir");
        MenuItem sauvegarder = new MenuItem("Sauvegarder");
        MenuItem fermerTout = new MenuItem("Fermer tout");
        MenuItem quitter = new MenuItem("Quitter");
        quitter.setOnAction(e -> System.exit(0));
        fichier.getItems().addAll(nouveau, ouvrir, sauvegarder, fermerTout, quitter);

        menuBar.getMenus().addAll(fichier, perspective, aide);
        layout.setTop(menuBar);

        // Add a TabPane for managing tabs
        TabPane tabPane = new TabPane();
        layout.setCenter(tabPane);

        // Pass menu items and TabPane to the controller
        PhotoEditorController controller = fxmlLoader.getController();
        controller.setOuvrirMenuItem(ouvrir);
        controller.setTabPane(tabPane);

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
