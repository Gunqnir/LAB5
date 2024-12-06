package com.example.labo5;

import javafx.scene.image.ImageView;

public class ThumbnailView extends ImageView implements Observer {
    public ThumbnailView() {
        super();
        setPreserveRatio(true);
    }

    @Override
    public void update(ImageModel subject) {
        if (subject.getImage() != null) {
            setImage(subject.getImage());
        }
    }
}
