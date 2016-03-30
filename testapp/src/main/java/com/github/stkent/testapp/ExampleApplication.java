package com.github.stkent.testapp;

import android.app.Application;

import com.github.stkent.amplify.Logger;
import com.github.stkent.amplify.tracking.Amplify;

public class ExampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Amplify.initialize(this)
               .setLogLevel(Logger.LogLevel.DEBUG)
               .setFeedbackEmailAddress("someone@example.com")
               .setAlwaysShow(true);
    }
}
