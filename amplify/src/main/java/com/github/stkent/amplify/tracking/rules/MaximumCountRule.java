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
package com.github.stkent.amplify.tracking.rules;

import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.interfaces.IEventBasedRule;

public final class MaximumCountRule implements IEventBasedRule<Integer> {

    private final int maximumCount;

    public MaximumCountRule(final int maximumCount) {
        if (maximumCount <= 0) {
            throw new IllegalStateException(
                    "Maximum count rule must be configured with a positive threshold");
        }

        this.maximumCount = maximumCount;
    }

    @Override
    public boolean shouldAllowFeedbackPromptByDefault() {
        return true;
    }

    @Override
    public boolean shouldAllowFeedbackPrompt(@NonNull final Integer cachedEventValue) {
        return cachedEventValue < maximumCount;
    }

    @NonNull
    @Override
    public String getDescription() {
        return "MaximumCountRule with maximum allowed count of " + maximumCount;
    }

}
