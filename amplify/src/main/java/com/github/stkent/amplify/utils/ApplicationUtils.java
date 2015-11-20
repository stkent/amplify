package com.github.stkent.amplify.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

public class ApplicationUtils {

    public static PackageInfo getPackageInfo(@NonNull final Context applicationContext, final int flags) throws PackageManager.NameNotFoundException {
        final PackageManager packageManager = applicationContext.getPackageManager();
        final String applicationPackageName = applicationContext.getPackageName();
        return packageManager.getPackageInfo(applicationPackageName, flags);
    }

}
