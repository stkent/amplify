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
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleableRes;

import com.github.stkent.amplify.R;
import com.github.stkent.amplify.prompt.interfaces.IQuestion;
import com.github.stkent.amplify.prompt.interfaces.IThanks;

import static com.github.stkent.amplify.utils.StringUtils.defaultIfBlank;

//@formatter:off
@SuppressWarnings({"PMD.ExcessiveParameterList", "checkstyle:parameternumber"})
public final class BasePromptViewConfig implements Parcelable {

    private static final String DEFAULT_USER_OPINION_QUESTION_TITLE                 = "Enjoying the app?";
    private static final String DEFAULT_POSITIVE_FEEDBACK_QUESTION_TITLE            = "Awesome! We'd love a Play Store review...";
    private static final String DEFAULT_CRITICAL_FEEDBACK_QUESTION_TITLE            = "Oh no! Would you like to send feedback?";
    private static final String DEFAULT_USER_OPINION_QUESTION_POSITIVE_BUTTON_LABEL = "Yes!";
    private static final String DEFAULT_USER_OPINION_QUESTION_NEGATIVE_BUTTON_LABEL = "No";
    private static final String DEFAULT_FEEDBACK_QUESTION_POSITIVE_BUTTON_LABEL     = "Sure thing!";
    private static final String DEFAULT_FEEDBACK_QUESTION_NEGATIVE_BUTTON_LABEL     = "Not right now";
    private static final String DEFAULT_THANKS_TITLE                                = "Thanks for your feedback!";

    private static final int DEFAULT_INTEGER_VALUE_IF_UNDEFINED = Integer.MAX_VALUE;

    /**
     * @return the long value for the attribute at <code>index</code>, if defined; null otherwise
     */
    @Nullable
    private static Long suppliedLongOrNull(
            @Nullable final TypedArray typedArray,
            @StyleableRes final int index) {

        if (typedArray != null) {
            final int integer = typedArray.getInt(index, DEFAULT_INTEGER_VALUE_IF_UNDEFINED);

            return integer != DEFAULT_INTEGER_VALUE_IF_UNDEFINED ? (long) integer : null;
        }

        return null;
    }

    @Nullable private final String userOpinionQuestionTitle;
    @Nullable private final String userOpinionQuestionSubtitle;
    @Nullable private final String userOpinionQuestionPositiveButtonLabel;
    @Nullable private final String userOpinionQuestionNegativeButtonLabel;
    @Nullable private final String positiveFeedbackQuestionTitle;
    @Nullable private final String positiveFeedbackQuestionSubtitle;
    @Nullable private final String positiveFeedbackQuestionPositiveButtonLabel;
    @Nullable private final String positiveFeedbackQuestionNegativeButtonLabel;
    @Nullable private final String criticalFeedbackQuestionTitle;
    @Nullable private final String criticalFeedbackQuestionSubtitle;
    @Nullable private final String criticalFeedbackQuestionPositiveButtonLabel;
    @Nullable private final String criticalFeedbackQuestionNegativeButtonLabel;
    @Nullable private final String thanksTitle;
    @Nullable private final String thanksSubtitle;
    @Nullable private final Long   thanksDisplayTimeMs;

