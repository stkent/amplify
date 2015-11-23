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

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.ILogger;

/**
 * An abstract representation of an event whose occurrences can be tracked
 * across consuming application launches.
 */
public interface IEvent {

    /**
     * @return a key that uniquely identifies this event within the
     *         consuming application
     */
    @NonNull
    String getTrackingKey();

    /**
     * A method that can be used to perform any initialization required to
     * track this event. Will be called once per launch of the consuming
     * application.
     *
     * @param applicationContext the context of the consuming application
     * @param logger logger to be used for messaging
     */
    void performRelatedInitialization(@NonNull final Context applicationContext, @NonNull final ILogger logger);

}
