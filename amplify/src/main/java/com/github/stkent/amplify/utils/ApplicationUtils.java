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

public final class ApplicationUtils {

    private ApplicationUtils() {

    }

    public static PackageInfo getPackageInfo(@NonNull final Context applicationContext) throws PackageManager.NameNotFoundException {
        return getPackageInfo(applicationContext, applicationContext.getPackageName());
    }

    public static PackageInfo getPackageInfo(
            @NonNull final Context applicationContext,
            @NonNull final String packageName) throws PackageManager.NameNotFoundException {
        return getPackageInfo(applicationContext, packageName, 0);
    }

    public static PackageInfo getPackageInfo(
            @NonNull final Context applicationContext,
            @NonNull final String packageName,
            final int flags) throws PackageManager.NameNotFoundException {
        final PackageManager packageManager = applicationContext.getPackageManager();
        return packageManager.getPackageInfo(packageName, flags);
    }

}
