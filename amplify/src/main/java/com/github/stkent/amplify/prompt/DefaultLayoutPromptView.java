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

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.github.stkent.amplify.prompt.interfaces.IPromptView;
import com.github.stkent.amplify.prompt.interfaces.IQuestion;
import com.github.stkent.amplify.prompt.interfaces.IQuestionView;
import com.github.stkent.amplify.prompt.interfaces.IThanksView;

public final class DefaultLayoutPromptView extends BasePromptView implements IPromptView {

    private DefaultLayoutPromptViewConfig defaultLayoutPromptViewConfig;

    public DefaultLayoutPromptView(final Context context) {
        this(context, null);
    }

    public DefaultLayoutPromptView(final Context context, final @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultLayoutPromptView(
            final Context context,
            @Nullable final AttributeSet attrs,
            final int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        loadConfigFromAttributeSet(attrs);
    }

    public void applyConfig(
            @NonNull final DefaultLayoutPromptViewConfig defaultLayoutPromptViewConfig) {

        if (isDisplayed) {
            throw new IllegalStateException(
                    "Configuration cannot be changed after the prompt is first displayed.");
        }

        this.defaultLayoutPromptViewConfig = defaultLayoutPromptViewConfig;
    }

    @NonNull
    @Override
    protected <T extends View & IQuestionView> T getUserOpinionQuestionView() {
        return null;
    }

    @NonNull
    @Override
    protected <T extends View & IQuestionView> T getPositiveFeedbackQuestionView() {
        return null;
    }

    @NonNull
    @Override
    protected <T extends View & IQuestionView> T getCriticalFeedbackQuestionView() {
        return null;
    }

    @NonNull
    @Override
    protected <T extends View & IThanksView> T getThanksView() {
        return null;
    }

    private void loadConfigFromAttributeSet(final AttributeSet attrs) {
        final TypedArray typedArray = getContext().getTheme()
                .obtainStyledAttributes(attrs, R.styleable.DefaultPromptView, 0, 0);

        defaultLayoutPromptViewConfig = new DefaultLayoutPromptViewConfig(typedArray);

        typedArray.recycle();
    }

}
