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
    public void testThatCheckBlocksPrompt_ifCooldownPeriodHasNotPassed() {
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
    public void testThatCheckDoesNotBlockPrompt_ifCooldownPeriodHasPassed() {
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
