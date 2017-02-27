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

import com.github.stkent.amplify.utils.time.RealSystemTimeProvider;
import com.github.stkent.amplify.utils.time.SystemTimeUtil;

import org.junit.After;
import org.junit.Before;

import static org.mockito.MockitoAnnotations.initMocks;

public class BaseTest {

    public static final long MARCH_18_2014_838PM_UTC = 1395175090000L;
    public static final String DEFAULT_MOCK_EVENT_TRACKING_KEY = "DEFAULT_MOCK_EVENT_TRACKING_KEY";

    private static final RealSystemTimeProvider REAL_SYSTEM_TIME_PROVIDER = new RealSystemTimeProvider();
    private static final FakeSystemTimeProvider DEFAULT_FAKE_SYSTEM_TIME_PROVIDER = new FakeSystemTimeProvider(MARCH_18_2014_838PM_UTC);

    protected BaseTest() {
        // This constructor intentionally left blank.
    }

    protected final void setFakeCurrentTimeMillis(final long fakeCurrentTimeMillis) {
        SystemTimeUtil.setSharedInstance(new FakeSystemTimeProvider(fakeCurrentTimeMillis));
    }

    @Before
    public final void globalSetUp() {
        SystemTimeUtil.setSharedInstance(DEFAULT_FAKE_SYSTEM_TIME_PROVIDER);

        initMocks(this);

        localSetUp();
    }

    public void localSetUp() {
        // This method intentionally left blank.
    }

    protected void localTearDown() {
        // This method intentionally left blank.
    }

    @After
    public final void globalTearDown() {
        localTearDown();

        SystemTimeUtil.setSharedInstance(REAL_SYSTEM_TIME_PROVIDER);
    }

}
