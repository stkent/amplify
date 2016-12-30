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
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.Environment;
import com.github.stkent.amplify.IEnvironment;
import com.github.stkent.amplify.tracking.Amplify;

import static android.content.Intent.ACTION_SENDTO;

public abstract class BaseEmailFeedbackCollector implements IFeedbackCollector {

    @NonNull
    protected abstract String getSubjectLine();

    @NonNull
    protected abstract String getBody();

    @NonNull
    private final IEnvironment environment;

    @NonNull
    private final String[] recipients;

    public BaseEmailFeedbackCollector(@NonNull final Context context, @NonNull final String... recipients) {
        this.environment = new Environment(context);
        this.recipients = recipients;
    }

    @Override
    public boolean collectFeedback(@NonNull final Activity currentActivity) {
        final Intent emailIntent = getEmailIntent();

        if (!environment.canHandleIntent(emailIntent)) {
            Amplify.getLogger().e("Unable to present email client chooser.");
            return false;
        }

        showFeedbackEmailChooser(currentActivity, emailIntent);
        return true;
    }

    @NonNull
    protected IEnvironment getEnvironment() {
        return environment;
    }

    @NonNull
    private Intent getEmailIntent() {
        final Intent result = new Intent(ACTION_SENDTO);
        result.setData(Uri.parse("mailto:"));
        result.putExtra(Intent.EXTRA_EMAIL, recipients);
        result.putExtra(Intent.EXTRA_SUBJECT, getSubjectLine());
        result.putExtra(Intent.EXTRA_TEXT, getBody());
        return result;
    }

    private void showFeedbackEmailChooser(@NonNull final Activity currentActivity, @NonNull final Intent emailIntent) {
        currentActivity.startActivity(Intent.createChooser(emailIntent, "Choose an email provider:"));
        currentActivity.overridePendingTransition(0, 0);
    }

}
