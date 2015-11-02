package com.github.stkent.amplify.tracking.interfaces;

import android.content.Context;
import android.support.annotation.NonNull;

public interface IEnvironmentCheck {

    boolean isMet(@NonNull final Context applicationContext);

}
