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

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.github.stkent.amplify.R;

@SuppressLint("ViewConstructor")
final class DefaultLayoutQuestionView extends CustomLayoutQuestionView {

    DefaultLayoutQuestionView(
            final Context context,
            @NonNull final DefaultLayoutPromptViewConfig config) {

        super(context, R.layout.default_question_view);

        setBackgroundColor(config.getFillColor());

        getTitleTextView().setTextColor(config.getTitleTextColor());

        if (getSubtitleTextView() != null) {
            getSubtitleTextView().setTextColor(config.getSubtitleTextColor());
        }


        setQuoteButtonUnquoteTextColor(getPositiveButton(), config.getPositiveButtonTextColor());
        setQuoteButtonUnquoteTextColor(getNegativeButton(), config.getNegativeButtonTextColor());

        // todo: instead, build appropriate drawables for button fill/border, with pressed states

        getPositiveButton().setBackgroundColor(config.getPositiveButtonBackgroundColor());
        getNegativeButton().setBackgroundColor(config.getNegativeButtonBackgroundColor());
    }

    /**
     * We are defensive here, because it's not uncommon to make "buttons" out of UI components like
     * FrameLayouts, say. If we can't cast to a TextView to obtain a setText method, the button text
     * will be left unchanged from the original layout.
     *
     * @param quoteButtonUnquote the "button" whose text color we wish to set
     * @param color the color we wish to apply
     */
    private void setQuoteButtonUnquoteTextColor(
            @NonNull final View quoteButtonUnquote,
            @ColorInt final int color) {

        if (quoteButtonUnquote instanceof TextView) {
            ((TextView) quoteButtonUnquote).setTextColor(color);
        }
    }

}
