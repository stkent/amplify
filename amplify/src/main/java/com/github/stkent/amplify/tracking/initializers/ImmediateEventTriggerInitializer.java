package com.github.stkent.amplify.tracking.initializers;

import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.AmplifyStateTracker;
import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.ITrackingInitializer;

public class ImmediateEventTriggerInitializer implements ITrackingInitializer {

    @Override
    public void initialize(@NonNull final AmplifyStateTracker amplifyStateTracker, @NonNull final IEvent event) {
        amplifyStateTracker.notifyEventTriggered(event);
    }
}
