package com.example.labo5;

import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import java.io.File;

public class PhotoEditorController {
    private ImageModel imageModel = new ImageModel(); // Central model for the image
    private Tab thumbnailTab; // The thumbnail tab
    private int perspectiveCount = 0; // Counter for naming perspectives

    @FXML
    private TabPane tabPane; // Injected from FXML
    private Menu perspectiveMenu;
    private MenuItem ouvrirMenuItem;
    private MenuItem sauvegarderMenuItem;
    private MenuItem fermerToutMenuItem;
    private MenuItem newPerspectiveMenuItem;

    public void setMenuItems(MenuItem ouvrir, MenuItem sauvegarder, MenuItem fermerTout, Menu perspective, MenuItem newPerspective) {
        this.ouvrirMenuItem = ouvrir;
        this.sauvegarderMenuItem = sauvegarder;
        this.fermerToutMenuItem = fermerTout;
        this.perspectiveMenu = perspective;
        this.newPerspectiveMenuItem = newPerspective;

        // Set actions
        this.ouvrirMenuItem.setOnAction(e -> onLoadImage());
        this.newPerspectiveMenuItem.setOnAction(e -> createNewPerspective());

        // Initially disable certain menu items
        this.perspectiveMenu.setDisable(true); // Disable entire "Perspective" menu
        this.sauvegarderMenuItem.setDisable(true);
        this.fermerToutMenuItem.setDisable(true);

        // Attach menu items as observers to the image model
        imageModel.attach(new MenuItemObserver(perspectiveMenu, true));
        imageModel.attach(new MenuItemObserver(sauvegarderMenuItem, true));
        imageModel.attach(new MenuItemObserver(fermerToutMenuItem, true));
    }

    private void onLoadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            System.out.println("File selected: " + selectedFile.getAbsolutePath());
            Image image = new Image(selectedFile.toURI().toString());
            imageModel.setImage(image); // Update the model, which updates all observers
            updateThumbnailTab(image); // Create or update the thumbnail tab
        } else {
            System.out.println("No file selected.");
        }
    }

    private void updateThumbnailTab(Image image) {
        if (thumbnailTab == null) {
            // Create the thumbnail tab if it doesn't exist
            thumbnailTab = new Tab("Thumbnail");
            javafx.scene.image.ImageView thumbnailView = new javafx.scene.image.ImageView();
            thumbnailView.setFitWidth(800); // Match the application size
            thumbnailView.setPreserveRatio(true);

            // Add the ImageView to the tab and make it non-closable
            thumbnailTab.setContent(thumbnailView);
            thumbnailTab.setClosable(false);

            // Add the thumbnail tab to the TabPane
            tabPane.getTabs().add(0, thumbnailTab); // Add as the first tab
        }

        // Update the thumbnail's ImageView with the new image
        javafx.scene.image.ImageView thumbnailView = (javafx.scene.image.ImageView) thumbnailTab.getContent();
        thumbnailView.setImage(image);

        // Select the thumbnail tab
        tabPane.getSelectionModel().select(thumbnailTab);
    }

    private void createNewPerspective() {
        if (imageModel.getImage() == null) {
            System.out.println("No image loaded to create a new perspective.");
            return;
        }

        // Increment the perspective count for unique names
        perspectiveCount++;

        // Create a new tab with the perspective name
        String perspectiveName = "Perspective " + perspectiveCount;
        Tab tab = new Tab(perspectiveName);

        // Create an ImageView for the perspective
        javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(imageModel.getImage());
        imageView.setFitWidth(800); // Match the application size
        imageView.setPreserveRatio(true);

        // Add the ImageView to the tab
        tab.setContent(imageView);
        tabPane.getTabs().add(tab);

        // Select the new tab
        tabPane.getSelectionModel().select(tab);
        System.out.println("New perspective created: " + perspectiveName);
    }
}
