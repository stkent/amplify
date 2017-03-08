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
package com.github.stkent.amplify.tracking.managers;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.helpers.BaseTest;
import com.github.stkent.amplify.helpers.FakeSettings;
import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.IEventBasedRule;

import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class TotalEventCountRulesManagerTest extends BaseTest {

    private TotalEventCountRulesManager totalEventCountRulesManager;

    private FakeSettings<Integer> fakeSettings;

    @Mock
    private IEvent mockEvent;
    @Mock
    private IEventBasedRule<Integer> mockRule;

    @Override
    public void localSetUp() {
        fakeSettings = new FakeSettings<>();

        totalEventCountRulesManager = new TotalEventCountRulesManager(fakeSettings);

        when(mockEvent.getTrackingKey()).thenReturn(DEFAULT_MOCK_EVENT_TRACKING_KEY);
        totalEventCountRulesManager.addEventBasedRule(mockEvent, mockRule);
    }

    @Test
    public void testThatEventsAreSavedWithCorrectTrackingKey() {
        // Arrange
        final String expectedTrackingKey = getExpectedTrackingKeyForEvent(mockEvent);
        assert fakeSettings.readTrackingValue(expectedTrackingKey) == null;

        // Act
        totalEventCountRulesManager.notifyEventTriggered(mockEvent);

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
        totalEventCountRulesManager.addEventBasedRule(mockEvent, mockRule);

        final Integer expectedEventCount = 7;
        assert expectedEventCount > 0;

        // Act
        for (int i = 0; i < expectedEventCount; i++) {
            totalEventCountRulesManager.notifyEventTriggered(mockEvent);
        }

        // Assert
        final Integer actualEventCount
                = fakeSettings.readTrackingValue(getExpectedTrackingKeyForEvent(mockEvent));

        assertEquals(
                "The correct number of events should have been recorded",
                expectedEventCount,
                actualEventCount);
    }

    private String getExpectedTrackingKeyForEvent(@NonNull final IEvent event) {
        return "AMPLIFY_" + event.getTrackingKey() + "_TOTAL_COUNT";
    }

}
