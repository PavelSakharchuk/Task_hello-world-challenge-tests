package com.rubrain.libertex;

import org.apache.logging.log4j.LogManager;

import java.util.Optional;


public class Logger {
    private static final ThreadLocal<Logger> instancePull = ThreadLocal.withInitial(() -> null);
    private final org.apache.logging.log4j.Logger LOG4J = LogManager.getLogger(Logger.class);

    private Logger() {
    }

    public static synchronized Logger getInstance() {
        return Optional
                .ofNullable(instancePull.get())
                .orElseGet(() -> {
                    instancePull.set(new Logger());
                    return instancePull.get();
                });
    }

    public void logTest(Class testClass, String testmethod) {
        LOG4J.info(String.format("--- Run Test: %s#%s ---", testClass, testmethod));
    }

    public void info(Object message) {
        LOG4J.info(message);
    }

    public void warn(Object message) {
        LOG4J.warn(message);
    }
}
