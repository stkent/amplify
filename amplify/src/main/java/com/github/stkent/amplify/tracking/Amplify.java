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
import com.github.stkent.amplify.tracking.interfaces.ITrackableEventListener;
import com.github.stkent.amplify.tracking.interfaces.IApplicationChecksManager;
import com.github.stkent.amplify.tracking.interfaces.IApplicationEventTimeProvider;
import com.github.stkent.amplify.tracking.interfaces.IApplicationVersionNameProvider;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentCapabilitiesProvider;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentCheck;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentChecksManager;
import com.github.stkent.amplify.tracking.interfaces.IEventCheck;
import com.github.stkent.amplify.tracking.interfaces.ITrackableEvent;
import com.github.stkent.amplify.tracking.managers.ApplicationChecksManager;
import com.github.stkent.amplify.tracking.managers.EnvironmentChecksManager;
import com.github.stkent.amplify.tracking.managers.FirstEventTimesManager;
import com.github.stkent.amplify.tracking.managers.LastEventTimesManager;
import com.github.stkent.amplify.tracking.managers.LastEventVersionsManager;
import com.github.stkent.amplify.tracking.managers.TotalEventCountsManager;
import com.github.stkent.amplify.views.PromptView;

public final class Amplify implements ITrackableEventListener {

    // static fields
    private static Amplify sharedInstance;

    private static final int DEFAULT_USER_GAVE_POSITIVE_FEEDBACK_MAXIMUM_COUNT = 1;
    private static final int DEFAULT_INSTALL_TIME_COOLDOWN_DAYS = 7;
    private static final int DEFAULT_LAST_UPDATE_TIME_COOLDOWN_DAYS = 7;
    private static final int DEFAULT_USER_DECLINED_CRITICAL_FEEDBACK_COOLDOWN_DAYS = 7;
    private static final int DEFAULT_USER_DECLINED_POSITIVE_FEEDBACK_COOLDOWN_DAYS = 7;
    private static final int DEFAULT_USER_GAVE_CRITICAL_FEEDBACK_COOLDOWN_DAYS = 7;

    // instance fields
    private final IApplicationVersionNameProvider applicationVersionNameProvider;

    private final IApplicationChecksManager applicationChecksManager;
    private final IEnvironmentChecksManager environmentChecksManager;
    private final FirstEventTimesManager firstEventTimesManager;
    private final LastEventTimesManager lastEventTimesManager;
    private final LastEventVersionsManager lastEventVersionsManager;
    private final TotalEventCountsManager totalEventCountsManager;

    private final ILogger logger;

    private boolean alwaysShow;
    private String packageName;
    private String feedbackEmail;

    public static Amplify get(@NonNull final Context context) {
        return get(context, new Logger());
    }

    public static Amplify get(@NonNull final Context context, @NonNull final ILogger logger) {
        synchronized (Amplify.class) {
            if (sharedInstance == null) {
                sharedInstance = new Amplify(context, logger);
            }
        }

        return sharedInstance;
    }

    // constructors

    private Amplify(
            @NonNull final Context context,
            @NonNull final ILogger logger) {
        final Context applicationContext = context.getApplicationContext();
        final IApplicationEventTimeProvider applicationEventTimeProvider = new ApplicationEventTimeProvider(applicationContext);
        final IEnvironmentCapabilitiesProvider environmentCapabilitiesProvider = new EnvironmentCapabilitiesProvider(applicationContext);

        this.applicationVersionNameProvider = new ApplicationVersionNameProvider(applicationContext);

        this.applicationChecksManager = new ApplicationChecksManager(applicationContext, applicationEventTimeProvider, logger);
        this.environmentChecksManager = new EnvironmentChecksManager(environmentCapabilitiesProvider, logger);
        this.firstEventTimesManager = new FirstEventTimesManager(applicationContext, logger);
        this.lastEventTimesManager = new LastEventTimesManager(applicationContext, logger);
        this.lastEventVersionsManager = new LastEventVersionsManager(applicationContext, logger);
        this.totalEventCountsManager = new TotalEventCountsManager(applicationContext, logger);

        this.logger = logger;
    }

    // configuration methods

