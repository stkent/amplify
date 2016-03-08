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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.github.stkent.amplify.prompt.interfaces.IPromptView;
import com.github.stkent.amplify.prompt.interfaces.IQuestionView;
import com.github.stkent.amplify.prompt.interfaces.IThanksView;

public class CustomLayoutPromptView extends BasePromptView implements IPromptView {

    public CustomLayoutPromptView(final Context context) {
        this(context, null);
    }

    public CustomLayoutPromptView(final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomLayoutPromptView(
            final Context context,
            @Nullable final AttributeSet attrs,
            final int defStyleAttr) {

        super(context, attrs, defStyleAttr);
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

}
