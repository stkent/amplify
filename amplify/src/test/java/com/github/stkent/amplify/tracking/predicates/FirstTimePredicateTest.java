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
package com.github.stkent.amplify.tracking.predicates;

import com.github.stkent.amplify.helpers.BaseTest;
import com.github.stkent.amplify.helpers.StubbedLogger;
import com.github.stkent.amplify.tracking.ClockUtil;
import com.github.stkent.amplify.tracking.interfaces.IApplicationInfoProvider;
import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.IEventCheck;
import com.github.stkent.amplify.tracking.interfaces.ISettings;
import com.github.stkent.amplify.tracking.interfaces.ITrackedEvent;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class FirstTimePredicateTest extends BaseTest {

    private FirstTimePredicate firstTimePredicate;

    @Mock
    private ISettings<Long> mockSettings;
    @Mock
    private IApplicationInfoProvider mockApplicationInfoProvider;
    @Mock
    private IEvent mockEvent;
    @Mock
    private IEventCheck<Long> mockEventCheck;

    @Captor
    private ArgumentCaptor<ITrackedEvent> trackedEventCaptor;

    @Override
    public void localSetUp() {
        firstTimePredicate = new FirstTimePredicate(
                new StubbedLogger(),
                mockSettings,
                mockApplicationInfoProvider);

        firstTimePredicate.trackEvent(mockEvent, mockEventCheck);
    }

    @Test
    public void testThatFirstEventTimeIsRecorded() {
        // Arrange
        final long fakeTimeEventFirstOccurred = MARCH_18_2014_838PM_UTC;
        ClockUtil.setFakeCurrentTimeMillis(MARCH_18_2014_838PM_UTC);

        // Act
        firstTimePredicate.eventTriggered(mockEvent);

        // Assert

        // Check that the correct event time was saved:
        verify(mockSettings, times(1))
                .writeEventValue(trackedEventCaptor.capture(), eq(fakeTimeEventFirstOccurred));

        // Check that the correct event was associated with this saved time:
        assertTrue(trackedEventCaptor.getValue().getEvent().equals(mockEvent));

        // Check that the correct event check was associated with this saved time:
        assertTrue(trackedEventCaptor.getValue().getEventCheck().equals(mockEventCheck));
    }

}
