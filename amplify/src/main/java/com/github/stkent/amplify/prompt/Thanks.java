package com.github.stkent.amplify.prompt;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.stkent.amplify.prompt.interfaces.IThanks;

final class Thanks implements IThanks {

    @NonNull
    private final String title;

    @Nullable
    private final String subTitle;

    protected Thanks(
            @NonNull  final String title,
            @Nullable final String subTitle) {

        this.title = title;
        this.subTitle = subTitle;
    }

    @NonNull
    @Override
    public String getTitle() {
        return title;
    }

    @Nullable
    @Override
    public String getSubTitle() {
        return subTitle;
    }

}
