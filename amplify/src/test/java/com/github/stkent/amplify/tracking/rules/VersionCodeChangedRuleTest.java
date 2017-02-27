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

public class VersionCodeChangedRuleTest extends BaseTest {

    @SuppressWarnings("ConstantConditions")
    @Test
    public void testThatRuleAllowsPromptIfEventHasNeverOccurred() {
        // Arrange
        final int fakeCurrentVersionCode = 42;
        final VersionCodeChangedRule versionCodeChangedRule = new VersionCodeChangedRule(fakeCurrentVersionCode);

        // Act
        final boolean ruleShouldAllowFeedbackPrompt = versionCodeChangedRule.shouldAllowFeedbackPromptByDefault();

        // Assert
        assertTrue(
                "Feedback prompt should be allowed if the associated event has never occurred.",
                ruleShouldAllowFeedbackPrompt);
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Test
    public void testThatRuleBlocksPromptIfAppVersionCodeHasNotChanged() {
        // Arrange
        final int fakeCachedVersionCode = 42;
        final int fakeCurrentVersionCode = fakeCachedVersionCode;
        final VersionCodeChangedRule versionCodeChangedRule = new VersionCodeChangedRule(fakeCurrentVersionCode);

        // Act
        final boolean ruleShouldAllowFeedbackPrompt =
                versionCodeChangedRule.shouldAllowFeedbackPrompt(fakeCurrentVersionCode);

        // Assert
        assertFalse(
                "Feedback prompt should be blocked if the app version code has not changed",
                ruleShouldAllowFeedbackPrompt);
    }

    @SuppressLint("Assert")
    @SuppressWarnings("ConstantConditions")
    @Test
    public void testThatRuleAllowsPromptIfAppVersionCodeHasIncreased() {
        // Arrange
        final int fakeCachedVersionCode = 42;
        final int fakeCurrentVersionCode = 69;
        assert fakeCachedVersionCode < fakeCurrentVersionCode;

        final VersionCodeChangedRule versionCodeChangedRule = new VersionCodeChangedRule(fakeCurrentVersionCode);

        // Act
        final boolean ruleShouldAllowFeedbackPrompt =
                versionCodeChangedRule.shouldAllowFeedbackPrompt(fakeCachedVersionCode);

        // Assert
        assertTrue(
                "Feedback prompt should be allowed if the app version code has changed",
                ruleShouldAllowFeedbackPrompt);
    }

}
