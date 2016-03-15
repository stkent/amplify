package com.github.stkent.amplify.prompt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.R;

@SuppressLint("ViewConstructor")
public final class DefaultLayoutThanksView extends CustomLayoutThanksView {

    public DefaultLayoutThanksView(
            final Context context,
            @NonNull final DefaultLayoutPromptViewConfig config) {

        super(context, R.layout.default_thanks_view);

        setBackgroundColor(config.getFillColor());

        titleTextView.setTextColor(config.getTitleTextColor());
        subtitleTextView.setTextColor(config.getSubtitleTextColor());
    }

}