    public BasePromptViewConfig(@NonNull final TypedArray typedArray) {
        userOpinionQuestionTitle = typedArray.getString(
                R.styleable.BasePromptView_prompt_view_user_opinion_question_title);

        userOpinionQuestionSubtitle = typedArray.getString(
                R.styleable.BasePromptView_prompt_view_user_opinion_question_subtitle);

        userOpinionQuestionPositiveButtonLabel = typedArray.getString(
                R.styleable.BasePromptView_prompt_view_user_opinion_question_positive_button_label);

        userOpinionQuestionNegativeButtonLabel = typedArray.getString(
                R.styleable.BasePromptView_prompt_view_user_opinion_question_negative_button_label);

        positiveFeedbackQuestionTitle = typedArray.getString(
                R.styleable.BasePromptView_prompt_view_positive_feedback_question_title);

        positiveFeedbackQuestionSubtitle = typedArray.getString(
                R.styleable.BasePromptView_prompt_view_positive_feedback_question_subtitle);

        positiveFeedbackQuestionPositiveButtonLabel = typedArray.getString(
                R.styleable.BasePromptView_prompt_view_positive_feedback_question_positive_button_label);

        positiveFeedbackQuestionNegativeButtonLabel = typedArray.getString(
                R.styleable.BasePromptView_prompt_view_positive_feedback_question_negative_button_label);

        criticalFeedbackQuestionTitle = typedArray.getString(
                R.styleable.BasePromptView_prompt_view_critical_feedback_question_title);

        criticalFeedbackQuestionSubtitle = typedArray.getString(
                R.styleable.BasePromptView_prompt_view_critical_feedback_question_subtitle);

        criticalFeedbackQuestionPositiveButtonLabel = typedArray.getString(
                R.styleable.BasePromptView_prompt_view_critical_feedback_question_positive_button_label);

        criticalFeedbackQuestionNegativeButtonLabel = typedArray.getString(
                R.styleable.BasePromptView_prompt_view_critical_feedback_question_negative_button_label);

        thanksTitle = typedArray.getString(R.styleable.BasePromptView_prompt_view_thanks_title);

        thanksSubtitle = typedArray.getString(
                R.styleable.BasePromptView_prompt_view_thanks_subtitle);

        thanksDisplayTimeMs = suppliedLongOrNull(
                typedArray,
                R.styleable.BasePromptView_prompt_view_thanks_display_time_ms);
    }

    protected BasePromptViewConfig(
            @Nullable final String userOpinionQuestionTitle,
            @Nullable final String userOpinionQuestionSubtitle,
            @Nullable final String userOpinionQuestionPositiveButtonLabel,
            @Nullable final String userOpinionQuestionNegativeButtonLabel,
            @Nullable final String positiveFeedbackQuestionTitle,
            @Nullable final String positiveFeedbackQuestionSubtitle,
            @Nullable final String positiveFeedbackQuestionPositiveButtonLabel,
            @Nullable final String positiveFeedbackQuestionNegativeButtonLabel,
            @Nullable final String criticalFeedbackQuestionTitle,
            @Nullable final String criticalFeedbackQuestionSubtitle,
            @Nullable final String criticalFeedbackQuestionPositiveButtonLabel,
            @Nullable final String criticalFeedbackQuestionNegativeButtonLabel,
            @Nullable final String thanksTitle,
            @Nullable final String thanksSubtitle,
            @Nullable final Long thanksDisplayTimeMs) {

        this.userOpinionQuestionTitle                    = userOpinionQuestionTitle;
        this.userOpinionQuestionSubtitle                 = userOpinionQuestionSubtitle;
        this.userOpinionQuestionPositiveButtonLabel      = userOpinionQuestionPositiveButtonLabel;
        this.userOpinionQuestionNegativeButtonLabel      = userOpinionQuestionNegativeButtonLabel;
        this.positiveFeedbackQuestionTitle               = positiveFeedbackQuestionTitle;
        this.positiveFeedbackQuestionSubtitle            = positiveFeedbackQuestionSubtitle;
        this.positiveFeedbackQuestionPositiveButtonLabel = positiveFeedbackQuestionPositiveButtonLabel;
        this.positiveFeedbackQuestionNegativeButtonLabel = positiveFeedbackQuestionNegativeButtonLabel;
        this.criticalFeedbackQuestionTitle               = criticalFeedbackQuestionTitle;
        this.criticalFeedbackQuestionSubtitle            = criticalFeedbackQuestionSubtitle;
        this.criticalFeedbackQuestionPositiveButtonLabel = criticalFeedbackQuestionPositiveButtonLabel;
        this.criticalFeedbackQuestionNegativeButtonLabel = criticalFeedbackQuestionNegativeButtonLabel;
        this.thanksTitle                                 = thanksTitle;
        this.thanksSubtitle                              = thanksSubtitle;
        this.thanksDisplayTimeMs                         = thanksDisplayTimeMs;
    }

