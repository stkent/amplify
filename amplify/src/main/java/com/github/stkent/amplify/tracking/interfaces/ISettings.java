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
import android.support.annotation.Nullable;

/**
 * An abstract representation of the persistence layer used to track events
 * across consuming application launches.
 */
public interface ISettings {

    /**
     * @param event an event that has been registered with the state tracker
     * @return the total number of times the provided event has occurred.
     */
    int getTotalEventCount(@NonNull final IEvent event);

    /**
     * @param event an event that has been registered with the state tracker
     * @param totalEventCount the total number of times the provided event has
     *        occurred
     */
    void setTotalEventCount(@NonNull final IEvent event, final int totalEventCount);

    /**
     * @param event an event that has been registered with the state tracker
     * @return the system time in millis of the most recent occurrence of the
     *         provided event
     */
    long getLastEventTime(@NonNull final IEvent event);

    /**
     * @param event an event that has been registered with the state tracker
     * @param lastEventTime the system time in millis of the most recent
     *        occurrence of the provided event
     */
    void setLastEventTime(@NonNull final IEvent event, final long lastEventTime);

    /**
     * @param event an event that has been registered with the state tracker
     * @return the consuming application version in which the provided event
     *         most recently occurred.
     */
    @Nullable
    String getLastEventVersion(@NonNull final IEvent event);

    /**
     * @param event an event that has been registered with the state tracker
     * @param lastEventVersion the consuming application version in which the
     *        provided event most recently occurred.
     */
    void setLastEventVersion(@NonNull final IEvent event, @NonNull final String lastEventVersion);

    /**
     * Resets all tracked state. Intended for debugging only.
     */
    void reset();

}
