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

import com.github.stkent.amplify.IEnvironment;
import com.github.stkent.amplify.helpers.BaseTest;

import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class GooglePlayStoreRuleTest extends BaseTest {

    private GooglePlayStoreRule googlePlayStoreRule;

    @Mock
    private IEnvironment environment;

    @Override
    public void localSetUp() {
        googlePlayStoreRule = new GooglePlayStoreRule();
    }

    @Test
    public void testThatEnvironmentRuleIsSatisfiedIfGooglePlayStoreIsInstalledOnDevice() {
        // Arrange
        when(environment.isGooglePlayStoreInstalled()).thenReturn(true);

        // Act
        final boolean isEnvironmentRuleSatisfied = googlePlayStoreRule.shouldAllowFeedbackPrompt(environment);

        // Assert
        assertTrue("Environment based rule should be satisfied", isEnvironmentRuleSatisfied);
    }

    @Test
    public void testThatEnvironmentRuleIsNotSatisfiedIfGooglePlayStoreIsNotInstalledOnDevice() {
        // Arrange
        when(environment.isGooglePlayStoreInstalled()).thenReturn(false);

        // Act
        final boolean isEnvironmentRuleSatisfied = googlePlayStoreRule.shouldAllowFeedbackPrompt(environment);

        // Assert
        assertFalse("Environment based rule should not be satisfied", isEnvironmentRuleSatisfied);
    }

}
