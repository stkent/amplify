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
package com.github.stkent.amplify.tracking;

import com.github.stkent.amplify.helpers.BaseTest;
import com.github.stkent.amplify.tracking.interfaces.IEvent;

import org.junit.Assert;
import org.junit.Test;

public class PromptInteractionEventTest extends BaseTest {

    @Test
    public void testThatUserIndicatedPositiveOpinionEventHasCorrectTrackingKey() {
        // Arrange
        final IEvent userIndicatedPositiveOpinionEvent
                = PromptInteractionEvent.USER_INDICATED_POSITIVE_OPINION;

        // Assert
        Assert.assertEquals(
                "User indicated positive opinion event should have correct tracking key",
                userIndicatedPositiveOpinionEvent.getTrackingKey(),
                "USER_INDICATED_POSITIVE_OPINION");
    }

    @Test
    public void testThatUserIndicatedCriticalOpinionEventHasCorrectTrackingKey() {
        // Arrange
        final IEvent userIndicatedCriticalOpinionEvent
                = PromptInteractionEvent.USER_INDICATED_CRITICAL_OPINION;

        // Assert
        Assert.assertEquals(
                "User indicated critical opinion event should have correct tracking key",
                userIndicatedCriticalOpinionEvent.getTrackingKey(),
                "USER_INDICATED_CRITICAL_OPINION");
    }

    @Test
    public void testThatUserGavePositiveFeedbackEventHasCorrectTrackingKey() {
        // Arrange
        final IEvent userGavePositiveFeedbackEvent
                = PromptInteractionEvent.USER_GAVE_POSITIVE_FEEDBACK;

        // Assert
        Assert.assertEquals(
                "User gave positive feedback event should have correct tracking key",
                userGavePositiveFeedbackEvent.getTrackingKey(),
                "USER_GAVE_POSITIVE_FEEDBACK");
    }

    @Test
    public void testThatUserGaveCriticalFeedbackEventHasCorrectTrackingKey() {
        // Arrange
        final IEvent userGaveCriticalFeedbackEvent
                = PromptInteractionEvent.USER_GAVE_CRITICAL_FEEDBACK;

        // Assert
        Assert.assertEquals(
                "User gave critical feedback event should have correct tracking key",
                userGaveCriticalFeedbackEvent.getTrackingKey(),
                "USER_GAVE_CRITICAL_FEEDBACK");
    }

    @Test
    public void testThatUserDeclinedPositiveFeedbackEventHasCorrectTrackingKey() {
        // Arrange
        final IEvent userDeclinedPositiveFeedbackEvent
                = PromptInteractionEvent.USER_DECLINED_POSITIVE_FEEDBACK;

        // Assert
        Assert.assertEquals(
                "User declined positive feedback event should have correct tracking key",
                userDeclinedPositiveFeedbackEvent.getTrackingKey(),
                "USER_DECLINED_POSITIVE_FEEDBACK");
    }

    @Test
    public void testThatUserDeclinedCriticalFeedbackEventHasCorrectTrackingKey() {
        // Arrange
        final IEvent userDeclinedCriticalFeedbackEvent
                = PromptInteractionEvent.USER_DECLINED_CRITICAL_FEEDBACK;

        // Assert
        Assert.assertEquals(
                "User declined critical feedback event should have correct tracking key",
                userDeclinedCriticalFeedbackEvent.getTrackingKey(),
                "USER_DECLINED_CRITICAL_FEEDBACK");
    }

    @Test
    public void testThatUserGaveFeedbackEventHasCorrectTrackingKey() {
        // Arrange
        final IEvent userGaveFeedbackEvent = PromptInteractionEvent.USER_GAVE_FEEDBACK;

        // Assert
        Assert.assertEquals(
                "User gave feedback event should have correct tracking key",
                userGaveFeedbackEvent.getTrackingKey(),
                "USER_GAVE_FEEDBACK");
    }

    @Test
    public void testThatUserDeclinedFeedbackEventHasCorrectTrackingKey() {
        // Arrange
        final IEvent userDeclinedFeedbackEvent = PromptInteractionEvent.USER_DECLINED_FEEDBACK;

        // Assert
        Assert.assertEquals(
                "User declined feedback event should have correct tracking key",
                userDeclinedFeedbackEvent.getTrackingKey(),
                "USER_DECLINED_FEEDBACK");
    }

}
