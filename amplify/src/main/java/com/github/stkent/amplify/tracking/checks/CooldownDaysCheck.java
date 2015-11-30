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

import com.github.stkent.amplify.utils.time.SystemTimeUtil;
import com.github.stkent.amplify.tracking.interfaces.IApplicationInfoProvider;
import com.github.stkent.amplify.tracking.interfaces.IEventCheck;

import java.util.concurrent.TimeUnit;

public final class CooldownDaysCheck implements IEventCheck<Long> {

    private final long cooldownPeriodDays;

    public CooldownDaysCheck(final long cooldownPeriodDays) {
        this.cooldownPeriodDays = cooldownPeriodDays;
    }

    @Override
    public boolean shouldAllowFeedbackPrompt(@NonNull final Long cachedEventValue, @NonNull final IApplicationInfoProvider applicationInfoProvider) {
        return (SystemTimeUtil.currentTimeMillis() - cachedEventValue) >= TimeUnit.DAYS.toMillis(cooldownPeriodDays);
    }

    @NonNull
    @Override
    public String getStatusString(@NonNull final Long cachedEventValue, @NonNull final IApplicationInfoProvider applicationInfoProvider) {
        final Long daysSinceLastEvent = TimeUnit.MILLISECONDS.toDays(SystemTimeUtil.currentTimeMillis() - cachedEventValue);
        return "Cooldown period: " + cooldownPeriodDays + " days. Time since last event: " + daysSinceLastEvent + " days.";
    }

}
