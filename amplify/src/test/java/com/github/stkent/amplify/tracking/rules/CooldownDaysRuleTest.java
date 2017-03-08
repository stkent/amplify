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

import android.annotation.SuppressLint;

import com.github.stkent.amplify.helpers.BaseTest;
import com.github.stkent.amplify.utils.time.SystemTimeUtil;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CooldownDaysRuleTest extends BaseTest {

    @SuppressLint("Assert")
    @SuppressWarnings("ConstantConditions")
    @Test
    public void testThatRuleAllowsPromptIfEventHasNeverOccurred() {
        // Arrange
        final int anyPositiveInteger = 1;
        assert anyPositiveInteger > 0;

        final CooldownDaysRule cooldownDaysRule = new CooldownDaysRule(anyPositiveInteger);

        // Act
        final boolean ruleShouldAllowFeedbackPrompt
                = cooldownDaysRule.shouldAllowFeedbackPromptByDefault();

        // Assert
        assertTrue(
                "Feedback prompt should be allowed if the associated event has never occurred.",
                ruleShouldAllowFeedbackPrompt);
    }

    @SuppressLint("Assert")
    @SuppressWarnings("ConstantConditions")
    @Test
    public void testThatRuleBlocksPromptIfCooldownPeriodHasNotPassed() {
        // Arrange
        final int cooldownTimeDays = 7;
        final int daysSinceLastEvent = 2;
        assert daysSinceLastEvent < cooldownTimeDays;

        final CooldownDaysRule cooldownDaysRule = new CooldownDaysRule(cooldownTimeDays);
        final long lastEventTime = SystemTimeUtil.currentTimeMillis() - TimeUnit.DAYS.toMillis(daysSinceLastEvent);

        // Act
        final boolean ruleShouldAllowFeedbackPrompt
                = cooldownDaysRule.shouldAllowFeedbackPrompt(lastEventTime);

        // Assert
        assertFalse(
                "Feedback prompt should be blocked if the cooldown period has not passed",
                ruleShouldAllowFeedbackPrompt);
    }

    @SuppressLint("Assert")
    @SuppressWarnings("ConstantConditions")
    @Test
    public void testThatRuleAllowsPromptIfCooldownPeriodHasPassed() {
        // Arrange
        final int cooldownTimeDays = 7;
        final int daysSinceLastEvent = 9;
        assert daysSinceLastEvent > cooldownTimeDays;

        final CooldownDaysRule cooldownDaysRule = new CooldownDaysRule(cooldownTimeDays);
        final long lastEventTime = SystemTimeUtil.currentTimeMillis() - TimeUnit.DAYS.toMillis(daysSinceLastEvent);

        // Act
        final boolean ruleShouldAllowFeedbackPrompt
                = cooldownDaysRule.shouldAllowFeedbackPrompt(lastEventTime);

        // Assert
        assertTrue(
                "Feedback prompt should be allowed if the cooldown period has passed",
                ruleShouldAllowFeedbackPrompt);
    }

}
