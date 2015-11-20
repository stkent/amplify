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

import com.github.stkent.amplify.tracking.interfaces.ILogger;
import com.github.stkent.amplify.tracking.interfaces.ISettings;
import com.github.stkent.amplify.tracking.interfaces.ITrackedEvent;

import java.util.Map;

public class Settings<T> implements ISettings<T> {

    private static final String SHARED_PREFERENCES_NAME = "AMPLIFY_SHARED_PREFERENCES_NAME";

    private final SharedPreferences sharedPreferences;
    private final ILogger logger;

    public Settings(Context applicationContext, ILogger logger) {
        this.logger = logger;
        this.sharedPreferences = applicationContext
                .getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public void writeEventValue(@NonNull final ITrackedEvent event, final T value) {

        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (value.getClass().equals(String.class)) {
            editor.putString(event.getTrackingKey(), (String) value);
        } else if (value.getClass().equals(Boolean.class)) {
            editor.putBoolean(event.getTrackingKey(), (Boolean) value);
        } else if (value.getClass().equals(Long.class)) {
            editor.putLong(event.getTrackingKey(), (Long) value);
        } else if (value.getClass().equals(Integer.class)) {
            editor.putInt(event.getTrackingKey(), (Integer) value);
        } else if (value.getClass().equals(Float.class)) {
            editor.putLong(event.getTrackingKey(), (Long) value);
        } else {
            throw new IllegalArgumentException("Event value must be one of String, Boolean, Long, Integer or Float");
        }

        //TODO is it alright that this is asynchronous?
        editor.apply();
    }

    @Nullable
    public T getEventValue(@NonNull final ITrackedEvent event) {
        final Map<String, ?> map = sharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : map.entrySet()) {
            if (entry.getKey().equals(event.getTrackingKey())) {
                return (T) entry.getValue();
            }
        }

        logger.e("No event value for " + event);

        return null;
    }

    public boolean hasEventValue(@NonNull final ITrackedEvent event) {
        return sharedPreferences.contains(event.getTrackingKey());
    }

}
