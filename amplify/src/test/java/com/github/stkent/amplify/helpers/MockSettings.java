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
package com.github.stkent.amplify.helpers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.IEventCheck;
import com.github.stkent.amplify.tracking.interfaces.ISettings;
import com.github.stkent.amplify.tracking.interfaces.ITrackedEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A mock settings implementation that stores most recent values written,
 * indexed first by IEvent, and then by IEventCheck. This allows for easier
 * verifications during testing.
 */
public class MockSettings<T> implements ISettings<T> {

    private final Map<IEvent, Map<IEventCheck, T>> mostRecentValuesWritten = new ConcurrentHashMap<>();

    @Nullable
    public T getEventValue(@NonNull final IEvent event, @NonNull final IEventCheck eventCheck) {
        if (!mostRecentValuesWritten.containsKey(event)) {
            return null;
        }

        final Map<IEventCheck, T> eventMap = mostRecentValuesWritten.get(event);

        if (!eventMap.containsKey(eventCheck)) {
            return null;
        }

        return eventMap.get(eventCheck);
    }

    @Override
    public void writeTrackingValue(@NonNull final ITrackedEvent trackedEvent, final T value) {
        final IEvent event = trackedEvent.getEvent();

        if (!mostRecentValuesWritten.containsKey(event)) {
            mostRecentValuesWritten.put(event, new ConcurrentHashMap<IEventCheck, T>());
        }

        mostRecentValuesWritten.get(event).put(trackedEvent.getEventCheck(), value);
    }

    @Nullable
    @Override
    public T readTrackingValue(@NonNull final ITrackedEvent trackedEvent) {
        return getEventValue(trackedEvent.getEvent(), trackedEvent.getEventCheck());
    }

    @Override
    public boolean hasEventValue(@NonNull final ITrackedEvent trackedEvent) {
        return readTrackingValue(trackedEvent) != null;
    }

}
