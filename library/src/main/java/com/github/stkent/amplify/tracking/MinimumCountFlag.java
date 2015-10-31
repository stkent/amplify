package com.github.stkent.amplify.tracking;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.interfaces.IEventCheck;

public final class MinimumCountFlag implements IEventCheck<Integer> {

    private final int minimumCount;

    private MinimumCountFlag(final int minimumCount) {
        this.minimumCount = minimumCount;
    }

    @Override
    public boolean shouldBlockFeedbackPrompt(@NonNull final Integer cachedEventValue, @NonNull final Context applicationContext) {
        return cachedEventValue < minimumCount;
    }

    @NonNull
    @Override
    public String getStatusString(@NonNull final Integer cachedEventValue, @NonNull final Context applicationContext) {
        return "Minimum required event count: " + minimumCount + ". Current event count: " + cachedEventValue + ".";
    }

}
