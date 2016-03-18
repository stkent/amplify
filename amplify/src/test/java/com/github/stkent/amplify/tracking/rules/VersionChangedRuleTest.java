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
package com.github.stkent.amplify.tracking.rules;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.github.stkent.amplify.helpers.BaseTest;
import com.github.stkent.amplify.utils.AppInfoProvider;

import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class VersionChangedRuleTest extends BaseTest {

    private VersionChangedRule versionChangedRule;

    @SuppressWarnings("FieldCanBeLocal")
    private PackageInfo fakePackageInfo;

    @Mock
    private AppInfoProvider mockAppInfoProvider;

    @Override
    public void localSetUp() {
        versionChangedRule = new VersionChangedRule();

        fakePackageInfo = new PackageInfo();
        when(mockAppInfoProvider.getPackageInfo()).thenReturn(fakePackageInfo);
        AppInfoProvider.setSharedInstance(mockAppInfoProvider);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void testThatRuleAllowsPromptIfEventHasNeverOccurred() {
        // Arrange
        fakePackageInfo.versionName = "any string";

        // Act
        final boolean ruleShouldAllowFeedbackPrompt
                = versionChangedRule.shouldAllowFeedbackPromptByDefault();

        // Assert
        assertTrue(
                "Feedback prompt should be allowed if the associated event has never occurred.",
                ruleShouldAllowFeedbackPrompt);
    }

    @Test
    public void testThatRuleBlocksPromptIfAppVersionHasNotChanged() throws PackageManager.NameNotFoundException {
        // Arrange
        final String fakeVersionName = "any string";

        fakePackageInfo.versionName = fakeVersionName;

        // Act
        final boolean ruleShouldAllowFeedbackPrompt =
                versionChangedRule.shouldAllowFeedbackPrompt(fakeVersionName);

        // Assert
        assertFalse(
                "Feedback prompt should be blocked if the app version has not changed",
                ruleShouldAllowFeedbackPrompt);
    }

    @SuppressLint("Assert")
    @SuppressWarnings("ConstantConditions")
    @Test
    public void testThatRuleAllowsPromptIfAppVersionHasChanged() throws PackageManager.NameNotFoundException {
        // Arrange
        final String fakeCachedVersionName = "any string";
        final String fakeCurrentVersionName = "any other string";
        assert !fakeCachedVersionName.equals(fakeCurrentVersionName);

        fakePackageInfo.versionName = fakeCurrentVersionName;

        // Act
        final boolean ruleShouldAllowFeedbackPrompt =
                versionChangedRule.shouldAllowFeedbackPrompt(fakeCachedVersionName);

        // Assert
        assertTrue(
                "Feedback prompt should be allowed if the app version has changed",
                ruleShouldAllowFeedbackPrompt);
    }

}
