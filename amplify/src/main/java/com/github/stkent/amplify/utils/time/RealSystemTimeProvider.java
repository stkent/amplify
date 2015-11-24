package com.github.stkent.amplify.utils.time;

public class RealSystemTimeProvider implements ISystemTimeProvider {

    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }

}
