package com.lld.logframework;

public class FileLogger extends AbstractLogger {

    public FileLogger(int level) {
        this.level = level;
    }

    @Override
    protected void write(String message) {
        try {
            // SDE-3: Resilience & Fault Tolerance
            // Simulate random disk IO failure
            if (Math.random() < 0.2) {
                throw new java.io.IOException("Disk Full or Unreachable");
            }
            System.out.println("FILE::Logger: " + message);
        } catch (java.io.IOException e) {
            // Graceful Degradation: Fallback to System.err
            System.err.println("ALERT: File Logging Failed (" + e.getMessage() + ") - Fallback to Console: " + message);
        }
    }
}
