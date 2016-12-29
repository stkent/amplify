package com.github.stkent.amplify.feedback;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.stkent.amplify.utils.PlayStoreUtil;
import com.github.stkent.amplify.utils.appinfo.IAppInfoProvider;
import com.github.stkent.amplify.utils.feedback.IEmailContentProvider;

public final class GooglePlayStoreFeedbackCollector implements IFeedbackCollector {

    @Nullable
    private final String overridePackageName;

    public GooglePlayStoreFeedbackCollector() {
        this(null);
    }

    public GooglePlayStoreFeedbackCollector(@Nullable final String overridePackageName) {
        this.overridePackageName = overridePackageName;
    }

    @Override
    public void collectFeedback(
            @NonNull final Activity currentActivity,
            @NonNull final IAppInfoProvider appInfoProvider,
            @NonNull final IEmailContentProvider emailContentProvider) {

        // fixme: fall back to default package name if no override supplied
        PlayStoreUtil.openPlayStoreToRate(currentActivity, overridePackageName);
    }

}
