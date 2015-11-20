package com.github.stkent.amplify.helpers;

import com.github.stkent.amplify.tracking.ClockUtil;

import org.junit.After;
import org.junit.Before;

import static org.mockito.MockitoAnnotations.initMocks;

public abstract class BaseTest {

    public static final long MARCH_18_2014_838PM_UTC = 1395175090000L;

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
