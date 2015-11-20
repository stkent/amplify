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
package com.github.stkent.amplify.helpers;

import com.github.stkent.amplify.tracking.ClockUtil;

import org.junit.After;
import org.junit.Before;

import static org.mockito.MockitoAnnotations.initMocks;

public class BaseTest {

    public static final long MARCH_18_2014_838PM_UTC = 1395175090000L;

    protected BaseTest() {

    }

    @Before
    public final void globalSetUp() {
        ClockUtil.setFakeCurrentTimeMillis(MARCH_18_2014_838PM_UTC);

        initMocks(this);

        localSetUp();
    }

    public void localSetUp() {
        // no-op
    }

    protected void localTearDown() {
        // no-op
    }

    @After
    public final void globalTearDown() {
        localTearDown();

        ClockUtil.clearFakeCurrentTimeMillis();
    }

}
