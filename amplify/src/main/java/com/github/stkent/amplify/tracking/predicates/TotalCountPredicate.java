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

import com.github.stkent.amplify.tracking.Settings;
import com.github.stkent.amplify.ILogger;
import com.github.stkent.amplify.tracking.interfaces.ITrackedEvent;

public class TotalCountPredicate extends EventPredicate<Integer> {

    public TotalCountPredicate(ILogger logger, Context applicationContext) {
        super(logger,
                new Settings<Integer>(applicationContext, logger),
                new ApplicationInfoProvider(applicationContext));
    }

    @Override
    public void eventTriggered(@NonNull final ITrackedEvent event) {

        final Integer cachedCount = getEventValue(event);
        final Integer updatedCount = cachedCount + 1;
        getLogger().d("TotalCountPredicate updating event value from: " + cachedCount + ", to: " + updatedCount);
        updateEventValue(event, updatedCount);
    }

    @Override
    public Integer defaultValue() {
        return 0;
    }
}
