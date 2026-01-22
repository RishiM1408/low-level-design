package com.lld.logframework;

public class LogManagerMain {

    private static AbstractLogger getChainOfLoggers() {
        // Create loggers
        // 1. ErrorLogger (Level 3) -> Writes only Errors
        AbstractLogger errorLogger = new ErrorLogger(AbstractLogger.ERROR);

        // 2. FileLogger (Level 2) -> Writes Debug and Errors
        AbstractLogger fileLogger = new FileLogger(AbstractLogger.DEBUG);

        // 3. ConsoleLogger (Level 1) -> Writes Info, Debug, Errors
        AbstractLogger consoleLogger = new ConsoleLogger(AbstractLogger.INFO);

        // Build the chain: E -> F -> C ?
        // If we want ALL handlers to check, we just chain them.
        // It doesn't strictly matter who is first if they all pass-through.
        // BUT, usually we want explicit routing.

        // Let's chain them: errorLogger -> fileLogger -> consoleLogger
        errorLogger.setNextLogger(fileLogger);
        fileLogger.setNextLogger(consoleLogger);

        return errorLogger;
    }

    public static void main(String[] args) {
        AbstractLogger loggerChain = getChainOfLoggers();

        System.out.println("--- 1. Logging INFO ---");
        // INFO is level 1.
        // ErrorLogger (3) : 3 <= 1? False. No write. keys next.
        // FileLogger (2) : 2 <= 1? False. No write. keys next.
        // ConsoleLogger (1) : 1 <= 1? True. Writes.
        loggerChain.logMessage(AbstractLogger.INFO, "This is an information.");

        System.out.println("\n--- 2. Logging DEBUG ---");
        // DEBUG is level 2.
        // ErrorLogger (3) : False.
        // FileLogger (2) : True. Writes (File).
        // ConsoleLogger (1) : True. Writes (Console).
        loggerChain.logMessage(AbstractLogger.DEBUG, "This is a debug level information.");

        System.out.println("\n--- 3. Logging ERROR ---");
        // ERROR is level 3.
        // ErrorLogger (3) : True. Writes (Error Console).
        // FileLogger (2) : True. Writes (File).
        // ConsoleLogger (1) : True. Writes (Standard Console).
        loggerChain.logMessage(AbstractLogger.ERROR, "This is an error information.");
    }
}