    @NonNull
    public IQuestion getUserOpinionQuestion() {
        return new Question(
                getUserOpinionQuestionTitle(),
                userOpinionQuestionSubtitle,
                getUserOpinionQuestionPositiveButtonLabel(),
                getUserOpinionQuestionNegativeButtonLabel());
    }

    @NonNull
    public IQuestion getPositiveFeedbackQuestion() {
        return new Question(
                getPositiveFeedbackQuestionTitle(),
                positiveFeedbackQuestionSubtitle,
                getPositiveFeedbackQuestionPositiveButtonLabel(),
                getPositiveFeedbackQuestionNegativeButtonLabel());
    }

    @NonNull
    public IQuestion getCriticalFeedbackQuestion() {
        return new Question(
                getCriticalFeedbackQuestionTitle(),
                criticalFeedbackQuestionSubtitle,
                getCriticalFeedbackQuestionPositiveButtonLabel(),
                getCriticalFeedbackQuestionNegativeButtonLabel());
    }

    @NonNull
    public IThanks getThanks() {
        return new Thanks(getThanksTitle(), thanksSubtitle);
    }

    @Nullable
    public Long getThanksDisplayTimeMs() {
        return thanksDisplayTimeMs;
    }

    @NonNull
    private String getUserOpinionQuestionTitle() {
        return defaultIfBlank(
                userOpinionQuestionTitle, DEFAULT_USER_OPINION_QUESTION_TITLE);
    }

    @NonNull
    private String getUserOpinionQuestionPositiveButtonLabel() {
        return defaultIfBlank(
                userOpinionQuestionPositiveButtonLabel,
                DEFAULT_USER_OPINION_QUESTION_POSITIVE_BUTTON_LABEL);
    }

    @NonNull
    private String getUserOpinionQuestionNegativeButtonLabel() {
        return defaultIfBlank(
                userOpinionQuestionNegativeButtonLabel,
                DEFAULT_USER_OPINION_QUESTION_NEGATIVE_BUTTON_LABEL);
    }

    @NonNull
    private String getPositiveFeedbackQuestionTitle() {
        return defaultIfBlank(
                positiveFeedbackQuestionTitle, DEFAULT_POSITIVE_FEEDBACK_QUESTION_TITLE);
    }

    @NonNull
    private String getPositiveFeedbackQuestionPositiveButtonLabel() {
        return defaultIfBlank(
                positiveFeedbackQuestionPositiveButtonLabel,
                DEFAULT_FEEDBACK_QUESTION_POSITIVE_BUTTON_LABEL);
    }

    @NonNull
    private String getPositiveFeedbackQuestionNegativeButtonLabel() {
        return defaultIfBlank(
                positiveFeedbackQuestionNegativeButtonLabel,
                DEFAULT_FEEDBACK_QUESTION_NEGATIVE_BUTTON_LABEL);
    }

    @NonNull
    private String getCriticalFeedbackQuestionTitle() {
        return defaultIfBlank(
                criticalFeedbackQuestionTitle, DEFAULT_CRITICAL_FEEDBACK_QUESTION_TITLE);
    }

    @NonNull
    private String getCriticalFeedbackQuestionPositiveButtonLabel() {
        return defaultIfBlank(
                criticalFeedbackQuestionPositiveButtonLabel,
                DEFAULT_FEEDBACK_QUESTION_POSITIVE_BUTTON_LABEL);
    }

    @NonNull
    private String getCriticalFeedbackQuestionNegativeButtonLabel() {
        return defaultIfBlank(
                criticalFeedbackQuestionNegativeButtonLabel,
                DEFAULT_FEEDBACK_QUESTION_NEGATIVE_BUTTON_LABEL);
    }

    @NonNull
    private String getThanksTitle() {
        return defaultIfBlank(thanksTitle, DEFAULT_THANKS_TITLE);
    }

    public static final class Builder {

