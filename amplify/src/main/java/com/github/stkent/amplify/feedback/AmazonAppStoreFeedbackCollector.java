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
package com.github.stkent.amplify.feedback;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import static android.content.Intent.ACTION_VIEW;

public final class AmazonAppStoreFeedbackCollector implements IFeedbackCollector {

    private static final String AMAZON_APP_STORE_URI_PREFIX = "http://www.amazon.com/gp/mas/dl/android?p=";

    @NonNull
    private static Uri getAmazonAppStoreUri(@NonNull final String packageName) {
        return Uri.parse(AMAZON_APP_STORE_URI_PREFIX + packageName);
    }

    @NonNull
    private final String packageName;

    public AmazonAppStoreFeedbackCollector(@NonNull final Context context) {
        this(context.getPackageName());
    }

    public AmazonAppStoreFeedbackCollector(@NonNull final String packageName) {
        this.packageName = packageName;
    }

    @Override
    public boolean collectFeedback(@NonNull final Activity currentActivity) {
        try {
            currentActivity.startActivity(new Intent(ACTION_VIEW, getAmazonAppStoreUri(packageName)));
            currentActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            return true;
        } catch (final ActivityNotFoundException ignored2) {
            return false;
        }
    }

}
