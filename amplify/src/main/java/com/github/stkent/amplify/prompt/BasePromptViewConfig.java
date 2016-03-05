package com.github.stkent.amplify.prompt;

import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.stkent.amplify.prompt.interfaces.IQuestion;
import com.github.stkent.amplify.prompt.interfaces.IThanks;

import static com.github.stkent.amplify.utils.StringUtils.defaultIfBlank;

// @formatter:off
public final class BasePromptViewConfig {

    private static final String DEFAULT_USER_OPINION_QUESTION_TITLE      = "Enjoying the app?";
    private static final String DEFAULT_POSITIVE_FEEDBACK_QUESTION_TITLE = "Awesome! We'd love a Play Store review...";
    private static final String DEFAULT_CRITICAL_FEEDBACK_QUESTION_TITLE = "Bummer. Would you like to send feedback?";
    private static final String DEFAULT_POSITIVE_BUTTON_LABEL            = "Sure thing!";
    private static final String DEFAULT_NEGATIVE_BUTTON_LABEL            = "Not right now";
    private static final String DEFAULT_THANKS_TITLE                     = "Thanks for your feedback!";

    @Nullable private final String userOpinionQuestionTitle;
    @Nullable private final String userOpinionQuestionPositiveButtonLabel;
    @Nullable private final String userOpinionQuestionNegativeButtonLabel;
    @Nullable private final String positiveFeedbackQuestionTitle;
    @Nullable private final String positiveFeedbackQuestionPositiveButtonLabel;
    @Nullable private final String positiveFeedbackQuestionNegativeButtonLabel;
    @Nullable private final String criticalFeedbackQuestionTitle;
    @Nullable private final String criticalFeedbackQuestionPositiveButtonLabel;
    @Nullable private final String criticalFeedbackQuestionNegativeButtonLabel;
    @Nullable private final String thanksTitle;
    @Nullable private final String userOpinionQuestionSubtitle;
    @Nullable private final String positiveFeedbackQuestionSubtitle;
    @Nullable private final String criticalFeedbackQuestionSubtitle;
    @Nullable private final String thanksSubtitle;

    public BasePromptViewConfig(@NonNull final TypedArray typedArray) {
        // TODO: fill this in, and document non-recycling well!
    }

    private BasePromptViewConfig(
            @Nullable final String userOpinionQuestionTitle,
            @Nullable final String userOpinionQuestionPositiveButtonLabel,
            @Nullable final String userOpinionQuestionNegativeButtonLabel,
            @Nullable final String positiveFeedbackQuestionTitle,
            @Nullable final String positiveFeedbackQuestionPositiveButtonLabel,
            @Nullable final String positiveFeedbackQuestionNegativeButtonLabel,
            @Nullable final String criticalFeedbackQuestionTitle,
            @Nullable final String criticalFeedbackQuestionPositiveButtonLabel,
            @Nullable final String criticalFeedbackQuestionNegativeButtonLabel,
            @Nullable final String thanksTitle,
            @Nullable final String userOpinionQuestionSubtitle,
            @Nullable final String positiveFeedbackQuestionSubtitle,
            @Nullable final String criticalFeedbackQuestionSubtitle,
            @Nullable final String thanksSubtitle) {

        this.userOpinionQuestionTitle                    = userOpinionQuestionTitle;
        this.userOpinionQuestionPositiveButtonLabel      = userOpinionQuestionPositiveButtonLabel;
        this.userOpinionQuestionNegativeButtonLabel      = userOpinionQuestionNegativeButtonLabel;
        this.positiveFeedbackQuestionTitle               = positiveFeedbackQuestionTitle;
        this.positiveFeedbackQuestionPositiveButtonLabel = positiveFeedbackQuestionPositiveButtonLabel;
        this.positiveFeedbackQuestionNegativeButtonLabel = positiveFeedbackQuestionNegativeButtonLabel;
        this.criticalFeedbackQuestionTitle               = criticalFeedbackQuestionTitle;
        this.criticalFeedbackQuestionPositiveButtonLabel = criticalFeedbackQuestionPositiveButtonLabel;
        this.criticalFeedbackQuestionNegativeButtonLabel = criticalFeedbackQuestionNegativeButtonLabel;
        this.thanksTitle                                 = thanksTitle;
        this.userOpinionQuestionSubtitle                 = userOpinionQuestionSubtitle;
        this.positiveFeedbackQuestionSubtitle            = positiveFeedbackQuestionSubtitle;
        this.criticalFeedbackQuestionSubtitle            = criticalFeedbackQuestionSubtitle;
        this.thanksSubtitle                              = thanksSubtitle;
    }

