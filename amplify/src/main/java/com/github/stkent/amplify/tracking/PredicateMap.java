package com.github.stkent.amplify.tracking;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.IEventCheck;
import com.github.stkent.amplify.tracking.interfaces.ILogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by bobbake4 on 11/13/15.
 */
public class PredicateMap<T> extends ConcurrentHashMap<IEvent, List<IEventCheck<T>>> {

    private final ILogger logger;
    private final GenericSettings<T> settings;
    private final Context applicationContext;

    public PredicateMap(ILogger logger, GenericSettings<T> settings, Context applicationContext) {
        this.logger = logger;
        this.settings = settings;
        this.applicationContext = applicationContext;
    }

    public void trackEvent(@NonNull final IEvent event, @NonNull final IEventCheck<T> predicate) {

        if (!containsKey(event)) {
            put(event, new ArrayList<IEventCheck<T>>());
        }

        get(event).add(predicate);

        logger.d(get(event).toString());
    }

    public boolean allowFeedbackPrompt() {

        for (final Map.Entry<IEvent, List<IEventCheck<T>>> eventCheckSet : entrySet()) {
            final IEvent event = eventCheckSet.getKey();

            final T cachedEventValue = settings.getEventValue(event);

            for (final IEventCheck<T> predicate : eventCheckSet.getValue()) {
                logger.d(event.getTrackingKey() + ": " + predicate.getStatusString(cachedEventValue, applicationContext));

                if (predicate.shouldBlockFeedbackPrompt(cachedEventValue, applicationContext)) {
                    return false;
                }
            }
        }

        return true;
    }

    public T getEventValue(@NonNull final IEvent event) {
        return settings.getEventValue(event);
    }

    public void updateEventValue(@NonNull final IEvent event, T value) {
        settings.writeEventValue(event, value);
    }

}
