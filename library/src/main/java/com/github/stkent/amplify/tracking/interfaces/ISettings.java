package com.github.stkent.amplify.tracking.interfaces;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface ISettings {

    int getTotalEventCount(@NonNull final IEvent event);
    void setTotalEventCount(@NonNull final IEvent event, final int totalEventCount);

    long getLastEventTime(@NonNull final IEvent event);
    void setLastEventTime(@NonNull final IEvent event, final long lastEventTime);

    @Nullable
    String getLastEventVersion(@NonNull final IEvent event);
    void setLastEventVersion(@NonNull final IEvent event, @NonNull final String lastEventVersion);

    void reset();

}
