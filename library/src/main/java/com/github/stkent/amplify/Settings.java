package com.github.stkent.amplify;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public final class Settings {

    private static SharedPreferences settings;
    private static Editor editor;

    private Settings() {

    }

    public static SharedPreferences getSettings() {
        return settings;
    }

    public static Editor getEditor() {
        return editor;
    }

    /**
     * Returns a SharedPreferences object for reading values from the standard location for SharedPreferences
     * @return SharedPreferences settings
     */
    public static SharedPreferences settings() {
        if (settings != null) {
            return settings;
        }

        settings = AppProvider.getAppContext().getSharedPreferences(AppProvider.getAppContext().getPackageName() + "_prefs", Context.MODE_PRIVATE);

        return settings;
    }

    /**
     * Returns an editor object linked to the standard location for SharedPreferences. You must call apply to save your changes
     * @return Editor editor
     */
    @SuppressLint("CommitPrefEdits")
    public static Editor editor() {
        if (editor != null) {
            return editor;
        }

        if (settings != null) {
            editor = settings.edit();
            return editor;
        }

        settings();
        editor = settings.edit();
        return editor;
    }

}
