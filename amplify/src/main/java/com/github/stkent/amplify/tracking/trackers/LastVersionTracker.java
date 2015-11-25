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
package com.github.stkent.amplify.tracking.trackers;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.github.stkent.amplify.ILogger;
import com.github.stkent.amplify.tracking.ApplicationInfoProvider;
import com.github.stkent.amplify.tracking.Settings;
import com.github.stkent.amplify.tracking.interfaces.IApplicationInfoProvider;
import com.github.stkent.amplify.tracking.interfaces.ISettings;

public class LastVersionTracker extends PublicEventTracker<String> {

    public LastVersionTracker(@NonNull final ILogger logger, @NonNull final Context applicationContext) {
        this(logger, new Settings<String>(applicationContext, logger), new ApplicationInfoProvider(applicationContext));
    }

    @VisibleForTesting
    protected LastVersionTracker(
            @NonNull final ILogger logger,
            @NonNull final ISettings<String> settings,
            @NonNull final IApplicationInfoProvider applicationInfoProvider) {
        super(logger, settings, applicationInfoProvider);
    }

    @NonNull
    @Override
    protected String getTrackingKeySuffix() {
        return getClass().getSimpleName();
    }

    @NonNull
    @Override
    public String defaultTrackingValue() {
        return "";
    }

    @NonNull
    @Override
    public String getUpdatedTrackingValue(@NonNull final String cachedTrackingValue) {
        try {
            return getApplicationInfoProvider().getVersionName();
        } catch (final PackageManager.NameNotFoundException e) {
            getLogger().d("Could not read current app version name.");
            return cachedTrackingValue;
        }
    }

}
