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
package com.github.stkent.amplify.tracking.checks;

import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.interfaces.IApplicationInfoProvider;
import com.github.stkent.amplify.tracking.interfaces.IEventCheck;

public final class MaximumCountCheck implements IEventCheck<Integer> {

    private final int maximumCount;

    public MaximumCountCheck(final int maximumCount) {
        this.maximumCount = maximumCount;
    }

    @Override
    public boolean shouldBlockFeedbackPrompt(@NonNull final Integer cachedEventValue, @NonNull final IApplicationInfoProvider applicationInfoProvider) {
        return cachedEventValue >= maximumCount;
    }

    @NonNull
    @Override
    public String getStatusString(@NonNull final Integer cachedEventValue, @NonNull final IApplicationInfoProvider applicationInfoProvider) {
        return "Maximum allowed event count: " + maximumCount + ". Current event count: " + cachedEventValue + ".";
    }

    @NonNull
    @Override
    public String getTrackingKey() {
        return "MAXIMUM_COUNT_CHECK";
    }

}
