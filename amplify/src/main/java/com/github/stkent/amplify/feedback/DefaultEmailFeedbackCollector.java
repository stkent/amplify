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

import android.content.Context;
import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DefaultEmailFeedbackCollector extends BaseEmailFeedbackCollector {

    public DefaultEmailFeedbackCollector(@NonNull final Context context, @NonNull final String... recipients) {
        super(context, recipients);
    }

    @NonNull
    @Override
    protected String getSubjectLine() {
        return getApp().getName() + " Android App Feedback";
    }

    @NonNull
    @Override
    protected String getBody() {
        final String androidVersionString = String.format(
                "%s (%s)", getEnvironment().getAndroidVersionName(), getEnvironment().getAndroidVersionCode());

        final String appVersionString = String.format("%s (%s)", getApp().getVersionName(), getApp().getVersionCode());

        // @formatter:off
        return    "Time Stamp: " + getCurrentUtcTimeStringForDate(new Date()) + "\n"
                + "App Version: " + appVersionString + "\n"
                + "Android Version: " + androidVersionString + "\n"
                + "Device Manufacturer: " + getDevice().getManufacturer() + "\n"
                + "Device Model: " + getDevice().getModel() + "\n"
                + "Display Resolution: " + getDevice().getResolution() + "\n"
                + "Display Density (Actual): " + getDevice().getActualDensity() + "\n"
                + "Display Density (Bucket) " + getDevice().getDensityBucket() + "\n"
                + "---------------------\n\n";
        // @formatter:on
    }

    @NonNull
    private String getCurrentUtcTimeStringForDate(final Date date) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss z", Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(date);
    }

}
