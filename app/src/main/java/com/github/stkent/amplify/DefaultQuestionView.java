package com.github.stkent.amplify;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DefaultQuestionView implements QuestionView {

    @NonNull
    private View view;

    @NonNull
    private TextView titleTextView;

    @NonNull
    private Button positiveButton;

    @NonNull
    private Button negativeButton;

    public DefaultQuestionView(@NonNull final View view) {
        this.view = view;

        final TextView titleTextView = (TextView) view.findViewById(R.id.amplify_title_text_view);
        if (titleTextView == null) {
            throw new IllegalArgumentException("Some string here");
        }

        final Button positiveButton = (Button) view.findViewById(R.id.amplify_positive_button);
        if (positiveButton == null) {
            throw new IllegalArgumentException("Some other string here");
        }

        final Button negativeButton = (Button) view.findViewById(R.id.amplify_negative_button);
        if (negativeButton == null) {
            throw new IllegalArgumentException("Some other other string here");
        }

        this.titleTextView = titleTextView;
        this.positiveButton = positiveButton;
        this.negativeButton = negativeButton;
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
