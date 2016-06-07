package com.github.stkent.amplify.utils.feedback;

import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.interfaces.IFeedbackDataProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DefaultEmailContentProvider implements IEmailContentProvider {

    protected static final String DEFAULT_EMAIL_SUBJECT_LINE_SUFFIX = " Android App Feedback";
    protected static final String DEFAULT_CONTENT_SEPARATOR = "---------------------\n\n";

    @NonNull
    @Override
    public String getEmailSubjectLine(@NonNull final IFeedbackDataProvider feedbackDataProvider) {
        return feedbackDataProvider.getAppNameString() + DEFAULT_EMAIL_SUBJECT_LINE_SUFFIX;
    }

    @NonNull
    @Override
    public String getInitialEmailBody(@NonNull final IFeedbackDataProvider feedbackDataProvider) {
        return    "My Device: " + feedbackDataProvider.getDeviceName()
                + "\n"
                + "App Version: " + feedbackDataProvider.getVersionDisplayString()
                + "\n"
                + "Android Version: " + feedbackDataProvider.getAndroidOsVersionDisplayString()
                + "\n"
                + "Time Stamp: " + getCurrentUtcTimeStringForDate(new Date())
                + "\n"
                + DEFAULT_CONTENT_SEPARATOR;
    }

    @NonNull
    protected String getCurrentUtcTimeStringForDate(final Date date) {
        final SimpleDateFormat simpleDateFormat
                = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss z", Locale.getDefault());

        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        return simpleDateFormat.format(date);
    }

}
