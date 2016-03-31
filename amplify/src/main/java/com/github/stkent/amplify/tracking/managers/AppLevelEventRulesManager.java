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
package com.github.stkent.amplify.tracking.managers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.stkent.amplify.ILogger;
import com.github.stkent.amplify.tracking.AmplifyExceptionHandler;
import com.github.stkent.amplify.tracking.interfaces.IAppEventTimeProvider;
import com.github.stkent.amplify.tracking.interfaces.IAppLevelEventRulesManager;
import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.IEventBasedRule;
import com.github.stkent.amplify.tracking.interfaces.IEventsManager;
import com.github.stkent.amplify.tracking.interfaces.ISettings;
import com.github.stkent.amplify.tracking.rules.CooldownDaysRule;

public final class AppLevelEventRulesManager implements IAppLevelEventRulesManager {

    private static final IEvent APP_CRASHED = new IEvent() {
        @NonNull
        @Override
        public String getTrackingKey() {
            return "APP_CRASHED";
        }
    };

    @NonNull
    private final ISettings<Long> settings;

    @NonNull
    private final ILogger logger;

    @NonNull
    private final IAppEventTimeProvider appEventTimeProvider;

    @Nullable
    private IEventBasedRule<Long> installTimeRule;

    @Nullable
    private IEventBasedRule<Long> lastUpdateTimeRule;

    @Nullable
    private IEventsManager<Long> lastAppCrashedTimeManager;

    public AppLevelEventRulesManager(
            @NonNull final ISettings<Long> settings,
            @NonNull final IAppEventTimeProvider appEventTimeProvider,
            @NonNull final ILogger logger) {

        this.settings = settings;
        this.appEventTimeProvider = appEventTimeProvider;
        this.logger = logger;
    }

    @Override
    public boolean shouldAllowFeedbackPrompt() {
        boolean result = true;

        if (installTimeRule != null) {
            final long installTime = appEventTimeProvider.getInstallTime();

            //noinspection ConstantConditions
            boolean installResult = installTimeRule.shouldAllowFeedbackPrompt(installTime);

            if (!installResult) {
                logger.d("Blocking prompt based on install time");
            }

            result = installResult;
        }

        if (lastUpdateTimeRule != null) {
            final long lastUpdateTime = appEventTimeProvider.getLastUpdateTime();

            boolean lastUpdateResult = lastUpdateTimeRule.shouldAllowFeedbackPrompt(lastUpdateTime);

            if (!lastUpdateResult) {
                logger.d("Blocking prompt based on last update time");
            }

            result = result && lastUpdateResult;
        }

        if (lastAppCrashedTimeManager != null) {
            result = result && lastAppCrashedTimeManager.shouldAllowFeedbackPrompt();
        }

        return result;
    }

    @Override
    public void setInstallTimeCooldownDays(final int cooldownPeriodDays) {
        installTimeRule = new CooldownDaysRule(cooldownPeriodDays);

        // This log message is morally correct, but technically misleading (we do not explicitly
        // track an IEvent implementation called APP_INSTALLED).
        logger.d("Registered " + installTimeRule.getDescription() + " for event APP_INSTALLED");
    }

    @Override
    public void setLastUpdateTimeCooldownDays(final int cooldownPeriodDays) {
        lastUpdateTimeRule = new CooldownDaysRule(cooldownPeriodDays);

        // This log message is morally correct, but technically misleading (we do not explicitly
        // track an IEvent implementation called APP_UPDATED).
        logger.d("Registered " + lastUpdateTimeRule.getDescription() + " for event APP_UPDATED");
    }

    @Override
    public void setLastCrashTimeCooldownDays(final int cooldownPeriodDays) {
        lastAppCrashedTimeManager = new LastEventTimeRulesManager(settings, logger);
        lastAppCrashedTimeManager.addEventBasedRule(
                APP_CRASHED, new CooldownDaysRule(cooldownPeriodDays));

        final Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler
                = Thread.getDefaultUncaughtExceptionHandler();

        if (!(defaultUncaughtExceptionHandler instanceof AmplifyExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(
                    new AmplifyExceptionHandler(this, Thread.getDefaultUncaughtExceptionHandler()));
        }
    }

    @Override
    public void notifyOfCrash() {
        if (lastAppCrashedTimeManager != null) {
            lastAppCrashedTimeManager.notifyEventTriggered(APP_CRASHED);
        }
    }

}
