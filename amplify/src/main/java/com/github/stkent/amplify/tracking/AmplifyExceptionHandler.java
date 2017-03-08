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
package com.github.stkent.amplify.tracking;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.stkent.amplify.tracking.interfaces.IAppLevelEventRulesManager;

import static java.lang.Thread.UncaughtExceptionHandler;

/**
 * An exception handler used to observe application crashes. Received
 * exceptions are forwarded to the provided default exception handler.
 */
public final class AmplifyExceptionHandler implements UncaughtExceptionHandler {

    @NonNull
    private final IAppLevelEventRulesManager appLevelEventRulesManager;

    @Nullable
    private final UncaughtExceptionHandler defaultExceptionHandler;

    public AmplifyExceptionHandler(
            @NonNull final IAppLevelEventRulesManager appLevelEventRulesManager,
            @Nullable final UncaughtExceptionHandler defaultExceptionHandler) {

        this.appLevelEventRulesManager = appLevelEventRulesManager;
        this.defaultExceptionHandler = defaultExceptionHandler;
    }

    @Override
    public void uncaughtException(final Thread thread, final Throwable throwable) {
        appLevelEventRulesManager.notifyOfCrash();

        // Call the original handler if provided.

        if (defaultExceptionHandler != null) {
            defaultExceptionHandler.uncaughtException(thread, throwable);
        }
    }

}
