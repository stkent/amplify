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
import com.github.stkent.amplify.tracking.interfaces.IAppVersionNameProvider;
import com.github.stkent.amplify.tracking.prerequisites.GooglePlayStorePrerequisite;
import com.github.stkent.amplify.tracking.rules.MaximumCountRule;
import com.github.stkent.amplify.tracking.rules.VersionChangedRule;
import com.github.stkent.amplify.tracking.interfaces.IAppLevelEventRulesManager;
import com.github.stkent.amplify.tracking.interfaces.IAppEventTimeProvider;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentCapabilitiesProvider;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentBasedRule;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentBasedRulesManager;
import com.github.stkent.amplify.tracking.interfaces.IEventBasedRule;
import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.IEventListener;
import com.github.stkent.amplify.tracking.managers.AppLevelEventRulesManager;
import com.github.stkent.amplify.tracking.managers.EnvironmentBasedRulesManager;
import com.github.stkent.amplify.tracking.managers.FirstEventTimeRulesManager;
import com.github.stkent.amplify.tracking.managers.LastEventTimeRulesManager;
import com.github.stkent.amplify.tracking.managers.LastEventVersionRulesManager;
import com.github.stkent.amplify.tracking.managers.TotalEventCountRulesManager;
import com.github.stkent.amplify.views.PromptView;

public final class Amplify implements IEventListener {

    private static final int DEFAULT_USER_GAVE_POSITIVE_FEEDBACK_MAXIMUM_COUNT = 1;
    private static final int DEFAULT_LAST_UPDATE_TIME_COOLDOWN_DAYS = 7;
    private static final int DEFAULT_LAST_CRASH_TIME_COOLDOWN_DAYS = 7;

    private static Amplify sharedInstance;

    private final IAppVersionNameProvider appVersionNameProvider;

    private final IAppLevelEventRulesManager appLevelEventRulesManager;
    private final IEnvironmentBasedRulesManager environmentBasedRulesManager;
    private final FirstEventTimeRulesManager firstEventTimeRulesManager;
    private final LastEventTimeRulesManager lastEventTimeRulesManager;
    private final LastEventVersionRulesManager lastEventVersionRulesManager;
    private final TotalEventCountRulesManager totalEventCountRulesManager;

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

    private Amplify(@NonNull final Context context, @NonNull final ILogger logger) {
        final Context appContext = context.getApplicationContext();
        final IAppEventTimeProvider appEventTimeProvider = new AppEventTimeProvider(appContext);

        final IEnvironmentCapabilitiesProvider environmentCapabilitiesProvider
                = new EnvironmentCapabilitiesProvider(appContext);

        this.appVersionNameProvider = new AppVersionNameProvider(appContext);

        this.appLevelEventRulesManager
                = new AppLevelEventRulesManager(appContext, appEventTimeProvider, logger);

        this.environmentBasedRulesManager
                = new EnvironmentBasedRulesManager(environmentCapabilitiesProvider, logger);

        this.firstEventTimeRulesManager = new FirstEventTimeRulesManager(appContext, logger);
        this.lastEventTimeRulesManager = new LastEventTimeRulesManager(appContext, logger);
        this.lastEventVersionRulesManager = new LastEventVersionRulesManager(appContext, logger);
        this.totalEventCountRulesManager = new TotalEventCountRulesManager(appContext, logger);

        this.logger = logger;
    }

    // Configuration methods

    public Amplify setFeedbackEmailAddress(@NonNull final String feedbackEmailAddress) {
        this.feedbackEmail = feedbackEmailAddress;
        return this;
    }

