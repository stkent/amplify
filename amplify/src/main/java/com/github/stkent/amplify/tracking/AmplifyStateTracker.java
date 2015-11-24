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

import com.github.stkent.amplify.AmplifyLogger;
import com.github.stkent.amplify.tracking.checks.CooldownDaysCheck;
import com.github.stkent.amplify.tracking.checks.GooglePlayStoreIsAvailableCheck;
import com.github.stkent.amplify.tracking.checks.MaximumCountCheck;
import com.github.stkent.amplify.tracking.checks.VersionChangedCheck;
import com.github.stkent.amplify.tracking.checks.WarmUpDaysCheck;
import com.github.stkent.amplify.tracking.initializers.ExceptionHandlingInitializer;
import com.github.stkent.amplify.tracking.initializers.ImmediateEventTriggerInitializer;
import com.github.stkent.amplify.tracking.interfaces.IAmplifyStateTracker;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentCheck;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentInfoProvider;
import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.IEventCheck;
import com.github.stkent.amplify.tracking.interfaces.ITrackingInitializer;
import com.github.stkent.amplify.tracking.trackers.FirstTimeTracker;
import com.github.stkent.amplify.tracking.trackers.LastTimeTracker;
import com.github.stkent.amplify.tracking.trackers.LastVersionTracker;
import com.github.stkent.amplify.tracking.trackers.TotalCountTracker;
import com.github.stkent.amplify.views.AmplifyView;

import java.util.ArrayList;
import java.util.List;

public final class AmplifyStateTracker implements IAmplifyStateTracker {

    // static fields

    private static IAmplifyStateTracker sharedInstance;
    private static final int ONE_WEEK = 7;
    private static final int ONE_DAY = 1;

    // instance fields

    private final IEnvironmentInfoProvider applicationInfoProvider;
    private final List<IEnvironmentCheck> environmentRequirements = new ArrayList<>();
    private final LastTimeTracker lastTimeTracker;
    private final FirstTimeTracker firstTimeTracker;
    private final LastVersionTracker lastVersionTracker;
    private final TotalCountTracker totalCountTracker;

    private boolean alwaysShow;

    public static IAmplifyStateTracker get(@NonNull final Context context) {
        synchronized (AmplifyStateTracker.class) {
            if (sharedInstance == null) {
                sharedInstance = new AmplifyStateTracker(context);
            }
        }

        return sharedInstance;
    }

    // constructors

    private AmplifyStateTracker(
            @NonNull final Context context) {
        final Context applicationContext = context.getApplicationContext();
        this.applicationInfoProvider = new EnvironmentInfoProvider(applicationContext);
        this.lastTimeTracker = new LastTimeTracker(applicationContext);
        this.firstTimeTracker = new FirstTimeTracker(applicationContext);
        this.lastVersionTracker = new LastVersionTracker(applicationContext);
        this.totalCountTracker = new TotalCountTracker(applicationContext);
    }

    // configuration methods

    @Override
    public IAmplifyStateTracker configureWithDefaults() {
        return this
                .addEnvironmentCheck(new GooglePlayStoreIsAvailableCheck())
                .trackFirstEventTime(IntegratedEvent.APP_INSTALLED, new WarmUpDaysCheck(ONE_WEEK), new ImmediateEventTriggerInitializer())
                .trackTotalEventCount(IntegratedEvent.USER_GAVE_POSITIVE_FEEDBACK, new MaximumCountCheck(ONE_DAY))
                .trackLastEventTime(IntegratedEvent.USER_GAVE_CRITICAL_FEEDBACK, new CooldownDaysCheck(ONE_WEEK))
                .trackLastEventTime(IntegratedEvent.USER_DECLINED_CRITICAL_FEEDBACK, new CooldownDaysCheck(ONE_WEEK))
                .trackLastEventVersion(IntegratedEvent.USER_DECLINED_CRITICAL_FEEDBACK, new VersionChangedCheck())
                .trackLastEventTime(IntegratedEvent.USER_DECLINED_POSITIVE_FEEDBACK, new CooldownDaysCheck(ONE_WEEK))
                .trackLastEventVersion(IntegratedEvent.USER_DECLINED_POSITIVE_FEEDBACK, new VersionChangedCheck())
                .trackLastEventTime(IntegratedEvent.APP_CRASHED, new CooldownDaysCheck(ONE_WEEK), new ExceptionHandlingInitializer())
                .trackLastEventVersion(IntegratedEvent.USER_GAVE_CRITICAL_FEEDBACK, new VersionChangedCheck());
    }

