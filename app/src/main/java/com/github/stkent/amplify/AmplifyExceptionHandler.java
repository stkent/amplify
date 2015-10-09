package com.github.stkent.amplify;

import static com.github.stkent.amplify.AmplifyStateTracker.ActionType.APP_CRASHED;
import static java.lang.Thread.UncaughtExceptionHandler;

public class AmplifyExceptionHandler implements UncaughtExceptionHandler {

    private UncaughtExceptionHandler defaultExceptionHandler;

    public AmplifyExceptionHandler(final UncaughtExceptionHandler uncaughtExceptionHandler) {
        defaultExceptionHandler = uncaughtExceptionHandler;
    }

    @Override
    public void uncaughtException(final Thread thread, final Throwable throwable) {
        AmplifyStateTracker.getInstance().notify(APP_CRASHED);

        // Call the original handler.
        defaultExceptionHandler.uncaughtException(thread, throwable);
    }

}
