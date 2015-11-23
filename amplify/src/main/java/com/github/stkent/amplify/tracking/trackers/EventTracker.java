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
package com.github.stkent.amplify.tracking.trackers;

import android.support.annotation.NonNull;

import com.github.stkent.amplify.ILogger;
import com.github.stkent.amplify.tracking.TrackedEvent;
import com.github.stkent.amplify.tracking.interfaces.IApplicationInfoProvider;
import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.IEventCheck;
import com.github.stkent.amplify.tracking.interfaces.ISettings;
import com.github.stkent.amplify.tracking.interfaces.ITrackedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class EventTracker<T> {

    private final ILogger logger;
    private final ISettings<T> settings;
    private final IApplicationInfoProvider applicationInfoProvider;
    private final ConcurrentHashMap<IEvent, List<IEventCheck<T>>> internalMap;

    @NonNull
    protected abstract T defaultTrackingValue();

    @NonNull
    protected abstract T computeUpdatedTrackingValue(@NonNull final T cachedEventValue);

    public EventTracker(
            @NonNull final ILogger logger,
            @NonNull final ISettings<T> settings,
            @NonNull final IApplicationInfoProvider applicationInfoProvider) {
        this.logger = logger;
        this.settings = settings;
        this.applicationInfoProvider = applicationInfoProvider;
        this.internalMap = new ConcurrentHashMap<>();
    }

    public void trackEvent(@NonNull final IEvent event, @NonNull final IEventCheck<T> predicate) {
        if (!containsEvent(event)) {
            internalMap.put(event, new ArrayList<IEventCheck<T>>());
        }

        internalMap.get(event).add(predicate);

        logger.d(internalMap.get(event).toString());
    }

    public void notifyEventTriggered(@NonNull final IEvent event) {
        if (containsEvent(event)) {

            final List<IEventCheck<T>> eventChecks = internalMap.get(event);

            for (final IEventCheck<T> predicate : eventChecks) {
                final ITrackedEvent trackedEvent = new TrackedEvent(event, predicate);

                final T cachedTrackingValue = getCachedTrackingValue(trackedEvent);
                final T updatedTrackingValue = computeUpdatedTrackingValue(cachedTrackingValue);

                settings.writeTrackingValue(trackedEvent, updatedTrackingValue);
            }
        }
    }

    public boolean allowFeedbackPrompt() {

        for (final Map.Entry<IEvent, List<IEventCheck<T>>> eventCheckSet : internalMap.entrySet()) {
            final IEvent event = eventCheckSet.getKey();

            for (final IEventCheck<T> predicate : eventCheckSet.getValue()) {
                final ITrackedEvent trackedEvent = new TrackedEvent(event, predicate);

                final T cachedEventValue = getCachedTrackingValue(trackedEvent);

                logger.d(trackedEvent.getTrackingKey() + ": " + predicate.getStatusString(cachedEventValue, applicationInfoProvider));

                if (predicate.shouldBlockFeedbackPrompt(cachedEventValue, applicationInfoProvider)) {
                    logger.d("Blocking feedback for event: " + trackedEvent + " because of check: " + predicate);
                    return false;
                }
            }
        }

        return true;
    }

    public boolean containsEvent(@NonNull final IEvent event) {
        return internalMap.containsKey(event);
    }

    protected ILogger getLogger() {
        return logger;
    }

    protected IApplicationInfoProvider getApplicationInfoProvider() {
        return applicationInfoProvider;
    }

    private T getCachedTrackingValue(@NonNull final ITrackedEvent trackedEvent) {
        T value = settings.readTrackingValue(trackedEvent);
        return value != null ? value : defaultTrackingValue();
    }

}
