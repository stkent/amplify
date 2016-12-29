package com.github.stkent.amplify.feedback;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.utils.appinfo.IAppInfoProvider;
import com.github.stkent.amplify.utils.feedback.IEmailContentProvider;

public interface IFeedbackCollector {

    // fixme: adjust this method sig
    void collectFeedback(@NonNull Activity currentActivity,
                         @NonNull IAppInfoProvider appInfoProvider,
                         @NonNull IEmailContentProvider emailContentProvider);

}
