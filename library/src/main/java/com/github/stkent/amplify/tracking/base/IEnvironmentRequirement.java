package com.github.stkent.amplify.tracking.base;

import android.content.Context;
import android.support.annotation.NonNull;

public interface IEnvironmentRequirement extends IUniqueIdentifierProvider {

    boolean isMet(@NonNull final Context applicationContext);

}
