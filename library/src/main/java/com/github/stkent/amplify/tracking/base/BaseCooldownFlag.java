package com.github.stkent.amplify.tracking.base;

import android.content.Context;
import android.support.annotation.NonNull;

public abstract class BaseCooldownFlag implements IFlag<Long> {

    abstract long cooldownTimeMillis();

    private Long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    @NonNull
    @Override
    public Long getInitialTrackedValue() {
        return getCurrentTimeMillis();
    }

    @NonNull
    @Override
    public Long incrementTrackedValue(@NonNull final Long currentValue, @NonNull final Context applicationContext) {
        return getCurrentTimeMillis();
    }

    @Override
    public boolean isActive(@NonNull final Long currentValue, @NonNull final Context applicationContext) {
        return getCurrentTimeMillis() - currentValue > cooldownTimeMillis();
    }

}
