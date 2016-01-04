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
import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.interfaces.IApplicationEventTimeProvider;
import com.github.stkent.amplify.utils.ApplicationUtils;

public class ApplicationEventTimeProvider implements IApplicationEventTimeProvider {

    private static final long DEFAULT_EVENT_TIME_MS = 0;

    @NonNull
    private final Context applicationContext;

    public ApplicationEventTimeProvider(@NonNull final Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public long getInstallTime() {
        try {
            return ApplicationUtils.getPackageInfo(applicationContext).firstInstallTime;
        } catch (PackageManager.NameNotFoundException e) {
            return DEFAULT_EVENT_TIME_MS;
        }
    }

    @Override
    public long getLastUpdateTime() {
        try {
            return ApplicationUtils.getPackageInfo(applicationContext).lastUpdateTime;
        } catch (PackageManager.NameNotFoundException e) {
            return DEFAULT_EVENT_TIME_MS;
        }
    }

}
