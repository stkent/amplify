package com.github.stkent.amplify.helpers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.stkent.amplify.tracking.interfaces.ISettings;
import com.github.stkent.amplify.tracking.interfaces.ITrackedEvent;

public class StubbedSettings<T> implements ISettings<T> {

    @Nullable
    private final T fakeCachedEventValue;

    public StubbedSettings(@Nullable final T fakeCachedEventValue) {
        this.fakeCachedEventValue = fakeCachedEventValue;
    }

    @Override
    public void writeEventValue(@NonNull final ITrackedEvent event, final T value) {
        // no-op
    }

    @Nullable
    @Override
    public T getEventValue(@NonNull final ITrackedEvent event) {
        return fakeCachedEventValue;
    }

    @Override
    public boolean hasEventValue(@NonNull final ITrackedEvent event) {
        return fakeCachedEventValue != null;
    }

}
