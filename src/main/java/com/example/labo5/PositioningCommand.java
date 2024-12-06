package com.example.labo5;

import javafx.scene.image.ImageView;

public class PositioningCommand implements Command {
    private final ImageView imageView;
    private final double initialX, initialY; // Initial position
    private final double finalX, finalY;     // Final position

    public PositioningCommand(ImageView imageView, double initialX, double initialY, double finalX, double finalY) {
        this.imageView = imageView;
        this.initialX = initialX;
        this.initialY = initialY;
        this.finalX = finalX;
        this.finalY = finalY;
    }

    @Override
    public void execute() {
        imageView.setTranslateX(finalX);
        imageView.setTranslateY(finalY);
    }

    @Override
    public void undo() {
        imageView.setTranslateX(initialX);
        imageView.setTranslateY(initialY);
    }
}
