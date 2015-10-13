package com.github.stkent.amplify;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Question {

    @NonNull
    private final String title;

    @NonNull
    private final String positiveButtonText;

    @NonNull
    private final String negativeButtonText;

    public Question(
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

        @NonNull final Question baseQuestion;

        @Nullable
        private String title;

        @Nullable
        private String positiveButtonText;

        @Nullable
        private String negativeButtonText;

        public Builder(@NonNull final Question baseQuestion) {
            this.baseQuestion = baseQuestion;
        }

        @NonNull
        public Builder setTitle(@Nullable final String title) {
            this.title = title;
            return this;
        }

        @NonNull
        public Builder setPositiveButtonText(@Nullable final String positiveButtonText) {
            this.positiveButtonText = positiveButtonText;
            return this;
        }

        @NonNull
        public Builder setNegativeButtonText(@Nullable final String negativeButtonText) {
            this.negativeButtonText = negativeButtonText;
            return this;
        }

        @NonNull
        public Question build() {
            final String newTitle = title != null ? title : baseQuestion.title;
            final String newPositiveButtonText = positiveButtonText != null
                    ? positiveButtonText : baseQuestion.positiveButtonText;
            final String newNegativeButtonText = negativeButtonText != null
                    ? negativeButtonText : baseQuestion.negativeButtonText;

            return new Question(newTitle, newPositiveButtonText, newNegativeButtonText);
        }

    }

}
