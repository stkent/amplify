package com.github.stkent.testapp;

import android.app.Application;

import com.github.stkent.amplify.logging.AndroidLogger;
import com.github.stkent.amplify.tracking.Amplify;

public class ExampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Amplify.setLogger(new AndroidLogger());

        Amplify.initSharedInstance(this)
               .setFeedbackEmailAddress("someone@example.com")
               .setAlwaysShow(true);
    }

}
