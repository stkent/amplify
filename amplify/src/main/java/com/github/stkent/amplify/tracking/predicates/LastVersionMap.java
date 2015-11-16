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
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.GenericSettings;
import com.github.stkent.amplify.tracking.TrackingUtils;
import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.ILogger;

/**
 * Created by bobbake4 on 11/16/15.
 */
public class LastVersionMap extends PredicateMap<String> {

    public LastVersionMap(ILogger logger, Context applicationContext) {
        super(logger, new GenericSettings<String>(applicationContext), applicationContext);
    }

    @Override
    public void eventTriggered(@NonNull IEvent event) {

        if (containsKey(event)) {
            try {
                final String currentVersion = TrackingUtils.getAppVersionName(getApplicationContext());
                updateEventValue(event, currentVersion);
            } catch (final PackageManager.NameNotFoundException e) {
                getLogger().d("Could not read current app version name.");
            }
        }
    }
}
