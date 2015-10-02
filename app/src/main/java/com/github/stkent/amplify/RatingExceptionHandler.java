package com.github.stkent.amplify;

import static com.github.stkent.amplify.RatingStateTracker.ActionType.APP_CRASHED;
import static java.lang.Thread.UncaughtExceptionHandler;

public class RatingExceptionHandler implements UncaughtExceptionHandler {

    private UncaughtExceptionHandler defaultExceptionHandler;

    public RatingExceptionHandler(final UncaughtExceptionHandler uncaughtExceptionHandler) {
        defaultExceptionHandler = uncaughtExceptionHandler;
    }

    @Override
    public void uncaughtException(final Thread thread, final Throwable throwable) {
        RatingStateTracker.getInstance().notify(APP_CRASHED);

        // Call the original handler.
        defaultExceptionHandler.uncaughtException(thread, throwable);
    }

}
