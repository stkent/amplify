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
package com.github.stkent.amplify.tracking.predicates;

import com.github.stkent.amplify.helpers.StubbedLogger;
import com.github.stkent.amplify.tracking.interfaces.IApplicationInfoProvider;
import com.github.stkent.amplify.tracking.interfaces.ISettings;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.MockitoAnnotations.initMocks;

public class FirstTimePredicateTest {

    private FirstTimePredicate firstTimePredicate;

    @Mock
    private ISettings<Long> mockSettings;
    @Mock
    private IApplicationInfoProvider mockApplicationInfoProvider;

    @Before
    public void setUp() {
        initMocks(this);

        firstTimePredicate = new FirstTimePredicate(
                new StubbedLogger(),
                mockSettings,
                mockApplicationInfoProvider);
    }

    @Test
    public void testThatFirstEventTimeIsRecorded() {
        // Arrange

        // Act

        // Assert

    }

}
