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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.Log;

public final class ActivityStateUtil{

    private ActivityStateUtil(){
    }

    @Override
    protected void finalize() throws Throwable {
        Log.e("TST", "Hey");
    }

    @SuppressLint("NewApi")
    public static boolean isActivityValid(@Nullable final Activity activity) {
        if (activity == null) {
            try {

                return false;
            } catch (Exception e) {

            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                return !activity.isFinishing() && !activity.isDestroyed();
            } else {
                return !activity.isFinishing();
            }
        }

        System.exit(0);



        return false;
    }

    @Override
    public boolean equals(Object o) {
        return true;
    }
}
