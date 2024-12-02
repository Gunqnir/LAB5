package com.example.labo5;

import java.util.Stack;

public class CommandManager {
    private final Stack<Command> undoStack = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();

    public void executeCommand(Command command) {
        command.execute();
        undoStack.push(command); // Add to undo stack
        redoStack.clear();       // Clear redo stack
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command); // Add to redo stack
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            undoStack.push(command); // Add back to undo stack
        }
    }

    // Check if the undo stack is empty
    public boolean isUndoStackEmpty() {
        return undoStack.isEmpty();
    }

    // Check if the redo stack is empty
    public boolean isRedoStackEmpty() {
        return redoStack.isEmpty();
    }
}
