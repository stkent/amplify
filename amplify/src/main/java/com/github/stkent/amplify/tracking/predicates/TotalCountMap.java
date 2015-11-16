package com.github.stkent.amplify.tracking.predicates;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.GenericSettings;
import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.ILogger;

/**
 * Created by bobbake4 on 11/16/15.
 */
public class TotalCountMap extends PredicateMap<Integer> {

    public TotalCountMap(ILogger logger, Context applicationContext) {
        super(logger, new GenericSettings<Integer>(applicationContext), applicationContext);
    }

    @Override
    public void eventTriggered(@NonNull IEvent event) {

        if (containsKey(event)) {
            final Integer cachedCount = getEventValue(event);
            final Integer updatedCount = cachedCount + 1;
            updateEventValue(event, updatedCount);
        }
    }
}
