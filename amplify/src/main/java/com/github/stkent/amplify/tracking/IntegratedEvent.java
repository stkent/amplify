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
package com.github.stkent.amplify.tracking;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.interfaces.IEvent;

public enum IntegratedEvent implements IEvent {

    APP_CRASHED,
    APP_INSTALLED,
    APP_UPDATED,
    USER_GAVE_POSITIVE_FEEDBACK,
    USER_GAVE_NEGATIVE_FEEDBACK,
    USER_DECLINED_RATING,
    USER_DECLINED_FEEDBACK;

    @NonNull
    @Override
    public String getTrackingKey() {
        return name();
    }

    @Override
    public void performRelatedInitialization(@NonNull final Context applicationContext) {
        if (this == APP_CRASHED) {
            final Thread.UncaughtExceptionHandler defaultExceptionHandler
                    = Thread.getDefaultUncaughtExceptionHandler();

            if (defaultExceptionHandler instanceof AmplifyExceptionHandler) {
                return;
            }

            Thread.setDefaultUncaughtExceptionHandler(
                    new AmplifyExceptionHandler(applicationContext, defaultExceptionHandler));
        }
    }

}
