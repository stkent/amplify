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
package com.github.stkent.amplify.utils;

public final class Constants {

    public static final String EXHAUSTIVE_SWITCH_EXCEPTION_MESSAGE
            = "This switch statement should be exhaustive.";

    public static final String MISSING_LAYOUT_IDS_EXCEPTION_MESSAGE
            = "Provided layout does not include views with required ids.";

    /**
     * The name of the SharedPreferences instance used to store all tracked event data by default.
     * DO NOT MODIFY! (If you do, any apps relying on the default Amplify SharedPreferences instance
     * will have all their tracking data reset automatically when they next update the library...)
     */
    public static final String DEFAULT_BACKING_SHARED_PREFERENCES_NAME
            = "AMPLIFY_SHARED_PREFERENCES_NAME";

    private Constants() {
        // This constructor intentionally left blank.
    }

}
