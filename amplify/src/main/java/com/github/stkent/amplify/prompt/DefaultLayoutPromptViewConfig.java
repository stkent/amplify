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

import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

// @formatter:off
public final class DefaultLayoutPromptViewConfig {

    @ColorInt
    private static final int DEFAULT_FOREGROUND_COLOR = 0xFFFFFFFF;

    @ColorInt
    private static final int DEFAULT_BACKGROUND_COLOR = 0xFFFFFFFF;

    /**
     * @return <code>primaryColor</code> if it is non-null; <code>defaultColor</code> otherwise
     */
    @ColorInt
    private static int defaultIfNull(
            @Nullable final Integer primaryColor,
            @ColorInt final int defaultColor) {

        return primaryColor != null ? primaryColor : defaultColor;
    }

    @Nullable private final Integer foregroundColor;
    @Nullable private final Integer backgroundColor;
    @Nullable private final Integer titleTextColor;
    @Nullable private final Integer subtitleTextColor;
    @Nullable private final Integer positiveButtonTextColor;
    @Nullable private final Integer positiveButtonBackgroundColor;
    @Nullable private final Integer positiveButtonBorderColor;
    @Nullable private final Integer negativeButtonTextColor;
    @Nullable private final Integer negativeButtonBackgroundColor;
    @Nullable private final Integer negativeButtonBorderColor;

    public DefaultLayoutPromptViewConfig(@NonNull final TypedArray typedArray) {
        // TODO: fill this in, and document non-recycling well!
    }

    private DefaultLayoutPromptViewConfig(
            @Nullable final Integer foregroundColor,
            @Nullable final Integer backgroundColor,
            @Nullable final Integer titleTextColor,
            @Nullable final Integer subtitleTextColor,
            @Nullable final Integer positiveButtonTextColor,
            @Nullable final Integer positiveButtonBackgroundColor,
            @Nullable final Integer positiveButtonBorderColor,
            @Nullable final Integer negativeButtonTextColor,
            @Nullable final Integer negativeButtonBackgroundColor,
            @Nullable final Integer negativeButtonBorderColor) {

        this.foregroundColor               = foregroundColor;
        this.backgroundColor               = backgroundColor;
        this.titleTextColor                = titleTextColor;
        this.subtitleTextColor             = subtitleTextColor;
        this.positiveButtonTextColor       = positiveButtonTextColor;
        this.positiveButtonBackgroundColor = positiveButtonBackgroundColor;
        this.positiveButtonBorderColor     = positiveButtonBorderColor;
        this.negativeButtonTextColor       = negativeButtonTextColor;
        this.negativeButtonBackgroundColor = negativeButtonBackgroundColor;
        this.negativeButtonBorderColor     = negativeButtonBorderColor;
    }

    @ColorInt
    private int getForegroundColor() {
        return defaultIfNull(foregroundColor, DEFAULT_FOREGROUND_COLOR);
    }

    @ColorInt
    private int getBackgroundColor() {
        return defaultIfNull(backgroundColor, DEFAULT_BACKGROUND_COLOR);
    }

    @ColorInt
    private int getTitleTextColor() {
        return defaultIfNull(titleTextColor, getForegroundColor());
    }

    @ColorInt
    private int getSubtitleTextColor() {
        return defaultIfNull(subtitleTextColor, getForegroundColor());
    }

    @ColorInt
    private int getPositiveButtonTextColor() {
        return defaultIfNull(positiveButtonTextColor, getBackgroundColor());
    }

    @ColorInt
    private int getPositiveButtonBackgroundColor() {
        return defaultIfNull(positiveButtonBackgroundColor, getForegroundColor());
    }

    @ColorInt
    private int getPositiveButtonBorderColor() {
        return defaultIfNull(positiveButtonBorderColor, getForegroundColor());
    }

    @ColorInt
    private int getNegativeButtonTextColor() {
        return defaultIfNull(negativeButtonTextColor, getForegroundColor());
    }

    @ColorInt
    private int getNegativeButtonBackgroundColor() {
        return defaultIfNull(negativeButtonBackgroundColor, getBackgroundColor());
    }

    @ColorInt
    private int getNegativeButtonBorderColor() {
        return defaultIfNull(negativeButtonBorderColor, getForegroundColor());
    }

    public static final class Builder {

        @Nullable private Integer foregroundColor;
        @Nullable private Integer backgroundColor;
        @Nullable private Integer titleTextColor;
        @Nullable private Integer subtitleTextColor;
        @Nullable private Integer positiveButtonTextColor;
        @Nullable private Integer positiveButtonBackgroundColor;
        @Nullable private Integer positiveButtonBorderColor;
        @Nullable private Integer negativeButtonTextColor;
        @Nullable private Integer negativeButtonBackgroundColor;
        @Nullable private Integer negativeButtonBorderColor;

        public Builder() {
            // This constructor intentionally left blank.
        }

        public Builder setForegroundColor(@ColorInt final int foregroundColor) {
            this.foregroundColor = foregroundColor;
            return this;
        }

        public Builder setBackgroundColor(@ColorInt final int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder setTitleTextColor(@ColorInt final int titleTextColor) {
            this.titleTextColor = titleTextColor;
            return this;
        }

        public Builder setSubtitleTextColor(@ColorInt final int subtitleTextColor) {
            this.subtitleTextColor = subtitleTextColor;
            return this;
        }

        public Builder setPositiveButtonTextColor(@ColorInt final int positiveButtonTextColor) {
            this.positiveButtonTextColor = positiveButtonTextColor;
            return this;
        }

        public Builder setPositiveButtonBackgroundColor(
                @ColorInt final int positiveButtonBackgroundColor) {

            this.positiveButtonBackgroundColor = positiveButtonBackgroundColor;
            return this;
        }

        public Builder setPositiveButtonBorderColor(@ColorInt final int positiveButtonBorderColor) {
            this.positiveButtonBorderColor = positiveButtonBorderColor;
            return this;
        }

        public Builder setNegativeButtonTextColor(@ColorInt final int negativeButtonTextColor) {
            this.negativeButtonTextColor = negativeButtonTextColor;
            return this;
        }

        public Builder setNegativeButtonBackgroundColor(
                @ColorInt final int negativeButtonBackgroundColor) {

            this.negativeButtonBackgroundColor = negativeButtonBackgroundColor;
            return this;
        }

        public Builder setNegativeButtonBorderColor(@ColorInt final int negativeButtonBorderColor) {
            this.negativeButtonBorderColor = negativeButtonBorderColor;
            return this;
        }

        public DefaultLayoutPromptViewConfig build() {
            return new DefaultLayoutPromptViewConfig(
                    foregroundColor,
                    backgroundColor,
                    titleTextColor,
                    subtitleTextColor,
                    positiveButtonTextColor,
                    positiveButtonBackgroundColor,
                    positiveButtonBorderColor,
                    negativeButtonTextColor,
                    negativeButtonBackgroundColor,
                    negativeButtonBorderColor);
        }

    }

}
// @formatter:on
