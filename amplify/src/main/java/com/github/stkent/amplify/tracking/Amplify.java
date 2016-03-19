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

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.stkent.amplify.ILogger;
import com.github.stkent.amplify.Logger;
import com.github.stkent.amplify.prompt.PromptPresenter;
import com.github.stkent.amplify.prompt.interfaces.IPromptPresenter;
import com.github.stkent.amplify.prompt.interfaces.IPromptView;
import com.github.stkent.amplify.tracking.interfaces.IAppEventTimeProvider;
import com.github.stkent.amplify.tracking.interfaces.IAppInfoProvider;
import com.github.stkent.amplify.tracking.interfaces.IAppLevelEventRulesManager;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentBasedRule;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentBasedRulesManager;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentCapabilitiesProvider;
import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.IEventBasedRule;
import com.github.stkent.amplify.tracking.interfaces.IEventListener;
import com.github.stkent.amplify.tracking.managers.AppLevelEventRulesManager;
import com.github.stkent.amplify.tracking.managers.EnvironmentBasedRulesManager;
import com.github.stkent.amplify.tracking.managers.FirstEventTimeRulesManager;
import com.github.stkent.amplify.tracking.managers.LastEventTimeRulesManager;
import com.github.stkent.amplify.tracking.managers.LastEventVersionCodeRulesManager;
import com.github.stkent.amplify.tracking.managers.LastEventVersionNameRulesManager;
import com.github.stkent.amplify.tracking.managers.TotalEventCountRulesManager;
import com.github.stkent.amplify.tracking.prerequisites.GooglePlayStoreRule;
import com.github.stkent.amplify.tracking.rules.MaximumCountRule;
import com.github.stkent.amplify.tracking.rules.VersionNameChangedRule;
import com.github.stkent.amplify.utils.ActivityStateUtil;
import com.github.stkent.amplify.utils.AppInfoProvider;
import com.github.stkent.amplify.utils.FeedbackUtil;
import com.github.stkent.amplify.utils.PlayStoreUtil;

public final class Amplify implements IEventListener {

    private static final int DEFAULT_USER_GAVE_POSITIVE_FEEDBACK_MAXIMUM_COUNT = 1;
    private static final int DEFAULT_LAST_UPDATE_TIME_COOLDOWN_DAYS = 7;
    private static final int DEFAULT_LAST_CRASH_TIME_COOLDOWN_DAYS = 7;

    private static Amplify sharedInstance;

    private final IAppLevelEventRulesManager appLevelEventRulesManager;
    private final IEnvironmentBasedRulesManager environmentBasedRulesManager;
    private final FirstEventTimeRulesManager firstEventTimeRulesManager;
    private final LastEventTimeRulesManager lastEventTimeRulesManager;
    private final LastEventVersionCodeRulesManager lastEventVersionCodeRulesManager;
    private final LastEventVersionNameRulesManager lastEventVersionNameRulesManager;
    private final TotalEventCountRulesManager totalEventCountRulesManager;

    private final ILogger logger;

    private boolean alwaysShow;
    private String packageName;
    private String feedbackEmailAddress;

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
        AppInfoProvider.initialize(context);
        final IAppInfoProvider appInfoProvider = AppInfoProvider.getSharedInstance();

        final Context appContext = context.getApplicationContext();
        final IAppEventTimeProvider appEventTimeProvider
                = new AppEventTimeProvider(appInfoProvider);

        final IEnvironmentCapabilitiesProvider environmentCapabilitiesProvider
                = new EnvironmentCapabilitiesProvider(appInfoProvider);

        this.appLevelEventRulesManager
                = new AppLevelEventRulesManager(appContext, appEventTimeProvider, logger);

        this.environmentBasedRulesManager
                = new EnvironmentBasedRulesManager(environmentCapabilitiesProvider, logger);

        this.firstEventTimeRulesManager = new FirstEventTimeRulesManager(appContext, logger);

        this.lastEventTimeRulesManager = new LastEventTimeRulesManager(appContext, logger);

        this.lastEventVersionNameRulesManager
                = new LastEventVersionNameRulesManager(appContext, appInfoProvider, logger);

        this.lastEventVersionCodeRulesManager
                = new LastEventVersionCodeRulesManager(appContext, appInfoProvider, logger);

        this.totalEventCountRulesManager = new TotalEventCountRulesManager(appContext, logger);

