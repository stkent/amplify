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

import com.github.stkent.amplify.App;
import com.github.stkent.amplify.Environment;
import com.github.stkent.amplify.IApp;
import com.github.stkent.amplify.IEnvironment;
import com.github.stkent.amplify.feedback.IFeedbackCollector;
import com.github.stkent.amplify.logging.ILogger;
import com.github.stkent.amplify.logging.NoOpLogger;
import com.github.stkent.amplify.prompt.interfaces.IPromptView;
import com.github.stkent.amplify.tracking.interfaces.IAppLevelEventRulesManager;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentBasedRule;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentBasedRulesManager;
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

import static android.content.Context.MODE_PRIVATE;
import static com.github.stkent.amplify.tracking.PromptInteractionEvent.USER_DECLINED_CRITICAL_FEEDBACK;
import static com.github.stkent.amplify.tracking.PromptInteractionEvent.USER_DECLINED_POSITIVE_FEEDBACK;
import static com.github.stkent.amplify.tracking.PromptInteractionEvent.USER_GAVE_CRITICAL_FEEDBACK;
import static com.github.stkent.amplify.tracking.PromptInteractionEvent.USER_GAVE_POSITIVE_FEEDBACK;

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

    public static Amplify initSharedInstance(@NonNull final Application application) {
        return initSharedInstance(application, Constants.DEFAULT_BACKING_SHARED_PREFERENCES_NAME);
    }

    private static Amplify initSharedInstance(
            @NonNull final Application application,
            @NonNull final String backingSharedPreferencesName) {

        synchronized (Amplify.class) {
            if (sharedInstance == null) {
                sharedInstance = new Amplify(application, backingSharedPreferencesName);
            }
        }

        return sharedInstance;
    }

    public static Amplify getSharedInstance() {
        synchronized (Amplify.class) {
            if (sharedInstance == null) {
                throw new IllegalStateException("You must call initSharedInstance before calling getSharedInstance.");
            }
        }

        return sharedInstance;
    }

    // End shared instance
    // Begin instance fields

    private final IAppLevelEventRulesManager appLevelEventRulesManager;
    private final IEnvironmentBasedRulesManager environmentBasedRulesManager;
    private final ActivityReferenceManager activityReferenceManager;
    private final IEventsManager<Long> firstEventTimeRulesManager;
    private final IEventsManager<Long> lastEventTimeRulesManager;
    private final IEventsManager<Integer> lastEventVersionCodeRulesManager;
    private final IEventsManager<String> lastEventVersionNameRulesManager;
    private final IEventsManager<Integer> totalEventCountRulesManager;

    private boolean alwaysShow;
    private IFeedbackCollector[] positiveFeedbackCollectors;
    private IFeedbackCollector[] criticalFeedbackCollectors;

    // End instance fields
    // Begin constructors

    private Amplify(@NonNull final Application application, @NonNull final String sharedPrefsName) {
        activityReferenceManager = new ActivityReferenceManager();
        application.registerActivityLifecycleCallbacks(activityReferenceManager);

        final IEnvironment environment = new Environment(application);

        this.environmentBasedRulesManager = new EnvironmentBasedRulesManager(environment);

        final SharedPreferences sharedPrefs = application.getSharedPreferences(sharedPrefsName, MODE_PRIVATE);
        final IApp app = new App(application);

        appLevelEventRulesManager = new AppLevelEventRulesManager(new Settings<Long>(sharedPrefs), app);
        firstEventTimeRulesManager = new FirstEventTimeRulesManager(new Settings<Long>(sharedPrefs));
        lastEventTimeRulesManager = new LastEventTimeRulesManager(new Settings<Long>(sharedPrefs));
        lastEventVersionNameRulesManager = new LastEventVersionNameRulesManager(new Settings<String>(sharedPrefs), app);
        lastEventVersionCodeRulesManager = new LastEventVersionCodeRulesManager(new Settings<Integer>(sharedPrefs), app);
        totalEventCountRulesManager = new TotalEventCountRulesManager(new Settings<Integer>(sharedPrefs));
    }

    // End constructors
    // Begin configuration methods

    public Amplify setPositiveFeedbackCollectors(@NonNull final IFeedbackCollector... feedbackCollectors) {
        positiveFeedbackCollectors = feedbackCollectors;
        return this;
    }

    public Amplify setCriticalFeedbackCollectors(@NonNull final IFeedbackCollector... feedbackCollectors) {
        criticalFeedbackCollectors = feedbackCollectors;
        return this;
    }

    public Amplify applyAllDefaultRules(@NonNull final Context context) {
        return this
                .addEnvironmentBasedRule(new GooglePlayStoreRule())
                .setLastUpdateTimeCooldownDays(DEFAULT_LAST_UPDATE_TIME_COOLDOWN_DAYS)
                .setLastCrashTimeCooldownDays(DEFAULT_LAST_CRASH_TIME_COOLDOWN_DAYS)
                .addTotalEventCountRule(USER_GAVE_POSITIVE_FEEDBACK,
                        new MaximumCountRule(DEFAULT_USER_GAVE_POSITIVE_FEEDBACK_MAXIMUM_COUNT))
                .addLastEventVersionNameRule(USER_GAVE_CRITICAL_FEEDBACK, new VersionNameChangedRule(context))
                .addLastEventVersionNameRule(USER_DECLINED_CRITICAL_FEEDBACK, new VersionNameChangedRule(context))
                .addLastEventVersionNameRule(USER_DECLINED_POSITIVE_FEEDBACK, new VersionNameChangedRule(context));
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

        if (event == USER_GAVE_POSITIVE_FEEDBACK) {
            final Activity activity = activityReferenceManager.getValidatedActivity();

            if (activity != null) {
                for (final IFeedbackCollector positiveFeedbackCollector : positiveFeedbackCollectors) {
                    final boolean feedbackCollected = positiveFeedbackCollector.collectFeedback(activity);
                    if (feedbackCollected) {
                        return;
                    }
                }
            }
        } else if (event == USER_GAVE_CRITICAL_FEEDBACK) {
            final Activity activity = activityReferenceManager.getValidatedActivity();

            if (activity != null) {
                for (final IFeedbackCollector criticalFeedbackCollector : criticalFeedbackCollectors) {
                    final boolean feedbackCollected = criticalFeedbackCollector.collectFeedback(activity);
                    if (feedbackCollected) {
                        return;
                    }
                }
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
        return positiveFeedbackCollectors != null && criticalFeedbackCollectors != null;
    }

}
