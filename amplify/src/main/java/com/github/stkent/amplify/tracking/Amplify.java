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
import com.github.stkent.amplify.tracking.interfaces.IPrerequisite;
import com.github.stkent.amplify.tracking.interfaces.IPrerequisitesManager;
import com.github.stkent.amplify.tracking.interfaces.IPromptRule;
import com.github.stkent.amplify.tracking.interfaces.ITrackableEvent;
import com.github.stkent.amplify.tracking.interfaces.ITrackableEventListener;
import com.github.stkent.amplify.tracking.managers.AppLevelEventRulesManager;
import com.github.stkent.amplify.tracking.managers.PrerequisitesManager;
import com.github.stkent.amplify.tracking.managers.FirstEventTimeRulesManager;
import com.github.stkent.amplify.tracking.managers.LastEventTimeRulesManager;
import com.github.stkent.amplify.tracking.managers.LastEventVersionRulesManager;
import com.github.stkent.amplify.tracking.managers.TotalEventCountRulesManager;
import com.github.stkent.amplify.views.PromptView;

public final class Amplify implements ITrackableEventListener {

    private static final int DEFAULT_USER_GAVE_POSITIVE_FEEDBACK_MAXIMUM_COUNT = 1;
    private static final int DEFAULT_LAST_UPDATE_TIME_COOLDOWN_DAYS = 7;
    private static final int DEFAULT_LAST_CRASH_TIME_COOLDOWN_DAYS = 7;

    private static Amplify sharedInstance;

    private final IAppVersionNameProvider appVersionNameProvider;

    private final IAppLevelEventRulesManager appappLevelEventRulesManager;
    private final IPrerequisitesManager prerequisitesManager;
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

        this.appappLevelEventRulesManager
                = new AppLevelEventRulesManager(appContext, appEventTimeProvider, logger);

        this.prerequisitesManager
                = new PrerequisitesManager(environmentCapabilitiesProvider, logger);

        this.firstEventTimeRulesManager = new FirstEventTimeRulesManager(appContext, logger);
        this.lastEventTimeRulesManager = new LastEventTimeRulesManager(appContext, logger);
        this.lastEventVersionRulesManager = new LastEventVersionRulesManager(appContext, logger);
        this.totalEventCountRulesManager = new TotalEventCountRulesManager(appContext, logger);

        this.logger = logger;
    }

    // configuration methods

    public Amplify configureWithDefaultBehavior() {
        return this
                .addPrerequisite(new GooglePlayStorePrerequisite())
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

    public Amplify addPrerequisite(@NonNull final IPrerequisite prerequisite) {
        prerequisitesManager.addPrerequisite(prerequisite);
        return this;
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
        appappLevelEventRulesManager.setInstallTimeCooldownDays(cooldownPeriodDays);
        return this;
    }

    public Amplify setLastUpdateTimeCooldownDays(final int cooldownPeriodDays) {
        appappLevelEventRulesManager.setLastUpdateTimeCooldownDays(cooldownPeriodDays);
        return this;
    }

    public Amplify setLastCrashTimeCooldownDays(final int cooldownPeriodDays) {
        appappLevelEventRulesManager.setLastCrashTimeCooldownDays(cooldownPeriodDays);
        return this;
    }

    public Amplify addTotalEventCountRule(
            @NonNull final ITrackableEvent event,
            @NonNull final IPromptRule<Integer> promptRule) {

        totalEventCountRulesManager.addEventPromptRule(event, promptRule);
        return this;
    }

    public Amplify addFirstEventTimeRule(
            @NonNull final ITrackableEvent event,
            @NonNull final IPromptRule<Long> promptRule) {

        firstEventTimeRulesManager.addEventPromptRule(event, promptRule);
        return this;
    }

    public Amplify addLastEventTimeRule(
            @NonNull final ITrackableEvent event,
            @NonNull final IPromptRule<Long> promptRule) {

        lastEventTimeRulesManager.addEventPromptRule(event, promptRule);
        return this;
    }

    public Amplify addLastEventVersionRule(
            @NonNull final ITrackableEvent event,
            @NonNull final IPromptRule<String> promptRule) {

        lastEventVersionRulesManager.addEventPromptRule(event, promptRule);
        return this;
    }

    // update methods

    @Override
    public void notifyEventTriggered(@NonNull final ITrackableEvent event) {
        logger.d("Triggered Event: " + event);
        totalEventCountRulesManager.notifyEventTriggered(event);
        firstEventTimeRulesManager.notifyEventTriggered(event);
        lastEventTimeRulesManager.notifyEventTriggered(event);
        lastEventVersionRulesManager.notifyEventTriggered(event);
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
                  appappLevelEventRulesManager.shouldAllowFeedbackPrompt()
                & prerequisitesManager.shouldAllowFeedbackPrompt()
                & totalEventCountRulesManager.shouldAllowFeedbackPrompt()
                & firstEventTimeRulesManager.shouldAllowFeedbackPrompt()
                & lastEventTimeRulesManager.shouldAllowFeedbackPrompt()
                & lastEventVersionRulesManager.shouldAllowFeedbackPrompt());
    }

}
