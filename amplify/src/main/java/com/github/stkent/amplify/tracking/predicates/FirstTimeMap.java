package com.github.stkent.amplify.tracking.predicates;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.GenericSettings;
import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.ILogger;

/**
 * Created by bobbake4 on 11/16/15.
 */
public class FirstTimeMap extends PredicateMap<Long> {

    public FirstTimeMap(ILogger logger, Context applicationContext) {
        super(logger, new GenericSettings<Long>(applicationContext), applicationContext);
    }

    @Override
    public void eventTriggered(@NonNull IEvent event) {

        if (containsKey(event)) {
            final Long cachedTime = getEventValue(event);

            if (cachedTime == Long.MAX_VALUE) {
                final Long currentTime = System.currentTimeMillis();
                updateEventValue(event, Math.min(cachedTime, currentTime));
            }
        }
    }
}
