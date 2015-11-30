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
import com.github.stkent.amplify.tracking.interfaces.IApplicationInfoProvider;
import com.github.stkent.amplify.tracking.interfaces.IEventCheck;
import com.github.stkent.amplify.tracking.interfaces.IEventManager;
import com.github.stkent.amplify.tracking.interfaces.IPublicEvent;
import com.github.stkent.amplify.tracking.interfaces.ISettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseEventManager<T> implements IEventManager<T> {

    private static final String AMPLIFY_TRACKING_KEY_PREFIX = "AMPLIFY_";

    private final ILogger logger;
    private final IApplicationInfoProvider applicationInfoProvider;
    private final ISettings<T> settings;
    private final ConcurrentHashMap<IPublicEvent, List<IEventCheck<T>>> internalMap;

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

    public BaseEventManager(
            @NonNull final ILogger logger,
            @NonNull final ISettings<T> settings,
            @NonNull final IApplicationInfoProvider applicationInfoProvider) {
        this.logger = logger;
        this.settings = settings;
        this.applicationInfoProvider = applicationInfoProvider;
        this.internalMap = new ConcurrentHashMap<>();
    }

    @Override
    public void trackEvent(@NonNull final IPublicEvent event, @NonNull final IEventCheck<T> eventCheck) {
        if (!containsEvent(event)) {
            internalMap.put(event, new ArrayList<IEventCheck<T>>());
        }

        internalMap.get(event).add(eventCheck);

        logger.d(internalMap.get(event).toString());
    }

    @Override
    public void notifyEventTriggered(@NonNull final IPublicEvent event) {
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

    @Override
    public boolean shouldAllowFeedbackPrompt() {

        for (final Map.Entry<IPublicEvent, List<IEventCheck<T>>> eventCheckSet : internalMap.entrySet()) {
            final IPublicEvent event = eventCheckSet.getKey();

            for (final IEventCheck<T> eventCheck : eventCheckSet.getValue()) {
                final T cachedEventValue = getCachedTrackingValue(event);

                logger.d(getTrackingKey(event) + ": " + eventCheck.getStatusString(cachedEventValue, applicationInfoProvider));

                if (eventCheck.shouldAllowFeedbackPrompt(cachedEventValue, applicationInfoProvider)) {
                    logger.d("Blocking feedback for event: " + event + " because of check: " + eventCheck);
                    return false;
                }
            }
        }

        return true;
    }

    public boolean containsEvent(@NonNull final IPublicEvent event) {
        return internalMap.containsKey(event);
    }

    protected ILogger getLogger() {
        return logger;
    }

    protected IApplicationInfoProvider getApplicationInfoProvider() {
        return applicationInfoProvider;
    }

    private String getTrackingKey(@NonNull final IPublicEvent event) {
        return AMPLIFY_TRACKING_KEY_PREFIX
                + event.getTrackingKey()
                + "_"
                + this.getTrackingKeySuffix().toUpperCase();
    }

    private T getCachedTrackingValue(@NonNull final IPublicEvent event) {
        T value = settings.readTrackingValue(getTrackingKey(event));
        return value != null ? value : defaultTrackingValue();
    }

}
