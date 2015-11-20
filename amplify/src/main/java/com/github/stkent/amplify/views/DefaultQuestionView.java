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
package com.github.stkent.amplify.views;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.github.stkent.amplify.R;
import com.github.stkent.amplify.utils.StringUtils;

public class DefaultQuestionView implements QuestionView {

    @NonNull
    private final View view;

    @NonNull
    private final TextView questionTextView;

    @Nullable
    private final TextView titleTextView;

    @NonNull
    private final View positiveButton;

    @NonNull
    private final View negativeButton;

    @SuppressWarnings("ConstantConditions")
    public DefaultQuestionView(@NonNull final View view) {
        this.view = view;

        titleTextView = (TextView) view.findViewById(R.id.amp_title_text_view);
        questionTextView = (TextView) view.findViewById(R.id.amp_question_text_view);
        if (questionTextView == null) {
            throw new IllegalArgumentException("You must supply a layout that includes a TextView with id amp_question_text_view");
        }

        positiveButton = view.findViewById(R.id.amp_positive_button);
        if (positiveButton == null) {
            throw new IllegalArgumentException("You must supply a layout that includes a View with id amp_positive_button");
        }

        negativeButton = view.findViewById(R.id.amp_negative_button);
        if (negativeButton == null) {
            throw new IllegalArgumentException("You must supply a layout that includes a View with id amp_negative_button");
        }
    }

    @NonNull
    @Override
    public View getView() {
        return view;
    }

    @NonNull
    @Override
    public View getPositiveButton() {
        return positiveButton;
    }

    @NonNull
    @Override
    public View getNegativeButton() {
        return negativeButton;
    }

    @Override
    public void setQuestion(@NonNull final String question) {
        questionTextView.setText(question);
    }

    @Override
    public void setTitle(@Nullable String title) {
        if (titleTextView != null) {
            if (StringUtils.isBlank(title)) {
                titleTextView.setVisibility(View.GONE);
            } else {
                titleTextView.setText(title);
            }
        }
    }

}
