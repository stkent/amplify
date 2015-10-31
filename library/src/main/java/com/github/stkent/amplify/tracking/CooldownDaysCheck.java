package com.github.stkent.amplify.tracking;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.interfaces.IEventCheck;

import java.util.concurrent.TimeUnit;

public final class CooldownDaysCheck implements IEventCheck<Long> {

    private final long cooldownPeriodDays;

    public CooldownDaysCheck(final long cooldownPeriodDays) {
        this.cooldownPeriodDays = cooldownPeriodDays;
    }

    @Override
    public boolean shouldBlockFeedbackPrompt(@NonNull final Long cachedEventValue, @NonNull final Context applicationContext) {
        return (System.currentTimeMillis() - cachedEventValue) < TimeUnit.DAYS.toMillis(cooldownPeriodDays);
    }

    @NonNull
    @Override
    public String getStatusString(@NonNull final Long cachedEventValue, @NonNull final Context applicationContext) {
        final Long daysSinceLastEvent = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - cachedEventValue);
        return "Cooldown period: " + cooldownPeriodDays + " days. Time since last event: " + daysSinceLastEvent + " days.";
    }

}
