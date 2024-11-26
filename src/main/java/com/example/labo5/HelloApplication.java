package com.example.labo5;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));

        stage.setTitle("PhotoEditor");

        //Create MenuBar
        MenuBar menuBar = new MenuBar();

        // add menu options
        Menu fichier = new Menu("Fichier");
        Menu perspective = new Menu("Perspective");
        Menu aide = new Menu("Aide");

        //add menuItems to Fichier
        MenuItem nouveau = new MenuItem("Nouveau");
        MenuItem ouvrir = new MenuItem("Ouvrir");
        MenuItem sauvegarder = new MenuItem("Fermer tout");;
        MenuItem quitter = new MenuItem("Quitter");
        //add action to quitter
        quitter.setOnAction(e -> System.exit(0));
        //add items to fichier
        fichier.getItems().addAll(nouveau,ouvrir, sauvegarder,quitter);

        //add menus to the menubar
        menuBar.getMenus().addAll(fichier, perspective,aide);

        //create borderPane
        BorderPane layout = new BorderPane();
        layout.setTop(menuBar);


        //Creating and setting the scene
        Scene scene = new Scene(layout, 500, 500);
        stage.setScene(scene);
        stage.show();


    }

    public static void main(String[] args) {
        launch();
    }
}