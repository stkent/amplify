/**
 * Copyright 2015 Stuart Kent
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.github.stkent.amplify;

import android.support.annotation.NonNull;
import android.util.Log;

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
