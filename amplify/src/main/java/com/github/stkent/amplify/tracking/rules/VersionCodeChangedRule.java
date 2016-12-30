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

public final class VersionCodeChangedRule implements IEventBasedRule<Integer> {

    private final int appVersionCode;

    public VersionCodeChangedRule(@NonNull final Context context) {
        this(new App(context).getVersionCode());
    }

    @VisibleForTesting(otherwise = PRIVATE)
    /* package-private */ VersionCodeChangedRule(final int appVersionCode) {
        this.appVersionCode = appVersionCode;
    }

    @Override
    public boolean shouldAllowFeedbackPromptByDefault() {
        return true;
    }

    @Override
    public boolean shouldAllowFeedbackPrompt(@NonNull final Integer cachedEventValue) {
        return cachedEventValue < appVersionCode;
    }

    @NonNull
    @Override
    public String getDescription() {
        return "VersionCodeChangedRule with current app version code " + appVersionCode;
    }

}
