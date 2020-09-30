package com.rubrain.libertex.test;

import com.rubrain.libertex.AfterTestExtension;
import com.rubrain.libertex.BeforeTestExtension;
import com.rubrain.libertex.Logger;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({
        BeforeTestExtension.class,
        AfterTestExtension.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseTest {
    protected static final Logger LOGGER = Logger.getInstance();
}
