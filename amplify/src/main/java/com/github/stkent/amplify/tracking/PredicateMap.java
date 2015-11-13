package com.github.stkent.amplify.tracking;

import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.IEventCheck;
import com.github.stkent.amplify.tracking.interfaces.ILogger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by bobbake4 on 11/13/15.
 */
public class PredicateMap<T> extends ConcurrentHashMap<IEvent, List<IEventCheck<T>>> {

    private final ILogger logger;

    public PredicateMap(ILogger logger) {
        this.logger = logger;
    }

    public void trackEvent(@NonNull final IEvent event, @NonNull final IEventCheck<T> predicate) {

        if (!containsKey(event)) {
            put(event, new ArrayList<IEventCheck<T>>());
        }

        get(event).add(predicate);

        logger.d(get(event).toString());
    }

}
