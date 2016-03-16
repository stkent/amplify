package com.github.stkent.testapp;

import android.app.Application;

import com.github.stkent.amplify.tracking.Amplify;

public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Amplify.get(this).setFeedbackEmailAddress("someone@example.com");
    }
}
