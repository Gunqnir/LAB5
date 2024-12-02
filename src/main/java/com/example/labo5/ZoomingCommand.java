package com.example.labo5;

import javafx.scene.image.ImageView;

public class ZoomingCommand implements Command {
    private final ImageView imageView;
    private final double initialZoom; // Initial zoom level
    private final double finalZoom;   // Final zoom level

    public ZoomingCommand(ImageView imageView, double initialZoom, double finalZoom) {
        this.imageView = imageView;
        this.initialZoom = initialZoom;
        this.finalZoom = finalZoom;
    }

    @Override
    public void execute() {
        // Set the ImageView to the final zoom level
        imageView.setScaleX(finalZoom);
        imageView.setScaleY(finalZoom);
    }

    @Override
    public void undo() {
        // Revert the ImageView to the initial zoom level
        imageView.setScaleX(initialZoom);
        imageView.setScaleY(initialZoom);
    }
}
