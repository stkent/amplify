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

public final class CooldownDaysRule implements IEventBasedRule<Long> {

    private final long cooldownPeriodDays;

    public CooldownDaysRule(final long cooldownPeriodDays) {
        if (cooldownPeriodDays <= 0) {
            throw new IllegalStateException(
                    "Cooldown days rule must be configured with a positive cooldown period");
        }

        this.cooldownPeriodDays = cooldownPeriodDays;
    }

    @Override
    public boolean shouldAllowFeedbackPromptByDefault() {
        return true;
    }

    @Override
    public boolean shouldAllowFeedbackPrompt(@NonNull final Long cachedEventValue) {
        return (SystemTimeUtil.currentTimeMillis() - cachedEventValue) >= DAYS.toMillis(cooldownPeriodDays);
    }

    @NonNull
    @Override
    public String getDescription() {
        return "CooldownDaysRule with a cooldown period of "
                + cooldownPeriodDays + " day" + (cooldownPeriodDays > 1 ? "s" : "");
    }

}
