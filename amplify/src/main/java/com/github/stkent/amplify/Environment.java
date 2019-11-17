/*
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
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.support.annotation.NonNull;

import java.util.List;

import static android.content.pm.PackageManager.GET_ACTIVITIES;
import static android.content.pm.PackageManager.MATCH_DEFAULT_ONLY;
import static com.github.stkent.amplify.utils.Constants.AMAZON_APP_STORE_PACKAGE_NAME;
import static com.github.stkent.amplify.utils.Constants.GOOGLE_PLAY_STORE_PACKAGE_NAME;

public final class Environment implements IEnvironment {

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

    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    @Override
    public boolean isAppInstalled(@NonNull final String packageName) {
        final PackageManager packageManager = appContext.getPackageManager();

        try {
            return packageManager.getPackageInfo(packageName, GET_ACTIVITIES) != null;
        } catch (final Exception ignored) {
            return false;
        }
    }

    @Override
    public boolean isAmazonAppStoreInstalled() {
        return isAppInstalled(AMAZON_APP_STORE_PACKAGE_NAME);
    }

    @Override
    public boolean isGooglePlayStoreInstalled() {
        /*
         * Note that we do not need to worry about differentiating between the Android Market and the Google Play Store
         * because the Android Market is only available on phones running 3.0-3.2.
         */
        return isAppInstalled(GOOGLE_PLAY_STORE_PACKAGE_NAME);
    }

    @Override
    public boolean canHandleIntent(@NonNull final Intent intent) {
        final List<ResolveInfo> resolveInfoList = appContext
                .getPackageManager()
                .queryIntentActivities(intent, MATCH_DEFAULT_ONLY);

        return !resolveInfoList.isEmpty();
    }

}
