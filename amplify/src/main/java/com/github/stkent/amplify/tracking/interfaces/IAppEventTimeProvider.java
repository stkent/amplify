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

/**
 * An abstract representation of a class that provides the times at which significant
 * application-level events last occurred.
 */
public interface IAppEventTimeProvider {

    /**
     * @return the time in milliseconds since the application was first installed
     */
    long getInstallTime();

    /**
     * @return the time in milliseconds since the applications was last updated
     */
    long getLastUpdateTime();

}
