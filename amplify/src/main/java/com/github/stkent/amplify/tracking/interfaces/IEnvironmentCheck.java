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

/**
 * An abstract representation of an event-independent prerequisite for
 * prompting the user for feedback.
 */
public interface IEnvironmentCheck {

    /**
     * @param applicationInfoProvider exposes information about the consuming
     *        application
     * @return true if the consuming application is able to prompt the user
     *         for feedback; false otherwise
     */
    boolean isMet(@NonNull final IEnvironmentInfoProvider applicationInfoProvider);

}
