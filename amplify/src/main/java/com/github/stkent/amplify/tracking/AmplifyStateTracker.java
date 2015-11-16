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
import android.support.annotation.NonNull;

import com.github.stkent.amplify.AmplifyView;
import com.github.stkent.amplify.Logger;
import com.github.stkent.amplify.tracking.checks.CooldownDaysCheck;
import com.github.stkent.amplify.tracking.checks.GooglePlayStoreIsAvailableCheck;
import com.github.stkent.amplify.tracking.checks.MaximumCountCheck;
import com.github.stkent.amplify.tracking.checks.VersionChangedCheck;
import com.github.stkent.amplify.tracking.checks.WarmUpDaysCheck;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentCheck;
import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.IEventCheck;
import com.github.stkent.amplify.tracking.interfaces.ILogger;
import com.github.stkent.amplify.tracking.predicates.FirstTimeMap;
import com.github.stkent.amplify.tracking.predicates.LastTimeMap;
import com.github.stkent.amplify.tracking.predicates.LastVersionMap;
import com.github.stkent.amplify.tracking.predicates.TotalCountMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class AmplifyStateTracker {

    // static fields

    private static AmplifyStateTracker sharedInstance;
    private static final int ONE_WEEK = 7;
    private static final int ONE_DAY = 1;

    // instance fields

    private final Context applicationContext;
    private final List<IEnvironmentCheck> environmentRequirements = new ArrayList<>();
    private final LastTimeMap lastEventTimePredicates;
    private final FirstTimeMap firstEventTimePredicates;
    private final LastVersionMap lastEventVersionPredicates;
    private final TotalCountMap totalEventCountPredicates;
    private final ILogger logger;

    public static AmplifyStateTracker get(@NonNull final Context context) {
        return get(context, new Logger());
    }

    public static AmplifyStateTracker get(
            @NonNull final Context context,
            @NonNull final ILogger logger) {
        synchronized (AmplifyStateTracker.class) {
            if (sharedInstance == null) {
                sharedInstance = new AmplifyStateTracker(context, logger);
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
            @NonNull final ILogger logger) {
        this.applicationContext = context.getApplicationContext();
        this.logger = logger;
        this.lastEventTimePredicates = new LastTimeMap(logger, applicationContext);
        this.firstEventTimePredicates = new FirstTimeMap(logger, applicationContext);
        this.lastEventVersionPredicates = new LastVersionMap(logger, applicationContext);
        this.totalEventCountPredicates = new TotalCountMap(logger, applicationContext);
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
        totalEventCountPredicates.eventTriggered(event);
        firstEventTimePredicates.eventTriggered(event);
        lastEventTimePredicates.eventTriggered(event);
        lastEventVersionPredicates.eventTriggered(event);
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
                && totalEventCountPredicates.allowFeedbackPrompt()
                && firstEventTimePredicates.allowFeedbackPrompt()
                && lastEventTimePredicates.allowFeedbackPrompt()
                && lastEventVersionPredicates.allowFeedbackPrompt();
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

    public AmplifyStateTracker defaultSettings() {
        return this.addEnvironmentCheck(new GooglePlayStoreIsAvailableCheck())
                .trackLastEventTime(IntegratedEvent.APP_INSTALLED, new WarmUpDaysCheck(ONE_WEEK))
                .trackTotalEventCount(IntegratedEvent.USER_GAVE_POSITIVE_FEEDBACK, new MaximumCountCheck(ONE_DAY))
                .trackLastEventTime(IntegratedEvent.USER_GAVE_NEGATIVE_FEEDBACK, new CooldownDaysCheck(ONE_WEEK))
                .trackLastEventTime(IntegratedEvent.USER_DECLINED_FEEDBACK, new CooldownDaysCheck(ONE_WEEK))
                .trackLastEventVersion(IntegratedEvent.USER_DECLINED_FEEDBACK, new VersionChangedCheck())
                .trackLastEventTime(IntegratedEvent.USER_DECLINED_RATING, new CooldownDaysCheck(ONE_WEEK))
                .trackLastEventVersion(IntegratedEvent.USER_DECLINED_RATING, new VersionChangedCheck())
                .trackLastEventTime(IntegratedEvent.APP_CRASHED, new CooldownDaysCheck(ONE_WEEK))
                .trackLastEventVersion(IntegratedEvent.USER_GAVE_NEGATIVE_FEEDBACK, new VersionChangedCheck());
    }
}
