package com.github.stkent.amplify.example;

import android.app.Application;

import com.github.stkent.amplify.Logger;
import com.github.stkent.amplify.tracking.Amplify;

import static com.github.stkent.amplify.BuildConfig.DEBUG;

public class ExampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Amplify.get(this)
                .configureWithDefaultBehavior()
                .setLogLevel(DEBUG ? Logger.LogLevel.DEBUG : Logger.LogLevel.NONE);
    }

}
