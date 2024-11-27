package com.example.labo5;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;

public class PhotoEditorController {
    @FXML
    private Label welcomeText;

    @FXML
    private ImageView imageView;

    private MenuItem ouvrirMenuItem;

    public void setOuvrirMenuItem(MenuItem ouvrirMenuItem) {
        this.ouvrirMenuItem = ouvrirMenuItem;
        this.ouvrirMenuItem.setOnAction(e -> onLoadImage());
    }

    private void onLoadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            imageView.setImage(image);
            welcomeText.setText("Image loaded: " + selectedFile.getName());
        }
    }
}
