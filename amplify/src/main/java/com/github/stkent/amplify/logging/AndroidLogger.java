/*
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
package com.github.stkent.amplify.logging;

import android.support.annotation.NonNull;
import android.util.Log;

/**
 * A logger that directs output to Android's logcat.
 */
public final class AndroidLogger implements ILogger {

    private static final String TAG = "Amplify";

    @NonNull
    private final String tag;

    public AndroidLogger() {
        this(TAG);
    }

    public AndroidLogger(@NonNull final String tag) {
        this.tag = tag;
    }

    public void d(@NonNull final String message) {
        Log.d(tag, message);
    }

    public void e(@NonNull final String message) {
        Log.e(tag, message);
    }

}
