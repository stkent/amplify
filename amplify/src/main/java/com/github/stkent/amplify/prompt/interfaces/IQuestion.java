package com.github.stkent.amplify.prompt.interfaces;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface IQuestion {

    @NonNull
    String getTitle();

    @Nullable
    String getSubTitle();

    @NonNull
    String getPositiveButtonLabel();

    @NonNull
    String getNegativeButtonLabel();

}
