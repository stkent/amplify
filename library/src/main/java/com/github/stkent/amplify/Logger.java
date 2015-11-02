package com.github.stkent.amplify;

import android.support.annotation.NonNull;
import android.util.Log;

import com.github.stkent.amplify.tracking.interfaces.ILogger;

public final class Logger implements ILogger {

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

    private LogLevel logLevel = LogLevel.ERROR;

    public void setLogLevel(@NonNull final LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public void d(@NonNull final String message) {
        if (logLevel.getVerbosity() >= LogLevel.DEBUG.getVerbosity()) {
            Log.d(TAG, message);
        }
    }

    public void e(@NonNull final String message) {
        if (logLevel.getVerbosity() >= LogLevel.ERROR.getVerbosity()) {
            Log.e(TAG, message);
        }
    }

}
