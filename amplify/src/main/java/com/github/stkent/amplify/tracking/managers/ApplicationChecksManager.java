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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.stkent.amplify.ILogger;
import com.github.stkent.amplify.tracking.AmplifyExceptionHandler;
import com.github.stkent.amplify.tracking.checks.CooldownDaysCheck;
import com.github.stkent.amplify.tracking.interfaces.IApplicationChecksManager;
import com.github.stkent.amplify.tracking.interfaces.IApplicationInfoProvider;
import com.github.stkent.amplify.tracking.interfaces.IEventCheck;
import com.github.stkent.amplify.tracking.interfaces.ITrackableEvent;
import com.github.stkent.amplify.tracking.interfaces.ITrackableEventsManager;

public class ApplicationChecksManager implements IApplicationChecksManager {

    private static final ITrackableEvent APP_CRASHED = new ITrackableEvent() {
        @NonNull
        @Override
        public String getTrackingKey() {
            return "APP_CRASHED";
        }
    };

    @NonNull
    private final Context applicationContext;

    @NonNull
    private final ILogger logger;

    @NonNull
    private final IApplicationInfoProvider applicationInfoProvider;

    @Nullable
    private IEventCheck<Long> installTimeEventCheck;

    @Nullable
    private IEventCheck<Long> lastUpdateTimeTimeEventCheck;

    @Nullable
    private ITrackableEventsManager<Long> lastCrashTimeManager;

    public ApplicationChecksManager(
            @NonNull final Context applicationContext,
            @NonNull final IApplicationInfoProvider applicationInfoProvider,
            @NonNull final ILogger logger) {
        this.applicationContext = applicationContext;
        this.applicationInfoProvider = applicationInfoProvider;
        this.logger = logger;
    }

    @Override
    public boolean shouldAllowFeedbackPrompt() {
        boolean result = true;

        if (installTimeEventCheck != null) {
            final long installTime = applicationInfoProvider.getInstallTime();

            //noinspection ConstantConditions
            result = result && installTimeEventCheck.shouldAllowFeedbackPrompt(installTime);
        }

        if (lastUpdateTimeTimeEventCheck != null) {
            final long lastUpdateTime = applicationInfoProvider.getLastUpdateTime();
            result = result && lastUpdateTimeTimeEventCheck.shouldAllowFeedbackPrompt(lastUpdateTime);
        }

        if (lastCrashTimeManager != null) {
            result = result && lastCrashTimeManager.shouldAllowFeedbackPrompt();
        }

        return result;
    }

    @Override
    public void setInstallTimeCooldownDays(final int cooldownPeriodDays) {
        installTimeEventCheck = new CooldownDaysCheck(cooldownPeriodDays);
    }

    @Override
    public void setLastUpdateTimeCooldownDays(final int cooldownPeriodDays) {
        lastUpdateTimeTimeEventCheck = new CooldownDaysCheck(cooldownPeriodDays);
    }

    @Override
    public void setLastCrashTimeCooldownDays(final int cooldownPeriodDays) {
        lastCrashTimeManager = new LastEventTimesManager(logger, applicationContext);
        lastCrashTimeManager.trackEvent(APP_CRASHED, new CooldownDaysCheck(cooldownPeriodDays));

        final Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

        if (!(defaultUncaughtExceptionHandler instanceof AmplifyExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(
                    new AmplifyExceptionHandler(this, Thread.getDefaultUncaughtExceptionHandler()));
        }
    }

    @Override
    public void notifyOfCrash() {
        if (lastCrashTimeManager != null) {
            lastCrashTimeManager.notifyEventTriggered(APP_CRASHED);
        } else {
            throw new IllegalStateException(
                    "The last crash time manager must be initialized before any app crashes can be reported.");
        }
    }

}
