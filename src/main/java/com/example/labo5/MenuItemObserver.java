package com.example.labo5;

import javafx.scene.control.MenuItem;

public class MenuItemObserver implements Observer {
    private final MenuItem menuItem;

    public MenuItemObserver(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    @Override
    public void update(ImageModel subject) {
        menuItem.setDisable(!subject.isImageLoaded());
    }
}

