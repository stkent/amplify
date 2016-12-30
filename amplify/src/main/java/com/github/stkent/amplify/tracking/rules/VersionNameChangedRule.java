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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.github.stkent.amplify.App;
import com.github.stkent.amplify.tracking.interfaces.IEventBasedRule;

import static android.support.annotation.VisibleForTesting.PRIVATE;

public final class VersionNameChangedRule implements IEventBasedRule<String> {

    @NonNull
    private final String appVersionName;

    public VersionNameChangedRule(@NonNull final Context context) {
        this(new App(context).getVersionName());
    }

    @VisibleForTesting(otherwise = PRIVATE)
    /* package-private */ VersionNameChangedRule(@NonNull final String appVersionName) {
        this.appVersionName = appVersionName;
    }

    @Override
    public boolean shouldAllowFeedbackPromptByDefault() {
        return true;
    }

    @Override
    public boolean shouldAllowFeedbackPrompt(@NonNull final String cachedEventValue) {
        return !cachedEventValue.equals(appVersionName);
    }

    @NonNull
    @Override
    public String getDescription() {
        return "VersionNameChangedRule with current app version name " + appVersionName;
    }

}
