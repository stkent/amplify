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

import com.github.stkent.amplify.helpers.BaseTest;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VersionNameChangedRuleTest extends BaseTest {

    @SuppressWarnings("ConstantConditions")
    @Test
    public void testThatRuleAllowsPromptIfEventHasNeverOccurred() {
        // Arrange
        final String fakeCurrentVersionName = "any string";
        final VersionNameChangedRule versionNameChangedRule = new VersionNameChangedRule(fakeCurrentVersionName);

        // Act
        final boolean ruleShouldAllowFeedbackPrompt = versionNameChangedRule.shouldAllowFeedbackPromptByDefault();

        // Assert
        assertTrue(
                "Feedback prompt should be allowed if the associated event has never occurred.",
                ruleShouldAllowFeedbackPrompt);
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Test
    public void testThatRuleBlocksPromptIfAppVersionNameHasNotChanged() {
        // Arrange
        final String fakeCachedVersionName = "any string";
        final String fakeCurrentVersionName = fakeCachedVersionName;
        final VersionNameChangedRule versionNameChangedRule = new VersionNameChangedRule(fakeCurrentVersionName);

        // Act
        final boolean ruleShouldAllowFeedbackPrompt =
                versionNameChangedRule.shouldAllowFeedbackPrompt(fakeCachedVersionName);

        // Assert
        assertFalse(
                "Feedback prompt should be blocked if the app version name has not changed",
                ruleShouldAllowFeedbackPrompt);
    }

    @SuppressLint("Assert")
    @Test
    public void testThatRuleAllowsPromptIfAppVersionNameHasChanged() {
        // Arrange
        final String fakeCachedVersionName = "any string";
        final String fakeCurrentVersionName = "any other string";
        assert !fakeCachedVersionName.equals(fakeCurrentVersionName);

        final VersionNameChangedRule versionNameChangedRule = new VersionNameChangedRule(fakeCurrentVersionName);

        // Act
        final boolean ruleShouldAllowFeedbackPrompt =
                versionNameChangedRule.shouldAllowFeedbackPrompt(fakeCachedVersionName);

        // Assert
        assertTrue(
                "Feedback prompt should be allowed if the app version name has changed",
                ruleShouldAllowFeedbackPrompt);
    }

}
