package com.github.stkent.amplify.tracking.checks;

import android.annotation.SuppressLint;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WarmUpDaysCheckTest {

    @SuppressLint("Assert")
    @SuppressWarnings("ConstantConditions")
    @Test
    public void testThatCheckBlocksPrompt_ifEventHasNotOccurredYet() {
        // Arrange
        final int warmUpTimeDays = 7;

        final WarmUpDaysCheck warmUpDaysCheck = new WarmUpDaysCheck(warmUpTimeDays);

        // fixme: refactor to avoid this nasty dependency on a magic number
        final long lastEventTime = Long.MAX_VALUE;

        // Act
        // todo: figure out what to pass instead of the null context here
        final boolean checkShouldBlockFeedbackPrompt
                = warmUpDaysCheck.shouldBlockFeedbackPrompt(lastEventTime, null);

        // Assert
        assertTrue("Feedback prompt should be blocked if the event has not occurred yet",
                checkShouldBlockFeedbackPrompt);
    }

    @SuppressLint("Assert")
    @SuppressWarnings("ConstantConditions")
    @Test
    public void testThatCheckBlocksPrompt_ifWarmUpPeriodHasNotPassed() {
        // Arrange
        final int warmUpTimeDays = 7;
        final int daysSinceLastEvent = 2;

        assert daysSinceLastEvent < warmUpTimeDays;

        final WarmUpDaysCheck warmUpDaysCheck = new WarmUpDaysCheck(warmUpTimeDays);
        final long lastEventTime = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(daysSinceLastEvent);

        // Act
        // todo: figure out what to pass instead of the null context here
        final boolean checkShouldBlockFeedbackPrompt
                = warmUpDaysCheck.shouldBlockFeedbackPrompt(lastEventTime, null);

        // Assert
        assertTrue("Feedback prompt should be blocked if the warm-up period has not passed",
                checkShouldBlockFeedbackPrompt);
    }

    @SuppressLint("Assert")
    @SuppressWarnings("ConstantConditions")
    @Test
    public void testThatCheckDoesNotBlockPrompt_ifWarmUpPeriodHasPassed() {
        // Arrange
        final int warmUpTimeDays = 7;
        final int daysSinceLastEvent = 9;

        assert daysSinceLastEvent > warmUpTimeDays;

        final WarmUpDaysCheck warmUpDaysCheck = new WarmUpDaysCheck(warmUpTimeDays);
        final long lastEventTime = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(daysSinceLastEvent);

        // Act
        final boolean checkShouldBlockFeedbackPrompt
                = warmUpDaysCheck.shouldBlockFeedbackPrompt(lastEventTime, null);

        // Assert
        assertFalse("Feedback prompt should not be blocked if the warm-up period has passed",
                checkShouldBlockFeedbackPrompt);
    }

}