    public Amplify configureWithDefaultBehavior() {
        return this
                .addEnvironmentBasedRule(new GooglePlayStorePrerequisite())
                .setLastUpdateTimeCooldownDays(DEFAULT_LAST_UPDATE_TIME_COOLDOWN_DAYS)
                .setLastCrashTimeCooldownDays(DEFAULT_LAST_CRASH_TIME_COOLDOWN_DAYS)
                .addTotalEventCountRule(PromptViewEvent.USER_GAVE_POSITIVE_FEEDBACK,
                        new MaximumCountRule(DEFAULT_USER_GAVE_POSITIVE_FEEDBACK_MAXIMUM_COUNT))
                .addLastEventVersionRule(PromptViewEvent.USER_GAVE_CRITICAL_FEEDBACK,
                        new VersionChangedRule(appVersionNameProvider))
                .addLastEventVersionRule(PromptViewEvent.USER_DECLINED_CRITICAL_FEEDBACK,
                        new VersionChangedRule(appVersionNameProvider))
                .addLastEventVersionRule(PromptViewEvent.USER_DECLINED_POSITIVE_FEEDBACK,
                        new VersionChangedRule(appVersionNameProvider));
    }

    public Amplify addEnvironmentBasedRule(@NonNull final IEnvironmentBasedRule rule) {
        environmentBasedRulesManager.addEnvironmentBasedRule(rule);
        return this;
    }

    public Amplify setInstallTimeCooldownDays(final int cooldownPeriodDays) {
        appLevelEventRulesManager.setInstallTimeCooldownDays(cooldownPeriodDays);
        return this;
    }

    public Amplify setLastUpdateTimeCooldownDays(final int cooldownPeriodDays) {
        appLevelEventRulesManager.setLastUpdateTimeCooldownDays(cooldownPeriodDays);
        return this;
    }

    public Amplify setLastCrashTimeCooldownDays(final int cooldownPeriodDays) {
        appLevelEventRulesManager.setLastCrashTimeCooldownDays(cooldownPeriodDays);
        return this;
    }

    public Amplify addTotalEventCountRule(
            @NonNull final IEvent event,
            @NonNull final IEventBasedRule<Integer> rule) {

        totalEventCountRulesManager.addEventBasedRule(event, rule);
        return this;
    }

    public Amplify addFirstEventTimeRule(
            @NonNull final IEvent event,
            @NonNull final IEventBasedRule<Long> rule) {

        firstEventTimeRulesManager.addEventBasedRule(event, rule);
        return this;
    }

    public Amplify addLastEventTimeRule(
            @NonNull final IEvent event,
            @NonNull final IEventBasedRule<Long> rule) {

        lastEventTimeRulesManager.addEventBasedRule(event, rule);
        return this;
    }

    public Amplify addLastEventVersionRule(
            @NonNull final IEvent event,
            @NonNull final IEventBasedRule<String> rule) {

        lastEventVersionRulesManager.addEventBasedRule(event, rule);
        return this;
    }

    // Debug configuration methods

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

    // Update methods

    @Override
    public void notifyEventTriggered(@NonNull final IEvent event) {
        logger.d("Triggered Event: " + event);
        totalEventCountRulesManager.notifyEventTriggered(event);
        firstEventTimeRulesManager.notifyEventTriggered(event);
        lastEventTimeRulesManager.notifyEventTriggered(event);
        lastEventVersionRulesManager.notifyEventTriggered(event);
    }

    // Query methods

    public void promptIfReady(@NonNull final PromptView promptView) {
        if (shouldAskForRating()) {
            promptView.injectDependencies(this, feedbackEmail, packageName, logger);
            promptView.show();
        }
    }

    public boolean shouldAskForRating() {
        return alwaysShow | (
                  appLevelEventRulesManager.shouldAllowFeedbackPrompt()
                & environmentBasedRulesManager.shouldAllowFeedbackPrompt()
                & totalEventCountRulesManager.shouldAllowFeedbackPrompt()
                & firstEventTimeRulesManager.shouldAllowFeedbackPrompt()
                & lastEventTimeRulesManager.shouldAllowFeedbackPrompt()
                & lastEventVersionRulesManager.shouldAllowFeedbackPrompt());
    }

}
