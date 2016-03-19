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

import android.content.pm.PackageInfo;
import android.os.Build;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.interfaces.IAppFeedbackDataProvider;
import com.github.stkent.amplify.tracking.interfaces.IAppInfoProvider;
import com.github.stkent.amplify.utils.StringUtils;

public final class AppFeedbackDataProvider implements IAppFeedbackDataProvider {

    @NonNull
    private final IAppInfoProvider appInfoProvider;

    public AppFeedbackDataProvider(@NonNull final IAppInfoProvider appInfoProvider) {
        this.appInfoProvider = appInfoProvider;
    }

    @NonNull
    @Override
    public String getDeviceName() {
        final String manufacturer = Build.MANUFACTURER;
        final String model = Build.MODEL;

        String deviceName;

        if (model.startsWith(manufacturer)) {
            deviceName = model;
        } else {
            deviceName = manufacturer + " " + model;
        }

        return StringUtils.capitalizeFully(deviceName);
    }

    @NonNull
    public String getVersionDisplayString() {
        final PackageInfo packageInfo = appInfoProvider.getPackageInfo();

        final String applicationVersionName = packageInfo.versionName;
        final int applicationVersionCode = packageInfo.versionCode;

        return String.format("%s (%s)", applicationVersionName, applicationVersionCode);
    }

    @NonNull
    @Override
    public CharSequence getAppNameString() {
        return appInfoProvider.getApplicationInfo().loadLabel(appInfoProvider.getPackageManager());
    }

}
