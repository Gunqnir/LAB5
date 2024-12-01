package com.example.labo5;

public interface Command {
    void execute(); // Perform the action
    void undo();    // Revert the action
}
