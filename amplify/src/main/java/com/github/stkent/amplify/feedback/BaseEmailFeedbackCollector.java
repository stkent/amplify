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
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.IApp;
import com.github.stkent.amplify.IDevice;
import com.github.stkent.amplify.IEnvironment;
import com.github.stkent.amplify.tracking.Amplify;

import java.util.Arrays;

import static android.content.Intent.ACTION_SENDTO;

public abstract class BaseEmailFeedbackCollector implements IFeedbackCollector {

    @NonNull
    protected abstract String getSubjectLine(
            @NonNull IApp app,
            @NonNull IEnvironment environment,
            @NonNull IDevice device);

    @NonNull
    protected abstract String getBody(
            @NonNull IApp app,
            @NonNull IEnvironment environment,
            @NonNull IDevice device);

    @NonNull
    private final String[] recipients;

    public BaseEmailFeedbackCollector(@NonNull final String... recipients) {
        this.recipients = Arrays.copyOf(recipients, recipients.length);
    }

    @Override
    public boolean tryCollectingFeedback(
            @NonNull final Activity currentActivity,
            @NonNull final IApp app,
            @NonNull final IEnvironment environment,
            @NonNull final IDevice device) {

        final Intent emailIntent = getEmailIntent(app, environment, device);

        if (!environment.canHandleIntent(emailIntent)) {
            Amplify.getLogger().e("Unable to present email client chooser.");
            return false;
        }

        showFeedbackEmailChooser(currentActivity, emailIntent);
        return true;
    }

    @NonNull
    private Intent getEmailIntent(
            @NonNull final IApp app,
            @NonNull final IEnvironment environment,
            @NonNull final IDevice device) {

        final Intent result = new Intent(ACTION_SENDTO);
        result.setData(Uri.parse("mailto:"));
        result.putExtra(Intent.EXTRA_EMAIL, recipients);
        result.putExtra(Intent.EXTRA_SUBJECT, getSubjectLine(app, environment, device));
        result.putExtra(Intent.EXTRA_TEXT, getBody(app, environment, device));
        return result;
    }

    private void showFeedbackEmailChooser(@NonNull final Activity currentActivity, @NonNull final Intent emailIntent) {
        currentActivity.startActivity(emailIntent);
        currentActivity.overridePendingTransition(0, 0);
    }

}
