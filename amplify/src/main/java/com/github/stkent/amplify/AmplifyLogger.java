package com.github.stkent.amplify;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

public class AmplifyLogger {

    @NonNull
    private static ILogger logger = new Logger();

    @NonNull
    public static ILogger getLogger() {
        return logger;
    }

    public static void setLogLevel(@NonNull final Logger.LogLevel logLevel) {
        logger.setLogLevel(logLevel);
    }

    @VisibleForTesting
    public static void setLogger(@NonNull final ILogger logger) {
        AmplifyLogger.logger = logger;
    }

}
