package com.example.labo5;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class PhotoEditorController {
    private final ImageModel imageModel = new ImageModel(); // Central model for the image

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
    private MenuItem ouvrirMenuItem; // Injected from FXML
    @FXML
    private MenuItem newPerspectiveMenuItem; // Injected from FXML
    @FXML
    private MenuItem sauvegarderMenuItem; // Placeholder for Save functionality
    @FXML
    private MenuItem fermerToutMenuItem; // Injected from FXML
    @FXML
    private HBox controlBar; // Toolbar at the bottom

    @FXML
    public void initialize() {
        // Initially hide the control bar as no image is loaded yet
        controlBar.setVisible(false);

        // Disable undo and redo initially
        undoButton.setDisable(true);
        redoButton.setDisable(true);

        undoButton.setOnAction(e -> {
            Perspective perspective = getSelectedPerspective();
            if (perspective != null) {
                perspective.getCommandManager().undo();
                updateUndoRedoButtons(perspective);
            }
        });

        redoButton.setOnAction(e -> {
            Perspective perspective = getSelectedPerspective();
            if (perspective != null) {
                perspective.getCommandManager().redo();
                updateUndoRedoButtons(perspective);
            }
        });

        // Listen to tab selection changes
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab instanceof Perspective) {
                Perspective perspective = (Perspective) newTab;
                updateUIForPerspective(perspective);
                updateUndoRedoButtons((Perspective) newTab);
            } else {
                resetUI(); // Hide controls for non-Perspective tabs
            }
        });

        // Listen to zoom slider changes
        zoomSlider.setOnMousePressed(event -> {
            Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
            if (selectedTab instanceof Perspective) {
                Perspective perspective = (Perspective) selectedTab;
                perspective.setZoomLevel(zoomSlider.getValue() / 100.0); // Record the initial zoom
            }
        });

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

                // Update the perspective zoom
                perspective.setZoomLevel(finalZoom);
                updateZoomLabel(finalZoom * 100);

                // Log the final zoom
                System.out.println("Zoom ended: Final zoom level: " + finalZoom);
            }
        });

    }

    private Perspective getSelectedPerspective() {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        return (selectedTab instanceof Perspective) ? (Perspective) selectedTab : null;
    }

    // Update undo/redo buttons dynamically
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



    private void initializeDragging(ImageView imageView, Perspective perspective) {
        imageView.setOnMousePressed(event -> {
            // Store the ImageView's initial position (translation values) when drag starts
            perspective.setX(imageView.getTranslateX());
            perspective.setY(imageView.getTranslateY());

            // Also store the mouse's starting position for relative movement
            perspective.setDragStartX(event.getSceneX());
            perspective.setDragStartY(event.getSceneY());
        });

        imageView.setOnMouseDragged(event -> {
            // Calculate deltas based on the mouse's movement
            double deltaX = event.getSceneX() - perspective.getDragStartX();
            double deltaY = event.getSceneY() - perspective.getDragStartY();

            // Update the ImageView's translation based on the deltas
            imageView.setTranslateX(perspective.getX() + deltaX);
            imageView.setTranslateY(perspective.getY() + deltaY);

            // Log real-time updates
            updatePositionLabel(imageView.getTranslateX(), -imageView.getTranslateY());
        });

        imageView.setOnMouseReleased(event -> {
            // Determine the final position of the ImageView after drag
            double initialX = perspective.getX();
            double initialY = perspective.getY();
            double finalX = imageView.getTranslateX();
            double finalY = imageView.getTranslateY();

            // Create and execute the PositioningCommand
            PositioningCommand dragCommand = new PositioningCommand(imageView, initialX, initialY, finalX, finalY);
            perspective.getCommandManager().executeCommand(dragCommand);

            // Update undo/redo buttons
            updateUndoRedoButtons(perspective);

            // Log final position
            System.out.println("Drag ended: Initial position (x, y): " + initialX + ", " + initialY);
            System.out.println("Drag ended: Final position (x, y): " + finalX + ", " + finalY);
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
        initializeDragging(perspective.getImageView(), perspective);

        System.out.println("New perspective created: " + perspective.getText());
    }



    public void setMenuItems(MenuItem ouvrir, MenuItem sauvegarder, Menu perspectiveMenu) {
        this.ouvrirMenuItem = ouvrir;
        this.sauvegarderMenuItem = sauvegarder;
        this.newPerspectiveMenuItem = perspectiveMenu;

        // Set up menu item actions
        this.ouvrirMenuItem.setOnAction(e -> onLoadImage());
        this.newPerspectiveMenuItem.setOnAction(e -> createNewPerspective());
        perspectiveMenu.setDisable(true); // Parent button is disabled, not menu item.

        // Initially disable menu items
        sauvegarderMenuItem.setDisable(true);

        // Attach observers to ImageModel for dynamic updates
        imageModel.attach(model -> {
            boolean imageLoaded = model.getImage() != null;
            perspectiveMenu.setDisable(!imageLoaded);
            sauvegarderMenuItem.setDisable(!imageLoaded);
        });
    }
}
