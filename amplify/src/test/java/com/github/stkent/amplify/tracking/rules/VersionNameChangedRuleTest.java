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
import android.content.pm.PackageManager.NameNotFoundException;

import com.github.stkent.amplify.helpers.BaseTest;
import com.github.stkent.amplify.utils.appinfo.AppInfoUtil;
import com.github.stkent.amplify.utils.appinfo.IAppInfoProvider;

import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class VersionNameChangedRuleTest extends BaseTest {

    private VersionNameChangedRule versionNameChangedRule;

    @SuppressWarnings("FieldCanBeLocal")
    private PackageInfo fakePackageInfo;

    @Mock
    private IAppInfoProvider mockAppInfoProvider;

    @Override
    public void localSetUp() {
        versionNameChangedRule = new VersionNameChangedRule();

        fakePackageInfo = new PackageInfo();
        when(mockAppInfoProvider.getPackageInfo()).thenReturn(fakePackageInfo);
        AppInfoUtil.setSharedAppInfoProvider(mockAppInfoProvider);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void testThatRuleAllowsPromptIfEventHasNeverOccurred() {
        // Act
        final boolean ruleShouldAllowFeedbackPrompt
                = versionNameChangedRule.shouldAllowFeedbackPromptByDefault();

        // Assert
        assertTrue(
                "Feedback prompt should be allowed if the associated event has never occurred.",
                ruleShouldAllowFeedbackPrompt);
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Test
    public void testThatRuleBlocksPromptIfAppVersionNameHasNotChanged()
            throws NameNotFoundException {

        // Arrange
        final String fakeCachedVersionName = "any string";
        final String fakeCurrentVersionName = fakeCachedVersionName;

        fakePackageInfo.versionName = fakeCachedVersionName;

        // Act
        final boolean ruleShouldAllowFeedbackPrompt =
                versionNameChangedRule.shouldAllowFeedbackPrompt(fakeCurrentVersionName);

        // Assert
        assertFalse(
                "Feedback prompt should be blocked if the app version name has not changed",
                ruleShouldAllowFeedbackPrompt);
    }

    @SuppressLint("Assert")
    @SuppressWarnings("ConstantConditions")
    @Test
    public void testThatRuleAllowsPromptIfAppVersionNameHasChanged() throws NameNotFoundException {
        // Arrange
        final String fakeCachedVersionName = "any string";
        final String fakeCurrentVersionName = "any other string";
        assert !fakeCachedVersionName.equals(fakeCurrentVersionName);

        fakePackageInfo.versionName = fakeCurrentVersionName;

        // Act
        final boolean ruleShouldAllowFeedbackPrompt =
                versionNameChangedRule.shouldAllowFeedbackPrompt(fakeCachedVersionName);

        // Assert
        assertTrue(
                "Feedback prompt should be allowed if the app version name has changed",
                ruleShouldAllowFeedbackPrompt);
    }

}
