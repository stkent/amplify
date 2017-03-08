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
package com.github.stkent.amplify.helpers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.stkent.amplify.tracking.interfaces.ISettings;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An in-memory implementation of ISettings.
 */
public class FakeSettings<T> implements ISettings<T> {

    private final Map<String, T> mostRecentValuesWritten = new ConcurrentHashMap<>();

    @Override
    public void writeTrackingValue(@NonNull final String trackingKey, final T value) {
        mostRecentValuesWritten.put(trackingKey, value);
    }

    @Nullable
    @Override
    public T readTrackingValue(@NonNull final String trackingKey) {
        return mostRecentValuesWritten.get(trackingKey);
    }

}
