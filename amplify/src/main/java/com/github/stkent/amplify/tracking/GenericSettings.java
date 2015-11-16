package com.github.stkent.amplify.tracking;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.stkent.amplify.tracking.interfaces.IEvent;

import java.util.Map;

/**
 * Created by bobbake4 on 11/13/15.
 */
public class GenericSettings<T> {

    private static final String SHARED_PREFERENCES_NAME = "AMPLIFY_SHARED_PREFERENCES_NAME";

    private SharedPreferences sharedPreferences;

    public GenericSettings(Context applicationContext) {
        this.sharedPreferences = applicationContext
                .getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public void writeEventValue(@NonNull final IEvent event, final T value) {

        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(value.getClass().equals(String.class)) {
            editor.putString(event.getTrackingKey(), (String) value);
        } else if (value.getClass().equals(Boolean.class)) {
            editor.putBoolean(event.getTrackingKey(), (Boolean) value);
        } else if (value.getClass().equals(Long.class)) {
            editor.putLong(event.getTrackingKey(), (Long) value);
        } else if (value.getClass().equals(Integer.class)) {
            editor.putInt(event.getTrackingKey(), (Integer) value);
        } else if (value.getClass().equals(Float.class)) {
            editor.putLong(event.getTrackingKey(), (Long) value);
        }

        //TODO is it alright that this is asynchronous?
        editor.apply();
    }

    @Nullable
    public T getEventValue(@NonNull final IEvent event) {
        Map<String, ?> map = sharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : map.entrySet()) {
            if (entry.getKey().equals(event.getTrackingKey())) {
                return (T) entry.getValue();
            }
        }

        return null;
    }

}
