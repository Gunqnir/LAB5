package com.example.labo5;

import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.List;

public class ImageModel {
    private List<Observer> observers = new ArrayList<>();
    private Image image;
    private boolean hasImageLoaded = false; // Track if an image is loaded

    public void attach(Observer o) {
        observers.add(o);
    }

    public void detach(Observer o) {
        observers.remove(o);
    }

    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(this);
        }
    }

    public void setImage(Image image) {
        this.image = image;
        this.hasImageLoaded = (image != null);
        notifyObservers();
    }

    public Image getImage() {
        return image;
    }

    public boolean isImageLoaded() {
        return hasImageLoaded;
    }
}
