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
            throw new IllegalArgumentException("Some string here");
        }

        positiveButton = (Button) view.findViewById(R.id.amp_positive_button);
        if (positiveButton == null) {
            throw new IllegalArgumentException("Some other string here");
        }

        negativeButton = (Button) view.findViewById(R.id.amp_negative_button);
        if (negativeButton == null) {
            throw new IllegalArgumentException("Some other other string here");
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
