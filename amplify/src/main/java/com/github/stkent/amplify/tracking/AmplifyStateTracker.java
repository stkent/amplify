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
import com.github.stkent.amplify.tracking.interfaces.IAmplifyStateTracker;
import com.github.stkent.amplify.tracking.interfaces.IApplicationVersionNameProvider;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentCheck;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentInfoProvider;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentCheckManager;
import com.github.stkent.amplify.tracking.interfaces.IEventCheck;
import com.github.stkent.amplify.tracking.interfaces.IPublicEvent;
import com.github.stkent.amplify.tracking.managers.EnvironmentCheckManager;
import com.github.stkent.amplify.tracking.managers.FirstEventTimesManager;
import com.github.stkent.amplify.tracking.managers.LastEventTimesManager;
import com.github.stkent.amplify.tracking.managers.LastEventVersionsManager;
import com.github.stkent.amplify.tracking.managers.TotalEventCountsManager;
import com.github.stkent.amplify.views.AmplifyView;

public final class AmplifyStateTracker implements IAmplifyStateTracker {

    // static fields

    private static IAmplifyStateTracker sharedInstance;
    private static final int ONE_WEEK = 7;
    private static final int ONE_DAY = 1;

    // instance fields

    private final Context applicationContext;
    private final IApplicationVersionNameProvider applicationVersionNameProvider;
    private final IEnvironmentInfoProvider environmentInfoProvider;
    private final IEnvironmentCheckManager environmentManager;
    private final FirstEventTimesManager firstEventTimesManager;
    private final LastEventTimesManager lastEventTimesManager;
    private final LastEventVersionsManager lastEventVersionsManager;
    private final TotalEventCountsManager totalEventCountsManager;
    private final ILogger logger;

    private boolean alwaysShow;

    public static IAmplifyStateTracker get(@NonNull final Context context) {
        return get(context, new Logger());
    }

    public static IAmplifyStateTracker get(@NonNull final Context context, @NonNull final ILogger logger) {
        synchronized (AmplifyStateTracker.class) {
            if (sharedInstance == null) {
                sharedInstance = new AmplifyStateTracker(context, logger);
            }
        }

        return sharedInstance;
    }

    // constructors

    private AmplifyStateTracker(
            @NonNull final Context context,
            @NonNull final ILogger logger) {
        this.applicationContext = context.getApplicationContext();
        this.applicationVersionNameProvider = new ApplicationVersionNameProvider(applicationContext);
        this.environmentInfoProvider = new EnvironmentInfoProvider(applicationContext);
        this.environmentManager = new EnvironmentCheckManager(logger, environmentInfoProvider);
        this.firstEventTimesManager = new FirstEventTimesManager(logger, applicationContext);
        this.lastEventTimesManager = new LastEventTimesManager(logger, applicationContext);
        this.lastEventVersionsManager = new LastEventVersionsManager(logger, applicationContext);
        this.totalEventCountsManager = new TotalEventCountsManager(logger, applicationContext);
        this.logger = logger;
    }

    // configuration methods

    @Override
    public IAmplifyStateTracker configureWithDefaults() {
        return this
                .addEnvironmentCheck(new GooglePlayStoreIsAvailableCheck())
                .addInitialAppInstallTimeCheck(new WarmUpDaysCheck(ONE_WEEK))
                .addLastCrashTimeCheck(new CooldownDaysCheck(ONE_WEEK))
                .trackTotalEventCount(AmplifyViewEvent.USER_GAVE_POSITIVE_FEEDBACK, new MaximumCountCheck(ONE_DAY))
                .trackLastEventTime(AmplifyViewEvent.USER_GAVE_CRITICAL_FEEDBACK, new CooldownDaysCheck(ONE_WEEK))
                .trackLastEventTime(AmplifyViewEvent.USER_DECLINED_CRITICAL_FEEDBACK, new CooldownDaysCheck(ONE_WEEK))
                .trackLastEventVersion(AmplifyViewEvent.USER_DECLINED_CRITICAL_FEEDBACK, new VersionChangedCheck(applicationVersionNameProvider))
                .trackLastEventTime(AmplifyViewEvent.USER_DECLINED_POSITIVE_FEEDBACK, new CooldownDaysCheck(ONE_WEEK))
                .trackLastEventVersion(AmplifyViewEvent.USER_DECLINED_POSITIVE_FEEDBACK, new VersionChangedCheck(applicationVersionNameProvider))
                .trackLastEventVersion(AmplifyViewEvent.USER_GAVE_CRITICAL_FEEDBACK, new VersionChangedCheck(applicationVersionNameProvider));
    }

