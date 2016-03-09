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

public final class CustomLayoutPromptViewConfig {

    private static final int DEFAULT_GET_LAYOUT_VALUE_IF_UNDEFINED = Integer.MAX_VALUE;

    /**
     * @return <code>primaryColor</code> if it is non-null; <code>defaultColor</code> otherwise
     */
    @LayoutRes
    private static int defaultIfNull(
            @Nullable final Integer primaryLayout,
            @Nullable final Integer defaultLayout) {

        return primaryLayout != null ? primaryLayout : defaultLayout;
    }

    /**
     * @return the color value for the attribute at <code>index</code>, if defined; null otherwise
     */
    private static Integer suppliedLayoutOrNull(
            @Nullable final TypedArray typedArray,
            @StyleableRes final int index) {

        if (typedArray != null) {
            final int color = typedArray.getColor(index, DEFAULT_GET_LAYOUT_VALUE_IF_UNDEFINED);
            return color != DEFAULT_GET_LAYOUT_VALUE_IF_UNDEFINED ? color : null;
        }

        return null;
    }

    @Nullable
    private final Integer baseQuestionLayout;

    @Nullable
    private final Integer userOpinionQuestionLayout;

    @Nullable
    private final Integer positiveFeedbackQuestionLayout;

    @Nullable
    private final Integer criticalFeedbackQuestionLayout;

    @LayoutRes
    private final int thanksLayout;

    public CustomLayoutPromptViewConfig(@NonNull final TypedArray typedArray) {
        final Integer baseQuestionLayout = suppliedLayoutOrNull(
                typedArray,
                R.styleable.CustomLayoutPromptView_prompt_view_base_question_layout);

        final Integer userOpinionQuestionLayout = suppliedLayoutOrNull(
                typedArray,
                R.styleable.CustomLayoutPromptView_prompt_view_user_opinion_question_layout);

        final Integer positiveFeedbackQuestionLayout = suppliedLayoutOrNull(
                typedArray,
                R.styleable.CustomLayoutPromptView_prompt_view_positive_feedback_question_layout);

        final Integer criticalFeedbackQuestionLayout = suppliedLayoutOrNull(
                typedArray,
                R.styleable.CustomLayoutPromptView_prompt_view_critical_feedback_question_layout);

        final Integer thanksLayout = suppliedLayoutOrNull(
                typedArray,
                R.styleable.CustomLayoutPromptView_prompt_view_thanks_layout);

        validateAllLayoutResourceIds(
                baseQuestionLayout,
                userOpinionQuestionLayout,
                positiveFeedbackQuestionLayout,
                criticalFeedbackQuestionLayout,
                thanksLayout);

        this.baseQuestionLayout = baseQuestionLayout;
        this.userOpinionQuestionLayout = userOpinionQuestionLayout;
        this.positiveFeedbackQuestionLayout = positiveFeedbackQuestionLayout;
        this.criticalFeedbackQuestionLayout = criticalFeedbackQuestionLayout;
        this.thanksLayout = thanksLayout;
    }

    public CustomLayoutPromptViewConfig(
            @Nullable final Integer baseQuestionLayout,
            @Nullable final Integer userOpinionQuestionLayout,
            @Nullable final Integer positiveFeedbackQuestionLayout,
            @Nullable final Integer criticalFeedbackQuestionLayout,
            @LayoutRes final int thanksLayout) {

        validateQuestionLayoutResourceIds(
                baseQuestionLayout,
                userOpinionQuestionLayout,
                positiveFeedbackQuestionLayout,
                criticalFeedbackQuestionLayout);

        this.baseQuestionLayout = baseQuestionLayout;
        this.userOpinionQuestionLayout = userOpinionQuestionLayout;
        this.positiveFeedbackQuestionLayout = positiveFeedbackQuestionLayout;
        this.criticalFeedbackQuestionLayout = criticalFeedbackQuestionLayout;
        this.thanksLayout = thanksLayout;
    }

    @LayoutRes
    private int getUserOpinionQuestionLayout() {
        return defaultIfNull(userOpinionQuestionLayout, baseQuestionLayout);
    }

    @LayoutRes
    private int getPositiveFeedbackQuestionLayout() {
        return defaultIfNull(positiveFeedbackQuestionLayout, baseQuestionLayout);
    }

    @LayoutRes
    private int getCriticalFeedbackQuestionLayout() {
        return defaultIfNull(criticalFeedbackQuestionLayout, baseQuestionLayout);
    }

    @LayoutRes
    private int getThanksLayout() {
        return thanksLayout;
    }

    private void validateAllLayoutResourceIds(
            @Nullable final Integer baseQuestionLayout,
            @Nullable final Integer userOpinionQuestionLayout,
            @Nullable final Integer positiveFeedbackQuestionLayout,
            @Nullable final Integer criticalFeedbackQuestionLayout,
            @Nullable final Integer thanksLayout) {

        if (thanksLayout == null) {
            throw new IllegalStateException("Must provide a thanks layout resource id.");
        }

        validateQuestionLayoutResourceIds(
                baseQuestionLayout,
                userOpinionQuestionLayout,
                positiveFeedbackQuestionLayout,
                criticalFeedbackQuestionLayout);
    }

    private void validateQuestionLayoutResourceIds(
            @Nullable final Integer baseQuestionLayout,
            @Nullable final Integer userOpinionQuestionLayout,
            @Nullable final Integer positiveFeedbackQuestionLayout,
            @Nullable final Integer criticalFeedbackQuestionLayout) {

        if (baseQuestionLayout == null &&
                (userOpinionQuestionLayout == null
                        || positiveFeedbackQuestionLayout == null
                        || criticalFeedbackQuestionLayout == null)) {

            throw new IllegalStateException("Must provide layout resource ids for all questions.");
        }
    }

}
