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
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentCapabilitiesProvider;
import com.github.stkent.amplify.tracking.interfaces.IPrerequisite;
import com.github.stkent.amplify.tracking.interfaces.IPrerequisitesManager;

import java.util.ArrayList;
import java.util.List;

public class PrerequisitesManager implements IPrerequisitesManager {

    @NonNull
    private final ILogger logger;

    @NonNull
    private final IEnvironmentCapabilitiesProvider environmentInfoProvider;

    @NonNull
    private final List<IPrerequisite> environmentChecks = new ArrayList<>();

    public PrerequisitesManager(@NonNull final IEnvironmentCapabilitiesProvider environmentInfoProvider,
                                      @NonNull final ILogger logger) {
        this.environmentInfoProvider = environmentInfoProvider;
        this.logger = logger;
    }

    @Override
    public void addPrerequisite(@NonNull final IPrerequisite prerequisite) {
        environmentChecks.add(prerequisite);
    }

    @Override
    public boolean shouldAllowFeedbackPrompt() {
        for (final IPrerequisite environmentCheck : environmentChecks) {
            if (!environmentCheck.shouldAllowFeedbackPrompt(environmentInfoProvider)) {
                logger.d("Blocking feedback because of environment check: " + environmentCheck);
                return false;
            }
        }

        return true;
    }

}
