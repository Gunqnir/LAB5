package com.example.labo5;

import java.util.Stack;

public class CommandManager {
    private static CommandManager instance; // Singleton instance
    private final Stack<Command> undoStack; // Stack for undo commands
    private final Stack<Command> redoStack; // Stack for redo commands

    private CommandManager() {
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    public static CommandManager getInstance() {
        if (instance == null) {
            instance = new CommandManager();
        }
        return instance;
    }

    public void executeCommand(Command command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear(); // Clear redo stack on new command
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command);
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            undoStack.push(command);
        }
    }

    public boolean isUndoStackEmpty() {
        return undoStack.isEmpty();
    }

    public boolean isRedoStackEmpty() {
        return redoStack.isEmpty();
    }
}
