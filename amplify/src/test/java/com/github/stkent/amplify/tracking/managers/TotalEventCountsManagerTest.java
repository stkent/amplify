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
package com.github.stkent.amplify.tracking.managers;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.ILogger;
import com.github.stkent.amplify.helpers.BaseTest;
import com.github.stkent.amplify.helpers.FakeSettings;
import com.github.stkent.amplify.tracking.interfaces.IEventCheck;
import com.github.stkent.amplify.tracking.interfaces.ITrackableEvent;

import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class TotalEventCountsManagerTest extends BaseTest {

    private TotalEventCountsManager totalEventCountsManager;

    private FakeSettings<Integer> fakeSettings;

    @Mock
    private ILogger mockLogger;
    @Mock
    private ITrackableEvent mockTrackableEvent;
    @Mock
    private IEventCheck<Integer> mockEventCheck;

    @Override
    public void localSetUp() {
        fakeSettings = new FakeSettings<>();

        totalEventCountsManager = new TotalEventCountsManager(
                mockLogger,
                fakeSettings);

        when(mockTrackableEvent.getTrackingKey()).thenReturn(DEFAULT_MOCK_EVENT_TRACKING_KEY);
        totalEventCountsManager.trackEvent(mockTrackableEvent, mockEventCheck);
    }

    @Test
    public void testThatEventsAreSavedWithCorrectTrackingKey() {
        // Arrange
        final String expectedTrackingKey = getExpectedTrackingKeyForEvent(mockTrackableEvent);
        assert fakeSettings.readTrackingValue(expectedTrackingKey) == null;

        // Act
        totalEventCountsManager.notifyEventTriggered(mockTrackableEvent);

        // Assert
        final Integer trackedTotalEventCount = fakeSettings.readTrackingValue(expectedTrackingKey);

        assertNotNull(
                "The total event count should have been saved using the correct tracking key",
                trackedTotalEventCount);
    }

    @SuppressLint("Assert")
    @Test
    public void testThatCorrectNumberOfEventsIsRecorded() {
        // Arrange
        totalEventCountsManager.trackEvent(mockTrackableEvent, mockEventCheck);

        final Integer expectedEventCount = 7;
        assert expectedEventCount > 0;

        // Act
        for (int i = 0; i < expectedEventCount; i++) {
            totalEventCountsManager.notifyEventTriggered(mockTrackableEvent);
        }

        // Assert
        final Integer actualEventCount = fakeSettings.readTrackingValue(getExpectedTrackingKeyForEvent(mockTrackableEvent));

        assertEquals(
                "The correct number of events should have been recorded",
                expectedEventCount,
                actualEventCount);
    }

    private String getExpectedTrackingKeyForEvent(@NonNull final ITrackableEvent event) {
        return "AMPLIFY_" + event.getTrackingKey() + "_TOTALEVENTCOUNTSMANAGER";
    }

}
