package com.example.labo5;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;

public class PhotoEditorController {
    private MenuItem ouvrirMenuItem;
    private TabPane tabPane;
    private Image currentImage;

    public void setOuvrirMenuItem(MenuItem ouvrirMenuItem) {
        this.ouvrirMenuItem = ouvrirMenuItem;
        this.ouvrirMenuItem.setOnAction(e -> onLoadImage());
    }

    public void setTabPane(TabPane tabPane) {
        this.tabPane = tabPane;
    }

    private void onLoadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            System.out.println("File selected: " + selectedFile.getAbsolutePath());
            currentImage = new Image(selectedFile.toURI().toString());
            addImageToTab(currentImage, selectedFile.getName());
        } else {
            System.out.println("No file selected.");
        }
    }

    private void addImageToTab(Image image, String fileName) {
        if (tabPane == null) {
            System.out.println("TabPane is null.");
            return;
        }

        // Create a new tab with the file name as the title
        Tab tab = new Tab(fileName);

        // Create an ImageView and set the image
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(800); // Adjust to your application's dimensions
        imageView.setPreserveRatio(true);

        // Add the ImageView to the tab
        tab.setContent(imageView);
        tabPane.getTabs().add(tab);

        // Select the new tab
        tabPane.getSelectionModel().select(tab);
        System.out.println("New tab created: " + fileName);
    }
}
