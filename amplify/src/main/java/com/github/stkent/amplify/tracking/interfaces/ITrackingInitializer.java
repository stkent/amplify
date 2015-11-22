package com.github.stkent.amplify.tracking.interfaces;

import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.AmplifyStateTracker;

public interface ITrackingInitializer {

    void initialize(@NonNull final AmplifyStateTracker amplifyStateTracker, @NonNull final IEvent event);

}
