package com.github.stkent.amplify;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public final class Settings {

    private static SharedPreferences sharedPreferences;
    private static Editor editor;

    private Settings() {

    }

    /**
     * Returns a SharedPreferences object for reading values from the standard location for SharedPreferences
     * @return SharedPreferences sharedPreferences
     */
    public static SharedPreferences getSharedPreferences() {
        synchronized (Settings.class) {
            if (sharedPreferences == null) {
                sharedPreferences = AppProvider.getAppContext().getSharedPreferences(AppProvider.getAppContext().getPackageName() + "_prefs",
                        Context.MODE_PRIVATE);
            }
        }

        return sharedPreferences;
    }

    /**
     * Returns an Editor object linked to the standard location for SharedPreferences. You must call apply to save your changes
     * @return Editor editor
     */
    @SuppressLint("CommitPrefEdits")
    public static Editor getEditor() {
        synchronized (Settings.class) {
            if (editor == null) {
                editor = getSharedPreferences().edit();
            }
        }

        return editor;
    }

}
