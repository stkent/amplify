/*
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
package com.github.stkent.amplify.tracking;

import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.interfaces.IEvent;

import static com.github.stkent.amplify.utils.Constants.EXHAUSTIVE_SWITCH_EXCEPTION_MESSAGE;

@SuppressWarnings("PMD.StdCyclomaticComplexity")
public enum PromptInteractionEvent implements IEvent {

    USER_INDICATED_POSITIVE_OPINION,
    USER_INDICATED_CRITICAL_OPINION,
    USER_GAVE_POSITIVE_FEEDBACK,
    USER_GAVE_CRITICAL_FEEDBACK,
    USER_DECLINED_POSITIVE_FEEDBACK,
    USER_DECLINED_CRITICAL_FEEDBACK,
    USER_GAVE_FEEDBACK,
    USER_DECLINED_FEEDBACK;

    @NonNull
    @Override
    public String getTrackingKey() {
        switch (this) {
            case USER_INDICATED_POSITIVE_OPINION:
                return "USER_INDICATED_POSITIVE_OPINION";
            case USER_INDICATED_CRITICAL_OPINION:
                return "USER_INDICATED_CRITICAL_OPINION";
            case USER_GAVE_CRITICAL_FEEDBACK:
                return "USER_GAVE_CRITICAL_FEEDBACK";
            case USER_GAVE_POSITIVE_FEEDBACK:
                return "USER_GAVE_POSITIVE_FEEDBACK";
            case USER_DECLINED_CRITICAL_FEEDBACK:
                return "USER_DECLINED_CRITICAL_FEEDBACK";
            case USER_DECLINED_POSITIVE_FEEDBACK:
                return "USER_DECLINED_POSITIVE_FEEDBACK";
            case USER_GAVE_FEEDBACK:
                return "USER_GAVE_FEEDBACK";
            case USER_DECLINED_FEEDBACK:
                return "USER_DECLINED_FEEDBACK";
        }

        throw new IllegalStateException(EXHAUSTIVE_SWITCH_EXCEPTION_MESSAGE);
    }

}
