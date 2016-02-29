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
import com.github.stkent.amplify.tracking.interfaces.IPromptRule;
import com.github.stkent.amplify.tracking.interfaces.ITrackableEvent;

import org.junit.Test;
import org.mockito.Mock;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class LastEventTimeRulesManagerTest extends BaseTest {

    private LastEventTimeRulesManager lastEventTimeRulesManager;

    private FakeSettings<Long> fakeSettings;

    @Mock
    private ILogger mockLogger;
    @Mock
    private ITrackableEvent mockTrackableEvent;
    @Mock
    private IPromptRule<Long> mockEventCheck;

    @Override
    public void localSetUp() {
        fakeSettings = new FakeSettings<>();

        lastEventTimeRulesManager = new LastEventTimeRulesManager(
                mockLogger,
                fakeSettings);

        when(mockTrackableEvent.getTrackingKey()).thenReturn(DEFAULT_MOCK_EVENT_TRACKING_KEY);
        lastEventTimeRulesManager.addEventPromptRule(mockTrackableEvent, mockEventCheck);
    }

    @Test
    public void testThatEventsAreSavedWithCorrectTrackingKey() {
        // Arrange
        final String expectedTrackingKey = getExpectedTrackingKeyForEvent(mockTrackableEvent);
        assert fakeSettings.readTrackingValue(expectedTrackingKey) == null;

        // Act
        lastEventTimeRulesManager.notifyEventTriggered(mockTrackableEvent);

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
        triggerEventAtTime(mockTrackableEvent, fakeEventTime);

        // Assert
        final Long trackedEventTime = fakeSettings.readTrackingValue(getExpectedTrackingKeyForEvent(mockTrackableEvent));

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
        triggerEventAtTime(mockTrackableEvent, fakeEventTimeEarlier);
        triggerEventAtTime(mockTrackableEvent, fakeEventTimeLater);

        // Assert
        final Long trackedEventTime = fakeSettings.readTrackingValue(getExpectedTrackingKeyForEvent(mockTrackableEvent));

        assertEquals(
                "The correct (latest) time should have been recorded for this event",
                Long.valueOf(fakeEventTimeLater),
                trackedEventTime);
    }

    private String getExpectedTrackingKeyForEvent(@NonNull final ITrackableEvent event) {
        return "AMPLIFY_" + event.getTrackingKey() + "_LASTEVENTTIMESMANAGER";
    }

    private void triggerEventAtTime(@NonNull final ITrackableEvent event, final long time) {
        setFakeCurrentTimeMillis(time);
        lastEventTimeRulesManager.notifyEventTriggered(event);
    }

}
