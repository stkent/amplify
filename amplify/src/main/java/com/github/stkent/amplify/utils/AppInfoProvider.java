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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

@SuppressWarnings({"PMD.ClassWithOnlyPrivateConstructorsShouldBeFinal", "checkstyle:finalclass"})
public class AppInfoProvider {

    private static final Object SYNC_LOCK = new Object();
    private static AppInfoProvider sharedInstance;

    public static void initialize(@NonNull final Context context) {
        synchronized (SYNC_LOCK) {
            if (sharedInstance == null) {
                sharedInstance = new AppInfoProvider(context);
            }
        }
    }

    @NonNull
    public static AppInfoProvider getSharedInstance() {
        if (sharedInstance == null) {
            throw new IllegalStateException(
                    "Must initialize AppInfoProvider with a context before calling getSharedInstance.");
        }

        return sharedInstance;
    }

    @VisibleForTesting
    public static void setSharedInstance(@NonNull  final AppInfoProvider sharedInstance) {
        AppInfoProvider.sharedInstance = sharedInstance;
    }

    private final Context appContext;

    private AppInfoProvider(@NonNull final Context appContext) {
        this.appContext = appContext;
    }

    public PackageInfo getPackageInfo() throws PackageManager.NameNotFoundException {
        return getPackageInfo(appContext.getPackageName());
    }

    public PackageInfo getPackageInfo(
            @NonNull final String packageName) throws PackageManager.NameNotFoundException {

        return getPackageInfo(packageName, 0);
    }

    public PackageInfo getPackageInfo(
            @NonNull final String packageName,
            final int flags) throws PackageManager.NameNotFoundException {

        final PackageManager packageManager = appContext.getPackageManager();
        return packageManager.getPackageInfo(packageName, flags);
    }

}