    public Amplify configureWithDefaults() {
        return this
                .addEnvironmentCheck(new GooglePlayStoreIsAvailableCheck())
                .setInstallTimeCooldownDays(DEFAULT_INSTALL_TIME_COOLDOWN_DAYS)
                .setLastCrashTimeCooldownDays(DEFAULT_LAST_UPDATE_TIME_COOLDOWN_DAYS)
                .trackTotalEventCount(PromptViewEvent.USER_GAVE_POSITIVE_FEEDBACK,
                        new MaximumCountCheck(DEFAULT_USER_GAVE_POSITIVE_FEEDBACK_MAXIMUM_COUNT))
                .trackLastEventTime(PromptViewEvent.USER_GAVE_CRITICAL_FEEDBACK,
                        new CooldownDaysCheck(DEFAULT_USER_GAVE_CRITICAL_FEEDBACK_COOLDOWN_DAYS))
                .trackLastEventTime(PromptViewEvent.USER_DECLINED_CRITICAL_FEEDBACK,
                        new CooldownDaysCheck(DEFAULT_USER_DECLINED_CRITICAL_FEEDBACK_COOLDOWN_DAYS))
                .trackLastEventTime(PromptViewEvent.USER_DECLINED_POSITIVE_FEEDBACK,
                        new CooldownDaysCheck(DEFAULT_USER_DECLINED_POSITIVE_FEEDBACK_COOLDOWN_DAYS))
                .trackLastEventVersion(PromptViewEvent.USER_DECLINED_CRITICAL_FEEDBACK,
                        new VersionChangedCheck(applicationVersionNameProvider))
                .trackLastEventVersion(PromptViewEvent.USER_DECLINED_POSITIVE_FEEDBACK,
                        new VersionChangedCheck(applicationVersionNameProvider))
                .trackLastEventVersion(PromptViewEvent.USER_GAVE_CRITICAL_FEEDBACK,
                        new VersionChangedCheck(applicationVersionNameProvider));
    }

    public Amplify setLogLevel(@NonNull final Logger.LogLevel logLevel) {
        logger.setLogLevel(logLevel);
        return this;
    }

    public Amplify setAlwaysShow(final boolean alwaysShow) {
        this.alwaysShow = alwaysShow;
        return this;
    }

    public Amplify setPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public Amplify setFeedbackEmail(String feedbackEmail) {
        this.feedbackEmail = feedbackEmail;
        return this;
    }

    public Amplify setInstallTimeCooldownDays(final int cooldownPeriodDays) {
        applicationChecksManager.setInstallTimeCooldownDays(cooldownPeriodDays);
        return this;
    }

    public Amplify setLastUpdateTimeCooldownDays(final int cooldownPeriodDays) {
        applicationChecksManager.setLastUpdateTimeCooldownDays(cooldownPeriodDays);
        return this;
    }

    public Amplify setLastCrashTimeCooldownDays(final int cooldownPeriodDays) {
        applicationChecksManager.setLastCrashTimeCooldownDays(cooldownPeriodDays);
        return this;
    }

    public Amplify trackTotalEventCount(@NonNull final ITrackableEvent event, @NonNull final IEventCheck<Integer> eventCheck) {
        totalEventCountsManager.trackEvent(event, eventCheck);
        return this;
    }

    public Amplify trackFirstEventTime(@NonNull final ITrackableEvent event, @NonNull final IEventCheck<Long> eventCheck) {
        firstEventTimesManager.trackEvent(event, eventCheck);
        return this;
    }

    public Amplify trackLastEventTime(@NonNull final ITrackableEvent event, @NonNull final IEventCheck<Long> eventCheck) {
        lastEventTimesManager.trackEvent(event, eventCheck);
        return this;
    }

    public Amplify trackLastEventVersion(@NonNull final ITrackableEvent event, @NonNull final IEventCheck<String> eventCheck) {
        lastEventVersionsManager.trackEvent(event, eventCheck);
        return this;
    }

    public Amplify addEnvironmentCheck(@NonNull final IEnvironmentCheck environmentCheck) {
        environmentChecksManager.addEnvironmentCheck(environmentCheck);
        return this;
    }

    // update methods

    @Override
    public void notifyEventTriggered(@NonNull final ITrackableEvent event) {
        logger.d("Triggered Event: " + event);
        totalEventCountsManager.notifyEventTriggered(event);
        firstEventTimesManager.notifyEventTriggered(event);
        lastEventTimesManager.notifyEventTriggered(event);
        lastEventVersionsManager.notifyEventTriggered(event);
    }

    // query methods

    public void promptIfReady(@NonNull final PromptView promptView) {
        if (shouldAskForRating()) {
            promptView.injectDependencies(this, logger, packageName, feedbackEmail);
            promptView.show();
        }
    }

    public boolean shouldAskForRating() {
        return alwaysShow | (
                  applicationChecksManager.shouldAllowFeedbackPrompt()
                & environmentChecksManager.shouldAllowFeedbackPrompt()
                & totalEventCountsManager .shouldAllowFeedbackPrompt()
                & firstEventTimesManager  .shouldAllowFeedbackPrompt()
                & lastEventTimesManager   .shouldAllowFeedbackPrompt()
                & lastEventVersionsManager.shouldAllowFeedbackPrompt());
    }

}
