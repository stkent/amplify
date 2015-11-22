package com.github.stkent.amplify.tracking.initializers;

import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.AmplifyExceptionHandler;
import com.github.stkent.amplify.tracking.AmplifyStateTracker;
import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.ITrackingInitializer;

public class ExceptionHandlingInitializer implements ITrackingInitializer {

    @Override
    public void initialize(@NonNull final AmplifyStateTracker amplifyStateTracker, @NonNull final IEvent event) {
        final Thread.UncaughtExceptionHandler defaultExceptionHandler
                = Thread.getDefaultUncaughtExceptionHandler();

        if (defaultExceptionHandler instanceof AmplifyExceptionHandler) {
            return;
        }

        Thread.setDefaultUncaughtExceptionHandler(
                new AmplifyExceptionHandler(amplifyStateTracker, defaultExceptionHandler));
    }

}
