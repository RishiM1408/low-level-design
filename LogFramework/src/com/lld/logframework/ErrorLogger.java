package com.lld.logframework;

public class ErrorLogger extends AbstractLogger {

    public ErrorLogger(int level) {
        this.level = level;
    }

    @Override
    protected void write(String message) {
        System.err.println("ERROR CONSOLE::Logger: " + message);
    }
}
