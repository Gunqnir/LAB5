package com.example.labo5;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class PhotoEditorController {
    private final ImageModel imageModel = new ImageModel();
    @FXML
    private TabPane tabPane;
    @FXML
    private Button undoButton;
    @FXML
    private Button redoButton;
    @FXML
    private Slider zoomSlider;
    @FXML
    private Label zoomPercentageLabel; // Label to display zoom percentage
    @FXML
    private Label positionLabel; // Label to display x and y coordinates
    @FXML
    private MenuItem ouvrirMenuItem;
    @FXML
    private MenuItem newPerspectiveMenuItem;
    @FXML
    private HBox controlBar; // Toolbar UI at the bottom

    @FXML
    public void initialize() {
        // Initially hide the control bar as no image is loaded yet
        controlBar.setVisible(false);

        // Disable undo and redo until an action is made
        undoButton.setDisable(true);
        redoButton.setDisable(true);

        undoButton.setOnAction(e -> {
            Perspective perspective = getSelectedPerspective();
            if (perspective != null) {
                perspective.getCommandManager().undo();

                // Update the zoom slider after an undo
                double currentZoom = perspective.getImageView().getScaleX();
                zoomSlider.setValue(currentZoom * 100);
                updateZoomLabel(currentZoom * 100);

                // Update the position label after an undo
                double currentX = perspective.getImageView().getTranslateX();
                double currentY = perspective.getImageView().getTranslateY();
                updatePositionLabel(currentX, -currentY);

                updateUndoRedoButtons(perspective);
            }
        });

        redoButton.setOnAction(e -> {
            Perspective perspective = getSelectedPerspective();
            if (perspective != null) {
                perspective.getCommandManager().redo();

                // Update the zoom slider to reflect the current zoom level after redo
                double currentZoom = perspective.getImageView().getScaleX();
                zoomSlider.setValue(currentZoom * 100);
                updateZoomLabel(currentZoom * 100);

                // Update the position label to reflect the current position after redo
                double currentX = perspective.getImageView().getTranslateX();
                double currentY = perspective.getImageView().getTranslateY();
                updatePositionLabel(currentX, -currentY);

                updateUndoRedoButtons(perspective);
            }
        });

        // Logic for changing tabs
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab instanceof Perspective) {
                Perspective perspective = (Perspective) newTab;
                updateUIForPerspective(perspective);
                updateUndoRedoButtons((Perspective) newTab);
            } else {
                resetUI(); // Hide controls for thumbnail tab
            }
        });

        // Logic for the zoom slider, tracks the beginning of the zoom action
        zoomSlider.setOnMousePressed(event -> {
            Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
            if (selectedTab instanceof Perspective) {
                Perspective perspective = (Perspective) selectedTab;
                perspective.setZoomLevel(zoomSlider.getValue() / 100.0); // Record the initial zoom
            }
        });

        // Logic for the zoom slider, tracks the end of the zoom action
        // Having a set beginning and end defined makes it easier to manage as a command
        zoomSlider.setOnMouseReleased(event -> {
            Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
            if (selectedTab instanceof Perspective) {
                Perspective perspective = (Perspective) selectedTab;

                double initialZoom = perspective.getZoomLevel(); // Initial zoom
                double finalZoom = zoomSlider.getValue() / 100.0; // Final zoom

                // Create a zoom command
                Command zoomCommand = new ZoomingCommand(perspective.getImageView(), initialZoom, finalZoom);

                // Execute the command using the perspective's CommandManager
                perspective.getCommandManager().executeCommand(zoomCommand);
                updateUndoRedoButtons(perspective);

                perspective.setZoomLevel(finalZoom);
                updateZoomLabel(finalZoom * 100);
                System.out.println("Zoom ended: Final zoom level: " + finalZoom);
            }
        });

    }

    private Perspective getSelectedPerspective() {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        return (selectedTab instanceof Perspective) ? (Perspective) selectedTab : null;
    }

    private void updateUndoRedoButtons(Perspective perspective) {
        if (perspective != null) {
            undoButton.setDisable(!perspective.getCommandManager().canUndo());
            redoButton.setDisable(!perspective.getCommandManager().canRedo());
        }
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

    // Logic for the image positioning functionality
    // Similar logic to the zoom feature, having a defined beginning and end helps manage commands
    // OnMouseDragged updates the position of the image while it's moving, but doesn't log every movement
    // We only care about the beginning and end, as to undo/redo
    private void initializeDragging(ImageView imageView, Perspective perspective) {
        // Two sets to know the image location on top of the mouse location
        imageView.setOnMousePressed(event -> {
            perspective.setX(imageView.getTranslateX());
            perspective.setY(imageView.getTranslateY());
            perspective.setDragStartX(event.getSceneX());
            perspective.setDragStartY(event.getSceneY());
        });

        imageView.setOnMouseDragged(event -> {
            double deltaX = event.getSceneX() - perspective.getDragStartX();
            double deltaY = event.getSceneY() - perspective.getDragStartY();
            imageView.setTranslateX(perspective.getX() + deltaX);
            imageView.setTranslateY(perspective.getY() + deltaY);
            updatePositionLabel(imageView.getTranslateX(), -imageView.getTranslateY());
        });

        imageView.setOnMouseReleased(event -> {
            // Determine the final position of the ImageView after drag
            double initialX = perspective.getX();
            double initialY = perspective.getY();
            double finalX = imageView.getTranslateX();
            double finalY = imageView.getTranslateY();

            PositioningCommand dragCommand = new PositioningCommand(imageView, initialX, initialY, finalX, finalY);
            perspective.getCommandManager().executeCommand(dragCommand);

            // Update undo/redo buttons
            updateUndoRedoButtons(perspective);
            System.out.println("Drag ended: Initial position (x, y): " + initialX + ", " + initialY);
            System.out.println("Drag ended: Final position (x, y): " + finalX + ", " + finalY);
        });
    }

    // Image uploader
    private void onLoadImage() {
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        java.io.File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            System.out.println("File selected: " + selectedFile.getAbsolutePath());
            javafx.scene.image.Image image = new javafx.scene.image.Image(selectedFile.toURI().toString());
            imageModel.setImage(image);
            updateThumbnailTab(image);
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

        tabPane.getTabs().add(perspective);
        tabPane.getSelectionModel().select(perspective);
        initializeDragging(perspective.getImageView(), perspective);
        System.out.println("New perspective created: " + perspective.getText());
    }

    // Links the methods to the buttons
    public void setMenuItems(MenuItem ouvrir, Menu perspectiveMenu) {
        this.ouvrirMenuItem = ouvrir;
        this.newPerspectiveMenuItem = perspectiveMenu;
        this.ouvrirMenuItem.setOnAction(e -> onLoadImage());
        this.newPerspectiveMenuItem.setOnAction(e -> createNewPerspective());
        perspectiveMenu.setDisable(true);

        imageModel.attach(model -> {
            boolean imageLoaded = model.getImage() != null;
            perspectiveMenu.setDisable(!imageLoaded);
        });
    }
}