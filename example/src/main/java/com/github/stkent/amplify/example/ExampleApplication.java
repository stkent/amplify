package com.github.stkent.amplify.example;

import android.app.Application;

import com.github.stkent.amplify.Logger;
import com.github.stkent.amplify.tracking.AmplifyStateTracker;

import static com.github.stkent.amplify.BuildConfig.DEBUG;

public class ExampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AmplifyStateTracker.get(this)
                .configureWithDefaults()
                .setLogLevel(DEBUG ? Logger.LogLevel.DEBUG : Logger.LogLevel.NONE);
    }

}
