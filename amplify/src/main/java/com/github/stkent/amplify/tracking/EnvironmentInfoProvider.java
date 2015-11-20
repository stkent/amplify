package com.github.stkent.amplify.tracking;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.interfaces.IEnvironmentInfoProvider;
import com.github.stkent.amplify.utils.ApplicationUtils;

import java.util.List;

import static android.content.pm.PackageManager.GET_ACTIVITIES;

public final class EnvironmentInfoProvider implements IEnvironmentInfoProvider {

    /**
     * Package name for the Google Play Store. Value can be verified here:
     * https://developers.google.com/android/reference/com/google/android/gms/common/GooglePlayServicesUtil.html#GOOGLE_PLAY_STORE_PACKAGE
     */
    private static final String GOOGLE_PLAY_STORE_PACKAGE_NAME = "com.android.vending";

    @NonNull
    private final Context applicationContext;

    public EnvironmentInfoProvider(@NonNull final Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public boolean isApplicationInstalled(@NonNull final String packageName) {
        try {
            ApplicationUtils.getPackageInfo(applicationContext, GET_ACTIVITIES);
            return true;
        } catch (final PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean isGooglePlayStoreInstalled() {
        // Note that we do not need to worry about differentiating between
        // Android Market and the Google Play Store because the Android Market
        // is only available on phones running 3.0-3.2.
        return isApplicationInstalled(GOOGLE_PLAY_STORE_PACKAGE_NAME);
    }

    @Override
    public boolean canHandleIntent(@NonNull final Intent intent) {
        final List<ResolveInfo> resolveInfoList = applicationContext.getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        return !resolveInfoList.isEmpty();
    }

}
