package com.example.labo5;

import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;

public class Perspective extends Tab implements Observer {
    private final ImageView imageView; // ImageView to display the image

    public Perspective(String title, ImageModel imageModel) {
        super(title); // Set the tab title
        this.imageView = new ImageView();
        this.imageView.setPreserveRatio(true); // Maintain aspect ratio
        this.imageView.setFitWidth(800); // Adjust width as needed
        this.imageView.setFitHeight(600); // Adjust height as needed

        // Set the current image from ImageModel
        if (imageModel.getImage() != null) {
            this.imageView.setImage(imageModel.getImage());
        }

        // Set the ImageView as the content of the tab
        this.setContent(imageView);
    }

    @Override
    public void update(ImageModel subject) {
        // Update the ImageView whenever the ImageModel changes
        this.imageView.setImage(subject.getImage());
    }
}
