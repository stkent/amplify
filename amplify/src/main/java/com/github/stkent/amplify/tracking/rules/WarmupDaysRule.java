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
package com.github.stkent.amplify.tracking.rules;

import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.interfaces.IPromptRule;
import com.github.stkent.amplify.utils.time.SystemTimeUtil;

import java.util.concurrent.TimeUnit;

public class WarmupDaysRule implements IPromptRule<Long> {

    private final long warmupPeriodDays;

    public WarmupDaysRule(final long warmupPeriodDays) {
        this.warmupPeriodDays = warmupPeriodDays;
    }

    @Override
    public boolean shouldAllowFeedbackPrompt(@NonNull final Long cachedEventValue) {
        return cachedEventValue != Long.MAX_VALUE
                && (SystemTimeUtil.currentTimeMillis() - cachedEventValue) > TimeUnit.DAYS.toMillis(warmupPeriodDays);
    }

    @NonNull
    @Override
    public String getStatusString(@NonNull final Long cachedEventValue) {
        final Long daysSinceFirstEvent = TimeUnit.MILLISECONDS.toDays(SystemTimeUtil.currentTimeMillis() - cachedEventValue);
        return "Warmup period: " + warmupPeriodDays + " days. Time since first event: " + daysSinceFirstEvent + " days.";
    }

}
