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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.github.stkent.amplify.tracking.TrackingUtils;

import java.util.List;

public final class FeedbackUtils {

    private static final String TAG = "FeedbackUtils";
    private static final int BASE_MESSAGE_LENGTH = 78;

    private FeedbackUtils() {

    }

    public static void showFeedbackEmailChooser(final Activity activity) {
        activity.startActivity(Intent.createChooser(getFeedbackEmailIntent(activity), "Choose an email provider:"));
        activity.overridePendingTransition(0, 0);
    }

    public static boolean canHandleFeedbackEmailIntent(final Context context) {
        final List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(getFeedbackEmailIntent(context),
                PackageManager.MATCH_DEFAULT_ONLY);
        return !resolveInfoList.isEmpty();
    }

    private static Intent getFeedbackEmailIntent(Context context) {

        String email;

        try {
            email = context.getString(R.string.amp_feedback_email);
        } catch (Resources.NotFoundException e) {
            throw new IllegalArgumentException("R.string.amp_feedback_email resource not found, you must set this in your strings file for the"
                    + " feedback util to function", e);
        }

        final String feedbackMailTo = "mailto:" + email;
        final String feedbackEmailSubject = Uri.encode("Android App Feedback", "UTF-8");
        final String appInfo = getAppInfo(context);

        final String uriString = feedbackMailTo
                + "?subject=" + feedbackEmailSubject
                + "&body=" + Uri.encode(appInfo);

        final Uri uri = Uri.parse(uriString);

        final Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(uri);
        return emailIntent;
    }

    private static String getAppInfo(final Context context) {
        String versionString = "Error fetching version string";

        try {
            versionString = TrackingUtils.getAppVersionCode(context) + " - " + TrackingUtils.getAppVersionName(context);
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, "MissingVersion", e);
        }

        return new StringBuilder(BASE_MESSAGE_LENGTH)

                .append("\n\n\n---------------------\nApp Version: ")

                .append(versionString)
                .append("\n")

                .append("Android OS Version: ")
                .append(Build.VERSION.RELEASE)
                .append(" - ")
                .append(Build.VERSION.SDK_INT)

                .append("\n")
                .append("Date: ")
                .append(System.currentTimeMillis())
                .toString();
    }

}
