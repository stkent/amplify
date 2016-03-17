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

public class LastEventVersionRulesManager extends BaseEventsManager<String> {

    @NonNull
    private final IAppInfoProvider appInfoProvider;

    public LastEventVersionRulesManager(
            @NonNull final Context appContext,
            @NonNull final IAppInfoProvider appInfoProvider,
            @NonNull final ILogger logger) {

        this(logger, appInfoProvider, new Settings<String>(appContext, logger));
    }

    @VisibleForTesting
    protected LastEventVersionRulesManager(
            @NonNull final ILogger logger,
            @NonNull final IAppInfoProvider appInfoProvider,
            @NonNull final ISettings<String> settings) {

        super(logger, settings);
        this.appInfoProvider = appInfoProvider;
    }

    @NonNull
    @Override
    protected String getTrackingKeySuffix() {
        return "LASTEVENTVERSIONSMANAGER";
    }

    @NonNull
    @Override
    public String getUpdatedTrackingValue(@Nullable final String cachedTrackingValue) {
        return appInfoProvider.getPackageInfo().versionName;
    }

}
