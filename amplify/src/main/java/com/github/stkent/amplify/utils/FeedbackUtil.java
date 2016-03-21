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
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.ILogger;
import com.github.stkent.amplify.tracking.interfaces.IAppFeedbackDataProvider;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentCapabilitiesProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class FeedbackUtil {

    private static final String DEFAULT_EMAIL_SUBJECT_LINE_SUFFIX = " Android App Feedback";

    private final IAppFeedbackDataProvider appFeedbackDataProvider;
    private final IEnvironmentCapabilitiesProvider environmentCapabilitiesProvider;
    private final ILogger logger;
    private final String feedbackEmailAddress;

    public FeedbackUtil(
            @NonNull final IAppFeedbackDataProvider appFeedbackDataProvider,
            @NonNull final IEnvironmentCapabilitiesProvider environmentCapabilitiesProvider,
            @NonNull final String feedbackEmailAddress,
            @NonNull final ILogger logger) {

        this.appFeedbackDataProvider = appFeedbackDataProvider;
        this.environmentCapabilitiesProvider = environmentCapabilitiesProvider;
        this.feedbackEmailAddress = feedbackEmailAddress;
        this.logger = logger;
    }

    public void showFeedbackEmailChooser(@NonNull final Activity activity) {
        final Intent feedbackEmailIntent = getFeedbackEmailIntent();

        if (!environmentCapabilitiesProvider.canHandleIntent(feedbackEmailIntent)) {
            logger.e("Unable to present email client chooser.");
            return;
        }

        activity.startActivity(Intent.createChooser(
                feedbackEmailIntent, "Choose an email provider:"));

        activity.overridePendingTransition(0, 0);
    }

    @NonNull
    private Intent getFeedbackEmailIntent() {
        final Intent result = new Intent(Intent.ACTION_SENDTO);
        result.setData(Uri.parse("mailto:"));
        result.putExtra(Intent.EXTRA_EMAIL, new String[]{feedbackEmailAddress});
        result.putExtra(Intent.EXTRA_SUBJECT, getEmailSubjectLine());
        result.putExtra(Intent.EXTRA_TEXT, getAppInfoString());
        return result;
    }

    @NonNull
    private String getEmailSubjectLine() {
        return appFeedbackDataProvider.getAppNameString() + DEFAULT_EMAIL_SUBJECT_LINE_SUFFIX;
    }

    @NonNull
    private String getAppInfoString() {
        return    "My Device: " + appFeedbackDataProvider.getDeviceName()
                + "\n"
                + "App Version: " + appFeedbackDataProvider.getVersionDisplayString()
                + "\n"
                + "Android Version: " + getAndroidOsVersionDisplayString()
                + "\n"
                + "Time Stamp: " + getCurrentUtcTimeStringForDate(new Date())
                + "\n"
                + "---------------------"
                + "\n\n";
    }

    @NonNull
    private String getAndroidOsVersionDisplayString() {
        return String.format("%s (%s)", Build.VERSION.RELEASE, Build.VERSION.SDK_INT);
    }

    @NonNull
    private String getCurrentUtcTimeStringForDate(final Date date) {
        final SimpleDateFormat simpleDateFormat
                = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss z", Locale.getDefault());

        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        return simpleDateFormat.format(date);
    }

}
