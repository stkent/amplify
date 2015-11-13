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
package com.github.stkent.amplify.tracking.interfaces;

import android.support.annotation.NonNull;

import com.github.stkent.amplify.Logger;

/**
 * An abstract representation of a logging class with customizable output
 * verbosity.
 */
public interface ILogger {

    /**
     * @param logLevel the most verbose log message type that should be
     *        displayed
     */
    void setLogLevel(@NonNull final Logger.LogLevel logLevel);

    /**
     * Record a log message with level Logger.LogLevel.DEBUG.
     *
     * @param message the message to be logged
     */
    void d(@NonNull final String message);

    /**
     * Record a log message with level Logger.LogLevel.ERROR.
     *
     * @param message the message to be logged
     */
    void e(@NonNull final String message);

}
