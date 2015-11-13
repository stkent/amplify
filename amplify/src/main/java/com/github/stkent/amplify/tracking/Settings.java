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

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.ISettings;

public final class Settings implements ISettings {

    private static final String SHARED_PREFERENCES_NAME = "AMPLIFY_SHARED_PREFERENCES_NAME";

    private static Settings sharedInstance;

    private final SharedPreferences sharedPreferences;

    public static Settings getSharedInstance(@NonNull final Context context) {
        synchronized (AmplifyStateTracker.class) {
            if (sharedInstance == null) {
                sharedInstance = new Settings(context);
            }
        }

        return sharedInstance;
    }

    private Settings(@NonNull final Context applicationContext) {
        this.sharedPreferences = applicationContext
                .getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public int getTotalEventCount(@NonNull final IEvent event) {
        final String key = generateTotalEventCountTrackingKey(event);
        return sharedPreferences.getInt(key, 0);
    }

    @Override
    public void setTotalEventCount(@NonNull final IEvent event, final int totalEventCount) {
        final String key = generateTotalEventCountTrackingKey(event);
        sharedPreferences.edit().putInt(key, totalEventCount).apply();
    }

    @Override
    public long getFirstEventTime(@NonNull final IEvent event) {
        final String key = generateFirstEventTimeTrackingKey(event);
        return sharedPreferences.getLong(key, Long.MAX_VALUE);
    }

    @Override
    public void setFirstEventTime(@NonNull final IEvent event, final long lastEventTime) {
        if (getFirstEventTime(event) != Long.MAX_VALUE) {
            throw new IllegalArgumentException(
                    "First event time is already set; cannot be overwritten");
        }

        final String key = generateFirstEventTimeTrackingKey(event);
        sharedPreferences.edit().putLong(key, lastEventTime).apply();
    }

    @Override
    public long getLastEventTime(@NonNull final IEvent event) {
        final String key = generateTotalEventCountTrackingKey(event);
        return sharedPreferences.getLong(key, 0);
    }

    @Override
    public void setLastEventTime(@NonNull final IEvent event, final long lastEventTime) {
        final String key = generateLastEventTimeTrackingKey(event);
        sharedPreferences.edit().putLong(key, lastEventTime).apply();
    }

    @Nullable
    @Override
    public String getLastEventVersion(@NonNull final IEvent event) {
        final String key = generateTotalEventCountTrackingKey(event);
        return sharedPreferences.getString(key, null);
    }

    @Override
    public void setLastEventVersion(@NonNull final IEvent event, @NonNull final String lastEventVersion) {
        final String key = generateLastEventVersionTrackingKey(event);
        sharedPreferences.edit().putString(key, lastEventVersion).apply();
    }

    @Override
    public void reset() {
        sharedPreferences.edit().clear().apply();
    }

    private String generateTotalEventCountTrackingKey(@NonNull final IEvent event) {
        return "TOTAL_EVENT_COUNT_" + event.getTrackingKey();
    }

    private String generateFirstEventTimeTrackingKey(@NonNull final IEvent event) {
        return "FIRST_EVENT_TIME_" + event.getTrackingKey();
    }

    private String generateLastEventTimeTrackingKey(@NonNull final IEvent event) {
        return "LAST_EVENT_TIME_" + event.getTrackingKey();
    }

    private String generateLastEventVersionTrackingKey(@NonNull final IEvent event) {
        return "LAST_EVENT_VERSION_" + event.getTrackingKey();
    }

}
