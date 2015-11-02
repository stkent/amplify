package com.github.stkent.amplify.tracking;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.interfaces.IEventCheck;

public final class MaximumCountCheck implements IEventCheck<Integer> {

    private final int maximumCount;

    public MaximumCountCheck(final int maximumCount) {
        this.maximumCount = maximumCount;
    }

    @Override
    public boolean shouldBlockFeedbackPrompt(@NonNull final Integer cachedEventValue, @NonNull final Context applicationContext) {
        return cachedEventValue >= maximumCount;
    }

    @NonNull
    @Override
    public String getStatusString(@NonNull final Integer cachedEventValue, @NonNull final Context applicationContext) {
        return "Maximum allowed event count: " + maximumCount + ". Current event count: " + cachedEventValue + ".";
    }

}
