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
package com.github.stkent.amplify.tracking;

import android.support.annotation.Nullable;

public final class ClockUtil {

    @Nullable
    private static Long fakeCurrentTimeMillis;

    private ClockUtil() {

    }

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
