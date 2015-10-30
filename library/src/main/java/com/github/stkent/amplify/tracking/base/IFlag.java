package com.github.stkent.amplify.tracking.base;

import android.content.Context;
import android.support.annotation.NonNull;

public interface IFlag<T> extends IUniqueIdentifierProvider {

    @NonNull
    T getInitialTrackedValue();

    @NonNull
    T incrementTrackedValue(@NonNull final T currentValue, @NonNull final Context applicationContext);

    boolean isActive(@NonNull final T currentValue, @NonNull final Context applicationContext);

}
