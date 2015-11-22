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

import com.github.stkent.amplify.ILogger;
import com.github.stkent.amplify.Logger;
import com.github.stkent.amplify.tracking.checks.CooldownDaysCheck;
import com.github.stkent.amplify.tracking.checks.GooglePlayStoreIsAvailableCheck;
import com.github.stkent.amplify.tracking.checks.MaximumCountCheck;
import com.github.stkent.amplify.tracking.checks.VersionChangedCheck;
import com.github.stkent.amplify.tracking.checks.WarmUpDaysCheck;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentCheck;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentInfoProvider;
import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.IEventCheck;
import com.github.stkent.amplify.tracking.trackers.FirstTimeTracker;
import com.github.stkent.amplify.tracking.trackers.LastTimeTracker;
import com.github.stkent.amplify.tracking.trackers.LastVersionTracker;
import com.github.stkent.amplify.tracking.trackers.TotalCountTracker;
import com.github.stkent.amplify.views.AmplifyView;

import java.util.ArrayList;
import java.util.List;

public final class AmplifyStateTracker {

    // static fields

    private static AmplifyStateTracker sharedInstance;
    private static final int ONE_WEEK = 7;
    private static final int ONE_DAY = 1;

    // instance fields

    private final Context applicationContext;
    private final IEnvironmentInfoProvider applicationInfoProvider;
    private final List<IEnvironmentCheck> environmentRequirements = new ArrayList<>();
    private final LastTimeTracker lastTimeTracker;
    private final FirstTimeTracker firstTimeTracker;
    private final LastVersionTracker lastVersionTracker;
    private final TotalCountTracker totalCountTracker;
    private final ILogger logger;

    private boolean alwaysShow;

    public static AmplifyStateTracker defaultTracker(@NonNull final Context context) {
        return AmplifyStateTracker.get(context)
                .addEnvironmentCheck(new GooglePlayStoreIsAvailableCheck())
                .trackFirstEventTime(IntegratedEvent.APP_INSTALLED, new WarmUpDaysCheck(ONE_WEEK))
                .trackTotalEventCount(IntegratedEvent.USER_GAVE_POSITIVE_FEEDBACK, new MaximumCountCheck(ONE_DAY))
                .trackLastEventTime(IntegratedEvent.USER_GAVE_NEGATIVE_FEEDBACK, new CooldownDaysCheck(ONE_WEEK))
                .trackLastEventTime(IntegratedEvent.USER_DECLINED_FEEDBACK, new CooldownDaysCheck(ONE_WEEK))
                .trackLastEventVersion(IntegratedEvent.USER_DECLINED_FEEDBACK, new VersionChangedCheck())
                .trackLastEventTime(IntegratedEvent.USER_DECLINED_RATING, new CooldownDaysCheck(ONE_WEEK))
                .trackLastEventVersion(IntegratedEvent.USER_DECLINED_RATING, new VersionChangedCheck())
                .trackLastEventTime(IntegratedEvent.APP_CRASHED, new CooldownDaysCheck(ONE_WEEK))
                .trackLastEventVersion(IntegratedEvent.USER_GAVE_NEGATIVE_FEEDBACK, new VersionChangedCheck());
    }

    public static AmplifyStateTracker get(@NonNull final Context context) {
        return get(context, new Logger());
    }

    public static AmplifyStateTracker get(@NonNull final Context context, @NonNull final ILogger logger) {
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

    public AmplifyStateTracker setAlwaysShow(final boolean alwaysShow) {
        this.alwaysShow = alwaysShow;
        return this;
    }

    // constructors

    private AmplifyStateTracker(
            @NonNull final Context context,
            @NonNull final ILogger logger) {
        this.applicationContext = context.getApplicationContext();
        this.applicationInfoProvider = new EnvironmentInfoProvider(applicationContext);
        this.logger = logger;
        this.lastTimeTracker = new LastTimeTracker(logger, applicationContext);
        this.firstTimeTracker = new FirstTimeTracker(logger, applicationContext);
        this.lastVersionTracker = new LastVersionTracker(logger, applicationContext);
        this.totalCountTracker = new TotalCountTracker(logger, applicationContext);
    }

    // configuration methods

    public AmplifyStateTracker trackTotalEventCount(@NonNull final IEvent event, @NonNull final IEventCheck<Integer> eventCheck) {
        // todo: check for conflicts here
        performEventRelatedInitializationIfRequired(event);
        totalCountTracker.trackEvent(event, eventCheck);
        return this;
    }

    public AmplifyStateTracker trackFirstEventTime(@NonNull final IEvent event, @NonNull final IEventCheck<Long> eventCheck) {
        // todo: check for conflicts here
        performEventRelatedInitializationIfRequired(event);
        firstTimeTracker.trackEvent(event, eventCheck);
        return this;
    }

    public AmplifyStateTracker trackLastEventTime(@NonNull final IEvent event, @NonNull final IEventCheck<Long> eventCheck) {
        // todo: check for conflicts here
        performEventRelatedInitializationIfRequired(event);
        lastTimeTracker.trackEvent(event, eventCheck);
        return this;
    }

    public AmplifyStateTracker trackLastEventVersion(@NonNull final IEvent event, @NonNull final IEventCheck<String> eventCheck) {
        // todo: check for conflicts here
        performEventRelatedInitializationIfRequired(event);
        lastVersionTracker.trackEvent(event, eventCheck);
        return this;
    }

    public AmplifyStateTracker addEnvironmentCheck(@NonNull final IEnvironmentCheck requirement) {
        // todo: check for conflicts here
        environmentRequirements.add(requirement);
        return this;
    }

    // update methods

    public AmplifyStateTracker notifyEventTriggered(@NonNull final IEvent event) {
        logger.d("Triggered Event: " + event);
        totalCountTracker.notifyEventTriggered(event);
        firstTimeTracker.notifyEventTriggered(event);
        lastTimeTracker.notifyEventTriggered(event);
        lastVersionTracker.notifyEventTriggered(event);
        return this;
    }

    // query methods

    public void promptIfReady(@NonNull final AmplifyView amplifyView) {
        if (shouldAskForRating()) {
            amplifyView.show();
        }
    }

    public boolean shouldAskForRating() {
        return alwaysShow | (allEnvironmentRequirementsMet()
                & totalCountTracker.allowFeedbackPrompt()
                & firstTimeTracker.allowFeedbackPrompt()
                & lastTimeTracker.allowFeedbackPrompt()
                & lastVersionTracker.allowFeedbackPrompt());
    }

    // private implementation:

    private boolean allEnvironmentRequirementsMet() {
        for (final IEnvironmentCheck environmentRequirement : environmentRequirements) {
            if (!environmentRequirement.isMet(applicationInfoProvider)) {
                logger.d("Environment requirement not met: " + environmentRequirement);
                return false;
            }
        }

        return true;
    }

    private void performEventRelatedInitializationIfRequired(@NonNull final IEvent event) {
        if (isEventAlreadyTracked(event)) {
            return;
        }

        event.performRelatedInitialization(applicationContext, logger);
    }

    private boolean isEventAlreadyTracked(@NonNull final IEvent event) {
        return lastTimeTracker.containsEvent(event)
                || lastVersionTracker.containsEvent(event)
                || totalCountTracker.containsEvent(event)
                || firstTimeTracker.containsEvent(event);
    }
}
