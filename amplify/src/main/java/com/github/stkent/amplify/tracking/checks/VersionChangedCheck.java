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

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.TrackingUtils;
import com.github.stkent.amplify.tracking.interfaces.IEventCheck;

public final class VersionChangedCheck implements IEventCheck<String> {

    @Override
    public boolean shouldBlockFeedbackPrompt(@NonNull final String cachedEventValue, @NonNull final Context applicationContext) {
        try {
            final String currentAppVersion = TrackingUtils.getAppVersionName(applicationContext);
            return !cachedEventValue.equals(currentAppVersion);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO: log here
            // TODO: be safe (return false) or strict (return true) here?
            return true;
        }
    }

    @NonNull
    @Override
    public String getStatusString(@NonNull final String cachedEventValue, @NonNull final Context applicationContext) {
        String statusStringSuffix;

        try {
            statusStringSuffix = "Current app version: " + TrackingUtils.getAppVersionName(applicationContext);
        } catch (PackageManager.NameNotFoundException e) {
            statusStringSuffix = "Current app version cannot be determined.";
        }

        return "Event last triggered for app version " + cachedEventValue + ". " + statusStringSuffix;
    }

}
