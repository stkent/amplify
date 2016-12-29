package com.github.stkent.testapp;

import android.app.Application;

import com.github.stkent.amplify.feedback.EmailFeedbackCollector;
import com.github.stkent.amplify.feedback.GooglePlayStoreFeedbackCollector;
import com.github.stkent.amplify.logging.AndroidLogger;
import com.github.stkent.amplify.tracking.Amplify;

public class ExampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Amplify.setLogger(new AndroidLogger());

        Amplify.initSharedInstance(this)
                .setPositiveFeedbackCollector(new EmailFeedbackCollector("someone@example.com"))
                .setCriticalFeedbackCollector(new GooglePlayStoreFeedbackCollector())
               .setAlwaysShow(true);
    }

}
