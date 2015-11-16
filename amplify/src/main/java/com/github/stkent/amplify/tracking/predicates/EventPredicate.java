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
package com.github.stkent.amplify.tracking.predicates;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.GenericSettings;
import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.IEventCheck;
import com.github.stkent.amplify.tracking.interfaces.ILogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by bobbake4 on 11/13/15.
 */
public abstract class EventPredicate<T> {

    private final ILogger logger;
    private final GenericSettings<T> settings;
    private final Context applicationContext;
    private final ConcurrentHashMap<IEvent, List<IEventCheck<T>>> internalMap;

    public abstract void eventTriggered(@NonNull final IEvent event);

    public EventPredicate(ILogger logger, GenericSettings<T> settings, Context applicationContext) {
        super();
        this.logger = logger;
        this.settings = settings;
        this.applicationContext = applicationContext;
        this.internalMap = new ConcurrentHashMap<>();
    }

    public void trackEvent(@NonNull final IEvent event, @NonNull final IEventCheck<T> predicate) {

        if (!containsEvent(event)) {
            internalMap.put(event, new ArrayList<IEventCheck<T>>());
        }

        internalMap.get(event).add(predicate);

        logger.d(internalMap.get(event).toString());
    }

    public boolean allowFeedbackPrompt() {

        for (final Map.Entry<IEvent, List<IEventCheck<T>>> eventCheckSet : internalMap.entrySet()) {
            final IEvent event = eventCheckSet.getKey();

            final T cachedEventValue = settings.getEventValue(event);

            for (final IEventCheck<T> predicate : eventCheckSet.getValue()) {
                logger.d(event.getTrackingKey() + ": " + predicate.getStatusString(cachedEventValue, applicationContext));

                if (predicate.shouldBlockFeedbackPrompt(cachedEventValue, applicationContext)) {
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

    protected Context getApplicationContext() {
        return applicationContext;
    }

    protected T getEventValue(@NonNull final IEvent event) {
        return settings.getEventValue(event);
    }

    protected void updateEventValue(@NonNull final IEvent event, T value) {
        settings.writeEventValue(event, value);
    }

}
