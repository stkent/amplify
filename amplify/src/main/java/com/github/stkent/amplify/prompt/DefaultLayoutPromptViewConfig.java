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
import android.support.annotation.Nullable;
import android.support.annotation.StyleableRes;

import com.github.stkent.amplify.R;

//@formatter:off
@SuppressWarnings({"PMD.ExcessiveParameterList", "checkstyle:parameternumber"})
public final class DefaultLayoutPromptViewConfig {

    @ColorInt
    private static final int DEFAULT_FOREGROUND_COLOR = 0xFFFFFFFF;

    @ColorInt
    private static final int DEFAULT_BACKGROUND_COLOR = 0xFF3C5A96;

    private static final int DEFAULT_GET_COLOR_VALUE_IF_UNDEFINED = Integer.MAX_VALUE;

    /**
     * @return <code>primaryColor</code> if it is non-null; <code>defaultColor</code> otherwise
     */
    @ColorInt
    private static int defaultIfNull(
            @Nullable final Integer primaryColor,
            @ColorInt final int defaultColor) {

        return primaryColor != null ? primaryColor : defaultColor;
    }

    /**
     * @return the color value for the attribute at <code>index</code>, if defined; null otherwise
     */
    private static Integer suppliedColorOrNull(
            @Nullable final TypedArray typedArray,
            @StyleableRes final int index) {

        if (typedArray != null) {
            final int color = typedArray.getColor(index, DEFAULT_GET_COLOR_VALUE_IF_UNDEFINED);
            return color != DEFAULT_GET_COLOR_VALUE_IF_UNDEFINED ? color : null;
        }

        return null;
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

    public DefaultLayoutPromptViewConfig(@Nullable final TypedArray typedArray) {
        foregroundColor = suppliedColorOrNull(
                typedArray,
                R.styleable.DefaultLayoutPromptView_prompt_view_foreground_color);

        backgroundColor = suppliedColorOrNull(
                typedArray,
                R.styleable.DefaultLayoutPromptView_prompt_view_background_color);

        titleTextColor = suppliedColorOrNull(
                typedArray,
                R.styleable.DefaultLayoutPromptView_prompt_view_title_text_color);

        subtitleTextColor = suppliedColorOrNull(
                typedArray,
                R.styleable.DefaultLayoutPromptView_prompt_view_subtitle_text_color);

        positiveButtonTextColor = suppliedColorOrNull(
                typedArray,
                R.styleable.DefaultLayoutPromptView_prompt_view_positive_button_text_color);

        positiveButtonBackgroundColor = suppliedColorOrNull(
                typedArray,
                R.styleable.DefaultLayoutPromptView_prompt_view_positive_button_background_color);

        positiveButtonBorderColor = suppliedColorOrNull(
                typedArray,
                R.styleable.DefaultLayoutPromptView_prompt_view_positive_button_border_color);

        negativeButtonTextColor = suppliedColorOrNull(
                typedArray,
                R.styleable.DefaultLayoutPromptView_prompt_view_negative_button_text_color);

        negativeButtonBackgroundColor = suppliedColorOrNull(
                typedArray,
                R.styleable.DefaultLayoutPromptView_prompt_view_negative_button_background_color);

        negativeButtonBorderColor = suppliedColorOrNull(
                typedArray,
                R.styleable.DefaultLayoutPromptView_prompt_view_negative_button_border_color);
    }

    protected DefaultLayoutPromptViewConfig(
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
    public int getFillColor() {
        return getBackgroundColor();
    }

    @ColorInt
    public int getTitleTextColor() {
        return defaultIfNull(titleTextColor, getForegroundColor());
    }

    @ColorInt
    public int getSubtitleTextColor() {
        return defaultIfNull(subtitleTextColor, getForegroundColor());
    }

    @ColorInt
    public int getPositiveButtonTextColor() {
        return defaultIfNull(positiveButtonTextColor, getBackgroundColor());
    }

    @ColorInt
    public int getPositiveButtonBackgroundColor() {
        return defaultIfNull(positiveButtonBackgroundColor, getForegroundColor());
    }

    @ColorInt
    public int getPositiveButtonBorderColor() {
        return defaultIfNull(positiveButtonBorderColor, getForegroundColor());
    }

    @ColorInt
    public int getNegativeButtonTextColor() {
        return defaultIfNull(negativeButtonTextColor, getForegroundColor());
    }

    @ColorInt
    public int getNegativeButtonBackgroundColor() {
        return defaultIfNull(negativeButtonBackgroundColor, getBackgroundColor());
    }

    @ColorInt
    public int getNegativeButtonBorderColor() {
        return defaultIfNull(negativeButtonBorderColor, getForegroundColor());
    }

    @ColorInt
    private int getForegroundColor() {
        return defaultIfNull(foregroundColor, DEFAULT_FOREGROUND_COLOR);
    }

    @ColorInt
    private int getBackgroundColor() {
        return defaultIfNull(backgroundColor, DEFAULT_BACKGROUND_COLOR);
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
//@formatter:on
