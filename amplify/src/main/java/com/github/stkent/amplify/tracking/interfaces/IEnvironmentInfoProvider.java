package com.github.stkent.amplify.tracking.interfaces;

import android.content.Intent;
import android.support.annotation.NonNull;

public interface IEnvironmentInfoProvider {

    boolean isApplicationInstalled(@NonNull final String packageName);

    boolean isGooglePlayStoreInstalled();

    boolean canHandleIntent(@NonNull final Intent intent);

}
