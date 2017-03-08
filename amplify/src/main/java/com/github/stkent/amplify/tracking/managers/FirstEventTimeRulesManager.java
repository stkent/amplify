/*
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

import com.github.stkent.amplify.tracking.interfaces.ISettings;
import com.github.stkent.amplify.utils.time.SystemTimeUtil;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public final class FirstEventTimeRulesManager extends BaseEventsManager<Long> {

    public FirstEventTimeRulesManager(@NonNull final ISettings<Long> settings) {
        super(settings);
    }

    @NonNull
    @Override
    protected String getTrackedEventDimensionDescription() {
        return "First time";
    }

    @NonNull
    @Override
    protected String getEventTrackingStatusStringSuffix(@NonNull final Long cachedEventValue) {
        final Long daysSinceFirstEvent = MILLISECONDS.toDays(SystemTimeUtil.currentTimeMillis() - cachedEventValue);
        return "first occurred " + daysSinceFirstEvent + " days ago";
    }

    @NonNull
    @Override
    public Long getUpdatedTrackingValue(@Nullable final Long cachedTrackingValue) {
        final long currentTimeMillis = SystemTimeUtil.currentTimeMillis();

        if (cachedTrackingValue == null) {
            return currentTimeMillis;
        } else {
            return Math.min(cachedTrackingValue, currentTimeMillis);
        }
    }

}
