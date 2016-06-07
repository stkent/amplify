package com.github.stkent.amplify.utils.feedback;

import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.interfaces.IFeedbackDataProvider;

public interface IEmailContentProvider {

    @NonNull
    String getEmailSubjectLine(@NonNull final IFeedbackDataProvider feedbackDataProvider);

    @NonNull
    String getInitialEmailBody(@NonNull final IFeedbackDataProvider feedbackDataProvider);

}
