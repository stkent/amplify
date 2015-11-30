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

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class LastEventTimesManagerTest extends BaseTest {

    private LastEventTimesManager lastEventTimesManager;

    private FakeSettings<Long> fakeSettings;

    @Mock
    private ILogger mockLogger;
    @Mock
    private IApplicationInfoProvider mockApplicationInfoProvider;
    @Mock
    private IPublicEvent mockPublicEvent;
    @Mock
    private IEventCheck<Long> mockEventCheck;

    @Override
    public void localSetUp() {
        fakeSettings = new FakeSettings<>();

        lastEventTimesManager = new LastEventTimesManager(
                mockLogger,
                fakeSettings,
                mockApplicationInfoProvider);

        when(mockPublicEvent.getTrackingKey()).thenReturn(DEFAULT_MOCK_EVENT_TRACKING_KEY);
        lastEventTimesManager.trackEvent(mockPublicEvent, mockEventCheck);
    }

    @Test
    public void testThatEventsAreSavedWithCorrectTrackingKey() {
        // Arrange
        final String expectedTrackingKey = getExpectedTrackingKeyForEvent(mockPublicEvent);
        assert fakeSettings.readTrackingValue(expectedTrackingKey) == null;

        // Act
        lastEventTimesManager.notifyEventTriggered(mockPublicEvent);

        // Assert
        final Long trackedEventTime = fakeSettings.readTrackingValue(expectedTrackingKey);

        assertNotNull(
                "The event time should have been saved using the correct tracking key",
                trackedEventTime);
    }

    @Test
    public void testThatFirstEventTimeIsRecorded() {
        // Arrange
        final long fakeEventTime = MARCH_18_2014_838PM_UTC;

        // Act
        triggerEventAtTime(mockPublicEvent, fakeEventTime);

        // Assert
        final Long trackedEventTime = fakeSettings.readTrackingValue(getExpectedTrackingKeyForEvent(mockPublicEvent));

        assertEquals(
                "The correct time should have been recorded for this event",
                Long.valueOf(fakeEventTime),
                trackedEventTime);
    }

    @SuppressLint("Assert")
    @Test
    public void testThatSecondEventTimeIsRecorded() {
        // Arrange
        final long fakeEventTimeEarlier = MARCH_18_2014_838PM_UTC;
        final long fakeEventTimeLater = fakeEventTimeEarlier + TimeUnit.DAYS.toMillis(1);
        assert fakeEventTimeEarlier < fakeEventTimeLater;

        // Act
        triggerEventAtTime(mockPublicEvent, fakeEventTimeEarlier);
        triggerEventAtTime(mockPublicEvent, fakeEventTimeLater);

        // Assert
        final Long trackedEventTime = fakeSettings.readTrackingValue(getExpectedTrackingKeyForEvent(mockPublicEvent));

        assertEquals(
                "The correct (latest) time should have been recorded for this event",
                Long.valueOf(fakeEventTimeLater),
                trackedEventTime);
    }

    private String getExpectedTrackingKeyForEvent(@NonNull final IEvent event) {
        return "AMPLIFY_" + event.getTrackingKey() + "_LASTEVENTTIMESMANAGER";
    }

    private void triggerEventAtTime(@NonNull final IPublicEvent event, final long time) {
        setFakeCurrentTimeMillis(time);
        lastEventTimesManager.notifyEventTriggered(event);
    }

}
