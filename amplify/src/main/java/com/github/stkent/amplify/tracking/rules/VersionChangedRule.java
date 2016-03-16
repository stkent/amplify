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
package com.github.stkent.amplify.tracking.rules;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.interfaces.IEventBasedRule;
import com.github.stkent.amplify.utils.AppInfoProvider;

public final class VersionChangedRule implements IEventBasedRule<String> {

    @Override
    public boolean shouldAllowFeedbackPrompt(@NonNull final String cachedEventValue) {
        try {
            final String currentAppVersion
                    = AppInfoProvider.getSharedInstance().getPackageInfo().versionName;

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
            statusStringSuffix = "Current app version: "
                    + AppInfoProvider.getSharedInstance().getPackageInfo().versionName;

        } catch (final PackageManager.NameNotFoundException e) {
            statusStringSuffix = "Current app version cannot be determined.";
        }

        return "Event last triggered for app version " + cachedEventValue + ". " + statusStringSuffix;
    }

}