    @Override
    public IAmplifyStateTracker setLogLevel(@NonNull final Logger.LogLevel logLevel) {
        logger.setLogLevel(logLevel);
        return this;
    }

    @Override
    public IAmplifyStateTracker setAlwaysShow(final boolean alwaysShow) {
        this.alwaysShow = alwaysShow;
        return this;
    }

    @Override
    public IAmplifyStateTracker addInitialAppInstallTimeCheck(@NonNull final IEventCheck<Long> eventCheck) {
        // fixme: fill this in
        return this;
    }

    @Override
    public IAmplifyStateTracker addLastAppUpdateTimeCheck(@NonNull final IEventCheck<Long> eventCheck) {
        // fixme: fill this in
        return this;
    }

    @Override
    public IAmplifyStateTracker addLastCrashTimeCheck(@NonNull final IEventCheck<Long> eventCheck) {
        // fixme: fill this in
        return this;
    }

    @Override
    public IAmplifyStateTracker trackTotalEventCount(@NonNull final IPublicEvent event, @NonNull final IEventCheck<Integer> eventCheck) {
        totalEventCountsManager.trackEvent(event, eventCheck);
        return this;
    }

    @Override
    public IAmplifyStateTracker trackFirstEventTime(@NonNull final IPublicEvent event, @NonNull final IEventCheck<Long> eventCheck) {
        firstEventTimesManager.trackEvent(event, eventCheck);
        return this;
    }

    @Override
    public IAmplifyStateTracker trackLastEventTime(@NonNull final IPublicEvent event, @NonNull final IEventCheck<Long> eventCheck) {
        lastEventTimesManager.trackEvent(event, eventCheck);
        return this;
    }

    @Override
    public IAmplifyStateTracker trackLastEventVersion(@NonNull final IPublicEvent event, @NonNull final IEventCheck<String> eventCheck) {
        lastEventVersionsManager.trackEvent(event, eventCheck);
        return this;
    }

    @Override
    public IAmplifyStateTracker addEnvironmentCheck(@NonNull final IEnvironmentCheck environmentCheck) {
        environmentManager.addEnvironmentCheck(environmentCheck);
        return this;
    }

    // update methods

    @Override
    public IAmplifyStateTracker notifyEventTriggered(@NonNull final IPublicEvent event) {
        logger.d("Triggered Event: " + event);
        totalEventCountsManager.notifyEventTriggered(event);
        firstEventTimesManager.notifyEventTriggered(event);
        lastEventTimesManager.notifyEventTriggered(event);
        lastEventVersionsManager.notifyEventTriggered(event);
        return this;
    }

    // query methods

    @Override
    public void promptIfReady(@NonNull final AmplifyView amplifyView) {
        if (shouldAskForRating()) {
            amplifyView.injectDependencies(this, logger);
            amplifyView.show();
        }
    }

    @Override
    public boolean shouldAskForRating() {
        return alwaysShow | (
                  environmentManager.shouldAllowFeedbackPrompt()
                & totalEventCountsManager.shouldAllowFeedbackPrompt()
                & firstEventTimesManager.shouldAllowFeedbackPrompt()
                & lastEventTimesManager.shouldAllowFeedbackPrompt()
                & lastEventVersionsManager.shouldAllowFeedbackPrompt());
    }

}
