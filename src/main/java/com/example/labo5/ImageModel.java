package com.example.labo5;

import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.List;

public class ImageModel {
    private List<Observer> observers = new ArrayList<>();
    private Image image;

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void detach(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }

    public void setImage(Image image) {
        this.image = image;
        notifyObservers();
    }

    public Image getImage() {
        return image;
    }

    // New method to check if an image is loaded
    public boolean isImageLoaded() {
        return image != null;
    }
}
