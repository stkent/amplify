package com.github.stkent.amplify.tracking;

import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.base.BaseCooldownFlag;

import java.util.concurrent.TimeUnit;

public class AppHasCrashedRecently extends BaseCooldownFlag {

    @NonNull
    @Override
    public String getUniqueIdentifier() {
        return getClass().getSimpleName();
    }

    @Override
    public long cooldownTimeMillis() {
        return TimeUnit.DAYS.toMillis(7);
    }

}
