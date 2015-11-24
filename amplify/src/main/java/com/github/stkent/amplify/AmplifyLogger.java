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
