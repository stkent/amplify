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
import android.support.annotation.VisibleForTesting;

import com.github.stkent.amplify.ILogger;
import com.github.stkent.amplify.tracking.Settings;
import com.github.stkent.amplify.tracking.interfaces.ISettings;
import com.github.stkent.amplify.utils.time.SystemTimeUtil;

public class LastEventTimesManager extends BaseEventManager<Long> {

    public LastEventTimesManager(@NonNull final ILogger logger, @NonNull final Context applicationContext) {
        this(logger, new Settings<Long>(applicationContext, logger));
    }

    @VisibleForTesting
    protected LastEventTimesManager(
            @NonNull final ILogger logger,
            @NonNull final ISettings<Long> settings) {
        super(logger, settings);
    }

    @NonNull
    @Override
    protected String getTrackingKeySuffix() {
        return getClass().getSimpleName();
    }

    @NonNull
    @Override
    public Long defaultTrackingValue() {
        return Long.MAX_VALUE;
    }

    @NonNull
    @Override
    public Long getUpdatedTrackingValue(@NonNull final Long cachedTrackingValue) {
        return SystemTimeUtil.currentTimeMillis();
    }

}
