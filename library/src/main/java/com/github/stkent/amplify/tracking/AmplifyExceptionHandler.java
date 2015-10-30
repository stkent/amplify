package com.github.stkent.amplify.tracking;

import static java.lang.Thread.UncaughtExceptionHandler;

public class AmplifyExceptionHandler implements UncaughtExceptionHandler {

    private final UncaughtExceptionHandler defaultExceptionHandler;

    public AmplifyExceptionHandler(final UncaughtExceptionHandler uncaughtExceptionHandler) {
        defaultExceptionHandler = uncaughtExceptionHandler;
    }

    @Override
    public void uncaughtException(final Thread thread, final Throwable throwable) {
        // TODO: notify of crash here

        // Call the original handler.
        defaultExceptionHandler.uncaughtException(thread, throwable);
    }

}
