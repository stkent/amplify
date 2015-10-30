package com.github.stkent.amplify.tracking.base;

import android.content.Context;
import android.support.annotation.NonNull;

public abstract class BaseCyclicalFlag implements IFlag<Integer> {

    abstract int getCycleLength();

    @NonNull
    @Override
    public Integer getInitialTrackedValue() {
        return 0;
    }

    @NonNull
    @Override
    public Integer incrementTrackedValue(@NonNull final Integer currentValue, @NonNull final Context applicationContext) {
        return currentValue + 1;
    }

    @Override
    public boolean isActive(@NonNull final Integer currentValue, @NonNull final Context applicationContext) {
        return (currentValue % getCycleLength()) == -1;
    }

}
