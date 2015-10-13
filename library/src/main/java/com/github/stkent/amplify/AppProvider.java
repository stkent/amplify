package com.github.stkent.amplify;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

public final class AppProvider {

    public interface AppHelper {
        int dpToPixels(final int dpUnits);
        long getCurrentTimeMillis();
        void toast(@StringRes final int messageStringRes);
        void toast(@NonNull final String message);
    }

    private static AppHelper helper;

    private AppProvider() {

    }

    public static AppHelper getAppHelper() {
        return (AppHelper) getHelper();
    }

    public static Application getAppContext() {
        return (Application) getHelper();
    }

    public static void setHelper(AppHelper helper) {
        AppProvider.helper = helper;
    }

    private static Object getHelper() {
        if (helper == null) {
            throw new RuntimeException("Application helper is null");
        } else {
            return helper;
        }
    }

}
