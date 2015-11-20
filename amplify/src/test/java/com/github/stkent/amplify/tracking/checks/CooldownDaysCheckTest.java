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

import android.annotation.SuppressLint;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CooldownDaysCheckTest {

    @SuppressLint("Assert")
    @SuppressWarnings("ConstantConditions")
    @Test
    public void testThatCheckBlocksPromptIfCooldownPeriodHasNotPassed() {
        // Arrange
        final int cooldownTimeDays = 7;
        final int daysSinceLastEvent = 2;

        assert daysSinceLastEvent < cooldownTimeDays;

        final CooldownDaysCheck cooldownDaysCheck = new CooldownDaysCheck(cooldownTimeDays);
        final long lastEventTime = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(daysSinceLastEvent);

        // Act
        // todo: figure out what to pass instead of the null context here
        final boolean checkShouldBlockFeedbackPrompt
                = cooldownDaysCheck.shouldBlockFeedbackPrompt(lastEventTime, null);

        // Assert
        assertTrue("Feedback prompt should be blocked if the cooldown period has not passed", checkShouldBlockFeedbackPrompt);
    }

    @SuppressLint("Assert")
    @SuppressWarnings("ConstantConditions")
    @Test
    public void testThatCheckDoesNotBlockPromptIfCooldownPeriodHasPassed() {
        // Arrange
        final int cooldownTimeDays = 7;
        final int daysSinceLastEvent = 9;

        assert daysSinceLastEvent > cooldownTimeDays;

        final CooldownDaysCheck cooldownDaysCheck = new CooldownDaysCheck(cooldownTimeDays);
        final long lastEventTime = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(daysSinceLastEvent);

        // Act
        final boolean checkShouldBlockFeedbackPrompt
                = cooldownDaysCheck.shouldBlockFeedbackPrompt(lastEventTime, null);

        // Assert
        assertFalse("Feedback prompt should not be blocked if the cooldown period has passed",
                checkShouldBlockFeedbackPrompt);
    }

}
