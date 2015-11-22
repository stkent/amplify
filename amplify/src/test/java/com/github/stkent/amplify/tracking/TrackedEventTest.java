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
package com.github.stkent.amplify.tracking;

import com.github.stkent.amplify.helpers.BaseTest;
import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.IEventCheck;
import com.github.stkent.amplify.tracking.interfaces.ITrackedEvent;

import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class TrackedEventTest extends BaseTest {

    @Mock
    private IEvent mockEvent;
    @Mock
    private IEventCheck mockEventCheck;

    @Test
    public void testThatTrackingKeyIsGeneratedCorrectly() {
        // Arrange
        final String trackedEventPrefixKey = "AMPLIFY_";
        final String mockEventKey = "MOCK_EVENT";
        final String mockEventCheckKey = "MOCK_EVENT_CHECK";
        final String expectedTrackingKey = trackedEventPrefixKey + mockEventKey + "_" + mockEventCheckKey;

        when(mockEvent.getTrackingKey()).thenReturn(mockEventKey);
        when(mockEventCheck.getTrackingKey()).thenReturn(mockEventCheckKey);

        // Act
        final ITrackedEvent trackedEvent = new TrackedEvent(mockEvent, mockEventCheck);

        // Assert
        final String actualTrackingKey = trackedEvent.getTrackingKey();

        assertEquals(
                "The generated tracking key for a TrackedEvent is incorrect",
                expectedTrackingKey,
                actualTrackingKey);
    }

}
