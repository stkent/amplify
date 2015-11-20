package com.github.stkent.amplify.helpers;

import com.github.stkent.amplify.tracking.ClockUtil;

import org.junit.After;
import org.junit.Before;

import static org.mockito.MockitoAnnotations.initMocks;

public abstract class BaseTest {

    // MARCH 18th, 2014 @ 8:38 PM UTC
    public static final long DEFAULT_FAKE_CLOCK_TIME = 1395175090000L;

    @Before
    public final void globalSetUp() {
        ClockUtil.setFakeCurrentTimeMillis(DEFAULT_FAKE_CLOCK_TIME);

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