    @Override
    public IAmplifyStateTracker setAlwaysShow(final boolean alwaysShow) {
        this.alwaysShow = alwaysShow;
        return this;
    }

    @Override
    public IAmplifyStateTracker trackTotalEventCount(@NonNull final IEvent event, @NonNull final IEventCheck<Integer> eventCheck) {
        totalCountTracker.trackEvent(event, eventCheck);
        return this;
    }

    @Override
    public IAmplifyStateTracker trackTotalEventCount(
            @NonNull final IEvent event,
            @NonNull final IEventCheck<Integer> eventCheck,
            @NonNull final ITrackingInitializer trackingInitializer) {
        trackTotalEventCount(event, eventCheck);

        if (!totalCountTracker.containsEvent(event)) {
            trackingInitializer.initialize(this, event);
        }

        return this;
    }

    @Override
    public IAmplifyStateTracker trackFirstEventTime(@NonNull final IEvent event, @NonNull final IEventCheck<Long> eventCheck) {
        firstTimeTracker.trackEvent(event, eventCheck);
        return this;
    }

    @Override
    public IAmplifyStateTracker trackFirstEventTime(
            @NonNull final IEvent event,
            @NonNull final IEventCheck<Long> eventCheck,
            @NonNull final ITrackingInitializer trackingInitializer) {
        trackFirstEventTime(event, eventCheck);

        if (!firstTimeTracker.containsEvent(event)) {
            trackingInitializer.initialize(this, event);
        }

        return this;
    }

    @Override
    public IAmplifyStateTracker trackLastEventTime(@NonNull final IEvent event, @NonNull final IEventCheck<Long> eventCheck) {
        lastTimeTracker.trackEvent(event, eventCheck);
        return this;
    }

    @Override
    public IAmplifyStateTracker trackLastEventTime(
            @NonNull final IEvent event,
            @NonNull final IEventCheck<Long> eventCheck,
            @NonNull final ITrackingInitializer trackingInitializer) {
        trackLastEventTime(event, eventCheck);

        if (!lastTimeTracker.containsEvent(event)) {
            trackingInitializer.initialize(this, event);
        }

        return this;
    }

    @Override
    public IAmplifyStateTracker trackLastEventVersion(@NonNull final IEvent event, @NonNull final IEventCheck<String> eventCheck) {
        lastVersionTracker.trackEvent(event, eventCheck);
        return this;
    }

    @Override
    public IAmplifyStateTracker trackLastEventVersion(
            @NonNull final IEvent event,
            @NonNull final IEventCheck<String> eventCheck,
            @NonNull final ITrackingInitializer trackingInitializer) {
        trackLastEventVersion(event, eventCheck);

        if (!lastVersionTracker.containsEvent(event)) {
            trackingInitializer.initialize(this, event);
        }

        return this;
    }

    @Override
    public IAmplifyStateTracker addEnvironmentCheck(@NonNull final IEnvironmentCheck requirement) {
        environmentRequirements.add(requirement);
        return this;
    }

    // update methods

    @Override
    public IAmplifyStateTracker notifyEventTriggered(@NonNull final IEvent event) {
        AmplifyLogger.getLogger().d("Triggered Event: " + event);
        totalCountTracker.notifyEventTriggered(event);
        firstTimeTracker.notifyEventTriggered(event);
        lastTimeTracker.notifyEventTriggered(event);
        lastVersionTracker.notifyEventTriggered(event);
        return this;
    }

    // query methods

    @Override
    public void promptIfReady(@NonNull final AmplifyView amplifyView) {
        if (shouldAskForRating()) {
            amplifyView.show();
        }
    }

    @Override
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
                AmplifyLogger.getLogger().d("Environment requirement not met: " + environmentRequirement);
                return false;
            }
        }

        return true;
    }

}
