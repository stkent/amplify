/*
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
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.stkent.amplify.R;
import com.github.stkent.amplify.prompt.interfaces.IQuestion;
import com.github.stkent.amplify.prompt.interfaces.IQuestionPresenter;
import com.github.stkent.amplify.prompt.interfaces.IQuestionView;
import com.github.stkent.amplify.utils.Constants;

@SuppressLint("ViewConstructor")
class CustomLayoutQuestionView extends FrameLayout implements IQuestionView {

    private static final String QUESTION_PRESENTER_MUST_BE_SET_EXCEPTION_MESSAGE
            = "Question presenter must be set before buttons can be clicked.";

    @NonNull
    private final TextView titleTextView;

    @Nullable
    private final TextView subtitleTextView;

    @NonNull
    private final View positiveButton;

    @NonNull
    private final View negativeButton;

    private IQuestionPresenter questionPresenter;

    /* default */ CustomLayoutQuestionView(
            final Context context,
            @LayoutRes final int layoutRes) {

        super(context);
        LayoutInflater.from(context).inflate(layoutRes, this, true);

        final TextView titleTextView = (TextView) findViewById(R.id.amplify_title_text_view);
        final View positiveButton = findViewById(R.id.amplify_positive_button);
        final View negativeButton = findViewById(R.id.amplify_negative_button);

        if (titleTextView == null || positiveButton == null || negativeButton == null) {
            throw new IllegalStateException(Constants.MISSING_LAYOUT_IDS_EXCEPTION_MESSAGE);
        }

        this.titleTextView = titleTextView;
        this.subtitleTextView = (TextView) findViewById(R.id.amplify_subtitle_text_view);
        this.positiveButton = positiveButton;
        this.negativeButton = negativeButton;

        positiveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (questionPresenter == null) {
                    throw new IllegalStateException(
                            QUESTION_PRESENTER_MUST_BE_SET_EXCEPTION_MESSAGE);
                }

                questionPresenter.userRespondedPositively();
            }
        });

        negativeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (questionPresenter == null) {
                    throw new IllegalStateException(
                            QUESTION_PRESENTER_MUST_BE_SET_EXCEPTION_MESSAGE);
                }

                questionPresenter.userRespondedNegatively();
            }
        });
    }

    @Override
    public void setPresenter(@NonNull final IQuestionPresenter questionPresenter) {
        this.questionPresenter = questionPresenter;
    }

    @Override
    public void bind(@NonNull final IQuestion question) {
        titleTextView.setText(question.getTitle());

        setQuoteButtonUnquoteText(positiveButton, question.getPositiveButtonLabel());
        setQuoteButtonUnquoteText(negativeButton, question.getNegativeButtonLabel());

        final String subtitle = question.getSubTitle();

        if (subtitleTextView != null) {
            if (subtitle != null) {
                subtitleTextView.setText(subtitle);
                subtitleTextView.setVisibility(VISIBLE);
            } else {
                subtitleTextView.setVisibility(GONE);
            }
        }
    }

    @NonNull
    protected TextView getTitleTextView() {
        return titleTextView;
    }

    @Nullable
    protected TextView getSubtitleTextView() {
        return subtitleTextView;
    }

    @NonNull
    protected View getPositiveButton() {
        return positiveButton;
    }

    @NonNull
    protected View getNegativeButton() {
        return negativeButton;
    }

    /**
     * We are defensive here, because it's not uncommon to make "buttons" out of UI components like
     * FrameLayouts, say. If we can't cast to a TextView to obtain a setText method, the button text
     * will be left unchanged.
     *
     * @param quoteButtonUnquote the "button" whose text we wish to set
     * @param text the text we wish to apply
     */
    private void setQuoteButtonUnquoteText(
            @NonNull final View quoteButtonUnquote,
            @NonNull final String text) {

        if (quoteButtonUnquote instanceof TextView) {
            ((TextView) quoteButtonUnquote).setText(text);
        }
    }

}
