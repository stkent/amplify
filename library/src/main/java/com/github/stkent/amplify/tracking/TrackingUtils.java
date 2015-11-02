package com.github.stkent.amplify.tracking;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

public final class TrackingUtils {

    private TrackingUtils() {

    }

    public static String getAppVersionName(@NonNull final Context applicationContext) throws PackageManager.NameNotFoundException {
        final PackageManager packageManager = applicationContext.getPackageManager();
        final String applicationPackageName = applicationContext.getPackageName();

        return packageManager.getPackageInfo(applicationPackageName, 0).versionName;
    }

}
