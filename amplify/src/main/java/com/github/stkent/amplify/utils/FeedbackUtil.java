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
import com.github.stkent.amplify.tracking.interfaces.IApplicationFeedbackDataProvider;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentCapabilitiesProvider;
import com.github.stkent.amplify.utils.time.SystemTimeUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class FeedbackUtil {

    private final IApplicationFeedbackDataProvider applicationFeedbackDataProvider;
    private final IEnvironmentCapabilitiesProvider environmentCapabilitiesProvider;
    private final ILogger logger;
    private final String feedbackEmail;

    public FeedbackUtil(
            @NonNull final IApplicationFeedbackDataProvider applicationFeedbackDataProvider,
            @NonNull final IEnvironmentCapabilitiesProvider environmentCapabilitiesProvider,
            @NonNull final String feedbackEmail,
            @NonNull final ILogger logger) {
        this.applicationFeedbackDataProvider = applicationFeedbackDataProvider;
        this.environmentCapabilitiesProvider = environmentCapabilitiesProvider;
        this.feedbackEmail = feedbackEmail;
        this.logger = logger;
    }

    public void showFeedbackEmailChooser(@Nullable final Activity activity) {
        final Intent feedbackEmailIntent = getFeedbackEmailIntent();

        if (!environmentCapabilitiesProvider.canHandleIntent(feedbackEmailIntent)) {
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

        // Uri.Builder is not useful here; see http://stackoverflow.com/a/12035226/2911458
        final StringBuilder uriStringBuilder = new StringBuilder("mailto:");

        try {
            uriStringBuilder.append(this.feedbackEmail);
        } catch (final IllegalStateException e) {
            logger.e("Feedback email address was not defined");
        }

        uriStringBuilder.append("?subject=")
                .append(feedbackEmailSubject)
                .append("&body=")
                .append(Uri.encode(appInfo));

        final Uri uri = Uri.parse(uriStringBuilder.toString());

        return new Intent(Intent.ACTION_SENDTO, uri);
    }

    @NonNull
    private String getApplicationInfoString() {
        String applicationVersionDisplayString;

        try {
            applicationVersionDisplayString = applicationFeedbackDataProvider.getVersionDisplayString();
        } catch (PackageManager.NameNotFoundException e) {
            logger.e("Unable to determine application version information.");

            applicationVersionDisplayString = "Unknown";
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM d, yyyy - hh:mm:ss a ZZZZ", Locale.getDefault());
        Date date = new Date(SystemTimeUtil.currentTimeMillis());
        String dateString = simpleDateFormat.format(date);

        return    "\n\n\n"
                + "---------------------"
                + "\n"
                + "Device: " + applicationFeedbackDataProvider.getDeviceName()
                + "\n"
                + "App Version: " + applicationVersionDisplayString
                + "\n"
                + "Android OS Version: " + getAndroidOsVersionDisplayString()
                + "\n"
                + "Date: " + dateString;
    }

    private String getAndroidOsVersionDisplayString() {
        // fixme: will this correctly reporting version information for the consuming application??
        return Build.VERSION.RELEASE + " - " + Build.VERSION.SDK_INT;
    }

}
