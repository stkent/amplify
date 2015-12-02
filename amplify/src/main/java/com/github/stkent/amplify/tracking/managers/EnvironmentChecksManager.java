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
package com.github.stkent.amplify.tracking.managers;

import android.support.annotation.NonNull;

import com.github.stkent.amplify.ILogger;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentCheck;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentInfoProvider;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentChecksManager;

import java.util.ArrayList;
import java.util.List;

public class EnvironmentChecksManager implements IEnvironmentChecksManager {

    @NonNull
    private final ILogger logger;

    @NonNull
    private final IEnvironmentInfoProvider environmentInfoProvider;

    @NonNull
    private final List<IEnvironmentCheck> environmentChecks = new ArrayList<>();

    public EnvironmentChecksManager(
            @NonNull final ILogger logger,
            @NonNull final IEnvironmentInfoProvider environmentInfoProvider) {
        this.logger = logger;
        this.environmentInfoProvider = environmentInfoProvider;
    }

    @Override
    public void addEnvironmentCheck(@NonNull final IEnvironmentCheck environmentCheck) {
        environmentChecks.add(environmentCheck);
    }

    @Override
    public boolean shouldAllowFeedbackPrompt() {
        for (final IEnvironmentCheck environmentCheck : environmentChecks) {
            if (!environmentCheck.shouldAllowFeedbackPrompt(environmentInfoProvider)) {
                return false;
            }
        }

        return true;
    }

}
