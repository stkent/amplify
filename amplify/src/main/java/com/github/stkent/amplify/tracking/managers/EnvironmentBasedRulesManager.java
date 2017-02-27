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

import com.github.stkent.amplify.IEnvironment;
import com.github.stkent.amplify.tracking.Amplify;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentBasedRule;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentBasedRulesManager;

import java.util.ArrayList;
import java.util.List;

public final class EnvironmentBasedRulesManager implements IEnvironmentBasedRulesManager {

    @NonNull
    private final IEnvironment environment;

    @NonNull
    private final List<IEnvironmentBasedRule> environmentBasedRules = new ArrayList<>();

    public EnvironmentBasedRulesManager(@NonNull final IEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public void addEnvironmentBasedRule(@NonNull final IEnvironmentBasedRule rule) {
        environmentBasedRules.add(rule);
    }

    @Override
    public boolean shouldAllowFeedbackPrompt() {
        for (final IEnvironmentBasedRule rule : environmentBasedRules) {
            if (!rule.shouldAllowFeedbackPrompt(environment)) {
                Amplify.getLogger().d("Blocking feedback because of environment based rule: " + rule);
                return false;
            }
        }

        return true;
    }

}
