package com.github.stkent.amplify.tracking;

import android.content.Context;
import android.support.annotation.NonNull;

import static java.lang.Thread.UncaughtExceptionHandler;

public class AmplifyExceptionHandler implements UncaughtExceptionHandler {

    @NonNull
    private final Context applicationContext;
    private final UncaughtExceptionHandler defaultExceptionHandler;

    public AmplifyExceptionHandler(
            @NonNull final Context applicationContext,
            final UncaughtExceptionHandler defaultExceptionHandler) {
        this.applicationContext = applicationContext.getApplicationContext();
        this.defaultExceptionHandler = defaultExceptionHandler;
    }

    @Override
    public void uncaughtException(final Thread thread, final Throwable throwable) {
        AmplifyStateTracker
                .get(applicationContext)
                .notifyEventTriggered(IntegratedEvent.APP_CRASHED);

        // Call the original handler.
        defaultExceptionHandler.uncaughtException(thread, throwable);
    }

}
