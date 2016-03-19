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
import android.support.annotation.VisibleForTesting;

import com.github.stkent.amplify.ILogger;
import com.github.stkent.amplify.tracking.Settings;
import com.github.stkent.amplify.tracking.interfaces.IAppInfoProvider;
import com.github.stkent.amplify.tracking.interfaces.ISettings;

public class LastEventVersionCodeRulesManager extends BaseEventsManager<Integer> {

    @NonNull
    private final IAppInfoProvider appInfoProvider;

    public LastEventVersionCodeRulesManager(
            @NonNull final Context appContext,
            @NonNull final IAppInfoProvider appInfoProvider,
            @NonNull final ILogger logger) {

        this(new Settings<Integer>(appContext), appInfoProvider, logger);
    }

    @VisibleForTesting
    protected LastEventVersionCodeRulesManager(
            @NonNull final ISettings<Integer> settings,
            @NonNull final IAppInfoProvider appInfoProvider,
            @NonNull final ILogger logger) {

        super(settings, logger);
        this.appInfoProvider = appInfoProvider;
    }

    @NonNull
    @Override
    protected String getTrackedEventDimensionDescription() {
        return "Last version code";
    }

    @NonNull
    @Override
    protected String getEventTrackingStatusStringSuffix(@NonNull final Integer cachedEventValue) {
        return " last occurred for app version code " + cachedEventValue;
    }

    @NonNull
    @Override
    public Integer getUpdatedTrackingValue(@Nullable final Integer cachedTrackingValue) {
        return appInfoProvider.getPackageInfo().versionCode;
    }

}
