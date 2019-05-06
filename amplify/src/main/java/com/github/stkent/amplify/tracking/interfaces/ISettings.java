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
package com.github.stkent.amplify.tracking.interfaces;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * An abstract representation of a class capable of persisting values across application launches.
 *
 * @param <T> the type of the value that can be written and read by implementations (Integer, Long
 *        or String)
 */
public interface ISettings<T> {

    /**
     * Write a new value to persistent storage.
     *
     * @param trackingKey the key with which to associate the new value
     * @param value the new value to be persisted
     */
    void writeTrackingValue(@NonNull String trackingKey, T value);

    /**
     * Retrieve a value (if it exists) from persistent storage.
     *
     * @param trackingKey the key whose associated value we wish to retrieve
     * @return the persisted value, if it exists; null otherwise
     */
    @Nullable
    T readTrackingValue(@NonNull String trackingKey);

}
