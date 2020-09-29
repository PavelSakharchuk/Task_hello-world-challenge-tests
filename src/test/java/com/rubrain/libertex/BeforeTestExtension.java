package com.rubrain.libertex;

import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class BeforeTestExtension implements BeforeTestExecutionCallback {
    protected static final Logger LOGGER = Logger.getInstance();

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) throws Exception {
        LOGGER.logTest(extensionContext.getRequiredTestClass(), extensionContext.getRequiredTestMethod().getName());
    }
}
