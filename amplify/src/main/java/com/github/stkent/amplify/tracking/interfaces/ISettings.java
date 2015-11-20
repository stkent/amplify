package com.github.stkent.amplify.tracking.interfaces;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface ISettings<T> {

    void writeEventValue(@NonNull final ITrackedEvent event, final T value);

    @Nullable
    T getEventValue(@NonNull final ITrackedEvent trackedEvent);

    boolean hasEventValue(@NonNull final ITrackedEvent trackedEvent);

}
