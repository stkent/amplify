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
package com.github.stkent.amplify.utils.feedback;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.Amplify;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentCapabilitiesProvider;
import com.github.stkent.amplify.tracking.interfaces.IFeedbackDataProvider;

public final class FeedbackUtil {

    private final IFeedbackDataProvider feedbackDataProvider;
    private final IEmailContentProvider emailContentProvider;
    private final IEnvironmentCapabilitiesProvider environmentCapabilitiesProvider;
    private final String feedbackEmailAddress;

    public FeedbackUtil(
            @NonNull final IFeedbackDataProvider feedbackDataProvider,
            @NonNull final IEmailContentProvider emailContentProvider,
            @NonNull final IEnvironmentCapabilitiesProvider environmentCapabilitiesProvider,
            @NonNull final String feedbackEmailAddress) {

        this.feedbackDataProvider = feedbackDataProvider;
        this.emailContentProvider = emailContentProvider;
        this.environmentCapabilitiesProvider = environmentCapabilitiesProvider;
        this.feedbackEmailAddress = feedbackEmailAddress;
    }

    public void showFeedbackEmailChooser(@NonNull final Activity activity) {
        final Intent feedbackEmailIntent = getFeedbackEmailIntent();

        if (!environmentCapabilitiesProvider.canHandleIntent(feedbackEmailIntent)) {
            Amplify.getLogger().e("Unable to present email client chooser.");
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
        result.putExtra(Intent.EXTRA_SUBJECT,
                emailContentProvider.getEmailSubjectLine(feedbackDataProvider));

        result.putExtra(Intent.EXTRA_TEXT,
                emailContentProvider.getInitialEmailBody(feedbackDataProvider));

        return result;
    }

}
