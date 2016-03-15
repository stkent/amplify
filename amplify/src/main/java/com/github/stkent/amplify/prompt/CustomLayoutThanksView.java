package com.github.stkent.amplify.prompt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.stkent.amplify.R;
import com.github.stkent.amplify.prompt.interfaces.IThanks;
import com.github.stkent.amplify.prompt.interfaces.IThanksView;

@SuppressLint("ViewConstructor")
public class CustomLayoutThanksView extends FrameLayout implements IThanksView {

    protected final TextView titleTextView;
    protected final TextView subtitleTextView;

    public CustomLayoutThanksView(
            final Context context,
            @LayoutRes final int layoutRes) {

        super(context);
        LayoutInflater.from(context).inflate(layoutRes, this, true);

        titleTextView = (TextView) findViewById(R.id.amplify_title_text_view);
        subtitleTextView = (TextView) findViewById(R.id.amplify_subtitle_text_view);
    }

    @Override
    public void bind(@NonNull final IThanks thanks) {
        titleTextView.setText(thanks.getTitle());

        final String subtitle = thanks.getSubTitle();

        if (subtitle != null) {
            subtitleTextView.setText(subtitle);
            subtitleTextView.setVisibility(VISIBLE);
        } else {
            subtitleTextView.setVisibility(GONE);
        }
    }
}
