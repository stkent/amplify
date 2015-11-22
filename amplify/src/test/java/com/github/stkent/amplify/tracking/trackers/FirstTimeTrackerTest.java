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

import com.github.stkent.amplify.ILogger;
import com.github.stkent.amplify.helpers.BaseTest;
import com.github.stkent.amplify.helpers.FakeSettings;
import com.github.stkent.amplify.tracking.ClockUtil;
import com.github.stkent.amplify.tracking.interfaces.IApplicationInfoProvider;
import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.IEventCheck;

import org.junit.Test;
import org.mockito.Mock;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class FirstTimeTrackerTest extends BaseTest {

    private FirstTimeTracker firstTimeTracker;

    private FakeSettings<Long> fakeSettings;

    @Mock
    private ILogger mockLogger;
    @Mock
    private IApplicationInfoProvider mockApplicationInfoProvider;
    @Mock
    private IEvent mockEvent;
    @Mock
    private IEventCheck<Long> mockEventCheck;

    @Override
    public void localSetUp() {
        fakeSettings = new FakeSettings<>();

        firstTimeTracker = new FirstTimeTracker(
                mockLogger,
                fakeSettings,
                mockApplicationInfoProvider);

        firstTimeTracker.trackEvent(mockEvent, mockEventCheck);
    }

    @Test
    public void testThatFirstEventTimeIsRecorded() {
        // Arrange
        final long fakeEventTime = MARCH_18_2014_838PM_UTC;

        // Act
        triggerEventAtTime(fakeEventTime);

        // Assert
        final Long savedEventTime = fakeSettings.getEventValue(mockEvent, mockEventCheck);

        assertEquals(
                "The correct time should have been recorded for this event",
                Long.valueOf(fakeEventTime), savedEventTime);
    }

    @SuppressLint("Assert")
    @Test
    public void testThatOnlyFirstEventTimeIsRecorded() {
        // Arrange
        final long fakeEventTimeEarlier = MARCH_18_2014_838PM_UTC;
        final long fakeEventTimeLater = fakeEventTimeEarlier + TimeUnit.DAYS.toMillis(1);
        assert fakeEventTimeEarlier < fakeEventTimeLater;

        // Act
        triggerEventAtTime(fakeEventTimeEarlier);
        triggerEventAtTime(fakeEventTimeLater);

        // Assert
        final Long savedEventTime = fakeSettings.getEventValue(mockEvent, mockEventCheck);

        assertEquals(
                "The correct (earliest) time should have been recorded for this event",
                Long.valueOf(fakeEventTimeEarlier), savedEventTime);
    }

    private void triggerEventAtTime(final long time) {
        ClockUtil.setFakeCurrentTimeMillis(time);
        firstTimeTracker.notifyEventTriggered(mockEvent);
    }

}
