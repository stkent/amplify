package com.github.stkent.amplify.prompt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.stkent.amplify.R;
import com.github.stkent.amplify.prompt.interfaces.IQuestion;
import com.github.stkent.amplify.prompt.interfaces.IQuestionPresenter;
import com.github.stkent.amplify.prompt.interfaces.IQuestionView;

@SuppressLint("ViewConstructor")
class DefaultQuestionView extends FrameLayout implements IQuestionView {

    private final TextView titleTextView;
    private final TextView subtitleTextView;
    private final Button positiveButton;
    private final Button negativeButton;

    private IQuestionPresenter questionPresenter;
    private DefaultLayoutPromptViewConfig config;

    public DefaultQuestionView(
            final Context context,
            @NonNull final DefaultLayoutPromptViewConfig config) {

        super(context);

        // inflate default xml and apply configuration here
        titleTextView = (TextView) findViewById(R.id.amplify_title_text_view);
        subtitleTextView = (TextView) findViewById(R.id.amplify_subtitle_text_view);
        positiveButton = (Button) findViewById(R.id.amplify_positive_button);
        negativeButton = (Button) findViewById(R.id.amplify_negative_button);

        setBackgroundColor(config.getFillColor());

        titleTextView.setTextColor(config.getTitleTextColor());
        subtitleTextView.setTextColor(config.getSubtitleTextColor());
        positiveButton.setTextColor(config.getPositiveButtonTextColor());
        negativeButton.setTextColor(config.getNegativeButtonBackgroundColor());

        // todo: build appropriate drawables for button fill/border, with pressed states
    }

    @Override
    public void setPresenter(@NonNull final IQuestionPresenter questionPresenter) {
        this.questionPresenter = questionPresenter;
    }

    @Override
    public void bind(@NonNull final IQuestion question) {
        titleTextView.setText(question.getTitle());
        positiveButton.setText(question.getPositiveButtonLabel());
        negativeButton.setText(question.getNegativeButtonLabel());

        final String subtitle = question.getSubTitle();

        if (subtitle != null) {
            subtitleTextView.setText(subtitle);
            subtitleTextView.setVisibility(VISIBLE);
        } else {
            subtitleTextView.setVisibility(GONE);
        }
    }

}
