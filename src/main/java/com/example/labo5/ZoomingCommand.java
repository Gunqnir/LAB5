package com.example.labo5;

import javafx.scene.image.ImageView;

public class ZoomingCommand implements Command {
    private final ImageView imageView;
    private double initialZoom; // Initial zoom level
    private double finalZoom;   // Final zoom level

    public ZoomingCommand(ImageView imageView, double initialZoom, double finalZoom) {
        this.imageView = imageView;
        this.initialZoom = initialZoom;
        this.finalZoom = finalZoom;
    }

    @Override
    public void execute() {
        initialZoom = imageView.getScaleX();
        imageView.setScaleX(finalZoom);
        imageView.setScaleY(finalZoom);
    }

    @Override
    public void undo() {
        finalZoom = imageView.getScaleX();
        imageView.setScaleX(initialZoom);
        imageView.setScaleY(initialZoom);
    }
}
