package com.github.stkent.testapp;

import android.app.Application;

import com.github.stkent.amplify.feedback.AmazonAppStoreFeedbackCollector;
import com.github.stkent.amplify.feedback.DefaultEmailFeedbackCollector;
import com.github.stkent.amplify.feedback.GooglePlayStoreFeedbackCollector;
import com.github.stkent.amplify.logging.AndroidLogger;
import com.github.stkent.amplify.tracking.Amplify;

public class ExampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Amplify.setLogger(new AndroidLogger());

        final String releasePackageName = "com.github.stkent.testapp";

        Amplify.initSharedInstance(this)
               .setPositiveFeedbackCollectors(
                       new AmazonAppStoreFeedbackCollector(releasePackageName),
                       new GooglePlayStoreFeedbackCollector(releasePackageName))
               .setCriticalFeedbackCollectors(
                       new DefaultEmailFeedbackCollector(this, "someone@example.com"))
               .setAlwaysShow(true);
    }

}