    @NonNull
    public IQuestion getUserOpinionQuestion() {
        return new Question(
                getUserOpinionQuestionTitle(),
                getUserOpinionQuestionSubtitle(),
                getUserOpinionQuestionPositiveButtonLabel(),
                getUserOpinionQuestionNegativeButtonLabel());
    }

    @NonNull
    public IQuestion getPositiveFeedbackQuestion() {
        return new Question(
                getPositiveFeedbackQuestionTitle(),
                getPositiveFeedbackQuestionSubtitle(),
                getPositiveFeedbackQuestionPositiveButtonLabel(),
                getPositiveFeedbackQuestionNegativeButtonLabel());
    }

    @NonNull
    public IQuestion getCriticalFeedbackQuestion() {
        return new Question(
                getCriticalFeedbackQuestionTitle(),
                getCriticalFeedbackQuestionSubtitle(),
                getCriticalFeedbackQuestionPositiveButtonLabel(),
                getCriticalFeedbackQuestionNegativeButtonLabel());
    }

    @NonNull
    public IThanks getThanks() {
        return new Thanks(getThanksTitle(), getThanksSubtitle());
    }

    @NonNull
    private String getUserOpinionQuestionTitle() {
        return defaultIfBlank(
                userOpinionQuestionTitle, DEFAULT_USER_OPINION_QUESTION_TITLE);
    }

    @Nullable
    private String getUserOpinionQuestionSubtitle() {
        return userOpinionQuestionSubtitle;
    }

    @NonNull
    private String getUserOpinionQuestionPositiveButtonLabel() {
        return defaultIfBlank(
                userOpinionQuestionPositiveButtonLabel, DEFAULT_POSITIVE_BUTTON_LABEL);
    }

    @NonNull
    private String getUserOpinionQuestionNegativeButtonLabel() {
        return defaultIfBlank(
                userOpinionQuestionNegativeButtonLabel, DEFAULT_NEGATIVE_BUTTON_LABEL);
    }

    @NonNull
    private String getPositiveFeedbackQuestionTitle() {
        return defaultIfBlank(
                positiveFeedbackQuestionTitle, DEFAULT_POSITIVE_FEEDBACK_QUESTION_TITLE);
    }

    @Nullable
    private String getPositiveFeedbackQuestionSubtitle() {
        return positiveFeedbackQuestionSubtitle;
    }

    @NonNull
    private String getPositiveFeedbackQuestionPositiveButtonLabel() {
        return defaultIfBlank(
                positiveFeedbackQuestionPositiveButtonLabel, DEFAULT_POSITIVE_BUTTON_LABEL);
    }

    @NonNull
    private String getPositiveFeedbackQuestionNegativeButtonLabel() {
        return defaultIfBlank(
                positiveFeedbackQuestionNegativeButtonLabel, DEFAULT_NEGATIVE_BUTTON_LABEL);
    }

    @NonNull
    private String getCriticalFeedbackQuestionTitle() {
        return defaultIfBlank(
                criticalFeedbackQuestionTitle, DEFAULT_CRITICAL_FEEDBACK_QUESTION_TITLE);
    }

    @Nullable
    private String getCriticalFeedbackQuestionSubtitle() {
        return criticalFeedbackQuestionSubtitle;
    }

    @NonNull
    private String getCriticalFeedbackQuestionPositiveButtonLabel() {
        return defaultIfBlank(
                criticalFeedbackQuestionPositiveButtonLabel, DEFAULT_POSITIVE_BUTTON_LABEL);
    }

    @NonNull
    private String getCriticalFeedbackQuestionNegativeButtonLabel() {
        return defaultIfBlank(
                criticalFeedbackQuestionNegativeButtonLabel, DEFAULT_NEGATIVE_BUTTON_LABEL);
    }

    @NonNull
    private String getThanksTitle() {
        return defaultIfBlank(thanksTitle, DEFAULT_THANKS_TITLE);
    }

    @Nullable
    private String getThanksSubtitle() {
        return thanksSubtitle;
    }

    public static final class Builder {

        @Nullable private String userOpinionQuestionTitle;
        @Nullable private String userOpinionQuestionPositiveButtonLabel;
        @Nullable private String userOpinionQuestionNegativeButtonLabel;
        @Nullable private String positiveFeedbackQuestionTitle;
        @Nullable private String positiveFeedbackQuestionPositiveButtonLabel;
        @Nullable private String positiveFeedbackQuestionNegativeButtonLabel;
        @Nullable private String criticalFeedbackQuestionTitle;
        @Nullable private String criticalFeedbackQuestionPositiveButtonLabel;
        @Nullable private String criticalFeedbackQuestionNegativeButtonLabel;
        @Nullable private String thanksTitle;
        @Nullable private String userOpinionQuestionSubtitle;
        @Nullable private String positiveFeedbackQuestionSubtitle;
        @Nullable private String criticalFeedbackQuestionSubtitle;
        @Nullable private String thanksSubtitle;

