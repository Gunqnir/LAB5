package com.example.labo5;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class PhotoEditorController {
    private final ImageModel imageModel = new ImageModel(); // Central model for the image
    private final CommandManager commandManager = new CommandManager(); // Command manager for undo/redo

    @FXML
    private TabPane tabPane; // Injected from FXML
    @FXML
    private Button undoButton; // Undo button
    @FXML
    private Button redoButton; // Redo button
    @FXML
    private Slider zoomSlider; // Slider for zooming
    @FXML
    private Label zoomPercentage; // Label to display zoom percentage
    @FXML
    private Label positionLabel; // Label to display x and y coordinates
    @FXML
    private MenuItem ouvrirMenuItem; // Injected from FXML
    @FXML
    private MenuItem newPerspectiveMenuItem; // Injected from FXML
    @FXML
    private MenuItem sauvegarderMenuItem; // Placeholder for Save functionality
    @FXML
    private HBox controlBar; // Toolbar at the bottom

    @FXML
    public void initialize() {
        // Set actions for toolbar buttons
        undoButton.setOnAction(e -> commandManager.undo());
        redoButton.setOnAction(e -> commandManager.redo());

        // Disable undo and redo buttons initially
        undoButton.setDisable(true);
        redoButton.setDisable(true);

        // Add listener to TabPane selection to toggle the control bar
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab != null && newTab.getText().startsWith("Perspective")) {
                controlBar.setVisible(true); // Show controls for Perspective tabs
            } else {
                controlBar.setVisible(false); // Hide controls for other tabs (e.g., Thumbnail)
            }
        });

        // Set up zoom slider actions
        zoomSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double zoomFactor = newValue.doubleValue() / 100.0; // Convert percentage to factor
                applyZoom(zoomFactor);
                zoomPercentage.setText(String.format("%.0f%%", newValue.doubleValue())); // Update zoom percentage label
            }
        });

        // Initialize zoom percentage to 100%
        zoomSlider.setValue(100);
        zoomPercentage.setText("100%");
    }

    private void applyZoom(double zoomFactor) {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        if (selectedTab instanceof Perspective) {
            Perspective perspective = (Perspective) selectedTab;
            ZoomingCommand zoomingCommand = new ZoomingCommand(perspective.getImageView(), zoomFactor);
            commandManager.executeCommand(zoomingCommand);
        }
    }

    private void updatePositionLabel(double x, double y) {
        positionLabel.setText(String.format("x = %.0f : y = %.0f", x, -y));
    }


    private void initializeDraggingForPerspective(Perspective perspective) {
        ImageView imageView = perspective.getImageView();

        imageView.setOnMousePressed(event -> {
            perspective.setStartX(event.getSceneX());
            perspective.setStartY(event.getSceneY());
        });

        imageView.setOnMouseDragged(event -> {
            double deltaX = event.getSceneX() - perspective.getStartX();
            double deltaY = event.getSceneY() - perspective.getStartY();

            // Create a PositioningCommand and execute it
            PositioningCommand positioningCommand = new PositioningCommand(imageView, deltaX, deltaY);
            commandManager.executeCommand(positioningCommand);

            // Update perspective drag start
            perspective.setStartX(event.getSceneX());
            perspective.setStartY(event.getSceneY());

            // Update the position label
            updatePositionLabel(imageView.getTranslateX(), imageView.getTranslateY());
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
        if (tabPane.getTabs().isEmpty() || !tabPane.getTabs().get(0).getText().equals("Thumbnail")) {
            javafx.scene.control.Tab thumbnailTab = new javafx.scene.control.Tab("Thumbnail");
            javafx.scene.image.ImageView thumbnailView = new javafx.scene.image.ImageView();
            thumbnailView.setPreserveRatio(true);
            thumbnailView.setFitWidth(800);
            thumbnailView.setImage(image);

            thumbnailTab.setContent(thumbnailView);
            thumbnailTab.setClosable(false);

            tabPane.getTabs().add(0, thumbnailTab);
        } else {
            javafx.scene.control.Tab thumbnailTab = tabPane.getTabs().get(0);
            javafx.scene.image.ImageView thumbnailView = (javafx.scene.image.ImageView) thumbnailTab.getContent();
            thumbnailView.setImage(image);
        }

        tabPane.getSelectionModel().select(0);
    }

    private void createNewPerspective() {
        if (imageModel.getImage() == null) {
            System.out.println("No image loaded to create a new perspective.");
            return;
        }

        int perspectiveCount = tabPane.getTabs().size();
        Perspective perspective = new Perspective("Perspective " + perspectiveCount, imageModel);
        imageModel.attach(perspective);

        // Initialize dragging functionality for the new perspective
        initializeDraggingForPerspective(perspective);

        tabPane.getTabs().add(perspective);
        tabPane.getSelectionModel().select(perspective);
        System.out.println("New perspective created: " + perspective.getText());
    }


    // Method to dynamically set menu items (called during application setup)
    public void setMenuItems(MenuItem ouvrir, MenuItem undo, MenuItem redo, MenuItem newPerspective, MenuItem sauvegarder) {
        this.ouvrirMenuItem = ouvrir;
        this.newPerspectiveMenuItem = newPerspective;
        this.sauvegarderMenuItem = sauvegarder;

        // Set up menu item actions
        this.ouvrirMenuItem.setOnAction(e -> onLoadImage());
        this.newPerspectiveMenuItem.setOnAction(e -> createNewPerspective());
    }
}
