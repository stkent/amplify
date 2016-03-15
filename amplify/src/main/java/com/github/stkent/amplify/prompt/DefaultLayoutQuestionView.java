package com.github.stkent.amplify.prompt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.github.stkent.amplify.R;

@SuppressLint("ViewConstructor")
final class DefaultLayoutQuestionView extends CustomLayoutQuestionView {

    public DefaultLayoutQuestionView(
            final Context context,
            @NonNull final DefaultLayoutPromptViewConfig config) {

        super(context, R.layout.default_question_view);

        setBackgroundColor(config.getFillColor());

        titleTextView.setTextColor(config.getTitleTextColor());
        subtitleTextView.setTextColor(config.getSubtitleTextColor());

        if (positiveButton instanceof TextView) {
            ((TextView) positiveButton).setTextColor(config.getPositiveButtonTextColor());
        }

        if (negativeButton instanceof TextView) {
            ((TextView) negativeButton).setTextColor(config.getNegativeButtonTextColor());
        }

        positiveButton.setBackgroundColor(config.getPositiveButtonBackgroundColor());
        negativeButton.setBackgroundColor(config.getNegativeButtonBackgroundColor());
    }

}
