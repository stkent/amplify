package com.github.stkent.amplify.tracking;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.Settings;
import com.github.stkent.amplify.tracking.base.IEnvironmentRequirement;
import com.github.stkent.amplify.tracking.base.IFlag;
import com.github.stkent.amplify.tracking.base.IUniqueIdentifierProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class AmplifyStateTracker {

    private static AmplifyStateTracker sharedInstance;

    private AmplifyStateTracker(@NonNull final Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    private final Context applicationContext;
    private final Map<String, IEnvironmentRequirement> environmentRequirements = new HashMap<>();
    private final Map<String, IFlag<Boolean>> trackableBooleanFlags = new HashMap<>();
    private final Map<String, IFlag<Integer>> trackableIntegerFlags = new HashMap<>();
    private final Map<String, IFlag<Long>> trackableLongFlags = new HashMap<>();
    private final Map<String, IFlag<String>> trackableStringFlags = new HashMap<>();

    // consumption methods

    public AmplifyStateTracker getSharedInstance(@NonNull final Context context) {
        if (sharedInstance == null) {
            synchronized (AmplifyStateTracker.class) {
                if (sharedInstance == null) {
                    sharedInstance = new AmplifyStateTracker(context);
                }
            }
        }

        return sharedInstance;
    }

    public boolean shouldAskForRating() {
        for (final IEnvironmentRequirement environmentRequirement : environmentRequirements.values()) {
            if (!environmentRequirement.isMet(applicationContext)) {
                return false;
            }
        }

        for (final IFlag<Boolean> flag : trackableBooleanFlags.values()) {
            final Boolean currentValue = Settings.getSharedPreferences().getBoolean(flag.getUniqueIdentifier(), flag.getInitialTrackedValue());

            if (flag.isActive(currentValue, applicationContext)) {
                return false;
            }
        }

        for (final IFlag<Integer> flag : trackableIntegerFlags.values()) {
            final Integer currentValue = Settings.getSharedPreferences().getInt(flag.getUniqueIdentifier(), flag.getInitialTrackedValue());

            if (flag.isActive(currentValue, applicationContext)) {
                return false;
            }
        }

        for (final IFlag<Long> flag : trackableLongFlags.values()) {
            final Long currentValue = Settings.getSharedPreferences().getLong(flag.getUniqueIdentifier(), flag.getInitialTrackedValue());

            if (flag.isActive(currentValue, applicationContext)) {
                return false;
            }
        }

        for (final IFlag<String> flag : trackableStringFlags.values()) {
            final String currentValue = Settings.getSharedPreferences().getString(flag.getUniqueIdentifier(), flag.getInitialTrackedValue());

            if (flag.isActive(currentValue, applicationContext)) {
                return false;
            }
        }

        return true;
    }

    // configuration methods

    public void registerAppCrashHandler() {
        final Thread.UncaughtExceptionHandler currentHandler = Thread.getDefaultUncaughtExceptionHandler();

        if (!(currentHandler instanceof AmplifyExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new AmplifyExceptionHandler(currentHandler));
        }
    }

    public void registerEnvironmentRequirement(@NonNull final IEnvironmentRequirement requirement) {
        checkForConfigurationConflict(requirement, trackableStringFlags.keySet());
        environmentRequirements.put(requirement.getUniqueIdentifier(), requirement);
    }

    public <T extends IFlag<Boolean>> void registerTrackableBooleanFlag(@NonNull final T trackableFlag) {
        checkForConfigurationConflict(trackableFlag, trackableStringFlags.keySet());
        trackableBooleanFlags.put(trackableFlag.getUniqueIdentifier(), trackableFlag);
    }

    public <T extends IFlag<Integer>> void registerTrackableIntegerFlag(@NonNull final T trackableFlag) {
        checkForConfigurationConflict(trackableFlag, trackableStringFlags.keySet());
        trackableIntegerFlags.put(trackableFlag.getUniqueIdentifier(), trackableFlag);
    }

    public <T extends IFlag<Long>> void registerTrackableLongFlag(@NonNull final T trackableFlag) {
        checkForConfigurationConflict(trackableFlag, trackableStringFlags.keySet());
        trackableLongFlags.put(trackableFlag.getUniqueIdentifier(), trackableFlag);
    }

    public <T extends IFlag<String>> void registerTrackableStringFlag(@NonNull final T trackableFlag) {
        checkForConfigurationConflict(trackableFlag, trackableStringFlags.keySet());
        trackableStringFlags.put(trackableFlag.getUniqueIdentifier(), trackableFlag);
    }

    // validation methods

    private void checkForConfigurationConflict(
            @NonNull final IUniqueIdentifierProvider newConfiguration,
            @NonNull final Set<String> existingConfigurationKeys) {
        if (existingConfigurationKeys.contains(newConfiguration.getUniqueIdentifier())) {
            // todo: this message
            throw new IllegalArgumentException("Some message indicating that this is not allowed.");
        }
    }

}
