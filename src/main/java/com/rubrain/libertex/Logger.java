package com.rubrain.libertex;

import org.apache.log4j.LogManager;

import java.util.Optional;


public class Logger {
    private static final ThreadLocal<Logger> instancePull = ThreadLocal.withInitial(() -> null);
    private org.apache.log4j.Logger LOG4J = LogManager.getLogger(Logger.class);

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

    public void logTest() {
        LOG4J.info(String.format("--- Run Test: %s#%s ---",
                Thread.currentThread().getStackTrace()[2].getClassName(),
                Thread.currentThread().getStackTrace()[2].getMethodName()));
    }

    public void info(Object message) {
        LOG4J.info(message);
    }
}
