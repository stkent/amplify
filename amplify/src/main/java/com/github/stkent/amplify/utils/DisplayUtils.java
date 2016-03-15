package com.github.stkent.amplify.utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

public final class DisplayUtils {

    // From http://stackoverflow.com/a/9563438/2911458 with modifications
    public static float dpToPx(
            @NonNull final Context context,
            final float dp) {

        final Resources resources = context.getResources();
        final DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        return dp * ((float) displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    private DisplayUtils() {

    }

}
