package com.github.stkent.amplify.tracking;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.interfaces.IEventCheck;

import java.util.concurrent.TimeUnit;

public class WarmupDaysCheck implements IEventCheck<Long> {

    private final long warmupPeriodDays;

    public WarmupDaysCheck(final long warmupPeriodDays) {
        this.warmupPeriodDays = warmupPeriodDays;
    }

    @Override
    public boolean shouldBlockFeedbackPrompt(@NonNull final Long cachedEventValue, @NonNull final Context applicationContext) {
        return (System.currentTimeMillis() - cachedEventValue) >= TimeUnit.DAYS.toMillis(warmupPeriodDays);
    }

    @NonNull
    @Override
    public String getStatusString(@NonNull final Long cachedEventValue, @NonNull final Context applicationContext) {
        final Long daysSinceLastEvent = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - cachedEventValue);
        return "Warmup period: " + warmupPeriodDays + " days. Time since last event: " + daysSinceLastEvent + " days.";
    }

}