        this.logger = logger;
    }

    // Configuration methods

    public Amplify setFeedbackEmailAddress(@NonNull final String feedbackEmailAddress) {
        this.feedbackEmailAddress = feedbackEmailAddress;
        return this;
    }

    public Amplify applyAllDefaultRules() {
        return this
                .addEnvironmentBasedRule(new GooglePlayStoreRule())
                .setLastUpdateTimeCooldownDays(DEFAULT_LAST_UPDATE_TIME_COOLDOWN_DAYS)
                .setLastCrashTimeCooldownDays(DEFAULT_LAST_CRASH_TIME_COOLDOWN_DAYS)
                .addTotalEventCountRule(PromptViewEvent.USER_GAVE_POSITIVE_FEEDBACK,
                        new MaximumCountRule(DEFAULT_USER_GAVE_POSITIVE_FEEDBACK_MAXIMUM_COUNT))
                .addLastEventVersionNameRule(PromptViewEvent.USER_GAVE_CRITICAL_FEEDBACK,
                        new VersionNameChangedRule())
                .addLastEventVersionNameRule(PromptViewEvent.USER_DECLINED_CRITICAL_FEEDBACK,
                        new VersionNameChangedRule())
                .addLastEventVersionNameRule(PromptViewEvent.USER_DECLINED_POSITIVE_FEEDBACK,
                        new VersionNameChangedRule());
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

    public Amplify addLastEventVersionCodeRule(
            @NonNull final IEvent event,
            @NonNull final IEventBasedRule<Integer> rule) {

        lastEventVersionCodeRulesManager.addEventBasedRule(event, rule);
        return this;
    }

    public Amplify addLastEventVersionNameRule(
            @NonNull final IEvent event,
            @NonNull final IEventBasedRule<String> rule) {

        lastEventVersionNameRulesManager.addEventBasedRule(event, rule);
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

    public Amplify setPackageName(@NonNull final String packageName) {
        this.packageName = packageName;
        return this;
    }

    // Update methods

    @Override
    public void notifyEventTriggered(@NonNull final IEvent event) {
        logger.d(event + " event triggered");
        totalEventCountRulesManager.notifyEventTriggered(event);
        firstEventTimeRulesManager.notifyEventTriggered(event);
        lastEventTimeRulesManager.notifyEventTriggered(event);
        lastEventVersionCodeRulesManager.notifyEventTriggered(event);
        lastEventVersionNameRulesManager.notifyEventTriggered(event);
    }

    // Query methods

    // todo: track activity using application hooks so we don't need to receive it here
    public void promptIfReady(
            @NonNull final Activity activity,
            @NonNull final IPromptView promptView) {

        promptIfReady(activity, promptView, null);
    }

    // todo: track activity using application hooks so we don't need to receive it here
    public void promptIfReady(
            @NonNull final Activity activity,
            @NonNull final IPromptView promptView,
            @Nullable final IEventListener<PromptViewEvent> promptViewEventListener) {

        if (feedbackEmailAddress == null) {
            throw new IllegalStateException(
                    "Must provide email address before attempting to prompt.");
        }

        final IEventListener<PromptViewEvent> combinedEventListener
                = new IEventListener<PromptViewEvent>() {

            @Override
            public void notifyEventTriggered(@NonNull final PromptViewEvent event) {
                Amplify.this.notifyEventTriggered(event);

                if (promptViewEventListener != null) {
                    promptViewEventListener.notifyEventTriggered(event);
                }

                if (ActivityStateUtil.isActivityValid(activity)) {
                    if (event == PromptViewEvent.USER_GAVE_POSITIVE_FEEDBACK) {
                        PlayStoreUtil.openPlayStoreToRate(activity, packageName);
                    } else if (event == PromptViewEvent.USER_GAVE_CRITICAL_FEEDBACK) {
                        final IAppInfoProvider appInfoProvider
                                = AppInfoProvider.getSharedInstance();

                        final FeedbackUtil feedbackUtil = new FeedbackUtil(
                                new AppFeedbackDataProvider(appInfoProvider),
                                new EnvironmentCapabilitiesProvider(appInfoProvider),
                                feedbackEmailAddress,
                                logger);

                        feedbackUtil.showFeedbackEmailChooser(activity);
                    }
                }
            }
        };

        if (shouldPrompt()) {
            final IPromptPresenter promptPresenter
                    = new PromptPresenter(combinedEventListener, promptView);

            promptPresenter.start();
        }
    }

    public boolean shouldPrompt() {
        return alwaysShow | (
                  appLevelEventRulesManager.shouldAllowFeedbackPrompt()
                & environmentBasedRulesManager.shouldAllowFeedbackPrompt()
                & totalEventCountRulesManager.shouldAllowFeedbackPrompt()
                & firstEventTimeRulesManager.shouldAllowFeedbackPrompt()
                & lastEventTimeRulesManager.shouldAllowFeedbackPrompt()
                & lastEventVersionCodeRulesManager.shouldAllowFeedbackPrompt()
                & lastEventVersionNameRulesManager.shouldAllowFeedbackPrompt());
    }

}
