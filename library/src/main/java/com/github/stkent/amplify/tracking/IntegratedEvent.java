package com.github.stkent.amplify.tracking;

import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.interfaces.IEvent;

public enum IntegratedEvent implements IEvent {

    APP_CRASHED,
    APP_INSTALLED,
    APP_UPDATED,
    USER_GAVE_POSITIVE_FEEDBACK,
    USER_GAVE_NEGATIVE_FEEDBACK;

    @NonNull
    @Override
    public String getTrackingKey() {
        return name();
    }

}