        @Nullable private String userOpinionQuestionTitle;
        @Nullable private String userOpinionQuestionSubtitle;
        @Nullable private String userOpinionQuestionPositiveButtonLabel;
        @Nullable private String userOpinionQuestionNegativeButtonLabel;
        @Nullable private String positiveFeedbackQuestionTitle;
        @Nullable private String positiveFeedbackQuestionSubtitle;
        @Nullable private String positiveFeedbackQuestionPositiveButtonLabel;
        @Nullable private String positiveFeedbackQuestionNegativeButtonLabel;
        @Nullable private String criticalFeedbackQuestionTitle;
        @Nullable private String criticalFeedbackQuestionSubtitle;
        @Nullable private String criticalFeedbackQuestionPositiveButtonLabel;
        @Nullable private String criticalFeedbackQuestionNegativeButtonLabel;
        @Nullable private String thanksTitle;
        @Nullable private String thanksSubtitle;
        @Nullable private Long thanksDisplayTimeMs;

        public Builder setUserOpinionQuestionTitle(
                @NonNull final String userOpinionQuestionTitle) {

            this.userOpinionQuestionTitle = userOpinionQuestionTitle;
            return this;
        }

        public Builder setUserOpinionQuestionSubtitle(
                @NonNull final String userOpinionQuestionSubtitle) {

            this.userOpinionQuestionSubtitle = userOpinionQuestionSubtitle;
            return this;
        }

        public Builder setUserOpinionQuestionPositiveButtonLabel(
                @NonNull final String userOpinionQuestionPositiveButtonLabel) {

            this.userOpinionQuestionPositiveButtonLabel = userOpinionQuestionPositiveButtonLabel;
            return this;
        }

        public Builder setUserOpinionQuestionNegativeButtonLabel(
                @NonNull final String userOpinionQuestionNegativeButtonLabel) {

            this.userOpinionQuestionNegativeButtonLabel = userOpinionQuestionNegativeButtonLabel;
            return this;
        }

        public Builder setPositiveFeedbackQuestionTitle(
                @NonNull final String positiveFeedbackQuestionTitle) {

            this.positiveFeedbackQuestionTitle = positiveFeedbackQuestionTitle;
            return this;
        }

        public Builder setPositiveFeedbackQuestionSubtitle(
                @NonNull final String positiveFeedbackQuestionSubtitle) {

            this.positiveFeedbackQuestionSubtitle = positiveFeedbackQuestionSubtitle;
            return this;
        }

        public Builder setPositiveFeedbackQuestionPositiveButtonLabel(
                @NonNull final String positiveFeedbackQuestionPositiveButtonLabel) {

            this.positiveFeedbackQuestionPositiveButtonLabel
                    = positiveFeedbackQuestionPositiveButtonLabel;

            return this;
        }

        public Builder setPositiveFeedbackQuestionNegativeButtonLabel(
                @NonNull final String positiveFeedbackQuestionNegativeButtonLabel) {

            this.positiveFeedbackQuestionNegativeButtonLabel
                    = positiveFeedbackQuestionNegativeButtonLabel;

            return this;
        }

        public Builder setCriticalFeedbackQuestionTitle(
                @NonNull final String criticalFeedbackQuestionTitle) {

            this.criticalFeedbackQuestionTitle = criticalFeedbackQuestionTitle;
            return this;
        }

        public Builder setCriticalFeedbackQuestionSubtitle(
                @NonNull final String criticalFeedbackQuestionSubtitle) {

            this.criticalFeedbackQuestionSubtitle = criticalFeedbackQuestionSubtitle;
            return this;
        }

        public Builder setCriticalFeedbackQuestionPositiveButtonLabel(
                @NonNull final String criticalFeedbackQuestionPositiveButtonLabel) {

            this.criticalFeedbackQuestionPositiveButtonLabel
                    = criticalFeedbackQuestionPositiveButtonLabel;

            return this;
        }

        public Builder setCriticalFeedbackQuestionNegativeButtonLabel(
                @NonNull final String criticalFeedbackQuestionNegativeButtonLabel) {

            this.criticalFeedbackQuestionNegativeButtonLabel
                    = criticalFeedbackQuestionNegativeButtonLabel;

            return this;
        }

