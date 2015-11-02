package com.github.stkent.amplify.tracking.interfaces;

import android.support.annotation.NonNull;

import com.github.stkent.amplify.Logger;

public interface ILogger {

    void setLogLevel(@NonNull final Logger.LogLevel logLevel);

    void d(@NonNull final String message);

    void e(@NonNull final String message);

}
