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
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.github.stkent.amplify.feedback.IFeedbackCollector;
import com.github.stkent.amplify.logging.ILogger;
import com.github.stkent.amplify.logging.NoOpLogger;
import com.github.stkent.amplify.prompt.interfaces.IPromptView;
import com.github.stkent.amplify.tracking.interfaces.IAppEventTimeProvider;
import com.github.stkent.amplify.tracking.interfaces.IAppLevelEventRulesManager;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentBasedRule;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentBasedRulesManager;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentCapabilitiesProvider;
import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.IEventBasedRule;
import com.github.stkent.amplify.tracking.interfaces.IEventListener;
import com.github.stkent.amplify.tracking.interfaces.IEventsManager;
import com.github.stkent.amplify.tracking.managers.AppLevelEventRulesManager;
import com.github.stkent.amplify.tracking.managers.EnvironmentBasedRulesManager;
import com.github.stkent.amplify.tracking.managers.FirstEventTimeRulesManager;
import com.github.stkent.amplify.tracking.managers.LastEventTimeRulesManager;
import com.github.stkent.amplify.tracking.managers.LastEventVersionCodeRulesManager;
import com.github.stkent.amplify.tracking.managers.LastEventVersionNameRulesManager;
import com.github.stkent.amplify.tracking.managers.TotalEventCountRulesManager;
import com.github.stkent.amplify.tracking.rules.GooglePlayStoreRule;
import com.github.stkent.amplify.tracking.rules.MaximumCountRule;
import com.github.stkent.amplify.tracking.rules.VersionNameChangedRule;
import com.github.stkent.amplify.utils.ActivityReferenceManager;
import com.github.stkent.amplify.utils.Constants;
import com.github.stkent.amplify.utils.appinfo.AppInfoUtil;
import com.github.stkent.amplify.utils.appinfo.IAppInfoProvider;
import com.github.stkent.amplify.utils.feedback.DefaultEmailContentProvider;

@SuppressWarnings({"PMD.ExcessiveParameterList", "checkstyle:parameternumber"})
public final class Amplify implements IEventListener {

    private static final int DEFAULT_USER_GAVE_POSITIVE_FEEDBACK_MAXIMUM_COUNT = 1;
    private static final int DEFAULT_LAST_UPDATE_TIME_COOLDOWN_DAYS = 7;
    private static final int DEFAULT_LAST_CRASH_TIME_COOLDOWN_DAYS = 7;

    // Begin logging

    private static ILogger sharedLogger = new NoOpLogger();

    public static ILogger getLogger() {
        return sharedLogger;
    }

    public static void setLogger(@NonNull final ILogger logger) {
        sharedLogger = logger;
    }

    // End logging
    // Begin shared instance

    private static Amplify sharedInstance;

    public static Amplify initSharedInstance(@NonNull final Application app) {
        return initSharedInstance(app, Constants.DEFAULT_BACKING_SHARED_PREFERENCES_NAME);
    }

    private static Amplify initSharedInstance(
            @NonNull final Application app,
            @NonNull final String backingSharedPreferencesName) {

        synchronized (Amplify.class) {
            if (sharedInstance == null) {
                sharedInstance = new Amplify(app, backingSharedPreferencesName);
            }
        }

        return sharedInstance;
    }

    public static Amplify getSharedInstance() {
        synchronized (Amplify.class) {
            if (sharedInstance == null) {
                throw new IllegalStateException(
                        "You must call initSharedInstance before calling getSharedInstance.");
            }
        }

        return sharedInstance;
    }

    // End shared instance
    // Begin instance fields

    private final IAppInfoProvider appInfoProvider;
    private final IAppLevelEventRulesManager appLevelEventRulesManager;
    private final IEnvironmentBasedRulesManager environmentBasedRulesManager;
    private final ActivityReferenceManager activityReferenceManager;
    private final IEventsManager<Long> firstEventTimeRulesManager;
    private final IEventsManager<Long> lastEventTimeRulesManager;
    private final IEventsManager<Integer> lastEventVersionCodeRulesManager;
    private final IEventsManager<String> lastEventVersionNameRulesManager;
    private final IEventsManager<Integer> totalEventCountRulesManager;

    private boolean alwaysShow;
    private IFeedbackCollector positiveFeedbackCollector;
    private IFeedbackCollector criticalFeedbackCollector;

    // End instance fields
    // Begin constructors

    private Amplify(@NonNull final Application app, @NonNull final String backingSharedPreferencesName) {
        final Context appContext = app.getApplicationContext();

        AppInfoUtil.initialize(appContext);
        this.appInfoProvider = AppInfoUtil.getSharedAppInfoProvider();

        final IAppEventTimeProvider appEventTimeProvider
                = new AppEventTimeProvider(appInfoProvider);

        final IEnvironmentCapabilitiesProvider environmentCapabilitiesProvider
                = new EnvironmentCapabilitiesProvider(appInfoProvider);

        this.activityReferenceManager = new ActivityReferenceManager();
        app.registerActivityLifecycleCallbacks(activityReferenceManager);

        this.environmentBasedRulesManager
                = new EnvironmentBasedRulesManager(environmentCapabilitiesProvider);

        final SharedPreferences backingSharedPreferences
                = app.getSharedPreferences(backingSharedPreferencesName, Context.MODE_PRIVATE);

        this.appLevelEventRulesManager = new AppLevelEventRulesManager(
                new Settings<Long>(backingSharedPreferences), appEventTimeProvider);

        this.firstEventTimeRulesManager = new FirstEventTimeRulesManager(
                new Settings<Long>(backingSharedPreferences));

        this.lastEventTimeRulesManager = new LastEventTimeRulesManager(
                new Settings<Long>(backingSharedPreferences));

        this.lastEventVersionNameRulesManager = new LastEventVersionNameRulesManager(
                new Settings<String>(backingSharedPreferences), appInfoProvider);

        this.lastEventVersionCodeRulesManager = new LastEventVersionCodeRulesManager(
                new Settings<Integer>(backingSharedPreferences), appInfoProvider);

        this.totalEventCountRulesManager = new TotalEventCountRulesManager(
                new Settings<Integer>(backingSharedPreferences));
    }

