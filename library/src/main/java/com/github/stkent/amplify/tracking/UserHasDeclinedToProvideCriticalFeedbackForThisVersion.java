package com.github.stkent.amplify.tracking;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.base.IFlag;

public class UserHasDeclinedToProvideCriticalFeedbackForThisVersion implements IFlag<String> {

    private static final String INITIAL_VALUE = "InitialValue";
    private static final String FAILURE_VALUE = "FailureValue";

    private String getVersionName(@NonNull final Context applicationContext) throws PackageManager.NameNotFoundException {
        final PackageManager packageManager = applicationContext.getPackageManager();
        final String applicationPackageName = applicationContext.getPackageName();

        return packageManager.getPackageInfo(applicationPackageName, 0).versionName;
    }

    @NonNull
    @Override
    public String getUniqueIdentifier() {
        return getClass().getSimpleName();
    }

    @NonNull
    @Override
    public String getInitialTrackedValue() {
        return INITIAL_VALUE;
    }

    @NonNull
    @Override
    public String incrementTrackedValue(@NonNull final String currentValue, @NonNull final Context applicationContext) {
        if (currentValue.equals(FAILURE_VALUE)) {
            return FAILURE_VALUE;
        }

        try {
            return getVersionName(applicationContext);
        } catch (final PackageManager.NameNotFoundException e) {
            return FAILURE_VALUE;
        }
    }

    @Override
    public boolean isActive(@NonNull final String currentValue, @NonNull final Context applicationContext) {
        if (FAILURE_VALUE.equals(currentValue)) {
            return true;
        }

        try {
            return currentValue.equals(getVersionName(applicationContext));
        } catch (final PackageManager.NameNotFoundException e) {
            return true;
        }
    }

}
