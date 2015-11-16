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
package com.github.stkent.amplify;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DefaultQuestionView implements QuestionView {

    @NonNull
    private final View view;

    @NonNull
    private final TextView titleTextView;

    @NonNull
    private final Button positiveButton;

    @NonNull
    private final Button negativeButton;

    @SuppressWarnings("ConstantConditions")
    public DefaultQuestionView(@NonNull final View view) {
        this.view = view;

        titleTextView = (TextView) view.findViewById(R.id.amp_title_text_view);
        if (titleTextView == null) {
            throw new IllegalArgumentException("You must supply a layout that includes a TextView with id amp_title_text_view");
        }

        positiveButton = (Button) view.findViewById(R.id.amp_positive_button);
        if (positiveButton == null) {
            throw new IllegalArgumentException("You must supply a layout that includes a Button with id amp_positive_button");
        }

        negativeButton = (Button) view.findViewById(R.id.amp_negative_button);
        if (negativeButton == null) {
            throw new IllegalArgumentException("You must supply a layout that includes a Button with id amp_negative_button");
        }
    }

    @NonNull
    @Override
    public View getView() {
        return view;
    }

    @NonNull
    @Override
    public Button getPositiveButton() {
        return positiveButton;
    }

    @NonNull
    @Override
    public Button getNegativeButton() {
        return negativeButton;
    }

    @Override
    public void setQuestion(@NonNull final Question question) {
        titleTextView.setText(question.getTitle());
        positiveButton.setText(question.getPositiveButtonText());
        negativeButton.setText(question.getNegativeButtonText());
    }

}
