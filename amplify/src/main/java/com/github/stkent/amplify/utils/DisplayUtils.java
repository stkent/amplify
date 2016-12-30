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
package com.github.stkent.amplify.utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import static java.lang.Math.max;
import static java.lang.Math.round;

public final class DisplayUtils {

    // From http://stackoverflow.com/a/9563438/2911458 with modifications
    public static int dpToPx(
            @NonNull final Context context,
            final int dp) {

        if (dp < 0) {
            throw new IllegalStateException("Dimension must be > 0.");
        }

        if (dp == 0) {
            return 0;
        }

        final Resources resources = context.getResources();
        final DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        final float floatResult
                = dp * ((float) displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);

        return max(1, round(floatResult));
    }

    private DisplayUtils() {
        // This constructor intentionally left blank.
    }

}
