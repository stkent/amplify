package com.github.stkent.amplify.tracking.checks;

import android.annotation.SuppressLint;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MaximumCountCheckTest {

    @SuppressLint("Assert")
    @SuppressWarnings("ConstantConditions")
    @Test
    public void testThatCheckBlocksPrompt_ifCountThresholdHasBeenExceeded() {
        // Arrange
        final int maximumEventCount = 7;
        final int currentEventCount = 9;

        assert currentEventCount > maximumEventCount;

        final MaximumCountCheck maximumCountCheck = new MaximumCountCheck(maximumEventCount);

        // Act
        // todo: figure out what to pass instead of the null context here
        final boolean checkShouldBlockFeedbackPrompt
                = maximumCountCheck.shouldBlockFeedbackPrompt(currentEventCount, null);

        // Assert
        assertTrue("Feedback prompt should be blocked if the count threshold has been exceeded",
                checkShouldBlockFeedbackPrompt);
    }

    @SuppressLint("Assert")
    @SuppressWarnings("ConstantConditions")
    @Test
    public void testThatCheckDoesNotBlockPrompt_ifCountThresholdHasNotBeenExceeded() {
        // Arrange
        final int maximumEventCount = 7;
        final int currentEventCount = 2;

        assert currentEventCount < maximumEventCount;

        final MaximumCountCheck maximumCountCheck = new MaximumCountCheck(maximumEventCount);

        // Act
        // todo: figure out what to pass instead of the null context here
        final boolean checkShouldBlockFeedbackPrompt
                = maximumCountCheck.shouldBlockFeedbackPrompt(currentEventCount, null);

        // Assert
        assertFalse("Feedback prompt should be blocked if the count threshold has been exceeded",
                checkShouldBlockFeedbackPrompt);
    }

}
