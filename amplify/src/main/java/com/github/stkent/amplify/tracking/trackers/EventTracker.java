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
import android.support.annotation.VisibleForTesting;

import com.github.stkent.amplify.ILogger;
import com.github.stkent.amplify.tracking.interfaces.IApplicationInfoProvider;
import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.IEventCheck;
import com.github.stkent.amplify.tracking.interfaces.ISettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class EventTracker<T> {

    private static final String AMPLIFY_TRACKING_KEY_PREFIX = "AMPLIFY_";

    protected final ILogger logger;
    protected final IApplicationInfoProvider applicationInfoProvider;

    private final ISettings<T> settings;
    private final ConcurrentHashMap<IEvent, List<IEventCheck<T>>> internalMap;

    /**
     * @return a key that uniquely identifies this event tracker within the
     *         consuming application
     */
    @NonNull
    protected abstract String getTrackingKeySuffix();

    @NonNull
    protected abstract T defaultTrackingValue();

    @NonNull
    protected abstract T getUpdatedTrackingValue(@NonNull final T cachedEventValue);

    public EventTracker(
            @NonNull final ILogger logger,
            @NonNull final ISettings<T> settings,
            @NonNull final IApplicationInfoProvider applicationInfoProvider) {
        this.logger = logger;
        this.settings = settings;
        this.applicationInfoProvider = applicationInfoProvider;
        this.internalMap = new ConcurrentHashMap<>();
    }

    public void trackEvent(@NonNull final IEvent event, @NonNull final IEventCheck<T> eventCheck) {
        if (!containsEvent(event)) {
            internalMap.put(event, new ArrayList<IEventCheck<T>>());
        }

        internalMap.get(event).add(eventCheck);

        logger.d(internalMap.get(event).toString());
    }

    public void notifyEventTriggered(@NonNull final IEvent event) {
        if (containsEvent(event)) {

            final T cachedTrackingValue = getCachedTrackingValue(event);
            final T updatedTrackingValue = getUpdatedTrackingValue(cachedTrackingValue);

            if (!updatedTrackingValue.equals(cachedTrackingValue)) {
                logger.d(IEventCheck.class.getSimpleName()
                        + " updating event value from: "
                        + cachedTrackingValue
                        + " to "
                        + updatedTrackingValue);
            }

            settings.writeTrackingValue(getTrackingKey(event), updatedTrackingValue);
        }
    }

    public boolean allowFeedbackPrompt() {

        for (final Map.Entry<IEvent, List<IEventCheck<T>>> eventCheckSet : internalMap.entrySet()) {
            final IEvent event = eventCheckSet.getKey();

            for (final IEventCheck<T> eventCheck : eventCheckSet.getValue()) {
                final T cachedEventValue = getCachedTrackingValue(event);

                logger.d(getTrackingKey(event) + ": " + eventCheck.getStatusString(cachedEventValue, applicationInfoProvider));

                if (eventCheck.shouldBlockFeedbackPrompt(cachedEventValue, applicationInfoProvider)) {
                    logger.d("Blocking feedback for event: " + event + " because of check: " + eventCheck);
                    return false;
                }
            }
        }

        return true;
    }

    public boolean containsEvent(@NonNull final IEvent event) {
        return internalMap.containsKey(event);
    }

    @VisibleForTesting
    protected String getTrackingKey(@NonNull final IEvent event) {
        return AMPLIFY_TRACKING_KEY_PREFIX
                + event.getTrackingKey()
                + "_"
                + this.getTrackingKeySuffix().toUpperCase();
    }

    private T getCachedTrackingValue(@NonNull final IEvent event) {
        T value = settings.readTrackingValue(getTrackingKey(event));
        return value != null ? value : defaultTrackingValue();
    }

}
