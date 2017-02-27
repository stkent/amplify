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

import com.github.stkent.amplify.IApp;
import com.github.stkent.amplify.helpers.BaseTest;
import com.github.stkent.amplify.helpers.FakeSettings;
import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.IEventBasedRule;

import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class LastEventVersionCodeRulesManagerTest extends BaseTest {

    private LastEventVersionCodeRulesManager lastEventVersionCodeRulesManager;

    private FakeSettings<Integer> fakeSettings;

    @Mock
    private IApp mockApp;

    @Mock
    private IEvent mockEvent;

    @Mock
    private IEventBasedRule<Integer> mockEventBasedRule;

    @Override
    public void localSetUp() {
        fakeSettings = new FakeSettings<>();

        lastEventVersionCodeRulesManager = new LastEventVersionCodeRulesManager(fakeSettings, mockApp);

        when(mockEvent.getTrackingKey()).thenReturn(DEFAULT_MOCK_EVENT_TRACKING_KEY);
        lastEventVersionCodeRulesManager.addEventBasedRule(mockEvent, mockEventBasedRule);
    }

    @Test
    public void testThatEventsAreSavedWithCorrectTrackingKey() {
        // Arrange
        final int fakeVersionCode = 17;

        final String expectedTrackingKey = getExpectedTrackingKeyForEvent(mockEvent);
        assert fakeSettings.readTrackingValue(expectedTrackingKey) == null;

        // Act
        triggerEventForAppVersionCode(fakeVersionCode);

        // Assert
        final Integer trackedEventVersionCode = fakeSettings.readTrackingValue(expectedTrackingKey);

        assertNotNull(
                "The application version code should have been saved using the correct tracking key",
                trackedEventVersionCode);
    }

    @SuppressWarnings("ConstantConditions")
    @SuppressLint("Assert")
    @Test
    public void testThatFirstAppVersionCodeIsRecorded() {
        // Arrange
        final int fakeVersionCode = 17;

        // Act
        triggerEventForAppVersionCode(fakeVersionCode);

        // Assert
        final int trackedEventVersionCode
                = fakeSettings.readTrackingValue(getExpectedTrackingKeyForEvent(mockEvent));

        assertEquals(
                "The correct application version code should have been recorded",
                fakeVersionCode,
                trackedEventVersionCode);
    }

    @SuppressWarnings("ConstantConditions")
    @SuppressLint("Assert")
    @Test
    public void testThatMostRecentAppVersionCodeIsRecorded() {
        // Arrange
        final int fakeFirstVersionCode = 17;
        final int fakeSecondVersionCode = 20;
        assert fakeFirstVersionCode < fakeSecondVersionCode;

        // Act
        triggerEventForAppVersionCode(fakeFirstVersionCode);
        triggerEventForAppVersionCode(fakeSecondVersionCode);

        // Assert
        final int trackedEventVersionCode
                = fakeSettings.readTrackingValue(getExpectedTrackingKeyForEvent(mockEvent));

        assertEquals(
                "The correct (latest) application version code should have been recorded",
                fakeSecondVersionCode,
                trackedEventVersionCode);
    }

    private String getExpectedTrackingKeyForEvent(@NonNull final IEvent event) {
        return "AMPLIFY_" + event.getTrackingKey() + "_LAST_VERSION_CODE";
    }

    private void triggerEventForAppVersionCode(final int appVersionCode) {
        when(mockApp.getVersionCode()).thenReturn(appVersionCode);
        lastEventVersionCodeRulesManager.notifyEventTriggered(mockEvent);
    }

}
