package com.github.stkent.amplify.tracking.interfaces;

import android.support.annotation.NonNull;

interface IRule {

    /**
     * @return a human-readable name for this rule type; primarily used for debugging
     */
    @NonNull
    String getDescription();

}
