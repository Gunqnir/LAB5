package com.example.labo5;

import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;

public class Perspective extends Tab implements Observer {
    private final CommandManager commandManager = new CommandManager();
    private final ImageView imageView;
    private double x = 0.0;
    private double y = 0.0;
    private double dragStartX = 0.0; // Mouse start X
    private double dragStartY = 0.0; // Mouse start Y
    private double zoomLevel = 1.0;

    public Perspective(String title, ImageModel imageModel) {
        super(title);
        this.imageView = new ImageView();
        this.imageView.setPreserveRatio(true);
        this.imageView.setFitWidth(800);
        this.imageView.setFitHeight(600);

        if (imageModel.getImage() != null) {
            this.imageView.setImage(imageModel.getImage());
        }

        this.setContent(imageView);
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    // Other Perspective methods (update, setX, setY, etc.)


    public ImageView getImageView() {
        return imageView;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getDragStartX() {
        return dragStartX;
    }

    public void setDragStartX(double dragStartX) {
        this.dragStartX = dragStartX;
    }

    public double getDragStartY() {
        return dragStartY;
    }

    public void setDragStartY(double dragStartY) {
        this.dragStartY = dragStartY;
    }

    public double getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(double zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    @Override
    public void update(ImageModel subject) {
        if (subject.getImage() != null) {
            this.imageView.setImage(subject.getImage());
        }
    }
}

