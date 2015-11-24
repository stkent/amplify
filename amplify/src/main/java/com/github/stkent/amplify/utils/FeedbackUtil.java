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
package com.github.stkent.amplify.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.stkent.amplify.ILogger;
import com.github.stkent.amplify.tracking.ClockUtil;
import com.github.stkent.amplify.tracking.interfaces.IApplicationInfoProvider;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentInfoProvider;

public final class FeedbackUtil {

    private final IApplicationInfoProvider applicationInfoProvider;
    private final IEnvironmentInfoProvider environmentInfoProvider;
    private final ILogger logger;

    public FeedbackUtil(
            @NonNull final IApplicationInfoProvider applicationInfoProvider,
            @NonNull final IEnvironmentInfoProvider environmentInfoProvider,
            @NonNull final ILogger logger) {
        this.applicationInfoProvider = applicationInfoProvider;
        this.environmentInfoProvider = environmentInfoProvider;
        this.logger = logger;
    }

    public void showFeedbackEmailChooser(@Nullable final Activity activity) {
        final Intent feedbackEmailIntent = getFeedbackEmailIntent();

        if (!environmentInfoProvider.canHandleIntent(feedbackEmailIntent)) {
            logger.e("Unable to present email client chooser.");

            return;
        }

        if (ActivityStateUtil.isActivityValid(activity)) {
            activity.startActivity(Intent.createChooser(getFeedbackEmailIntent(), "Choose an email provider:"));
            activity.overridePendingTransition(0, 0);
        }
    }

    @NonNull
    private Intent getFeedbackEmailIntent() {
        // fixme: pull in app name here?
        final String feedbackEmailSubject = Uri.encode("Android App Feedback", "UTF-8");
        final String appInfo = getApplicationInfoString();

        final StringBuilder uriStringBuilder = new StringBuilder("mailto:");

        try {
            uriStringBuilder.append(applicationInfoProvider.getFeedbackEmailAddress());
        } catch (final IllegalStateException e) {
            logger.e("Feedback email address was not defined");
        }

        uriStringBuilder.append("?subject=")
                .append(feedbackEmailSubject)
                .append("&body=")
                .append(appInfo);

        final Uri uri = Uri.parse(uriStringBuilder.toString());

        return new Intent(Intent.ACTION_SENDTO, uri);
    }

    @NonNull
    private String getApplicationInfoString() {
        String applicationVersionDisplayString;

        try {
            applicationVersionDisplayString = applicationInfoProvider.getApplicationVersionDisplayString();
        } catch (PackageManager.NameNotFoundException e) {
            logger.e("Unable to determine application version information.");

            applicationVersionDisplayString = "Unknown";
        }

        return  "\n\n\n" +
                "---------------------" +
                "\n" +
                "App Version: " + applicationVersionDisplayString +
                "\n" +
                "Android OS Version: " + getAndroidOsVersionDisplayString() +
                "\n" +
                "Date: " + ClockUtil.getCurrentTimeMillis();
    }

    private String getAndroidOsVersionDisplayString() {
        // fixme: will this correctly reporting version information for the consuming application??
        return Build.VERSION.RELEASE + " - " + Build.VERSION.SDK_INT;
    }

}
