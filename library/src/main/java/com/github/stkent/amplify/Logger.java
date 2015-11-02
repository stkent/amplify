package com.github.stkent.amplify;

import android.support.annotation.NonNull;
import android.util.Log;

public final class Logger {

    private static final String TAG = "Amplify Library";

    public enum LogLevel {
        NONE(0),
        ERROR(1),
        DEBUG(2);

        private final int verbosity;

        LogLevel(final int verbosity) {
            this.verbosity = verbosity;
        }

        public int getVerbosity() {
            return verbosity;
        }
    }

    private static LogLevel logLevel = LogLevel.ERROR;

    private Logger() {

    }

    public static void setLogLevel(@NonNull final LogLevel logLevel) {
        Logger.logLevel = logLevel;
    }

    public static void d(@NonNull final String message) {
        if (logLevel.getVerbosity() >= LogLevel.DEBUG.getVerbosity()) {
            Log.d(TAG, message);
        }
    }

    public static void e(@NonNull final String message) {
        if (logLevel.getVerbosity() >= LogLevel.ERROR.getVerbosity()) {
            Log.e(TAG, message);
        }
    }

}
