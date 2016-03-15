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

    protected final TextView titleTextView;
    protected final TextView subtitleTextView;
    protected final View positiveButton;
    protected final View negativeButton;

    private IQuestionPresenter questionPresenter;

    public CustomLayoutQuestionView(
            final Context context,
            @LayoutRes final int layoutRes) {

        super(context);
        LayoutInflater.from(context).inflate(layoutRes, this, true);

        titleTextView = (TextView) findViewById(R.id.amplify_title_text_view);
        subtitleTextView = (TextView) findViewById(R.id.amplify_subtitle_text_view);
        positiveButton = findViewById(R.id.amplify_positive_button);
        negativeButton = findViewById(R.id.amplify_negative_button);

        positiveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                questionPresenter.userRespondedPositively();
            }
        });

        negativeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                questionPresenter.userRespondedNegatively();
            }
        });

        // todo: build appropriate drawables for button fill/border, with pressed states
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

        if (subtitle != null) {
            subtitleTextView.setText(subtitle);
            subtitleTextView.setVisibility(VISIBLE);
        } else {
            subtitleTextView.setVisibility(GONE);
        }
    }

}
