package com.github.stkent.amplify.tracking;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.stkent.amplify.tracking.interfaces.IEnvironmentCheck;
import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.IEventCheck;
import com.github.stkent.amplify.tracking.interfaces.ISettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class AmplifyStateTracker {

    // static members

    private static AmplifyStateTracker sharedInstance;

    public static AmplifyStateTracker get(@NonNull final Context context) {
        if (sharedInstance == null) {
            synchronized (AmplifyStateTracker.class) {
                if (sharedInstance == null) {
                    sharedInstance = new AmplifyStateTracker(context);
                }
            }
        }

        return sharedInstance;
    }

    // instance members

    private final Context applicationContext;
    private final List<IEnvironmentCheck> environmentRequirements = new ArrayList<>();
    private final Map<IEvent, List<IEventCheck<Long>>> lastEventTimePredicates = new HashMap<>();
    private final Map<IEvent, List<IEventCheck<String>>> lastEventVersionPredicates = new HashMap<>();
    private final Map<IEvent, List<IEventCheck<Integer>>> totalEventCountPredicates = new HashMap<>();

    // TODO: when writing tests, loosen the visibility and override this method to provide an
    // alternate implementation
    private ISettings getSettings() {
        return Settings.getSharedInstance(applicationContext);
    }

    // constructors

    private AmplifyStateTracker(@NonNull final Context context) {
        this.applicationContext = context.getApplicationContext();
    }

    // configuration methods

    public AmplifyStateTracker trackTotalEventCount(@NonNull final IEvent event, @NonNull final IEventCheck<Integer> predicate) {
        // todo: check for conflicts here
        performEventRelatedInitializationIfRequired(event);

        if (!totalEventCountPredicates.containsKey(event)) {
            totalEventCountPredicates.put(event, new ArrayList<IEventCheck<Integer>>());
        }

        totalEventCountPredicates.get(event).add(predicate);
        Log.d("TAG", totalEventCountPredicates.get(event).toString());

        return this;
    }

    public AmplifyStateTracker trackLastEventTime(@NonNull final IEvent event, @NonNull final IEventCheck<Long> predicate) {
        // todo: check for conflicts here
        performEventRelatedInitializationIfRequired(event);

        if (!lastEventTimePredicates.containsKey(event)) {
            lastEventTimePredicates.put(event, new ArrayList<IEventCheck<Long>>());
        }

        lastEventTimePredicates.get(event).add(predicate);
        Log.d("TAG", lastEventTimePredicates.get(event).toString());

        return this;
    }

    public AmplifyStateTracker trackLastEventVersion(@NonNull final IEvent event, @NonNull final IEventCheck<String> predicate) {
        // todo: check for conflicts here
        performEventRelatedInitializationIfRequired(event);

        if (!lastEventVersionPredicates.containsKey(event)) {
            lastEventVersionPredicates.put(event, new ArrayList<IEventCheck<String>>());
        }

        lastEventVersionPredicates.get(event).add(predicate);
        Log.d("TAG", lastEventVersionPredicates.get(event).toString());

        return this;
    }

    public AmplifyStateTracker addEnvironmentCheck(@NonNull final IEnvironmentCheck requirement) {
        // todo: check for conflicts here
        environmentRequirements.add(requirement);
        return this;
    }

    // update methods

    public AmplifyStateTracker notifyEventTriggered(@NonNull final IEvent event) {
        if (totalEventCountPredicates.containsKey(event)) {
            final Integer cachedCount = getSettings().getTotalEventCount(event);
            final Integer updatedCount = cachedCount + 1;
            getSettings().setTotalEventCount(event, updatedCount);
        }

        if (lastEventTimePredicates.containsKey(event)) {
            final Long currentTime = System.currentTimeMillis();
            getSettings().setLastEventTime(event, currentTime);
        }

        if (lastEventVersionPredicates.containsKey(event)) {
            try {
                final String currentVersion = TrackingUtils.getAppVersionName(applicationContext);
                getSettings().setLastEventVersion(event, currentVersion);
            } catch (final PackageManager.NameNotFoundException e) {
                // TODO: log a warning here
            }
        }

        return this;
    }

    // query methods

    public boolean shouldAskForRating() {
        for (final IEnvironmentCheck environmentRequirement : environmentRequirements) {
            if (!environmentRequirement.isMet(applicationContext)) {
                return false;
            }
        }

        for (final IEvent event : totalEventCountPredicates.keySet()) {
            final Integer totalEventCount = getSettings().getTotalEventCount(event);

            for (final IEventCheck<Integer> predicate : totalEventCountPredicates.get(event)) {
                Log.d("TAG", event.getTrackingKey() + ": " + predicate.getStatusString(totalEventCount, applicationContext));

                if (predicate.shouldBlockFeedbackPrompt(totalEventCount, applicationContext)) {
                    return false;
                }
            }
        }

        for (final IEvent event : lastEventTimePredicates.keySet()) {
            final Long lastEventTime = getSettings().getLastEventTime(event);

            for (final IEventCheck<Long> predicate : lastEventTimePredicates.get(event)) {
                Log.d("TAG", event.getTrackingKey() + ": " + predicate.getStatusString(lastEventTime, applicationContext));

                if (predicate.shouldBlockFeedbackPrompt(lastEventTime, applicationContext)) {
                    return false;
                }
            }
        }

        for (final IEvent event : lastEventVersionPredicates.keySet()) {
            final String lastEventVersion = getSettings().getLastEventVersion(event);

            if (lastEventVersion != null) {
                for (final IEventCheck<String> predicate : lastEventVersionPredicates.get(event)) {
                    Log.d("TAG", event.getTrackingKey() + ": " + predicate.getStatusString(lastEventVersion, applicationContext));

                    if (predicate.shouldBlockFeedbackPrompt(lastEventVersion, applicationContext)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    // private implementation:

    private void performEventRelatedInitializationIfRequired(@NonNull final IEvent event) {
        if (isEventAlreadyTracked(event)) {
            return;
        }

        event.performRelatedInitialization(applicationContext);
    }

    private boolean isEventAlreadyTracked(@NonNull final IEvent event) {
        final Set<IEvent> allTrackedEvents = new HashSet<>();

        allTrackedEvents.addAll(lastEventTimePredicates.keySet());
        allTrackedEvents.addAll(lastEventVersionPredicates.keySet());
        allTrackedEvents.addAll(totalEventCountPredicates.keySet());

        return allTrackedEvents.contains(event);
    }

}
