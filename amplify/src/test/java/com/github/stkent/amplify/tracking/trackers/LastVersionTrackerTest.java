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
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.ILogger;
import com.github.stkent.amplify.helpers.BaseTest;
import com.github.stkent.amplify.helpers.FakeSettings;
import com.github.stkent.amplify.tracking.interfaces.IApplicationInfoProvider;
import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.IEventCheck;

import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class LastVersionTrackerTest extends BaseTest {

    private LastVersionTracker lastVersionTracker;

    private FakeSettings<String> fakeSettings;

    @Mock
    private ILogger mockLogger;
    @Mock
    private IApplicationInfoProvider mockApplicationInfoProvider;
    @Mock
    private IEvent mockEvent;
    @Mock
    private IEventCheck<String> mockEventCheck;

    @Override
    public void localSetUp() {
        fakeSettings = new FakeSettings<>();

        lastVersionTracker = new LastVersionTracker(
                mockLogger,
                fakeSettings,
                mockApplicationInfoProvider);

        lastVersionTracker.trackEvent(mockEvent, mockEventCheck);
    }

    @SuppressLint("Assert")
    @Test
    public void testThatFirstApplicationVersionNameIsRecorded() throws PackageManager.NameNotFoundException {
        // Arrange
        final String fakeVersionName = "any string";

        // Act
        triggerEventForAppVersion(fakeVersionName);

        // Assert
        final String savedVersionName = fakeSettings.getEventValue(mockEvent, mockEventCheck);

        assertEquals("The correct application version name should have been recorded", fakeVersionName, savedVersionName);
    }

    @SuppressLint("Assert")
    @Test
    public void testThatSecondApplicationVersionNameIsRecorded() throws PackageManager.NameNotFoundException {
        // Arrange
        final String fakeFirstVersionName = "any string";
        final String fakeSecondVersionName = "any other string";
        assert !fakeFirstVersionName.equals(fakeSecondVersionName);

        // Act
        triggerEventForAppVersion(fakeFirstVersionName);
        triggerEventForAppVersion(fakeSecondVersionName);

        // Assert
        final String savedVersionName = fakeSettings.getEventValue(mockEvent, mockEventCheck);

        assertEquals(
                "The correct (latest) application version name should have been recorded",
                fakeSecondVersionName,
                savedVersionName);
    }

    private void triggerEventForAppVersion(
            @NonNull final String appVersionName) throws PackageManager.NameNotFoundException {
        when(mockApplicationInfoProvider.getVersionName()).thenReturn(appVersionName);
        lastVersionTracker.notifyEventTriggered(mockEvent);
    }

}
