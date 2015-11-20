package com.github.stkent.amplify.tracking.interfaces;

import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.annotation.NonNull;

public interface IApplicationInfoProvider {

    @NonNull
    String getVersionName() throws PackageManager.NameNotFoundException;

    int getVersionCode() throws PackageManager.NameNotFoundException;

    @NonNull
    String getFeedbackEmailAddress() throws Resources.NotFoundException;

}
