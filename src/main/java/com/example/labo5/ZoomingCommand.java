package com.example.labo5;

import javafx.scene.image.ImageView;

public class ZoomingCommand implements Command {
    private final ImageView imageView;
    private double initialZoom; // Initial zoom level (dynamically updated)
    private double finalZoom;   // Final zoom level (dynamically updated)

    public ZoomingCommand(ImageView imageView, double initialZoom, double finalZoom) {
        this.imageView = imageView;
        this.initialZoom = initialZoom;
        this.finalZoom = finalZoom;
    }

    @Override
    public void execute() {
        // Update the initialZoom dynamically in case the command is re-executed
        initialZoom = imageView.getScaleX();

        // Apply the final zoom level
        imageView.setScaleX(finalZoom);
        imageView.setScaleY(finalZoom);
    }

    @Override
    public void undo() {
        // Update the finalZoom dynamically in case the command is undone
        finalZoom = imageView.getScaleX();

        // Revert to the initial zoom level
        imageView.setScaleX(initialZoom);
        imageView.setScaleY(initialZoom);
    }
}
