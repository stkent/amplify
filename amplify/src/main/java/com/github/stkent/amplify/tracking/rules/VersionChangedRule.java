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

import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.interfaces.IEventBasedRule;
import com.github.stkent.amplify.utils.AppInfoProvider;

public final class VersionChangedRule implements IEventBasedRule<String> {

    @Override
    public boolean shouldAllowFeedbackPromptByDefault() {
        return true;
    }

    @Override
    public boolean shouldAllowFeedbackPrompt(@NonNull final String cachedEventValue) {
        return !cachedEventValue.equals(getCurrentAppVersionName());
    }

    @NonNull
    @Override
    public String getStatusString(@NonNull final String cachedEventValue) {
        return "Event last triggered for app version " + cachedEventValue + ". "
                + "Current app version: " + getCurrentAppVersionName();
    }

    @NonNull
    @Override
    public String getDescription() {
        return "VersionChangedRule";
    }

    @NonNull
    private String getCurrentAppVersionName() {
        // We access the singleton AppInfoProvider instance statically here to make it possible for
        // library consumers to create new VersionChangedRule instances easily!
        return AppInfoProvider.getSharedInstance().getPackageInfo().versionName;
    }

}
