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
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleableRes;

import com.github.stkent.amplify.R;
import com.github.stkent.amplify.utils.Constants;

public final class CustomLayoutPromptViewConfig implements Parcelable {

    private static final int DEFAULT_LAYOUT_RES_ID_IF_UNDEFINED = Integer.MAX_VALUE;

    /**
     * @return the color value for the attribute at <code>index</code>, if defined; null otherwise
     */
    @Nullable
    private static Integer suppliedLayoutOrNull(@NonNull final TypedArray typedArray, @StyleableRes final int index) {
        final int layoutResourceId = typedArray.getResourceId(index, DEFAULT_LAYOUT_RES_ID_IF_UNDEFINED);
        return layoutResourceId != DEFAULT_LAYOUT_RES_ID_IF_UNDEFINED ? layoutResourceId : null;
    }

    @Nullable
    private final Integer questionLayout;

    @Nullable
    private final Integer thanksLayout;

    public CustomLayoutPromptViewConfig(@NonNull final TypedArray typedArray) {
        this.questionLayout =
                suppliedLayoutOrNull(typedArray, R.styleable.CustomLayoutPromptView_prompt_view_question_layout);

        this.thanksLayout =
                suppliedLayoutOrNull(typedArray, R.styleable.CustomLayoutPromptView_prompt_view_thanks_layout);
    }

    public CustomLayoutPromptViewConfig(
            @LayoutRes final int questionLayout,
            @Nullable @LayoutRes final Integer thanksLayout) {

        this.questionLayout = questionLayout;
        this.thanksLayout = thanksLayout;
    }

    public boolean isValid() {
        return questionLayout != null;
    }

    @LayoutRes
    public int getQuestionLayout() {
        if (questionLayout == null) {
            throw new IllegalStateException(Constants.MISSING_LAYOUT_IDS_EXCEPTION_MESSAGE);
        }

        return questionLayout;
    }

    @Nullable @LayoutRes
    public Integer getThanksLayout() {
        return thanksLayout;
    }

    // Parcelable

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.questionLayout);
        dest.writeValue(this.thanksLayout);
    }

    @SuppressLint("ParcelClassLoader")
    protected CustomLayoutPromptViewConfig(Parcel in) {
        this.questionLayout = (Integer) in.readValue(null);
        this.thanksLayout = (Integer) in.readValue(null);
    }

    public static final Parcelable.Creator<CustomLayoutPromptViewConfig> CREATOR
            = new Parcelable.Creator<CustomLayoutPromptViewConfig>() {

        @Override
        public CustomLayoutPromptViewConfig createFromParcel(final Parcel in) {
            return new CustomLayoutPromptViewConfig(in);
        }

        @Override
        public CustomLayoutPromptViewConfig[] newArray(final int size) {
            return new CustomLayoutPromptViewConfig[size];
        }

    };

}