    @VisibleForTesting
    protected Amplify(
            @NonNull final IAppInfoProvider appInfoProvider,
            @NonNull final IAppLevelEventRulesManager appLevelEventRulesManager,
            @NonNull final IEnvironmentBasedRulesManager environmentBasedRulesManager,
            @NonNull final ActivityReferenceManager activityReferenceManager,
            @NonNull final IEventsManager<Long> firstEventTimeRulesManager,
            @NonNull final IEventsManager<Long> lastEventTimeRulesManager,
            @NonNull final IEventsManager<Integer> lastEventVersionCodeRulesManager,
            @NonNull final IEventsManager<String> lastEventVersionNameRulesManager,
            @NonNull final IEventsManager<Integer> totalEventCountRulesManager) {

        this.appInfoProvider = appInfoProvider;
        this.appLevelEventRulesManager = appLevelEventRulesManager;
        this.environmentBasedRulesManager = environmentBasedRulesManager;
        this.activityReferenceManager = activityReferenceManager;
        this.firstEventTimeRulesManager = firstEventTimeRulesManager;
        this.lastEventTimeRulesManager = lastEventTimeRulesManager;
        this.lastEventVersionCodeRulesManager = lastEventVersionCodeRulesManager;
        this.lastEventVersionNameRulesManager = lastEventVersionNameRulesManager;
        this.totalEventCountRulesManager = totalEventCountRulesManager;
    }

    // End constructors
    // Begin configuration methods

    public Amplify setPositiveFeedbackCollector(@NonNull final IFeedbackCollector feedbackCollector) {
        positiveFeedbackCollector = feedbackCollector;
        return this;
    }

    public Amplify setCriticalFeedbackCollector(@NonNull final IFeedbackCollector feedbackCollector) {
        criticalFeedbackCollector = feedbackCollector;
        return this;
    }

    public Amplify applyAllDefaultRules() {
        return this
                .addEnvironmentBasedRule(new GooglePlayStoreRule())
                .setLastUpdateTimeCooldownDays(DEFAULT_LAST_UPDATE_TIME_COOLDOWN_DAYS)
                .setLastCrashTimeCooldownDays(DEFAULT_LAST_CRASH_TIME_COOLDOWN_DAYS)
                .addTotalEventCountRule(PromptInteractionEvent.USER_GAVE_POSITIVE_FEEDBACK,
                        new MaximumCountRule(DEFAULT_USER_GAVE_POSITIVE_FEEDBACK_MAXIMUM_COUNT))
                .addLastEventVersionNameRule(PromptInteractionEvent.USER_GAVE_CRITICAL_FEEDBACK,
                        new VersionNameChangedRule())
                .addLastEventVersionNameRule(PromptInteractionEvent.USER_DECLINED_CRITICAL_FEEDBACK,
                        new VersionNameChangedRule())
                .addLastEventVersionNameRule(PromptInteractionEvent.USER_DECLINED_POSITIVE_FEEDBACK,
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

    // End configuration methods
    // Begin debug configuration methods

    public Amplify setAlwaysShow(final boolean alwaysShow) {
        this.alwaysShow = alwaysShow;
        return this;
    }

    // End debug configuration methods
    // Begin update methods

    @Override
    public void notifyEventTriggered(@NonNull final IEvent event) {
        sharedLogger.d(event.getTrackingKey() + " event triggered");
        totalEventCountRulesManager.notifyEventTriggered(event);
        firstEventTimeRulesManager.notifyEventTriggered(event);
        lastEventTimeRulesManager.notifyEventTriggered(event);
        lastEventVersionCodeRulesManager.notifyEventTriggered(event);
        lastEventVersionNameRulesManager.notifyEventTriggered(event);

        if (event == PromptInteractionEvent.USER_GAVE_POSITIVE_FEEDBACK) {
            final Activity activity = activityReferenceManager.getValidatedActivity();

            if (activity != null) {
                positiveFeedbackCollector.collectFeedback(activity, appInfoProvider, new DefaultEmailContentProvider());
            }
        } else if (event == PromptInteractionEvent.USER_GAVE_CRITICAL_FEEDBACK) {
            final Activity activity = activityReferenceManager.getValidatedActivity();

            if (activity != null) {
                criticalFeedbackCollector.collectFeedback(activity, appInfoProvider, new DefaultEmailContentProvider());
            }
        }
    }

    // End update methods
    // Begin query methods

    public void promptIfReady(@NonNull final IPromptView promptView) {
        if (!isConfigured()) {
            throw new IllegalStateException("Must finish configuration before attempting to prompt.");
        }

        if (shouldPrompt()) {
            promptView.getPresenter().start();
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

    // End query methods

    private boolean isConfigured() {
        return positiveFeedbackCollector != null && criticalFeedbackCollector != null;
    }

}
