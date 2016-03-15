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
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.stkent.amplify.R;
import com.github.stkent.amplify.prompt.interfaces.IThanks;
import com.github.stkent.amplify.prompt.interfaces.IThanksView;

@SuppressLint("ViewConstructor")
class CustomLayoutThanksView extends FrameLayout implements IThanksView {

    private final TextView titleTextView;
    private final TextView subtitleTextView;

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

    protected TextView getTitleTextView() {
        return titleTextView;
    }

    protected TextView getSubtitleTextView() {
        return subtitleTextView;
    }

}
