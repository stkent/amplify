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
package com.github.stkent.amplify.tracking.checks;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;

import com.github.stkent.amplify.helpers.BaseTest;
import com.github.stkent.amplify.tracking.interfaces.IApplicationInfoProvider;

import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class VersionChangedCheckTest extends BaseTest {

    private VersionChangedCheck versionChangedCheck;

    @Mock
    private IApplicationInfoProvider mockApplicationInfoProvider;

    @Override
    public void localSetUp() {
        versionChangedCheck = new VersionChangedCheck();
    }

    @Test
    public void testThatCheckBlocksPromptIfAppVersionHasNotChanged() throws PackageManager.NameNotFoundException {
        // Arrange
        final String fakeVersionName = "any string";

        when(mockApplicationInfoProvider.getVersionName()).thenReturn(fakeVersionName);

        // Act
        final boolean shouldBlockFeedbackPrompt = versionChangedCheck.shouldBlockFeedbackPrompt(
                fakeVersionName, mockApplicationInfoProvider);

        // Assert
        assertTrue("Feedback prompt should be blocked if the app version has not changed", shouldBlockFeedbackPrompt);
    }

    @SuppressLint("Assert")
    @SuppressWarnings("ConstantConditions")
    @Test
    public void testThatCheckDoesNotBlockPromptIfAppVersionHasChanged() throws PackageManager.NameNotFoundException {
        // Arrange
        final String fakeCachedVersionName = "any string";
        final String fakeCurrentVersionName = "any other string";
        assert !fakeCachedVersionName.equals(fakeCurrentVersionName);

        when(mockApplicationInfoProvider.getVersionName()).thenReturn(fakeCurrentVersionName);

        // Act
        final boolean shouldBlockFeedbackPrompt = versionChangedCheck.shouldBlockFeedbackPrompt(
                fakeCachedVersionName, mockApplicationInfoProvider);

        // Assert
        assertFalse("Feedback prompt should not be blocked if the app version has changed",
                shouldBlockFeedbackPrompt);
    }

}
