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

public class PromptViewEventTest extends BaseTest {

    @Test
    public void testThatPromptShownEventHasCorrectTrackingKey() {
        // Arrange
        final IEvent promptShownEvent = PromptViewEvent.PROMPT_SHOWN;

        // Assert
        Assert.assertEquals(
                "Prompt shown event should have correct tracking key",
                promptShownEvent.getTrackingKey(),
                "PROMPT_SHOWN");
    }

    @Test
    public void testThatThanksShownEventHasCorrectTrackingKey() {
        // Arrange
        final IEvent thanksShownEvent = PromptViewEvent.THANKS_SHOWN;

        // Assert
        Assert.assertEquals(
                "Thanks shown event should have correct tracking key",
                thanksShownEvent.getTrackingKey(),
                "THANKS_SHOWN");
    }

    @Test
    public void testThatPromptDismissedEventHasCorrectTrackingKey() {
        // Arrange
        final IEvent promptDismissedEvent = PromptViewEvent.PROMPT_DISMISSED;

        // Assert
        Assert.assertEquals(
                "Prompt dismissed event should have correct tracking key",
                promptDismissedEvent.getTrackingKey(),
                "PROMPT_DISMISSED");
    }

}
