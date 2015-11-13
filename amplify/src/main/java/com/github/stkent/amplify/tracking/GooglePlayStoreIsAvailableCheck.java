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

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.interfaces.IEnvironmentCheck;

import static android.content.pm.PackageManager.GET_ACTIVITIES;

/**
 * An implementation of {@code IEnvironmentCheck} that verifies whether or not
 * the Google Play Store is installed on the current device.
 */
public class GooglePlayStoreIsAvailableCheck implements IEnvironmentCheck {

    @Override
    public boolean isMet(@NonNull final Context applicationContext) {
        final PackageManager pm = applicationContext.getPackageManager();
        boolean playServicesInstalled;

        try {
            final PackageInfo info = pm.getPackageInfo("com.android.vending", GET_ACTIVITIES);
            final String label = (String) info.applicationInfo.loadLabel(pm);
            playServicesInstalled = label != null && !label.equals("Market");
        } catch (final PackageManager.NameNotFoundException e) {
            playServicesInstalled = false;
        }

        return playServicesInstalled;
    }

}
