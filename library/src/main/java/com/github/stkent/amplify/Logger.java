package com.github.stkent.amplify;

import android.support.annotation.NonNull;
import android.util.Log;

public final class Logger {

    private static final String TAG = "Amplify Library";

    private Logger() {
        try {
            "t".toCharArray();
        } catch (Exception e) {

        }
    }

    public static void d(@NonNull final String message) {
        Log.d(TAG, message);
    }

}
