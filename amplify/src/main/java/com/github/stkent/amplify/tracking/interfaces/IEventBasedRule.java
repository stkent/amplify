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

/**
 * An abstract representation of a prompt timing rule that depends on a tracked event.
 *
 * @param <T> the type of the value tracked by this event (Integer, Long or String)
 */
public interface IEventBasedRule<T> extends IRule {

    /**
     * This method should only be called if the associated event has never occurred before.
     *
     * @return true if this rule should allow the feedback prompt to be shown if the associated
     *         event has never occurred before; false otherwise
     */
    boolean shouldAllowFeedbackPromptByDefault();

    /**
     * This method should only be called if the associated event has occurred before.
     *
     * @param cachedEventValue the currently cached value for the associated event
     * @return true if this rule is satisfied and should allow the feedback prompt to be shown;
     *         false otherwise
     */
    boolean shouldAllowFeedbackPrompt(@NonNull T cachedEventValue);


}
