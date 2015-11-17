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
package com.github.stkent.amplify.tracking.predicates;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.GenericSettings;
import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.ILogger;

/**
 * Created by bobbake4 on 11/16/15.
 */
public class TotalCountPredicate extends EventPredicate<Integer> {

    public TotalCountPredicate(ILogger logger, Context applicationContext) {
        super(logger, new GenericSettings<Integer>(applicationContext, logger), applicationContext);
    }

    @Override
    public void eventTriggered(@NonNull IEvent event) {

        if (containsEvent(event)) {
            final Integer cachedCount = getEventValue(event);
            final Integer updatedCount = cachedCount + 1;
            updateEventValue(event, updatedCount);
        }
    }

    @Override
    public Integer defaultValue() {
        return 0;
    }
}