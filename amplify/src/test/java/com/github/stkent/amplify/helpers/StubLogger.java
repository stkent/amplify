package com.github.stkent.amplify.helpers;

import android.support.annotation.NonNull;

import com.github.stkent.amplify.ILogger;
import com.github.stkent.amplify.Logger;

public class StubLogger implements ILogger {

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
