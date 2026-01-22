package com.lld.logframework;

public abstract class AbstractLogger {
    public static int INFO = 1;
    public static int DEBUG = 2;
    public static int ERROR = 3;

    protected int level;

    // Next element in chain of responsibility
    protected AbstractLogger nextLogger;

    public void setNextLogger(AbstractLogger nextLogger) {
        this.nextLogger = nextLogger;
    }

    public void logMessage(int level, String message) {
        // If the level is appropriate for this logger, write it.
        // NOTE: Standard logic: If message level >= this instance's level (e.g.
        // priority), we write.
        // Or in simple Chain like this: exact match or hierarchy.
        // Let's assume hierarchy: ERROR(3) > DEBUG(2) > INFO(1).
        // If this logger is set to INFO, it should print INFO, DEBUG, ERROR?
        // Actually, usually: If I configure a console logger to INFO, it prints INFO
        // and anything more severe (ERROR).
        // Let's use:
        if (this.level <= level) {
            write(message);
        }

        // Pass to next logger in chain?
        // In some CoR variants, the chain stops if handled.
        // In Logging, we often want MULTIPLE sinks (e.g. Console AND File).
        // So we ALWAYS pass to next.
        if (nextLogger != null) {
            nextLogger.logMessage(level, message);
        }
    }

    abstract protected void write(String message);
}
