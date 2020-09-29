package com.rubrain.libertex;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class AfterTestExtension implements AfterTestExecutionCallback {
    protected static final Logger LOGGER = Logger.getInstance();

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
        Class testClass = extensionContext.getRequiredTestClass();
        String testMethod = extensionContext.getRequiredTestMethod().getName();

        Boolean testFailed = extensionContext.getExecutionException().isPresent();
        if (testFailed) {
            LOGGER.warn(String.format("--- FAILED Test: '%s#%s' ---", testClass, testMethod));
        } else {
            LOGGER.warn(String.format("--- PASSED Test: '%s#%s' ---", testClass, testMethod));
        }
    }
}
