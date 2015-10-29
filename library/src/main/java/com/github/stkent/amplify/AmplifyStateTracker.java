package com.github.stkent.amplify;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import static android.content.pm.PackageManager.GET_ACTIVITIES;
import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public final class AmplifyStateTracker {

    public <T extends IEventType> void incrementEventType(Class<T> eventType) {

    }

    public interface ICooldownEvent {
        long cooldownTimeMillis();
    }

//     maybe this should be renamed as a condition?
    public interface IEventType<T> {
        @NonNull
        String getKey();

        @NonNull
        T getInitialValue();

        @NonNull
        T increment(@NonNull final T currentValue, @NonNull final Context applicationContext);

        Boolean shouldAskForFeedback(@NonNull final T currentValue, @NonNull final Context applicationContext);
    }

    public interface IEnvironmentChecker {
        boolean isSatisfied(@NonNull final Context applicationContext);
    }

    public class GooglePlayServicesChecker implements IEnvironmentChecker {
        @Override
        public boolean isSatisfied(@NonNull final Context applicationContext) {
            final PackageManager pm = applicationContext.getPackageManager();
            boolean playServicesInstalled;

            try {
                final PackageInfo info = pm.getPackageInfo("com.android.vending", GET_ACTIVITIES);
                final String label = (String) info.applicationInfo.loadLabel(pm);
                playServicesInstalled = label != null && !label.equals("Market");
            } catch (final PackageManager.NameNotFoundException e) {
                playServicesInstalled = false;
            }

            return playServicesInstalled;
        }
    }

    public abstract class CooldownEvent implements IEventType<Long>, ICooldownEvent {

        private Long getCurrentTimeMillis() {
            return System.currentTimeMillis();
        }

        @NonNull
        @Override
        public Long getInitialValue() {
            return getCurrentTimeMillis();
        }

        @NonNull
        @Override
        public Long increment(@NonNull final Long currentValue, @NonNull final Context applicationContext) {
            return getCurrentTimeMillis();
        }

        @Override
        public Boolean shouldAskForFeedback(@NonNull final Long currentValue, @NonNull final Context applicationContext) {
            if (getCurrentTimeMillis() - currentValue < cooldownTimeMillis()) {
                return false;
            }

            return null;
        }
    }

    public class UserDeclinedFeedbackEvent extends CooldownEvent {

        @NonNull
        @Override
        public String getKey() {
            return "UserDeclinedFeedbackEvent";
        }

        @Override
        public long cooldownTimeMillis() {
            return TimeUnit.DAYS.toMillis(7);
        }
    }

    public class AppCrashedEvent extends CooldownEvent {

        @NonNull
        @Override
        public String getKey() {
            return "AppCrashEvent";
        }

        @Override
        public long cooldownTimeMillis() {
            return TimeUnit.DAYS.toMillis(7);
        }
    }

    public class UserDeclinedToGiveFeedbackForCurrentVersionEvent implements IEventType<String> {

        private static final String INITIAL_VALUE = "InitialValue";
        private static final String FAILURE_VALUE = "FailureValue";

        private String getVersionName(@NonNull final Context applicationContext) throws PackageManager.NameNotFoundException {
            final PackageManager packageManager = applicationContext.getPackageManager();
            final String applicationPackageName = applicationContext.getPackageName();

            return packageManager.getPackageInfo(applicationPackageName, 0).versionName;
        }

        @NonNull
        @Override
        public String getKey() {
            return "UserDeclinedToGiveFeedbackForCurrentVersionEvent";
        }

        @NonNull
        @Override
        public String getInitialValue() {
            return INITIAL_VALUE;
        }

        @NonNull
        @Override
        public String increment(@NonNull final String currentValue, @NonNull final Context applicationContext) {
            if (currentValue.equals(FAILURE_VALUE)) {
                return FAILURE_VALUE;
            }

            try {
                return getVersionName(applicationContext);
            } catch (final PackageManager.NameNotFoundException e) {
                return FAILURE_VALUE;
            }
        }

        @Override
        public Boolean shouldAskForFeedback(@NonNull final String currentValue, @NonNull final Context applicationContext) {
            if (FAILURE_VALUE.equals(currentValue)) {
                return false;
            }

            try {
                if (currentValue.equals(getVersionName(applicationContext))) {
                    return false;
                }

                return null;
            } catch (final PackageManager.NameNotFoundException e) {
                return false;
            }
        }
    }

    public class UserRatedAppEvent implements IEventType<Integer> {

        @NonNull
        @Override
        public String getKey() {
            return "UserRatedAppEvent";
        }

        @NonNull
        @Override
        public Integer getInitialValue() {
            return 0;
        }

        @NonNull
        @Override
        public Integer increment(@NonNull final Integer currentValue, @NonNull final Context applicationContext) {
            return Math.max(currentValue + 1, Integer.MAX_VALUE);
        }

        @Override
        public Boolean shouldAskForFeedback(@NonNull final Integer currentValue, @NonNull final Context applicationContext) {
            if (currentValue > 0) {
                return false;
            }

            return null;
        }
    }

    public enum ActionType {
        USER_GAVE_RATING,
        USER_DECLINED_RATING,
        USER_GAVE_FEEDBACK,
        USER_DECLINED_FEEDBACK,
        APP_CRASHED
    }

    private static final long RATING_PROMPT_COOLDOWN_TIME_MS = DAYS.toMillis(7);
    private static final long DEFAULT_LAST_ACTION_TIME_MS = 0;
    private static final int DEFAULT_RATED_VERSION_CODE = -1;
    private static final int DEFAULT_LAST_FEEDBACK_VERSION_CODE = -1;

    private static AmplifyStateTracker instance;

    private AmplifyStateTracker() {
    }

    public static AmplifyStateTracker getInstance() {
        synchronized (AmplifyStateTracker.class) {
            if (instance == null) {
                instance = new AmplifyStateTracker();
            }
        }

        return instance;
    }

    public boolean shouldAskForRating() {
        if (userHasRatedApp()) {
            Log.d(Constants.LOG_TAG, "User has already rated the app. Should not ask for rating/feedback.");
            return false;
        }

        if (!isGooglePlayInstalled(AppProvider.getAppContext())) {
            Log.d(Constants.LOG_TAG, "Play Store is not installed. Should not ask for rating/feedback.");
            return false;
        }

        if (userHasGivenFeedbackForCurrentVersion()) {
            Log.d(Constants.LOG_TAG, "User has already given feedback for this app version. Should not ask for rating/feedback.");
            return false;
        }

        if (isInCooldownMode()) {
            Log.d(Constants.LOG_TAG, "Last negative action (crash, rating declined, feedback declined) was less than "
                    + MILLISECONDS.toDays(RATING_PROMPT_COOLDOWN_TIME_MS) + " days ago. Should not ask for rating/feedback.");
            return false;
        }

        return true;
    }

    public void notify(final ActionType actionType) {
        final SharedPreferences.Editor editor = Settings.getEditor();

        switch (actionType) {
            case USER_GAVE_RATING:
                editor.putInt(Constants.RATED_VERSION_CODE, BuildConfig.VERSION_CODE);
                break;
            case USER_GAVE_FEEDBACK:
                editor.putInt(Constants.LAST_FEEDBACK_VERSION_CODE, BuildConfig.VERSION_CODE);
                break;
            case USER_DECLINED_RATING:
            case USER_DECLINED_FEEDBACK:
            case APP_CRASHED:
                editor.putLong(Constants.LAST_NEGATIVE_ACTION_TIME_MS, System.currentTimeMillis());
                break;
            default:
                break;
        }

        editor.apply();
    }

    public void reset() {
        final SharedPreferences.Editor editor = Settings.getEditor();

        Log.d(Constants.LOG_TAG, "Reset rating tracker state.");
        editor.remove(Constants.RATED_VERSION_CODE);
        editor.remove(Constants.LAST_FEEDBACK_VERSION_CODE);
        editor.remove(Constants.LAST_NEGATIVE_ACTION_TIME_MS);
        editor.apply();
    }

    public void initRatingExceptionHandler() {
        final Thread.UncaughtExceptionHandler currentHandler = Thread.getDefaultUncaughtExceptionHandler();

        if (!(currentHandler instanceof AmplifyExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new AmplifyExceptionHandler(currentHandler));
        }
    }

    private boolean userHasRatedApp() {
        final int ratedVersionCode = Settings.getSharedPreferences().getInt(Constants.RATED_VERSION_CODE, DEFAULT_RATED_VERSION_CODE);
        return ratedVersionCode != DEFAULT_RATED_VERSION_CODE;
    }

    private boolean isGooglePlayInstalled(final Context context) {
        final PackageManager pm = context.getPackageManager();
        boolean playServicesInstalled;

        try {
            final PackageInfo info = pm.getPackageInfo("com.android.vending", GET_ACTIVITIES);
            final String label = (String) info.applicationInfo.loadLabel(pm);
            playServicesInstalled = label != null && !label.equals("Market");
        } catch (PackageManager.NameNotFoundException e) {
            playServicesInstalled = false;
        }

        return playServicesInstalled;
    }

    private boolean userHasGivenFeedbackForCurrentVersion() {
        final int lastFeedbackVersionCode = Settings.getSharedPreferences().getInt(Constants.LAST_FEEDBACK_VERSION_CODE,
                DEFAULT_LAST_FEEDBACK_VERSION_CODE);
        return lastFeedbackVersionCode < BuildConfig.VERSION_CODE;
    }

    private boolean isInCooldownMode() {
        final long timeSinceLastNegativeAction = System.currentTimeMillis() - Settings.getSharedPreferences().getLong(
                Constants.LAST_NEGATIVE_ACTION_TIME_MS, DEFAULT_LAST_ACTION_TIME_MS);
        return timeSinceLastNegativeAction < RATING_PROMPT_COOLDOWN_TIME_MS;
    }

}
