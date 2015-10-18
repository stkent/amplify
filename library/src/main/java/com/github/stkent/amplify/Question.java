package com.github.stkent.amplify;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Question {

    private static final String DEFAULT_POSITIVE_BUTTON_TEXT = "Yes";
    private static final String DEFAULT_NEGATIVE_BUTTON_TEXT = "No";

    @NonNull
    private final String title;

    @NonNull
    private final String positiveButtonText;

    @NonNull
    private final String negativeButtonText;

    private Question(
            @NonNull final String title,
            @NonNull final String positiveButtonText,
            @NonNull final String negativeButtonText) {
        this.title = title;
        this.positiveButtonText = positiveButtonText;
        this.negativeButtonText = negativeButtonText;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getPositiveButtonText() {
        return positiveButtonText;
    }

    @NonNull
    public String getNegativeButtonText() {
        return negativeButtonText;
    }

    public static class Builder {

        public static Builder withTitle(@NonNull final String title) {
            return new Builder(title);
        }

        @NonNull
        private String title;

        @Nullable
        private String positiveButtonText;

        @Nullable
        private String negativeButtonText = DEFAULT_NEGATIVE_BUTTON_TEXT;

        private Builder(@NonNull final String title) {
            this.title = title;
        }

        @NonNull
        public Builder andPositiveButtonText(@Nullable final String positiveButtonText) {
            this.positiveButtonText = positiveButtonText;
            return this;
        }

        @NonNull
        public Builder andNegativeButtonText(@Nullable final String negativeButtonText) {
            this.negativeButtonText = negativeButtonText;
            return this;
        }

        @NonNull
        public Question build() {
            final String resolvedPositiveButtonText =
                    positiveButtonText != null
                            ? positiveButtonText
                            : DEFAULT_POSITIVE_BUTTON_TEXT;


            final String resolvedNegativeButtonText =
                    negativeButtonText != null
                            ? negativeButtonText
                            : DEFAULT_NEGATIVE_BUTTON_TEXT;

            return new Question(
                    title,
                    resolvedPositiveButtonText,
                    resolvedNegativeButtonText);
        }

    }

}
