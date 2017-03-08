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
import com.github.stkent.amplify.utils.time.SystemTimeUtil;

import static java.util.concurrent.TimeUnit.DAYS;

public final class WarmupDaysRule implements IEventBasedRule<Long> {

    private final long warmupPeriodDays;

    public WarmupDaysRule(final long warmupPeriodDays) {
        if (warmupPeriodDays <= 0) {
            throw new IllegalStateException(
                    "Warmup days rule must be configured with a positive warmup period");
        }

        this.warmupPeriodDays = warmupPeriodDays;
    }

    @Override
    public boolean shouldAllowFeedbackPromptByDefault() {
        return false;
    }

    @Override
    public boolean shouldAllowFeedbackPrompt(@NonNull final Long cachedEventValue) {
        return SystemTimeUtil.currentTimeMillis() - cachedEventValue > DAYS.toMillis(warmupPeriodDays);
    }

    @NonNull
    @Override
    public String getDescription() {
        return "WarmupDaysRule with a warmup period of "
                + warmupPeriodDays + " day" + (warmupPeriodDays > 1 ? "s" : "");
    }

}
