package com.github.stkent.amplify;

import android.support.annotation.NonNull;
import android.util.Log;

public final class Logger {

    private static final String TAG = "Amplify Library";

    private Logger() {

    }

    public static void d(@NonNull final String message) {
        Log.d(TAG, message);
    }

}
