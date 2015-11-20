package com.github.stkent.amplify.helpers;

import android.support.annotation.NonNull;

import com.github.stkent.amplify.Logger;
import com.github.stkent.amplify.tracking.interfaces.ILogger;

public class StubbedLogger implements ILogger {

    @Override
    public void setLogLevel(@NonNull final Logger.LogLevel logLevel) {
        // no-op
    }

    @Override
    public void d(@NonNull final String message) {
        // no-op
    }

    @Override
    public void e(@NonNull final String message) {
        // no-op
    }

}
