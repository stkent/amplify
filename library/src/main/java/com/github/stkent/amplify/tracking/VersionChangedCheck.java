package com.github.stkent.amplify.tracking;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.interfaces.IEventCheck;

public final class VersionChangedCheck implements IEventCheck<String> {

    @Override
    public boolean shouldBlockFeedbackPrompt(@NonNull final String cachedEventValue, @NonNull final Context applicationContext) {
        try {
            final String currentAppVersion = TrackingUtils.getAppVersionName(applicationContext);
            return !cachedEventValue.equals(currentAppVersion);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO: log here
            // TODO: be safe (return false) or strict (return true) here?
            return true;
        }
    }

    @NonNull
    @Override
    public String getStatusString(@NonNull final String cachedEventValue, @NonNull final Context applicationContext) {
        String statusStringSuffix;

        try {
            statusStringSuffix = "Current app version: " + TrackingUtils.getAppVersionName(applicationContext);
        } catch (PackageManager.NameNotFoundException e) {
            statusStringSuffix = "Current app version cannot be determined.";
        }

        return "Event last triggered for app version " + cachedEventValue + ". " + statusStringSuffix;
    }

}
