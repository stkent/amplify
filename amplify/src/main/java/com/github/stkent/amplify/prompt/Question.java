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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.stkent.amplify.prompt.interfaces.IQuestion;

final class Question implements IQuestion {

    @NonNull
    private final String title;

    @Nullable
    private final String subTitle;

    @NonNull
    private final String positiveButtonLabel;

    @NonNull
    private final String negativeButtonLabel;

    /* default */ Question(
            @NonNull  final String title,
            @Nullable final String subTitle,
            @NonNull  final String positiveButtonLabel,
            @NonNull  final String negativeButtonLabel) {

        this.title = title;
        this.subTitle = subTitle;
        this.positiveButtonLabel = positiveButtonLabel;
        this.negativeButtonLabel = negativeButtonLabel;
    }

    @NonNull
    @Override
    public String getTitle() {
        return title;
    }

    @Nullable
    @Override
    public String getSubTitle() {
        return subTitle;
    }

    @NonNull
    @Override
    public String getPositiveButtonLabel() {
        return positiveButtonLabel;
    }

    @NonNull
    @Override
    public String getNegativeButtonLabel() {
        return negativeButtonLabel;
    }

}
