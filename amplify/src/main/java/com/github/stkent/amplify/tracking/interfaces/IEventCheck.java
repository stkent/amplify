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
package com.github.stkent.amplify.tracking.interfaces;

import android.support.annotation.NonNull;

/**
 * An abstract representation of an event-dependent check that must be satisfied whenever we prompt
 * the user for feedback.
 *
 * @param <T> the type of the value tracked by this event (Integer, Long or String)
 */
public interface IEventCheck<T> {

    /**
     * Call this method to determine whether or not to prompt the user for feedback at this time.
     * @param cachedEventValue the current value associated with the tracked event this check is
     *        associated with
     * @return true if this check is satisfied and should allow the feedback prompt to be shown;
     *         false otherwise
     */
    boolean shouldAllowFeedbackPrompt(@NonNull final T cachedEventValue);

    /**
     * @param cachedEventValue the current value associated with the tracked event this check is
     *        applied to
     * @return a string representation of the current check status; primarily used for debugging
     */
    @NonNull
    String getStatusString(@NonNull final T cachedEventValue);

}
