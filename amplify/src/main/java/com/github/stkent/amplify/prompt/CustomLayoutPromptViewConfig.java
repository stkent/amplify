package com.github.stkent.amplify.prompt;

import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public final class CustomLayoutPromptViewConfig {

    @LayoutRes
    private final int questionLayout;

    @LayoutRes
    private final int thanksLayout;

    public CustomLayoutPromptViewConfig(@NonNull final TypedArray typedArray) {
        // TODO: fill this in, and document non-recycling well!
    }

    public CustomLayoutPromptViewConfig(
            @LayoutRes final int questionLayout,
            @LayoutRes final int thanksLayout) {

        this.questionLayout = questionLayout;
        this.thanksLayout = thanksLayout;
    }

    @LayoutRes
    private int getQuestionLayout() {
        return questionLayout;
    }

    @LayoutRes
    private int getThanksLayout() {
        return thanksLayout;
    }

}
