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
package com.github.stkent.amplify.tracking.checks;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.interfaces.IApplicationVersionNameProvider;
import com.github.stkent.amplify.tracking.interfaces.IEventCheck;

public final class VersionChangedCheck implements IEventCheck<String> {

    @NonNull
    private final IApplicationVersionNameProvider applicationVersionNameProvider;

    public VersionChangedCheck(@NonNull final IApplicationVersionNameProvider applicationVersionNameProvider) {
        this.applicationVersionNameProvider = applicationVersionNameProvider;
    }

    @Override
    public boolean shouldAllowFeedbackPrompt(@NonNull final String cachedEventValue) {
        try {
            final String currentAppVersion = applicationVersionNameProvider.getVersionName();
            return !cachedEventValue.equals(currentAppVersion);
        } catch (final PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @NonNull
    @Override
    public String getStatusString(@NonNull final String cachedEventValue) {
        String statusStringSuffix;

        try {
            statusStringSuffix = "Current app version: " + applicationVersionNameProvider.getVersionName();
        } catch (final PackageManager.NameNotFoundException e) {
            statusStringSuffix = "Current app version cannot be determined.";
        }

        return "Event last triggered for app version " + cachedEventValue + ". " + statusStringSuffix;
    }

}
