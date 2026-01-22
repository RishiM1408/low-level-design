package com.lld.logframework;

public class FileLogger extends AbstractLogger {

    public FileLogger(int level) {
        this.level = level;
    }

    @Override
    protected void write(String message) {
        System.out.println("FILE::Logger: " + message);
        // In real impl, write to file using Files.write or streams.
    }
}
