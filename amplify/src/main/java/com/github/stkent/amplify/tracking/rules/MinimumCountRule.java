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

public final class MinimumCountRule implements IEventBasedRule<Integer> {

    private final int minimumCount;

    public MinimumCountRule(final int minimumCount) {
        if (minimumCount <= 0) {
            throw new IllegalStateException(
                    "Minimum count rule must be configured with a positive threshold");
        }

        this.minimumCount = minimumCount;
    }

    @Override
    public boolean shouldAllowFeedbackPromptByDefault() {
        return false;
    }

    @Override
    public boolean shouldAllowFeedbackPrompt(@NonNull final Integer cachedEventValue) {
        return cachedEventValue >= minimumCount;
    }

    @NonNull
    @Override
    public String getDescription() {
        return "MinimumCountRule with minimum required count of " + minimumCount;
    }

}
