package com.github.stkent.amplify.tracking;

import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.base.BaseThresholdFlag;

public class UserHasAlreadyGivenPositiveFeedback extends BaseThresholdFlag {

    @NonNull
    @Override
    public String getUniqueIdentifier() {
        return getClass().getSimpleName();
    }

    @Override
    int getThresholdValue() {
        return 1;
    }

}
