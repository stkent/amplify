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
package com.github.stkent.amplify.feedback;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.stkent.amplify.IApp;
import com.github.stkent.amplify.IDevice;
import com.github.stkent.amplify.IEnvironment;
import com.github.stkent.amplify.utils.StringUtils;

import static android.content.Intent.ACTION_VIEW;

public final class GooglePlayStoreFeedbackCollector implements IFeedbackCollector {

    private static final String ANDROID_MARKET_URI_PREFIX = "market://details?id=";

    private static final String GOOGLE_PLAY_STORE_URI_PREFIX = "https://play.google.com/store/apps/details?id=";

    @Nullable
    private final String overridePackageName;

    public GooglePlayStoreFeedbackCollector() {
        this.overridePackageName = null;
    }

    public GooglePlayStoreFeedbackCollector(@NonNull final String overridePackageName) {
        this.overridePackageName = overridePackageName;
    }

    @Override
    public boolean tryCollectingFeedback(
            @NonNull final Activity currentActivity,
            @NonNull final IApp app,
            @NonNull final IEnvironment environment,
            @NonNull final IDevice device) {

        final String packageName = StringUtils.defaultIfBlank(overridePackageName, currentActivity.getPackageName());

        try {
            currentActivity.startActivity(new Intent(ACTION_VIEW, getAndroidMarketUri(packageName)));
            currentActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            return true;
        } catch (final ActivityNotFoundException ignored) {
            try {
                currentActivity.startActivity(new Intent(ACTION_VIEW, getGooglePlayStoreUri(packageName)));
                currentActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            } catch (final ActivityNotFoundException ignored2) {
                return false;
            }
        }
    }

    @NonNull
    private Uri getAndroidMarketUri(@NonNull final String packageName) {
        return Uri.parse(ANDROID_MARKET_URI_PREFIX + packageName);
    }

    @NonNull
    private Uri getGooglePlayStoreUri(@NonNull final String packageName) {
        return Uri.parse(GOOGLE_PLAY_STORE_URI_PREFIX + packageName);
    }

}