        private Builder setUserOpinionQuestionTitle(
                @NonNull final String userOpinionQuestionTitle) {

            this.userOpinionQuestionTitle = userOpinionQuestionTitle;
            return this;
        }

        private Builder setUserOpinionQuestionPositiveButtonLabel(
                @NonNull final String userOpinionQuestionPositiveButtonLabel) {

            this.userOpinionQuestionPositiveButtonLabel = userOpinionQuestionPositiveButtonLabel;
            return this;
        }

        private Builder setUserOpinionQuestionNegativeButtonLabel(
                @NonNull final String userOpinionQuestionNegativeButtonLabel) {

            this.userOpinionQuestionNegativeButtonLabel = userOpinionQuestionNegativeButtonLabel;
            return this;
        }

        private Builder setPositiveFeedbackQuestionTitle(
                @NonNull final String positiveFeedbackQuestionTitle) {

            this.positiveFeedbackQuestionTitle = positiveFeedbackQuestionTitle;
            return this;
        }

        private Builder setPositiveFeedbackQuestionPositiveButtonLabel(
                @NonNull final String positiveFeedbackQuestionPositiveButtonLabel) {

            this.positiveFeedbackQuestionPositiveButtonLabel
                    = positiveFeedbackQuestionPositiveButtonLabel;

            return this;
        }

        private Builder setPositiveFeedbackQuestionNegativeButtonLabel(
                @NonNull final String positiveFeedbackQuestionNegativeButtonLabel) {

            this.positiveFeedbackQuestionNegativeButtonLabel
                    = positiveFeedbackQuestionNegativeButtonLabel;

            return this;
        }

        private Builder setCriticalFeedbackQuestionTitle(
                @NonNull final String criticalFeedbackQuestionTitle) {

            this.criticalFeedbackQuestionTitle = criticalFeedbackQuestionTitle;
            return this;
        }

        private Builder setCriticalFeedbackQuestionPositiveButtonLabel(
                @NonNull final String criticalFeedbackQuestionPositiveButtonLabel) {

            this.criticalFeedbackQuestionPositiveButtonLabel
                    = criticalFeedbackQuestionPositiveButtonLabel;

            return this;
        }

        private Builder setCriticalFeedbackQuestionNegativeButtonLabel(
                @NonNull final String criticalFeedbackQuestionNegativeButtonLabel) {

            this.criticalFeedbackQuestionNegativeButtonLabel
                    = criticalFeedbackQuestionNegativeButtonLabel;

            return this;
        }

        private Builder setThanksTitle(@NonNull final String thanksTitle) {
            this.thanksTitle = thanksTitle;
            return this;
        }

        private Builder setUserOpinionQuestionSubtitle(
                @NonNull final String userOpinionQuestionSubtitle) {

            this.userOpinionQuestionSubtitle = userOpinionQuestionSubtitle;
            return this;
        }

        private Builder setPositiveFeedbackQuestionSubtitle(
                @NonNull final String positiveFeedbackQuestionSubtitle) {

            this.positiveFeedbackQuestionSubtitle = positiveFeedbackQuestionSubtitle;
            return this;
        }

        private Builder setCriticalFeedbackQuestionSubtitle(
                @NonNull final String criticalFeedbackQuestionSubtitle) {

            this.criticalFeedbackQuestionSubtitle = criticalFeedbackQuestionSubtitle;
            return this;
        }

        private Builder setThanksSubtitle(@NonNull final String thanksSubtitle) {
            this.thanksSubtitle = thanksSubtitle;
            return this;
        }

        private BasePromptViewConfig build() {
            return new BasePromptViewConfig(
                userOpinionQuestionTitle,
                userOpinionQuestionPositiveButtonLabel,
                userOpinionQuestionNegativeButtonLabel,
                positiveFeedbackQuestionTitle,
                positiveFeedbackQuestionPositiveButtonLabel,
                positiveFeedbackQuestionNegativeButtonLabel,
                criticalFeedbackQuestionTitle,
                criticalFeedbackQuestionPositiveButtonLabel,
                criticalFeedbackQuestionNegativeButtonLabel,
                thanksTitle,
                userOpinionQuestionSubtitle,
                positiveFeedbackQuestionSubtitle,
                criticalFeedbackQuestionSubtitle,
                thanksSubtitle);
        }
    }

}
// @formatter:on