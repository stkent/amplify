package com.github.stkent.amplify.tracking;

import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.interfaces.IEvent;

public enum ApplicationEvent implements IEvent {

    APP_INSTALLED,
    APP_UPDATED,
    APP_CRASHED;

    @NonNull
    @Override
    public String getTrackingKey() {
        return name();
    }

}
