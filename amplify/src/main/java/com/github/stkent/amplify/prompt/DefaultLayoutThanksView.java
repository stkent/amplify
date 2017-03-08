/*
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
import android.support.annotation.NonNull;

import com.github.stkent.amplify.R;

@SuppressLint("ViewConstructor")
public final class DefaultLayoutThanksView extends CustomLayoutThanksView {

    public DefaultLayoutThanksView(
            final Context context,
            @NonNull final DefaultLayoutPromptViewConfig config) {

        super(context, R.layout.default_thanks_view);

        setBackgroundColor(config.getFillColor());

        getTitleTextView().setTextColor(config.getTitleTextColor());

        if (getSubtitleTextView() != null) {
            getSubtitleTextView().setTextColor(config.getSubtitleTextColor());
        }
    }

}
