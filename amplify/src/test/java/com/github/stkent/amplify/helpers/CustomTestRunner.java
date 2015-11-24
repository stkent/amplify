package com.github.stkent.amplify.helpers;

import com.github.stkent.amplify.AmplifyLogger;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class CustomTestRunner extends BlockJUnit4ClassRunner {

    private static boolean initialized = false;

    public CustomTestRunner(Class<?> klass) throws InitializationError {
        super(klass);

        synchronized (CustomTestRunner.class) {
            if (!initialized) {
                globalOneTimeSetUp();
            }
        }
    }

    private static void globalOneTimeSetUp() {
        AmplifyLogger.setLogger(new StubLogger());
        initialized = true;
    }

}
