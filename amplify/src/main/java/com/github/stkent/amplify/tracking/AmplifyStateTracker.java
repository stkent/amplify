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
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.Logger;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentCheck;
import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.IEventCheck;
import com.github.stkent.amplify.tracking.interfaces.ILogger;
import com.github.stkent.amplify.tracking.interfaces.ISettings;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class AmplifyStateTracker {

    // static fields

    private static AmplifyStateTracker sharedInstance;

    // instance fields

    private final Context applicationContext;
    private final List<IEnvironmentCheck> environmentRequirements = new ArrayList<>();
    private final Map<IEvent, List<IEventCheck<Long>>> lastEventTimePredicates = new ConcurrentHashMap<>();
    private final Map<IEvent, List<IEventCheck<String>>> lastEventVersionPredicates = new ConcurrentHashMap<>();
    private final Map<IEvent, List<IEventCheck<Integer>>> totalEventCountPredicates = new ConcurrentHashMap<>();
    private ISettings settings;
    private ILogger logger;

    public static AmplifyStateTracker get(@NonNull final Context context) {
        synchronized (AmplifyStateTracker.class) {
            if (sharedInstance == null) {
                sharedInstance = new AmplifyStateTracker(context);
            }
        }

        return sharedInstance;
    }

    public AmplifyStateTracker setLogLevel(@NonNull final Logger.LogLevel logLevel) {
        getLogger().setLogLevel(logLevel);
        return this;
    }

    // TODO: when writing tests, loosen the visibility and override this method to provide an
    // alternate implementation
    private ISettings getSettings() {
        if (settings == null) {
            settings = Settings.getSharedInstance(applicationContext);
        }

        return settings;
    }

    // TODO: when writing tests, loosen the visibility and override this method to provide an
    // alternate implementation
    private ILogger getLogger() {
        if (logger == null) {
            logger = new Logger();
        }

        return logger;
    }

    // constructors

    private AmplifyStateTracker(@NonNull final Context context) {
        this.applicationContext = context.getApplicationContext();
    }

    // configuration methods

    public AmplifyStateTracker trackTotalEventCount(@NonNull final IEvent event, @NonNull final IEventCheck<Integer> predicate) {
        // todo: check for conflicts here
        performEventRelatedInitializationIfRequired(event);

        if (!totalEventCountPredicates.containsKey(event)) {
            totalEventCountPredicates.put(event, new ArrayList<IEventCheck<Integer>>());
        }

        totalEventCountPredicates.get(event).add(predicate);
        getLogger().d(totalEventCountPredicates.get(event).toString());

        return this;
    }

    public AmplifyStateTracker trackLastEventTime(@NonNull final IEvent event, @NonNull final IEventCheck<Long> predicate) {
        // todo: check for conflicts here
        performEventRelatedInitializationIfRequired(event);

        if (!lastEventTimePredicates.containsKey(event)) {
            lastEventTimePredicates.put(event, new ArrayList<IEventCheck<Long>>());
        }

        lastEventTimePredicates.get(event).add(predicate);
        getLogger().d(lastEventTimePredicates.get(event).toString());

        return this;
    }

    public AmplifyStateTracker trackLastEventVersion(@NonNull final IEvent event, @NonNull final IEventCheck<String> predicate) {
        // todo: check for conflicts here
        performEventRelatedInitializationIfRequired(event);

        if (!lastEventVersionPredicates.containsKey(event)) {
            lastEventVersionPredicates.put(event, new ArrayList<IEventCheck<String>>());
        }

        lastEventVersionPredicates.get(event).add(predicate);
        getLogger().d(lastEventVersionPredicates.get(event).toString());

        return this;
    }

    public AmplifyStateTracker addEnvironmentCheck(@NonNull final IEnvironmentCheck requirement) {
        // todo: check for conflicts here
        environmentRequirements.add(requirement);
        return this;
    }

    // update methods

    public AmplifyStateTracker notifyEventTriggered(@NonNull final IEvent event) {
        if (totalEventCountPredicates.containsKey(event)) {
            final Integer cachedCount = getSettings().getTotalEventCount(event);
            final Integer updatedCount = cachedCount + 1;
            getSettings().setTotalEventCount(event, updatedCount);
        }

        if (lastEventTimePredicates.containsKey(event)) {
            final Long currentTime = System.currentTimeMillis();
            getSettings().setLastEventTime(event, currentTime);
        }

        if (lastEventVersionPredicates.containsKey(event)) {
            try {
                final String currentVersion = TrackingUtils.getAppVersionName(applicationContext);
                getSettings().setLastEventVersion(event, currentVersion);
            } catch (final PackageManager.NameNotFoundException e) {
                getLogger().d("Could not read current app version name.");
            }
        }

        return this;
    }

    // query methods

    public boolean shouldAskForRating() {
        return allEnvironmentRequirementsMet()
                && allTotalEventCountPredicatesAllowFeedbackPrompt()
                && allLastEventTimePredicatesAllowFeedbackPrompt()
                && allLastEventVersionPredicatesAllowFeedbackPrompt();
    }

    // private implementation:

    private boolean allEnvironmentRequirementsMet() {
        for (final IEnvironmentCheck environmentRequirement : environmentRequirements) {
            if (!environmentRequirement.isMet(applicationContext)) {
                return false;
            }
        }

        return true;
    }

    private boolean allTotalEventCountPredicatesAllowFeedbackPrompt() {
        for (final Map.Entry<IEvent, List<IEventCheck<Integer>>> eventCheckSet : totalEventCountPredicates.entrySet()) {
            final IEvent event = eventCheckSet.getKey();

            final Integer totalEventCount = getSettings().getTotalEventCount(event);

            for (final IEventCheck<Integer> predicate : eventCheckSet.getValue()) {
                getLogger().d(event.getTrackingKey() + ": " + predicate.getStatusString(totalEventCount, applicationContext));

                if (predicate.shouldBlockFeedbackPrompt(totalEventCount, applicationContext)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean allLastEventTimePredicatesAllowFeedbackPrompt() {
        for (final Map.Entry<IEvent, List<IEventCheck<Long>>> eventCheckSet : lastEventTimePredicates.entrySet()) {
            final IEvent event = eventCheckSet.getKey();

            final Long lastEventTime = getSettings().getLastEventTime(event);

            for (final IEventCheck<Long> predicate : eventCheckSet.getValue()) {
                getLogger().d(event.getTrackingKey() + ": " + predicate.getStatusString(lastEventTime, applicationContext));

                if (predicate.shouldBlockFeedbackPrompt(lastEventTime, applicationContext)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean allLastEventVersionPredicatesAllowFeedbackPrompt() {
        for (final Map.Entry<IEvent, List<IEventCheck<String>>> eventCheckSet : lastEventVersionPredicates.entrySet()) {
            final IEvent event = eventCheckSet.getKey();

            final String lastEventVersion = getSettings().getLastEventVersion(event);

            if (lastEventVersion != null) {
                for (final IEventCheck<String> predicate : eventCheckSet.getValue()) {
                    getLogger().d(event.getTrackingKey() + ": " + predicate.getStatusString(lastEventVersion, applicationContext));

                    if (predicate.shouldBlockFeedbackPrompt(lastEventVersion, applicationContext)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private void performEventRelatedInitializationIfRequired(@NonNull final IEvent event) {
        if (isEventAlreadyTracked(event)) {
            return;
        }

        event.performRelatedInitialization(applicationContext);
    }

    private boolean isEventAlreadyTracked(@NonNull final IEvent event) {
        final Set<IEvent> allTrackedEvents = new HashSet<>();

        allTrackedEvents.addAll(lastEventTimePredicates.keySet());
        allTrackedEvents.addAll(lastEventVersionPredicates.keySet());
        allTrackedEvents.addAll(totalEventCountPredicates.keySet());

        return allTrackedEvents.contains(event);
    }

}
