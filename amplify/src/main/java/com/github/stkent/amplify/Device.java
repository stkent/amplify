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
package com.github.stkent.amplify;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import static java.util.Locale.US;

public final class Device implements IDevice {

    @NonNull
    private static String getDensityBucketString(@NonNull final DisplayMetrics displayMetrics) {
        switch (displayMetrics.densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                return "ldpi";
            case DisplayMetrics.DENSITY_MEDIUM:
                return "mdpi";
            case DisplayMetrics.DENSITY_HIGH:
                return "hdpi";
            case DisplayMetrics.DENSITY_XHIGH:
                return "xhdpi";
            case DisplayMetrics.DENSITY_XXHIGH:
                return "xxhdpi";
            case DisplayMetrics.DENSITY_XXXHIGH:
                return "xxxhdpi";
            case DisplayMetrics.DENSITY_TV:
                return "tvdpi";
            default:
                return "Unknown";
        }
    }

    @NonNull
    private final String resolution;

    @NonNull
    private final String actualDensity;

    @NonNull
    private final String densityBucket;

    public Device(@NonNull final Context context) {
        final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        resolution = displayMetrics.heightPixels + "x" + displayMetrics.widthPixels;
        actualDensity = displayMetrics.densityDpi + "dpi";
        densityBucket = getDensityBucketString(displayMetrics);
    }

    @NonNull
    @Override
    public String getManufacturer() {
        return Build.MANUFACTURER.toUpperCase(US);
    }

    @NonNull
    @Override
    public String getModel() {
        return Build.MODEL.toUpperCase(US);
    }

    @NonNull
    @Override
    public String getResolution() {
        return resolution;
    }

    @NonNull
    @Override
    public String getActualDensity() {
        return actualDensity;
    }

    @NonNull
    public String getDensityBucket() {
        return densityBucket;
    }

}
