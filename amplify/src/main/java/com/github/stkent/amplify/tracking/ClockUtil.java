package com.github.stkent.amplify.tracking;

import android.support.annotation.Nullable;

public final class ClockUtil {

    @Nullable
    private static Long fakeCurrentTimeMillis = null;

    // todo: consider injecting this around the app rather than relying
    // on static access
    public static long getCurrentTimeMillis() {
        if (fakeCurrentTimeMillis != null) {
            return fakeCurrentTimeMillis;
        }

        return System.currentTimeMillis();
    }

    public static void setFakeCurrentTimeMillis(final long fakeCurrentTimeMillis) {
        ClockUtil.fakeCurrentTimeMillis = fakeCurrentTimeMillis;
    }

    public static void clearFakeCurrentTimeMillis() {
        ClockUtil.fakeCurrentTimeMillis = null;
    }

}
