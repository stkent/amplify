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
package com.github.stkent.amplify.tracking.managers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.stkent.amplify.tracking.Amplify;
import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.IEventBasedRule;
import com.github.stkent.amplify.tracking.interfaces.IEventsManager;
import com.github.stkent.amplify.tracking.interfaces.ISettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseEventsManager<T> implements IEventsManager<T> {

    private static final String AMPLIFY_TRACKING_KEY_PREFIX = "AMPLIFY_";

    private final ISettings<T> settings;

    private final ConcurrentHashMap<IEvent, List<IEventBasedRule<T>>> internalMap;

    @NonNull
    protected abstract String getTrackedEventDimensionDescription();

    /**
     * This method should only be called if the associated event has occurred before.
     *
     * @param cachedEventValue the currently cached value for the associated event
     * @return a string representation of an associated event's tracking status, given the currently
     *         cached value for that event
     */
    @NonNull
    protected abstract String getEventTrackingStatusStringSuffix(
            @NonNull final T cachedEventValue);

    /**
     * @param cachedEventValue the existing cached value associated with the tracked event; null if
     *                         the event has never occurred before
     * @return a new value to replace the existing value in the cache
     */
    @NonNull
    protected abstract T getUpdatedTrackingValue(@Nullable final T cachedEventValue);

    protected BaseEventsManager(@NonNull final ISettings<T> settings) {
        this.settings = settings;
        this.internalMap = new ConcurrentHashMap<>();
    }

    @Override
    public void addEventBasedRule(
            @NonNull final IEvent event,
            @NonNull final IEventBasedRule<T> rule) {

        if (!isTrackingEvent(event)) {
            internalMap.put(event, new ArrayList<IEventBasedRule<T>>());
        }

        internalMap.get(event).add(rule);

        Amplify.getLogger().d(
                "Registered " + rule.getDescription() + " for event " + event.getTrackingKey());
    }

    @Override
    public void notifyEventTriggered(@NonNull final IEvent event) {
        if (isTrackingEvent(event)) {
            final T cachedTrackingValue = getCachedTrackingValue(event);
            final T updatedTrackingValue = getUpdatedTrackingValue(cachedTrackingValue);

            if (cachedTrackingValue == null) {
                Amplify.getLogger().d(
                        "Setting " + getTrackedEventDimensionDescription().toLowerCase(Locale.US)
                      + " of " + event.getTrackingKey()
                      + " event to " + updatedTrackingValue);
            } else if (!updatedTrackingValue.equals(cachedTrackingValue)) {
                Amplify.getLogger().d(
                        "Updating " + getTrackedEventDimensionDescription().toLowerCase(Locale.US)
                      + " of " + event.getTrackingKey()
                      + " event from " + cachedTrackingValue
                      + " to " + updatedTrackingValue);
            }

            settings.writeTrackingValue(getTrackingKey(event), updatedTrackingValue);
        }
    }

    @Override
    public boolean shouldAllowFeedbackPrompt() {
        boolean result = true;

        for (final Map.Entry<IEvent, List<IEventBasedRule<T>>> rules : internalMap.entrySet()) {
            final IEvent event = rules.getKey();

            for (final IEventBasedRule<T> rule : rules.getValue()) {
                final T cachedEventValue = getCachedTrackingValue(event);

                if (cachedEventValue != null) {
                    Amplify.getLogger().d(
                            event.getTrackingKey()
                          + " event "
                          + getEventTrackingStatusStringSuffix(cachedEventValue));

                    if (!rule.shouldAllowFeedbackPrompt(cachedEventValue)) {
                        logPromptBlockedMessage(rule, event);
                        result = false;
                    }
                } else {
                    Amplify.getLogger().d(
                            "No tracked value for "
                          + getTrackedEventDimensionDescription().toLowerCase(Locale.US)
                          + " of "
                          + event.getTrackingKey()
                          + " event");

                    if (!rule.shouldAllowFeedbackPromptByDefault()) {
                        logPromptBlockedMessage(rule, event);
                        result = false;
                    }
                }
            }
        }

        return result;
    }

    private boolean isTrackingEvent(@NonNull final IEvent event) {
        return internalMap.containsKey(event);
    }

    private String getTrackingKey(@NonNull final IEvent event) {
        return AMPLIFY_TRACKING_KEY_PREFIX
                + event.getTrackingKey()
                + "_"
                + getTrackingKeySuffix().toUpperCase();
    }

    /**
     * @return a key that uniquely identifies this event tracker within the embedding application
     */
    @NonNull
    private String getTrackingKeySuffix() {
        return getTrackedEventDimensionDescription()
                .trim()
                .toUpperCase(Locale.US)
                .replaceAll("\\s+", "_");
    }

    @Nullable
    private T getCachedTrackingValue(@NonNull final IEvent event) {
        return settings.readTrackingValue(getTrackingKey(event));
    }

    private void logPromptBlockedMessage(
            @NonNull final IEventBasedRule<T> rule,
            @NonNull final IEvent event) {

        Amplify.getLogger().d(
                "Blocking feedback because of " + rule.getDescription()
              + " associated with " + event.getTrackingKey() + " event");
    }

}
