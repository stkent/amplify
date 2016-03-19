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
package com.github.stkent.amplify.utils.appinfo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

public final class AppInfoUtil {

    private static final Object SYNC_LOCK = new Object();

    @Nullable
    private static IAppInfoProvider sharedAppInfoProvider;

    public static void initialize(@NonNull final Context context) {
        synchronized (SYNC_LOCK) {
            if (sharedAppInfoProvider == null) {
                sharedAppInfoProvider = new RealAppInfoProvider(context);
            }
        }
    }

    @NonNull
    public static IAppInfoProvider getSharedAppInfoProvider() {
        if (sharedAppInfoProvider == null) {
            throw new IllegalStateException(
                    "Must initialize AppInfoProvider before calling getSharedAppInfoProvider.");
        }

        return sharedAppInfoProvider;
    }

    @VisibleForTesting
    public static void setSharedAppInfoProvider(@NonNull final IAppInfoProvider appInfoProvider) {
        sharedAppInfoProvider = appInfoProvider;
    }

    private AppInfoUtil() {

    }

}
