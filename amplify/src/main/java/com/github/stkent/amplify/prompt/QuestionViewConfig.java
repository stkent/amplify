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
