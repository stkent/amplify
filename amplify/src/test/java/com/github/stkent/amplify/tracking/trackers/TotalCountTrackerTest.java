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
package com.github.stkent.amplify.tracking.trackers;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.ILogger;
import com.github.stkent.amplify.helpers.BaseTest;
import com.github.stkent.amplify.helpers.FakeSettings;
import com.github.stkent.amplify.tracking.interfaces.IApplicationInfoProvider;
import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.IEventCheck;
import com.github.stkent.amplify.tracking.interfaces.IPublicEvent;

import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class TotalCountTrackerTest extends BaseTest {

    private TotalCountTracker totalCountTracker;

    private FakeSettings<Integer> fakeSettings;

    @Mock
    private ILogger mockLogger;
    @Mock
    private IApplicationInfoProvider mockApplicationInfoProvider;
    @Mock
    private IPublicEvent mockPublicEvent;
    @Mock
    private IEventCheck<Integer> mockEventCheck;

    @Override
    public void localSetUp() {
        fakeSettings = new FakeSettings<>();

        totalCountTracker = new TotalCountTracker(
                mockLogger,
                fakeSettings,
                mockApplicationInfoProvider);

        when(mockPublicEvent.getTrackingKey()).thenReturn(DEFAULT_MOCK_EVENT_TRACKING_KEY);
        totalCountTracker.trackEvent(mockPublicEvent, mockEventCheck);
    }

    @Test
    public void testThatEventsAreSavedWithCorrectTrackingKey() {
        // Arrange
        final String expectedTrackingKey = getExpectedTrackingKeyForEvent(mockPublicEvent);
        assert fakeSettings.readTrackingValue(expectedTrackingKey) == null;

        // Act
        totalCountTracker.notifyEventTriggered(mockPublicEvent);

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
        totalCountTracker.trackEvent(mockPublicEvent, mockEventCheck);

        final Integer expectedEventCount = 7;
        assert expectedEventCount > 0;

        // Act
        for (int i = 0; i < expectedEventCount; i++) {
            totalCountTracker.notifyEventTriggered(mockPublicEvent);
        }

        // Assert
        final Integer actualEventCount = fakeSettings.readTrackingValue(getExpectedTrackingKeyForEvent(mockPublicEvent));

        assertEquals(
                "The correct number of events should have been recorded",
                expectedEventCount,
                actualEventCount);
    }

    private String getExpectedTrackingKeyForEvent(@NonNull final IEvent event) {
        return "AMPLIFY_" + event.getTrackingKey() + "_TOTALCOUNTTRACKER";
    }

}
