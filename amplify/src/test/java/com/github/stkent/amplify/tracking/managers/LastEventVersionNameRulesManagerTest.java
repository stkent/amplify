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

public class LastEventVersionNameRulesManagerTest extends BaseTest {

    private LastEventVersionNameRulesManager lastEventVersionNameRulesManager;

    private FakeSettings<String> fakeSettings;

    @Mock
    private IApp mockApp;

    @Mock
    private IEvent mockEvent;

    @Mock
    private IEventBasedRule<String> mockEventBasedRule;

    @Override
    public void localSetUp() {
        fakeSettings = new FakeSettings<>();

        lastEventVersionNameRulesManager = new LastEventVersionNameRulesManager(fakeSettings, mockApp);

        when(mockEvent.getTrackingKey()).thenReturn(DEFAULT_MOCK_EVENT_TRACKING_KEY);
        lastEventVersionNameRulesManager.addEventBasedRule(mockEvent, mockEventBasedRule);
    }

    @Test
    public void testThatEventsAreSavedWithCorrectTrackingKey() {
        // Arrange
        final String fakeVersionName = "any string";

        final String expectedTrackingKey = getExpectedTrackingKeyForEvent(mockEvent);
        assert fakeSettings.readTrackingValue(expectedTrackingKey) == null;

        // Act
        triggerEventForAppVersionName(fakeVersionName);

        // Assert
        final String trackedEventVersionName = fakeSettings.readTrackingValue(expectedTrackingKey);

        assertNotNull(
                "The application version name should have been saved using the correct tracking key",
                trackedEventVersionName);
    }

    @SuppressLint("Assert")
    @Test
    public void testThatFirstAppVersionNameIsRecorded() {
        // Arrange
        final String fakeVersionName = "any string";

        // Act
        triggerEventForAppVersionName(fakeVersionName);

        // Assert
        final String trackedEventVersionName
                = fakeSettings.readTrackingValue(getExpectedTrackingKeyForEvent(mockEvent));

        assertEquals(
                "The correct application version name should have been recorded",
                fakeVersionName,
                trackedEventVersionName);
    }

    @SuppressLint("Assert")
    @Test
    public void testThatMostRecentAppVersionNameIsRecorded() {
        // Arrange
        final String fakeFirstVersionName = "any string";
        final String fakeSecondVersionName = "any other string";
        assert !fakeFirstVersionName.equals(fakeSecondVersionName);

        // Act
        triggerEventForAppVersionName(fakeFirstVersionName);
        triggerEventForAppVersionName(fakeSecondVersionName);

        // Assert
        final String trackedEventVersionName
                = fakeSettings.readTrackingValue(getExpectedTrackingKeyForEvent(mockEvent));

        assertEquals(
                "The correct (latest) application version name should have been recorded",
                fakeSecondVersionName,
                trackedEventVersionName);
    }

    private String getExpectedTrackingKeyForEvent(@NonNull final IEvent event) {
        return "AMPLIFY_" + event.getTrackingKey() + "_LAST_VERSION_NAME";
    }

    private void triggerEventForAppVersionName(@NonNull final String appVersionName) {
        when(mockApp.getVersionName()).thenReturn(appVersionName);
        lastEventVersionNameRulesManager.notifyEventTriggered(mockEvent);
    }

}
