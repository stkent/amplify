package com.github.stkent.amplify.tracking.interfaces;

import android.content.Context;
import android.support.annotation.NonNull;

public interface IEvent {

    @NonNull
    String getTrackingKey();

    void performRelatedInitialization(@NonNull final Context applicationContext);

}
