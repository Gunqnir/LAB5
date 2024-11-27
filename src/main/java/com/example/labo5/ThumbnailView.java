package com.example.labo5;

import javafx.scene.image.ImageView;

public class ThumbnailView extends ImageView implements Observer {
    public ThumbnailView() {
        super();
        setFitWidth(200); // Adjust size for thumbnail
        setPreserveRatio(true);
    }

    @Override
    public void update(ImageModel subject) {
        if (subject.getImage() != null) {
            setImage(subject.getImage()); // Update the image
        }
    }
}
