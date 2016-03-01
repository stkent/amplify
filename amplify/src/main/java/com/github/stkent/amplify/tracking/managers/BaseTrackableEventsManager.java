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

import com.github.stkent.amplify.ILogger;
import com.github.stkent.amplify.tracking.interfaces.IEventBasedRule;
import com.github.stkent.amplify.tracking.interfaces.ITrackableEvent;
import com.github.stkent.amplify.tracking.interfaces.ITrackableEventsManager;
import com.github.stkent.amplify.tracking.interfaces.ISettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseTrackableEventsManager<T> implements ITrackableEventsManager<T> {

    private static final String AMPLIFY_TRACKING_KEY_PREFIX = "AMPLIFY_";

    private final ILogger logger;
    private final ISettings<T> settings;
    private final ConcurrentHashMap<ITrackableEvent, List<IEventBasedRule<T>>> internalMap;

    /**
     * @return a key that uniquely identifies this event tracker within the embedding application
     */
    @NonNull
    protected abstract String getTrackingKeySuffix();

    @NonNull
    protected abstract T defaultTrackingValue();

    @NonNull
    protected abstract T getUpdatedTrackingValue(@NonNull final T cachedEventValue);

    protected BaseTrackableEventsManager(
            @NonNull final ILogger logger,
            @NonNull final ISettings<T> settings) {
        this.logger = logger;
        this.settings = settings;
        this.internalMap = new ConcurrentHashMap<>();
    }

    @Override
    public void addEventBasedRule(@NonNull final ITrackableEvent trackableEvent, @NonNull final IEventBasedRule<T> rule) {
        if (!isTrackingEvent(trackableEvent)) {
            internalMap.put(trackableEvent, new ArrayList<IEventBasedRule<T>>());
        }

        internalMap.get(trackableEvent).add(rule);

        logger.d(internalMap.get(trackableEvent).toString());
    }

    @Override
    public void notifyEventTriggered(@NonNull final ITrackableEvent trackableEvent) {
        if (isTrackingEvent(trackableEvent)) {

            final T cachedTrackingValue = getCachedTrackingValue(trackableEvent);
            final T updatedTrackingValue = getUpdatedTrackingValue(cachedTrackingValue);

            if (!updatedTrackingValue.equals(cachedTrackingValue)) {
                logger.d(IEventBasedRule.class.getSimpleName()
                        + " updating event value from: "
                        + cachedTrackingValue
                        + " to "
                        + updatedTrackingValue);
            }

            settings.writeTrackingValue(getTrackingKey(trackableEvent), updatedTrackingValue);
        }
    }

    @Override
    public boolean shouldAllowFeedbackPrompt() {

        for (final Map.Entry<ITrackableEvent, List<IEventBasedRule<T>>> eventCheckSet : internalMap.entrySet()) {
            final ITrackableEvent event = eventCheckSet.getKey();

            for (final IEventBasedRule<T> eventCheck : eventCheckSet.getValue()) {
                final T cachedEventValue = getCachedTrackingValue(event);

                logger.d(getTrackingKey(event) + ": " + eventCheck.getStatusString(cachedEventValue));

                if (!eventCheck.shouldAllowFeedbackPrompt(cachedEventValue)) {
                    logger.d("Blocking feedback for event: " + event + " because of check: " + eventCheck);
                    return false;
                }
            }
        }

        return true;
    }

    protected ILogger getLogger() {
        return logger;
    }

    private boolean isTrackingEvent(@NonNull final ITrackableEvent event) {
        return internalMap.containsKey(event);
    }

    private String getTrackingKey(@NonNull final ITrackableEvent event) {
        return AMPLIFY_TRACKING_KEY_PREFIX
                + event.getTrackingKey()
                + "_"
                + this.getTrackingKeySuffix().toUpperCase();
    }

    private T getCachedTrackingValue(@NonNull final ITrackableEvent event) {
        T value = settings.readTrackingValue(getTrackingKey(event));
        return value != null ? value : defaultTrackingValue();
    }

}
