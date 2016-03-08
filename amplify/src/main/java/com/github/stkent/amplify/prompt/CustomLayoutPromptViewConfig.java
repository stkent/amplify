/**
 * Copyright 2015 Stuart Kent
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
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
