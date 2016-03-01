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
package com.github.stkent.amplify.tracking;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.interfaces.IEnvironmentCapabilitiesProvider;
import com.github.stkent.amplify.utils.AppUtils;

import java.util.List;

import static android.content.pm.PackageManager.GET_ACTIVITIES;

public final class EnvironmentCapabilitiesProvider implements IEnvironmentCapabilitiesProvider {

    /**
     * Package name for the Google Play Store. Value can be verified here:
     * https://developers.google.com/android/reference/com/google/android/gms/common/GooglePlayServicesUtil.html#GOOGLE_PLAY_STORE_PACKAGE
     */
    private static final String GOOGLE_PLAY_STORE_PACKAGE_NAME = "com.android.vending";

    @NonNull
    private final Context appContext;

    public EnvironmentCapabilitiesProvider(@NonNull final Context appContext) {
        this.appContext = appContext;
    }

    @Override
    public boolean isAppInstalled(@NonNull final String packageName) {
        try {
            AppUtils.getPackageInfo(appContext, packageName, GET_ACTIVITIES);
            return true;
        } catch (final PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean isGooglePlayStoreInstalled() {
        // Note that we do not need to worry about differentiating between Android Market and the
        // Google Play Store because the Android Market is only available on phones running 3.0-3.2.
        return isAppInstalled(GOOGLE_PLAY_STORE_PACKAGE_NAME);
    }

    @Override
    public boolean canHandleIntent(@NonNull final Intent intent) {
        final List<ResolveInfo> resolveInfoList = appContext.getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        return !resolveInfoList.isEmpty();
    }

}
