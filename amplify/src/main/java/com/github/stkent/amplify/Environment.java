/**
 * Copyright 2015 Stuart Kent
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.github.stkent.amplify;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import static android.content.pm.PackageManager.GET_ACTIVITIES;
import static android.content.pm.PackageManager.MATCH_DEFAULT_ONLY;

public final class Environment implements IEnvironment {

    /**
     * Package name for the Google Play Store. Value can be verified here:
     * https://developers.google.com/android/reference/com/google/android/gms/common/GooglePlayServicesUtil.html#GOOGLE_PLAY_STORE_PACKAGE
     */
    private static final String GOOGLE_PLAY_STORE_PACKAGE_NAME = "com.android.vending";

    /**
     * Package name for the Amazon App Store.
     */
    private static final String AMAZON_APP_STORE_PACKAGE_NAME = "com.amazon.venezia";

    @NonNull
    private final Context appContext;

    public Environment(@NonNull final Context context) {
        this.appContext = context.getApplicationContext();
    }

    @NonNull
    @Override
    public String getAndroidVersionName() {
        return Build.VERSION.RELEASE;
    }

    @Override
    public int getAndroidVersionCode() {
        return Build.VERSION.SDK_INT;
    }

    @Override
    public boolean isAppInstalled(@NonNull final String packageName) {
        return getPackageInfo(packageName, GET_ACTIVITIES) != null;
    }

    @Override
    public boolean isAmazonAppStoreInstalled() {
        return isAppInstalled(AMAZON_APP_STORE_PACKAGE_NAME);
    }

    @Override
    public boolean isGooglePlayStoreInstalled() {
        // Note that we do not need to worry about differentiating between Android Market and the
        // Google Play Store because the Android Market is only available on phones running 3.0-3.2.
        return isAppInstalled(GOOGLE_PLAY_STORE_PACKAGE_NAME);
    }

    @Override
    public boolean canHandleIntent(@NonNull final Intent intent) {
        final List<ResolveInfo> resolveInfoList = appContext
                .getPackageManager()
                .queryIntentActivities(intent, MATCH_DEFAULT_ONLY);

        return !resolveInfoList.isEmpty();
    }

    @Nullable
    private PackageInfo getPackageInfo(@NonNull final String packageName, final int flags) {
        final PackageManager packageManager = appContext.getPackageManager();

        try {
            return packageManager.getPackageInfo(packageName, flags);
        } catch (final PackageManager.NameNotFoundException ignored) {
            return null;
        }
    }

}
