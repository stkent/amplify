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
package com.github.stkent.amplify.tracking.interfaces;

import android.support.annotation.NonNull;

import com.github.stkent.amplify.IEnvironment;

/**
 * An abstract representation of a prompt timing rule that depends on the environment in which the
 * embedding application is running.
 */
public interface IEnvironmentBasedRule extends IRule {

    /**
     * Call to determine whether this rule allows us to prompt the user for feedback at this time.
     *
     * @param environment provides relevant information about the environment in which
     *        the embedding application is currently running
     * @return true if this rule is satisfied and should allow the feedback prompt to be shown;
     *         false otherwise
     */
    boolean shouldAllowFeedbackPrompt(@NonNull final IEnvironment environment);

}
