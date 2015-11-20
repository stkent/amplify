package com.github.stkent.amplify.tracking.predicates;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.R;
import com.github.stkent.amplify.tracking.interfaces.IApplicationInfoProvider;
import com.github.stkent.amplify.utils.ApplicationUtils;

public class ApplicationInfoProvider implements IApplicationInfoProvider {

    private final Context applicationContext;

    public ApplicationInfoProvider(@NonNull final Context context) {
        this.applicationContext = context.getApplicationContext();
    }

    @NonNull
    @Override
    public String getVersionName() throws PackageManager.NameNotFoundException {
        return ApplicationUtils.getPackageInfo(applicationContext, 0).versionName;
    }

    @Override
    public int getVersionCode() throws PackageManager.NameNotFoundException {
        return ApplicationUtils.getPackageInfo(applicationContext, 0).versionCode;
    }

    @NonNull
    @Override
    public String getFeedbackEmailAddress() throws IllegalStateException {
        try {
            return applicationContext.getString(R.string.amp_feedback_email);
        } catch (Resources.NotFoundException e) {
            throw new IllegalArgumentException("R.string.amp_feedback_email"
                    + "resource not found, you must set this in your strings file for the feedback util to function", e);
        }
    }

}
