package com.example.labo5;

import javafx.scene.control.MenuItem;

public class MenuItemObserver implements Observer {
    private final MenuItem menuItem;
    private final boolean enabledWhenImageLoaded;

    public MenuItemObserver(MenuItem menuItem, boolean enabledWhenImageLoaded) {
        this.menuItem = menuItem;
        this.enabledWhenImageLoaded = enabledWhenImageLoaded;
    }

    @Override
    public void update(ImageModel subject) {
        if (subject.isImageLoaded()) {
            menuItem.setDisable(!enabledWhenImageLoaded); // Enable if true
        } else {
            menuItem.setDisable(enabledWhenImageLoaded); // Disable otherwise
        }
    }
}
