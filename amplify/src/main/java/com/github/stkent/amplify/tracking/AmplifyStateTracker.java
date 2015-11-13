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

import com.github.stkent.amplify.AmplifyView;
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

public final class AmplifyStateTracker {

    // static fields

    private static AmplifyStateTracker sharedInstance;

    // instance fields

    private final Context applicationContext;
    private final List<IEnvironmentCheck> environmentRequirements = new ArrayList<>();
    private final PredicateMap<Long> lastEventTimePredicates;
    private final PredicateMap<Long> firstEventTimePredicates;
    private final PredicateMap<String> lastEventVersionPredicates;
    private final PredicateMap<Integer> totalEventCountPredicates;
    private final ISettings settings;
    private final ILogger logger;

    public static AmplifyStateTracker get(@NonNull final Context context) {
        return get(context, Settings.getSharedInstance(context), new Logger());
    }

    public static AmplifyStateTracker get(
            @NonNull final Context context,
            @NonNull final ISettings settings,
            @NonNull final ILogger logger) {
        synchronized (AmplifyStateTracker.class) {
            if (sharedInstance == null) {
                sharedInstance = new AmplifyStateTracker(context, settings, logger);
            }
        }

        return sharedInstance;
    }

    public AmplifyStateTracker setLogLevel(@NonNull final Logger.LogLevel logLevel) {
        logger.setLogLevel(logLevel);
        return this;
    }

    // constructors

    private AmplifyStateTracker(
            @NonNull final Context context,
            @NonNull final ISettings settings,
            @NonNull final ILogger logger) {
        this.applicationContext = context.getApplicationContext();
        this.settings = settings;
        this.logger = logger;
        this.lastEventTimePredicates = new PredicateMap<>(logger);
        this.firstEventTimePredicates = new PredicateMap<>(logger);
        this.lastEventVersionPredicates = new PredicateMap<>(logger);
        this.totalEventCountPredicates = new PredicateMap<>(logger);
    }

    // configuration methods

    public AmplifyStateTracker trackTotalEventCount(@NonNull final IEvent event, @NonNull final IEventCheck<Integer> predicate) {
        // todo: check for conflicts here
        performEventRelatedInitializationIfRequired(event);
        totalEventCountPredicates.trackEvent(event, predicate);
        return this;
    }

    public AmplifyStateTracker trackFirstEventTime(@NonNull final IEvent event, @NonNull final IEventCheck<Long> predicate) {
        // todo: check for conflicts here
        performEventRelatedInitializationIfRequired(event);
        firstEventTimePredicates.trackEvent(event, predicate);
        return this;
    }

    public AmplifyStateTracker trackLastEventTime(@NonNull final IEvent event, @NonNull final IEventCheck<Long> predicate) {
        // todo: check for conflicts here
        performEventRelatedInitializationIfRequired(event);
        lastEventTimePredicates.trackEvent(event, predicate);
        return this;
    }

    public AmplifyStateTracker trackLastEventVersion(@NonNull final IEvent event, @NonNull final IEventCheck<String> predicate) {
        // todo: check for conflicts here
        performEventRelatedInitializationIfRequired(event);
        lastEventVersionPredicates.trackEvent(event, predicate);
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
            final Integer cachedCount = settings.getTotalEventCount(event);
            final Integer updatedCount = cachedCount + 1;
            settings.setTotalEventCount(event, updatedCount);
        }

        if (firstEventTimePredicates.containsKey(event)) {
            final Long cachedTime = settings.getFirstEventTime(event);

            if (cachedTime == Long.MAX_VALUE) {
                final Long currentTime = System.currentTimeMillis();
                settings.setFirstEventTime(event, Math.min(cachedTime, currentTime));
            }
        }

        if (lastEventTimePredicates.containsKey(event)) {
            final Long currentTime = System.currentTimeMillis();
            settings.setLastEventTime(event, currentTime);
        }

        if (lastEventVersionPredicates.containsKey(event)) {
            try {
                final String currentVersion = TrackingUtils.getAppVersionName(applicationContext);
                settings.setLastEventVersion(event, currentVersion);
            } catch (final PackageManager.NameNotFoundException e) {
                logger.d("Could not read current app version name.");
            }
        }

        return this;
    }

    // query methods

    public void promptIfReady(@NonNull final AmplifyView amplifyView) {
        if (shouldAskForRating()) {
            amplifyView.show();
        }
    }

    public boolean shouldAskForRating() {
        return allEnvironmentRequirementsMet()
                && allTotalEventCountPredicatesAllowFeedbackPrompt()
                && allFirstEventTimePredicatesAllowFeedbackPrompt()
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

            final Integer totalEventCount = settings.getTotalEventCount(event);

            for (final IEventCheck<Integer> predicate : eventCheckSet.getValue()) {
                logger.d(event.getTrackingKey() + ": " + predicate.getStatusString(totalEventCount, applicationContext));

                if (predicate.shouldBlockFeedbackPrompt(totalEventCount, applicationContext)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean allFirstEventTimePredicatesAllowFeedbackPrompt() {
        for (final Map.Entry<IEvent, List<IEventCheck<Long>>> eventCheckSet : firstEventTimePredicates.entrySet()) {
            final IEvent event = eventCheckSet.getKey();

            final Long firstEventTime = settings.getFirstEventTime(event);

            for (final IEventCheck<Long> predicate : eventCheckSet.getValue()) {
                logger.d(event.getTrackingKey() + ": " + predicate.getStatusString(firstEventTime, applicationContext));

                if (predicate.shouldBlockFeedbackPrompt(firstEventTime, applicationContext)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean allLastEventTimePredicatesAllowFeedbackPrompt() {
        for (final Map.Entry<IEvent, List<IEventCheck<Long>>> eventCheckSet : lastEventTimePredicates.entrySet()) {
            final IEvent event = eventCheckSet.getKey();

            final Long lastEventTime = settings.getLastEventTime(event);

            for (final IEventCheck<Long> predicate : eventCheckSet.getValue()) {
                logger.d(event.getTrackingKey() + ": " + predicate.getStatusString(lastEventTime, applicationContext));

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

            final String lastEventVersion = settings.getLastEventVersion(event);

            if (lastEventVersion != null) {
                for (final IEventCheck<String> predicate : eventCheckSet.getValue()) {
                    logger.d(event.getTrackingKey() + ": " + predicate.getStatusString(lastEventVersion, applicationContext));

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
