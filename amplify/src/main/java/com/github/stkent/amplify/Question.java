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
package com.github.stkent.amplify;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public final class Question {

    private static final String DEFAULT_POSITIVE_BUTTON_TEXT = "Yes";
    private static final String DEFAULT_NEGATIVE_BUTTON_TEXT = "No";

    @NonNull
    private final String title;

    @NonNull
    private final String positiveButtonText;

    @NonNull
    private final String negativeButtonText;

    protected Question(
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

    public static final class Builder {

        @NonNull
        private final String title;

        @Nullable
        private String positiveButtonText;

        @Nullable
        private String negativeButtonText = DEFAULT_NEGATIVE_BUTTON_TEXT;

        public static Builder withTitle(@NonNull final String title) {
            return new Builder(title);
        }

        private Builder(@NonNull final String title) {
            this.title = title;
        }

        @NonNull
        public Builder andPositiveButtonText(@Nullable final String newPositiveButtonText) {
            positiveButtonText = newPositiveButtonText;
            return this;
        }

        @NonNull
        public Builder andNegativeButtonText(@Nullable final String newNegativeButtonText) {
            negativeButtonText = newNegativeButtonText;
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
