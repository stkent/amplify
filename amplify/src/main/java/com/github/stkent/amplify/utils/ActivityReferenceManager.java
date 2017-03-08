/*
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

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

public final class ActivityReferenceManager implements ActivityLifecycleCallbacks {

    @Nullable
    private WeakReference<Activity> wActivity;

    @Override
    public void onActivityCreated(final Activity activity, final Bundle savedInstanceState) {
        // This method intentionally left blank
    }

    @Override
    public void onActivityStarted(final Activity activity) {
        // This method intentionally left blank
    }

    @Override
    public void onActivityResumed(final Activity activity) {
        wActivity = new WeakReference<>(activity);
    }

    @Override
    public void onActivityPaused(final Activity activity) {
        // Intentionally left blank
    }

    @Override
    public void onActivityStopped(final Activity activity) {
        // This method intentionally left blank
    }

    @Override
    public void onActivitySaveInstanceState(final Activity activity, final Bundle outState) {
        // This method intentionally left blank
    }

    @Override
    public void onActivityDestroyed(final Activity activity) {
        // This method intentionally left blank
    }

    @Nullable
    public Activity getValidatedActivity() {
        if (wActivity == null) {
            return null;
        }

        final Activity activity = wActivity.get();
        if (!isActivityValid(activity)) {
            return null;
        }

        return activity;
    }

    private boolean isActivityValid(@Nullable final Activity activity) {
        if (activity == null) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return !activity.isFinishing() && !activity.isDestroyed();
        } else {
            return !activity.isFinishing();
        }
    }

}
