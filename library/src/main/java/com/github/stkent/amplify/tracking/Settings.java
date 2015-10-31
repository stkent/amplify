package com.github.stkent.amplify.tracking;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.ISettings;

public final class Settings implements ISettings {

    private static final String SHARED_PREFERENCES_NAME = "AMPLIFY_SHARED_PREFERENCES_NAME";

    private static Settings sharedInstance;

    public static Settings getSharedInstance(@NonNull final Context context) {
        if (sharedInstance == null) {
            synchronized (AmplifyStateTracker.class) {
                if (sharedInstance == null) {
                    sharedInstance = new Settings(context);
                }
            }
        }

        return sharedInstance;
    }

    private final SharedPreferences sharedPreferences;

    private Settings(@NonNull final Context applicationContext) {
        this.sharedPreferences = applicationContext
                .getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public int getTotalEventCount(@NonNull final IEvent event) {
        final String key = generateTotalEventCountTrackingKey(event);
        return sharedPreferences.getInt(key, 0);
    }

    @Override
    public void setTotalEventCount(@NonNull final IEvent event, final int totalEventCount) {
        final String key = generateTotalEventCountTrackingKey(event);
        sharedPreferences.edit().putInt(key, totalEventCount).apply();
    }

    @Override
    public long getLastEventTime(@NonNull final IEvent event) {
        final String key = generateTotalEventCountTrackingKey(event);
        return sharedPreferences.getLong(key, 0);
    }

    @Override
    public void setLastEventTime(@NonNull final IEvent event, final long lastEventTime) {
        final String key = generateLastEventTimeTrackingKey(event);
        sharedPreferences.edit().putLong(key, lastEventTime).apply();
    }

    @Nullable
    @Override
    public String getLastEventVersion(@NonNull final IEvent event) {
        final String key = generateTotalEventCountTrackingKey(event);
        return sharedPreferences.getString(key, null);
    }

    @Override
    public void setLastEventVersion(@NonNull final IEvent event, @NonNull final String lastEventVersion) {
        final String key = generateLastEventVersionTrackingKey(event);
        sharedPreferences.edit().putString(key, lastEventVersion).apply();
    }

    @Override
    public void reset() {
        sharedPreferences.edit().clear().apply();
    }

    private String generateTotalEventCountTrackingKey(@NonNull final IEvent event) {
        return "TOTAL_EVENT_COUNT_" + event.getTrackingKey();
    }

    private String generateLastEventTimeTrackingKey(@NonNull final IEvent event) {
        return "LAST_EVENT_TIME_" + event.getTrackingKey();
    }

    private String generateLastEventVersionTrackingKey(@NonNull final IEvent event) {
        return "LAST_EVENT_VERSION_" + event.getTrackingKey();
    }

}
