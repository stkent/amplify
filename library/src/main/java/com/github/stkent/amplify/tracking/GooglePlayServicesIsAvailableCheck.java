package com.github.stkent.amplify.tracking;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.interfaces.IEnvironmentCheck;

import static android.content.pm.PackageManager.GET_ACTIVITIES;

public class GooglePlayServicesIsAvailableCheck implements IEnvironmentCheck {

    @Override
    public boolean isMet(@NonNull final Context applicationContext) {
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
