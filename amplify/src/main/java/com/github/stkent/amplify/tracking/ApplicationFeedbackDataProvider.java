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
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.R;
import com.github.stkent.amplify.tracking.interfaces.IApplicationFeedbackDataProvider;
import com.github.stkent.amplify.tracking.interfaces.IApplicationVersionNameProvider;
import com.github.stkent.amplify.utils.ApplicationUtils;
import com.github.stkent.amplify.utils.StringUtils;

public class ApplicationFeedbackDataProvider implements IApplicationFeedbackDataProvider {

    @NonNull
    private final Context applicationContext;

    @NonNull
    private final IApplicationVersionNameProvider applicationVersionNameProvider;

    public ApplicationFeedbackDataProvider(@NonNull final Context applicationContext) {
        this.applicationContext = applicationContext;
        this.applicationVersionNameProvider = new ApplicationVersionNameProvider(applicationContext);
    }

    @NonNull
    @Override
    public String getDeviceName() {
        final String manufacturer = Build.MANUFACTURER;
        final String model = Build.MODEL;

        String deviceName;

        if (model.startsWith(manufacturer)) {
            deviceName = StringUtils.capitalize(model);
        } else {
            deviceName = StringUtils.capitalize(manufacturer) + " " + model;
        }

        return deviceName == null ? "Unknown Device" : deviceName;
    }

    @NonNull
    @Override
    public String getVersionDisplayString() throws PackageManager.NameNotFoundException {
        final int applicationVersionCode = ApplicationUtils.getPackageInfo(applicationContext).versionCode;

        // todo: prefer String.format here
        return applicationVersionNameProvider.getVersionName() + " (" + applicationVersionCode + ")";
    }

    @NonNull
    @Override
    public String getFeedbackEmailAddress() throws IllegalStateException {
        try {
            return applicationContext.getString(R.string.amp_feedback_email);
        } catch (Resources.NotFoundException e) {
            throw new IllegalArgumentException("R.string.amp_feedback_email"
                    + "resource not found, you must set this in your strings file for the feedback util to function", e);
        }
    }

}
