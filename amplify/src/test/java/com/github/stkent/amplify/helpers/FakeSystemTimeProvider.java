package com.github.stkent.amplify.helpers;

import com.github.stkent.amplify.utils.time.ISystemTimeProvider;

public class FakeSystemTimeProvider implements ISystemTimeProvider {

    private final long fakeCurrentTimeMillis;

    public FakeSystemTimeProvider(final long fakeCurrentTimeMillis) {
        this.fakeCurrentTimeMillis = fakeCurrentTimeMillis;
    }

    @Override
    public long currentTimeMillis() {
        return fakeCurrentTimeMillis;
    }

}
