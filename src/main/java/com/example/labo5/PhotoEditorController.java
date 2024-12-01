package com.example.labo5;

import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;

public class PhotoEditorController {
    private ImageModel imageModel = new ImageModel(); // Central model for the image
    @FXML
    private TabPane tabPane; // Injected from FXML
    @FXML
    private MenuItem ouvrirMenuItem;
    private MenuItem sauvegarderMenuItem;
    private MenuItem fermerToutMenuItem;
    private Menu perspectiveMenu;
    @FXML
    private MenuItem newPerspectiveMenuItem;

    // Method to set menu items
    public void setMenuItems(MenuItem ouvrir, MenuItem sauvegarder, MenuItem fermerTout, Menu perspective, MenuItem newPerspective) {
        this.ouvrirMenuItem = ouvrir;
        this.sauvegarderMenuItem = sauvegarder;
        this.fermerToutMenuItem = fermerTout;
        this.perspectiveMenu = perspective;
        this.newPerspectiveMenuItem = newPerspective;

        // Set up menu item actions
        this.ouvrirMenuItem.setOnAction(e -> onLoadImage());
        this.newPerspectiveMenuItem.setOnAction(e -> createNewPerspective());

        // Initially disable certain menu items
        this.perspectiveMenu.setDisable(true); // Disable the entire Perspective menu
        this.sauvegarderMenuItem.setDisable(true);
        this.fermerToutMenuItem.setDisable(true);

        // Attach menu items to ImageModel as observers
        imageModel.attach(subject -> {
            boolean imageLoaded = subject.getImage() != null;
            perspectiveMenu.setDisable(!imageLoaded);
            sauvegarderMenuItem.setDisable(!imageLoaded);
            fermerToutMenuItem.setDisable(!imageLoaded);
        });
    }

    private void onLoadImage() {
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        java.io.File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            System.out.println("File selected: " + selectedFile.getAbsolutePath());
            javafx.scene.image.Image image = new javafx.scene.image.Image(selectedFile.toURI().toString());
            imageModel.setImage(image); // Update the ImageModel with the new image
            updateThumbnailTab(image); // Create or update the thumbnail tab
        } else {
            System.out.println("No file selected.");
        }
    }


    private void updateThumbnailTab(javafx.scene.image.Image image) {
        // Check if the thumbnail tab exists
        if (tabPane.getTabs().isEmpty() || !tabPane.getTabs().get(0).getText().equals("Thumbnail")) {
            // Create the thumbnail tab
            javafx.scene.control.Tab thumbnailTab = new javafx.scene.control.Tab("Thumbnail");
            javafx.scene.image.ImageView thumbnailView = new javafx.scene.image.ImageView();
            thumbnailView.setPreserveRatio(true);
            thumbnailView.setFitWidth(800); // Adjust to match your app dimensions
            thumbnailView.setImage(image);

            // Set the ImageView as the content of the tab
            thumbnailTab.setContent(thumbnailView);
            thumbnailTab.setClosable(false); // Prevent the thumbnail tab from being closed

            // Add the tab to the TabPane
            tabPane.getTabs().add(0, thumbnailTab); // Add it as the first tab
        } else {
            // Update the existing thumbnail tab's ImageView
            javafx.scene.control.Tab thumbnailTab = tabPane.getTabs().get(0);
            javafx.scene.image.ImageView thumbnailView = (javafx.scene.image.ImageView) thumbnailTab.getContent();
            thumbnailView.setImage(image);
        }

        // Select the thumbnail tab
        tabPane.getSelectionModel().select(0);
    }



    private void createNewPerspective() {
        if (imageModel.getImage() == null) {
            System.out.println("No image loaded to create a new perspective.");
            return;
        }

        // Count the number of tabs for unique perspective names
        int perspectiveCount = tabPane.getTabs().size();

        // Create a new Perspective tab linked to the ImageModel
        Perspective perspective = new Perspective("Perspective " + perspectiveCount, imageModel);
        imageModel.attach(perspective); // Attach the perspective as an observer of the ImageModel

        // Add the perspective tab to the TabPane
        tabPane.getTabs().add(perspective);

        // Select the new tab
        tabPane.getSelectionModel().select(perspective);
        System.out.println("New perspective created: " + perspective.getText());
    }


}
