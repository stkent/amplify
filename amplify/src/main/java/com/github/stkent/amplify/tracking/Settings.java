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

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.stkent.amplify.tracking.interfaces.ISettings;

import java.util.Map;

public final class Settings<T> implements ISettings<T> {

    private final SharedPreferences sharedPreferences;

    public Settings(@NonNull final SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void writeTrackingValue(@NonNull final String trackingKey, final T value) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        if (value.getClass().equals(String.class)) {
            editor.putString(trackingKey, (String) value);
        } else if (value.getClass().equals(Boolean.class)) {
            editor.putBoolean(trackingKey, (Boolean) value);
        } else if (value.getClass().equals(Long.class)) {
            editor.putLong(trackingKey, (Long) value);
        } else if (value.getClass().equals(Integer.class)) {
            editor.putInt(trackingKey, (Integer) value);
        } else if (value.getClass().equals(Float.class)) {
            editor.putLong(trackingKey, (Long) value);
        } else {
            throw new IllegalArgumentException(
                    "Event value must be one of String, Boolean, Long, Integer or Float");
        }

        editor.apply();
    }

    @Nullable
    public T readTrackingValue(@NonNull final String trackingKey) {
        final Map<String, ?> map = sharedPreferences.getAll();

        for (final Map.Entry<String, ?> entry : map.entrySet()) {
            if (entry.getKey().equals(trackingKey)) {
                return (T) entry.getValue();
            }
        }

        return null;
    }

}
