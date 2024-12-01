package com.example.labo5;

import javafx.scene.image.ImageView;

public class PositioningCommand implements Command {
    private final ImageView imageView;
    private final double deltaX, deltaY;
    private final double previousX, previousY;

    public PositioningCommand(ImageView imageView, double deltaX, double deltaY) {
        this.imageView = imageView;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.previousX = imageView.getTranslateX();
        this.previousY = imageView.getTranslateY();
    }

    @Override
    public void execute() {
        imageView.setTranslateX(previousX + deltaX);
        imageView.setTranslateY(previousY + deltaY);
    }

    @Override
    public void undo() {
        imageView.setTranslateX(previousX);
        imageView.setTranslateY(previousY);
    }
}
