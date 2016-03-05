package com.github.stkent.amplify.prompt;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.stkent.amplify.prompt.interfaces.IQuestion;

final class Question implements IQuestion {

    @NonNull
    private final String title;

    @Nullable
    private final String subTitle;

    @NonNull
    private final String positiveButtonLabel;

    @NonNull
    private final String negativeButtonLabel;

    public Question(
            @NonNull  final String title,
            @Nullable final String subTitle,
            @NonNull  final String positiveButtonLabel,
            @NonNull  final String negativeButtonLabel) {

        this.title = title;
        this.subTitle = subTitle;
        this.positiveButtonLabel = positiveButtonLabel;
        this.negativeButtonLabel = negativeButtonLabel;
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

    @NonNull
    @Override
    public String getPositiveButtonLabel() {
        return positiveButtonLabel;
    }

    @NonNull
    @Override
    public String getNegativeButtonLabel() {
        return negativeButtonLabel;
    }

}
