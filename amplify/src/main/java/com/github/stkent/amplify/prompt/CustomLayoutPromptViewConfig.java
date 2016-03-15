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
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleableRes;

import com.github.stkent.amplify.R;
import com.github.stkent.amplify.utils.Constants;

public final class CustomLayoutPromptViewConfig {

    private static final int DEFAULT_LAYOUT_RES_ID_IF_UNDEFINED = Integer.MAX_VALUE;

    /**
     * @return the color value for the attribute at <code>index</code>, if defined; null otherwise
     */
    private static Integer suppliedLayoutOrNull(
            @Nullable final TypedArray typedArray,
            @StyleableRes final int index) {

        if (typedArray != null) {
            final int layoutResourceId
                    = typedArray.getResourceId(index, DEFAULT_LAYOUT_RES_ID_IF_UNDEFINED);

            return layoutResourceId != DEFAULT_LAYOUT_RES_ID_IF_UNDEFINED ? layoutResourceId : null;
        }

        return null;
    }

    @LayoutRes
    private final int questionLayout;

    @Nullable
    @LayoutRes
    private final Integer thanksLayout;

    public CustomLayoutPromptViewConfig(@NonNull final TypedArray typedArray) {
        final Integer questionLayout = suppliedLayoutOrNull(
                typedArray,
                R.styleable.CustomLayoutPromptView_prompt_view_question_layout);

        if (questionLayout == null) {
            throw new IllegalStateException(Constants.MISSING_LAYOUT_IDS_EXCEPTION_MESSAGE);
        }

        this.questionLayout = questionLayout;

        this.thanksLayout = suppliedLayoutOrNull(
                typedArray,
                R.styleable.CustomLayoutPromptView_prompt_view_thanks_layout);
    }

    public CustomLayoutPromptViewConfig(
            @LayoutRes final int questionLayout,
            @Nullable @LayoutRes final Integer thanksLayout) {

        this.questionLayout = questionLayout;
        this.thanksLayout = thanksLayout;
    }

    @LayoutRes
    public int getQuestionLayout() {
        return questionLayout;
    }

    @Nullable @LayoutRes
    public Integer getThanksLayout() {
        return thanksLayout;
    }

}
