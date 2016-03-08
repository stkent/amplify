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

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

// formatter:off
final class QuestionViewConfig {

    @ColorInt private final int    fillColor;
    @ColorInt private final int    titleTextColor;
    @ColorInt private final int    subtitleTextColor;
    @ColorInt private final int    positiveButtonTextColor;
    @ColorInt private final int    positiveButtonBackgroundColor;
    @ColorInt private final int    positiveButtonBorderColor;
    @ColorInt private final int    negativeButtonTextColor;
    @ColorInt private final int    negativeButtonBackgroundColor;
    @ColorInt private final int    negativeButtonBorderColor;
    @NonNull  private final String title;
    @NonNull  private final String positiveButtonLabel;
    @NonNull  private final String negativeButtonLabel;
    @Nullable private final String subtitle;

    private QuestionViewConfig(
            @ColorInt final int    fillColor,
            @ColorInt final int    titleTextColor,
            @ColorInt final int    subtitleTextColor,
            @ColorInt final int    positiveButtonTextColor,
            @ColorInt final int    positiveButtonBackgroundColor,
            @ColorInt final int    positiveButtonBorderColor,
            @ColorInt final int    negativeButtonTextColor,
            @ColorInt final int    negativeButtonBackgroundColor,
            @ColorInt final int    negativeButtonBorderColor,
            @NonNull  final String title,
            @NonNull  final String positiveButtonLabel,
            @NonNull  final String negativeButtonLabel,
            @Nullable final String subtitle) {

        this.fillColor = fillColor;
        this.titleTextColor = titleTextColor;
        this.subtitleTextColor = subtitleTextColor;
        this.positiveButtonTextColor = positiveButtonTextColor;
        this.positiveButtonBackgroundColor = positiveButtonBackgroundColor;
        this.positiveButtonBorderColor = positiveButtonBorderColor;
        this.negativeButtonTextColor = negativeButtonTextColor;
        this.negativeButtonBackgroundColor = negativeButtonBackgroundColor;
        this.negativeButtonBorderColor = negativeButtonBorderColor;
        this.title = title;
        this.positiveButtonLabel = positiveButtonLabel;
        this.negativeButtonLabel = negativeButtonLabel;
        this.subtitle = subtitle;
    }

    @ColorInt
    private int getFillColor() {
        return fillColor;
    }

    @ColorInt
    private int getTitleTextColor() {
        return titleTextColor;
    }

    @ColorInt
    private int getSubtitleTextColor() {
        return subtitleTextColor;
    }

    @ColorInt
    private int getPositiveButtonTextColor() {
        return positiveButtonTextColor;
    }

    @ColorInt
    private int getPositiveButtonBackgroundColor() {
        return positiveButtonBackgroundColor;
    }

    @ColorInt
    private int getPositiveButtonBorderColor() {
        return positiveButtonBorderColor;
    }

    @ColorInt
    private int getNegativeButtonTextColor() {
        return negativeButtonTextColor;
    }

    @ColorInt
    private int getNegativeButtonBackgroundColor() {
        return negativeButtonBackgroundColor;
    }

    @ColorInt
    private int getNegativeButtonBorderColor() {
        return negativeButtonBorderColor;
    }

    @NonNull
    private String getTitle() {
        return title;
    }

    @NonNull
    private String getPositiveButtonLabel() {
        return positiveButtonLabel;
    }

    @NonNull
    private String getNegativeButtonLabel() {
        return negativeButtonLabel;
    }

    @Nullable
    private String getSubtitle() {
        return subtitle;
    }
}
// formatter:on
