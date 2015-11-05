package com.github.stkent.amplify.tracking.interfaces;

import android.content.Context;
import android.support.annotation.NonNull;

public interface IEventCheck<T> {

    boolean shouldBlockFeedbackPrompt(@NonNull final T cachedEventValue, @NonNull final Context applicationContext);

    @NonNull
    String getStatusString(@NonNull final T cachedEventValue, @NonNull final Context applicationContext);

}
