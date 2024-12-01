package com.example.labo5;

import javafx.scene.image.ImageView;

public class ZoomingCommand implements Command {
    private final ImageView imageView;
    private final double zoomFactor;
    private double previousZoom;

    public ZoomingCommand(ImageView imageView, double zoomFactor) {
        this.imageView = imageView;
        this.zoomFactor = zoomFactor;
    }

    @Override
    public void execute() {
        previousZoom = imageView.getScaleX(); // Save the current zoom level
        imageView.setScaleX(imageView.getScaleX() * zoomFactor);
        imageView.setScaleY(imageView.getScaleY() * zoomFactor);
    }

    @Override
    public void undo() {
        imageView.setScaleX(previousZoom); // Restore the previous zoom level
        imageView.setScaleY(previousZoom);
    }
}
