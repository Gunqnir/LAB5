package com.example.labo5;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class PhotoEditorController {
    private final ImageModel imageModel = new ImageModel(); // Central model for the image
    private final CommandManager commandManager = CommandManager.getInstance(); // Singleton CommandManager

    @FXML
    private TabPane tabPane; // Injected from FXML
    @FXML
    private Button undoButton; // Undo button
    @FXML
    private Button redoButton; // Redo button
    @FXML
    private Slider zoomSlider; // Slider for zooming
    @FXML
    private Label zoomPercentageLabel; // Label to display zoom percentage
    @FXML
    private Label positionLabel; // Label to display x and y coordinates
    @FXML
    private HBox controlBar; // Toolbar at the bottom
    @FXML
    private MenuItem ouvrirMenuItem; // Injected from FXML
    @FXML
    private MenuItem newPerspectiveMenuItem; // Injected from FXML
    @FXML
    private MenuItem sauvegarderMenuItem; // Placeholder for Save functionality

    @FXML
    public void initialize() {
        // Set actions for undo and redo
        undoButton.setOnAction(e -> {
            commandManager.undo();
            updateUndoRedoButtons();
        });

        redoButton.setOnAction(e -> {
            commandManager.redo();
            updateUndoRedoButtons();
        });

        // Disable undo and redo initially
        updateUndoRedoButtons();

        // Listen to tab selection changes
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab instanceof Perspective) {
                Perspective perspective = (Perspective) newTab;
                updateUIForPerspective(perspective);
            } else {
                resetUI(); // Hide controls for non-Perspective tabs
            }
        });

        // Listen to zoom slider changes
        zoomSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
            if (selectedTab instanceof Perspective) {
                Perspective perspective = (Perspective) selectedTab;
                double zoomFactor = newVal.doubleValue() / 100.0; // Convert slider value to zoom factor
                ZoomingCommand zoomingCommand = new ZoomingCommand(perspective.getImageView(), zoomFactor);
                commandManager.executeCommand(zoomingCommand);

                updateZoomLabel(newVal.doubleValue());
                updateUndoRedoButtons();
            }
        });
    }

    private void updateUndoRedoButtons() {
        undoButton.setDisable(commandManager.isUndoStackEmpty());
        redoButton.setDisable(commandManager.isRedoStackEmpty());
    }

    private void updateUIForPerspective(Perspective perspective) {
        controlBar.setVisible(true);

        // Update the zoom slider and label
        double zoomPercentage = perspective.getZoomLevel() * 100;
        zoomSlider.setValue(zoomPercentage);
        updateZoomLabel(zoomPercentage);

        // Update the position label
        updatePositionLabel(perspective.getX(), perspective.getY());
    }

    private void resetUI() {
        controlBar.setVisible(false);
    }

    private void updateZoomLabel(double zoomPercentage) {
        zoomPercentageLabel.setText(String.format("%.0f%%", zoomPercentage));
    }

    private void updatePositionLabel(double x, double y) {
        positionLabel.setText(String.format("x = %.0f : y = %.0f", x, y));
    }

    private void initializeDraggingForPerspective(Perspective perspective) {
        ImageView imageView = perspective.getImageView();

        imageView.setOnMousePressed(event -> {
            perspective.setX(event.getSceneX());
            perspective.setY(event.getSceneY());
        });

        imageView.setOnMouseDragged(event -> {
            double deltaX = event.getSceneX() - perspective.getX();
            double deltaY = event.getSceneY() - perspective.getY();

            PositioningCommand positioningCommand = new PositioningCommand(imageView, deltaX, deltaY);
            commandManager.executeCommand(positioningCommand);

            perspective.setX(event.getSceneX());
            perspective.setY(event.getSceneY());

            updatePositionLabel(imageView.getTranslateX(), -imageView.getTranslateY());
            updateUndoRedoButtons();
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
        imageModel.attach(perspective); // Attach the perspective as an observer of the ImageModel

        tabPane.getTabs().add(perspective);
        tabPane.getSelectionModel().select(perspective);

        // Initialize dragging and zooming for the new perspective
        initializeDraggingForPerspective(perspective);

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
