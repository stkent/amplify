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

    CustomLayoutQuestionView(
            final Context context,
            @LayoutRes final int layoutRes) {

        super(context);
        LayoutInflater.from(context).inflate(layoutRes, this, true);

        final TextView titleTextView = (TextView) findViewById(R.id.amplify_title_text_view);
        final View positiveButton = findViewById(R.id.amplify_positive_button);
        final View negativeButton = findViewById(R.id.amplify_negative_button);

        if (titleTextView == null || positiveButton == null || negativeButton == null) {
            throw new IllegalStateException(
                    "Provided layout does not include views with required ids.");
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

        if (positiveButton instanceof TextView) {
            ((TextView) positiveButton).setText(question.getPositiveButtonLabel());
        }

        if (negativeButton instanceof TextView) {
            ((TextView) negativeButton).setText(question.getNegativeButtonLabel());
        }

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
}
