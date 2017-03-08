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

public enum PromptViewEvent implements IEvent {

    PROMPT_SHOWN,
    THANKS_SHOWN,
    PROMPT_DISMISSED;

    @NonNull
    @Override
    public String getTrackingKey() {
        switch (this) {
            case PROMPT_SHOWN:
                return "PROMPT_SHOWN";
            case THANKS_SHOWN:
                return "THANKS_SHOWN";
            case PROMPT_DISMISSED:
                return "PROMPT_DISMISSED";
        }

        throw new IllegalStateException(EXHAUSTIVE_SWITCH_EXCEPTION_MESSAGE);
    }

}
