package com.github.stkent.amplify.tracking.trackers;

import android.annotation.SuppressLint;

import com.github.stkent.amplify.helpers.BaseTest;
import com.github.stkent.amplify.helpers.FakeSettings;
import com.github.stkent.amplify.helpers.StubLogger;
import com.github.stkent.amplify.tracking.interfaces.IApplicationInfoProvider;
import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.IEventCheck;

import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;

public class TotalCountTrackerTest extends BaseTest {

    @Mock
    private IApplicationInfoProvider mockApplicationInfoProvider;
    @Mock
    private IEvent mockEvent;
    @Mock
    private IEventCheck<Integer> mockEventCheck;

    @SuppressLint("Assert")
    @Test
    public void testThatCorrectNumberOfEventsIsRecorded() {
        // Arrange
        final FakeSettings<Integer> fakeSettings = new FakeSettings<>();

        final TotalCountTracker totalCountTracker = new TotalCountTracker(
                new StubLogger(),
                fakeSettings,
                mockApplicationInfoProvider);

        totalCountTracker.trackEvent(mockEvent, mockEventCheck);

        final Integer expectedEventCount = 7;
        assert expectedEventCount > 0;

        // Act
        for (int i = 0; i < expectedEventCount; i++) {
            totalCountTracker.notifyEventTriggered(mockEvent);
        }

        // Assert
        final Integer actualEventCount = fakeSettings.getEventValue(mockEvent, mockEventCheck);

        assertEquals(
                "The correct number of events should have been recorded",
                expectedEventCount,
                actualEventCount);
    }

}