        public Builder setThanksTitle(@NonNull final String thanksTitle) {
            this.thanksTitle = thanksTitle;
            return this;
        }

        public Builder setThanksSubtitle(@NonNull final String thanksSubtitle) {
            this.thanksSubtitle = thanksSubtitle;
            return this;
        }

        public Builder setThanksDisplayTimeMs(final int thanksDisplayTimeMs) {
            this.thanksDisplayTimeMs = (long) thanksDisplayTimeMs;
            return this;
        }

        public BasePromptViewConfig build() {
            return new BasePromptViewConfig(
                    userOpinionQuestionTitle,
                    userOpinionQuestionSubtitle,
                    userOpinionQuestionPositiveButtonLabel,
                    userOpinionQuestionNegativeButtonLabel,
                    positiveFeedbackQuestionTitle,
                    positiveFeedbackQuestionSubtitle,
                    positiveFeedbackQuestionPositiveButtonLabel,
                    positiveFeedbackQuestionNegativeButtonLabel,
                    criticalFeedbackQuestionTitle,
                    criticalFeedbackQuestionSubtitle,
                    criticalFeedbackQuestionPositiveButtonLabel,
                    criticalFeedbackQuestionNegativeButtonLabel,
                    thanksTitle,
                    thanksSubtitle,
                    thanksDisplayTimeMs);
        }
    }

    // Parcelable

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeValue(userOpinionQuestionTitle);
        dest.writeValue(userOpinionQuestionSubtitle);
        dest.writeValue(userOpinionQuestionPositiveButtonLabel);
        dest.writeValue(userOpinionQuestionNegativeButtonLabel);
        dest.writeValue(positiveFeedbackQuestionTitle);
        dest.writeValue(positiveFeedbackQuestionSubtitle);
        dest.writeValue(positiveFeedbackQuestionPositiveButtonLabel);
        dest.writeValue(positiveFeedbackQuestionNegativeButtonLabel);
        dest.writeValue(criticalFeedbackQuestionTitle);
        dest.writeValue(criticalFeedbackQuestionSubtitle);
        dest.writeValue(criticalFeedbackQuestionPositiveButtonLabel);
        dest.writeValue(criticalFeedbackQuestionNegativeButtonLabel);
        dest.writeValue(thanksTitle);
        dest.writeValue(thanksSubtitle);
        dest.writeValue(thanksDisplayTimeMs);
    }

    @SuppressLint("ParcelClassLoader")
    protected BasePromptViewConfig(@NonNull final Parcel in) {
        userOpinionQuestionTitle = (String) in.readValue(null);
        userOpinionQuestionSubtitle = (String) in.readValue(null);
        userOpinionQuestionPositiveButtonLabel = (String) in.readValue(null);
        userOpinionQuestionNegativeButtonLabel = (String) in.readValue(null);
        positiveFeedbackQuestionTitle = (String) in.readValue(null);
        positiveFeedbackQuestionSubtitle = (String) in.readValue(null);
        positiveFeedbackQuestionPositiveButtonLabel = (String) in.readValue(null);
        positiveFeedbackQuestionNegativeButtonLabel = (String) in.readValue(null);
        criticalFeedbackQuestionTitle = (String) in.readValue(null);
        criticalFeedbackQuestionSubtitle = (String) in.readValue(null);
        criticalFeedbackQuestionPositiveButtonLabel = (String) in.readValue(null);
        criticalFeedbackQuestionNegativeButtonLabel = (String) in.readValue(null);
        thanksTitle = (String) in.readValue(null);
        thanksSubtitle = (String) in.readValue(null);
        thanksDisplayTimeMs = (Long) in.readValue(null);
    }

    public static final Parcelable.Creator<BasePromptViewConfig> CREATOR
            = new Parcelable.Creator<BasePromptViewConfig>() {

        @Override
        public BasePromptViewConfig createFromParcel(final Parcel in) {
            return new BasePromptViewConfig(in);
        }

        @Override
        public BasePromptViewConfig[] newArray(final int size) {
            return new BasePromptViewConfig[size];
        }

    };

}
//@formatter:on
