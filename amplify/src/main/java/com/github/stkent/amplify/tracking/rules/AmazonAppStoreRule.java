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

import com.github.stkent.amplify.IEnvironment;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentBasedRule;

/**
 * An implementation of {@code IEnvironmentBasedRule} that verifies whether or not the Amazon App Store is installed on
 * the current device.
 */
public final class AmazonAppStoreRule implements IEnvironmentBasedRule {

    @Override
    public boolean shouldAllowFeedbackPrompt(@NonNull final IEnvironment environment) {
        return environment.isAmazonAppStoreInstalled();
    }

    @NonNull
    @Override
    public String getDescription() {
        return "AmazonAppStoreRule";
    }

}
