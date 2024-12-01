package com.example.labo5;

import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class Perspective extends Tab implements Observer {
    private final ImageView imageView;
    private double startX; // Dragging start X position
    private double startY; // Dragging start Y position

    public Perspective(String title, ImageModel imageModel) {
        super(title);
        this.imageView = new ImageView();
        this.imageView.setPreserveRatio(true);
        this.imageView.setFitWidth(800);
        this.imageView.setFitHeight(600);

        // Set the current image
        if (imageModel.getImage() != null) {
            this.imageView.setImage(imageModel.getImage());
        }

        this.setContent(imageView);
        initializeDragFeature();
    }

    private void initializeDragFeature() {
        // Add drag handlers to the ImageView
        imageView.setOnMousePressed(this::onMousePressed);
        imageView.setOnMouseDragged(this::onMouseDragged);
    }

    private void onMousePressed(MouseEvent event) {
        startX = event.getSceneX();
        startY = event.getSceneY();
    }

    private void onMouseDragged(MouseEvent event) {
        double deltaX = event.getSceneX() - startX;
        double deltaY = event.getSceneY() - startY;

        // Update image translation
        imageView.setTranslateX(imageView.getTranslateX() + deltaX);
        imageView.setTranslateY(imageView.getTranslateY() + deltaY);

        // Update start position
        startX = event.getSceneX();
        startY = event.getSceneY();
    }

    public ImageView getImageView() {
        return imageView;
    }

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    @Override
    public void update(ImageModel subject) {
        this.imageView.setImage(subject.getImage());
    }
}
