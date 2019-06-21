package org.algorithm_visualizer;

public class Response {
    boolean success;
    String errorMessage;
    String commands;

    public Response(String commands) {
        this.success = true;
        this.commands = commands;
    }

    public Response(Exception exception) {
        this.success = false;
        this.errorMessage = exception.getMessage();
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getCommands() {
        return commands;
    }
}
