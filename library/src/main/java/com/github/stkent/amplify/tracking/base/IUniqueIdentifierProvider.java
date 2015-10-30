package com.github.stkent.amplify.tracking.base;

import android.support.annotation.NonNull;

public interface IUniqueIdentifierProvider {

    @NonNull
    String getUniqueIdentifier();

}
