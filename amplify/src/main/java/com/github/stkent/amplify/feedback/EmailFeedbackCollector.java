package com.github.stkent.amplify.feedback;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.EnvironmentCapabilitiesProvider;
import com.github.stkent.amplify.tracking.FeedbackDataProvider;
import com.github.stkent.amplify.utils.appinfo.IAppInfoProvider;
import com.github.stkent.amplify.utils.feedback.FeedbackUtil;
import com.github.stkent.amplify.utils.feedback.IEmailContentProvider;

public final class EmailFeedbackCollector implements IFeedbackCollector {

    @NonNull
    private final String emailAddress;

    public EmailFeedbackCollector(@NonNull final String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public void collectFeedback(
            @NonNull final Activity currentActivity,
            @NonNull final IAppInfoProvider appInfoProvider,
            @NonNull final IEmailContentProvider emailContentProvider) {

        final FeedbackUtil feedbackUtil = new FeedbackUtil(
                new FeedbackDataProvider(appInfoProvider),
                emailContentProvider,
                new EnvironmentCapabilitiesProvider(appInfoProvider),
                emailAddress);

        feedbackUtil.showFeedbackEmailChooser(currentActivity);
    }

}
