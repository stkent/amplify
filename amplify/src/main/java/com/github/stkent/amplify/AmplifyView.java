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

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.github.stkent.amplify.tracking.AmplifyStateTracker;
import com.github.stkent.amplify.tracking.IntegratedEvent;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class AmplifyView extends FrameLayout {

    private enum UserOpinion {
        UNKNOWN,
        POSITIVE,
        NEGATIVE
    }

    private enum LayoutState {
        QUESTION,
        CONFIRMATION
    }

    private static final LayoutParams CONTENT_VIEW_LAYOUT_PARAMS
            = new LayoutParams(MATCH_PARENT, MATCH_PARENT);

    private LayoutState layoutState;
    private AmplifyStateTracker ratingStateTracker;
    private Question userOpinionQuestion;
    private Question positiveFeedbackQuestion;
    private Question criticalFeedbackQuestion;
    private UserOpinion userOpinion = UserOpinion.UNKNOWN;

    @LayoutRes
    private int questionLayoutResId;

    @LayoutRes
    private int confirmationLayoutResId;

    @Nullable
    private QuestionView cachedQuestionView;

    @Nullable
    private View cachedConfirmationView;

    public AmplifyView(final Context context) {
        this(context, null);
    }

    public AmplifyView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AmplifyView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    protected void respondToNegativeFeedback() {
        //TODO implement open email chooser
        //showFeedbackEmailChooser();
    }

    protected void respondToPositiveFeedback() {
        PlayStoreUtil.openPlayStoreToRate((Activity) getContext());
    }

    private void init(final Context context, @Nullable final AttributeSet attrs) {
        ratingStateTracker = AmplifyStateTracker.get(context);

        final TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Amplify, 0, 0);

        // TODO: add proper default handling; checking for resource type
        questionLayoutResId = typedArray.getResourceId(R.styleable.Amplify_amplify_question_layout, 0);
        confirmationLayoutResId = typedArray.getResourceId(R.styleable.Amplify_amplify_confirmation_layout, 0);

        //TODO add default string loading
        final String userOpinionQuestionTitle = StringUtils.defaultIfBlank(typedArray.getString(
                R.styleable.Amplify_amplify_user_opinion_question), "");
        final String userOpinionPositiveButtonText = StringUtils.defaultIfBlank(typedArray.getString(
                R.styleable.Amplify_amplify_user_opinion_positive_button_text), "");
        final String userOpinionNegativeButtonText = StringUtils.defaultIfBlank(typedArray.getString(
                R.styleable.Amplify_amplify_user_opinion_negative_button_text), "");
        userOpinionQuestion = Question.Builder
                .withTitle(userOpinionQuestionTitle)
                .andPositiveButtonText(userOpinionPositiveButtonText)
                .andNegativeButtonText(userOpinionNegativeButtonText)
                .build();

        //TODO add default string loading
        final String positiveFeedbackQuestionTitle = StringUtils.defaultIfBlank(typedArray.getString(
                R.styleable.Amplify_amplify_positive_feedback_question), "");
        final String positiveFeedbackPositiveButtonText = StringUtils.defaultIfBlank(typedArray.getString(
                R.styleable.Amplify_amplify_positive_feedback_positive_button_text), "");
        final String positiveFeedbackNegativeButtonText = StringUtils.defaultIfBlank(typedArray.getString(
                R.styleable.Amplify_amplify_positive_feedback_negative_button_text), "");

        positiveFeedbackQuestion = Question.Builder
                .withTitle(positiveFeedbackQuestionTitle)
                .andPositiveButtonText(positiveFeedbackPositiveButtonText)
                .andNegativeButtonText(positiveFeedbackNegativeButtonText)
                .build();

        //TODO add default string loading
        final String criticalFeedbackQuestionTitle = StringUtils.defaultIfBlank(typedArray.getString(
                R.styleable.Amplify_amplify_critical_feedback_question), "");
        final String criticalFeedbackPositiveButtonText = StringUtils.defaultIfBlank(typedArray.getString(
                R.styleable.Amplify_amplify_critical_feedback_positive_button_text), "");
        final String criticalFeedbackNegativeButtonText = StringUtils.defaultIfBlank(typedArray.getString(
                R.styleable.Amplify_amplify_critical_feedback_negative_button_text), "");

        criticalFeedbackQuestion = Question.Builder
                .withTitle(criticalFeedbackQuestionTitle)
                .andPositiveButtonText(criticalFeedbackPositiveButtonText)
                .andNegativeButtonText(criticalFeedbackNegativeButtonText)
                .build();

        typedArray.recycle();

        askFirstQuestion();
    }

    private void askFirstQuestion() {
        setContentLayoutForNewState(LayoutState.QUESTION);

        if (cachedQuestionView != null) {
            cachedQuestionView.setQuestion(userOpinionQuestion);
        }
    }

    private void askSecondQuestion() {
        setContentLayoutForNewState(LayoutState.QUESTION);

        if (cachedQuestionView != null) {
            if (userOpinion == UserOpinion.POSITIVE) {
                cachedQuestionView.setQuestion(positiveFeedbackQuestion);
            } else if (userOpinion == UserOpinion.NEGATIVE) {
                cachedQuestionView.setQuestion(criticalFeedbackQuestion);
            }
        }
    }

    private void thankUser() {
        setContentLayoutForNewState(LayoutState.CONFIRMATION);
    }

    private void setContentLayoutForNewState(@NonNull final LayoutState newLayoutState) {
        switch (newLayoutState) {
            case QUESTION:
                if (this.layoutState != LayoutState.QUESTION) {
                    removeAllViews();
                    addQuestionView();
                }

                break;
            case CONFIRMATION:
                if (this.layoutState != LayoutState.CONFIRMATION) {
                    removeAllViews();
                    addConfirmationView();
                }
                break;
        }

        layoutState = newLayoutState;
    }

    private void addConfirmationView() {
        if (cachedConfirmationView == null) {
            try {
                cachedConfirmationView = LayoutInflater.from(getContext()).inflate(confirmationLayoutResId, this, false);
            } catch (Resources.NotFoundException exception) {
                // TODO: consolidate and set wording
                throw new IllegalArgumentException("Must provide a valid layout resource.", exception);
            }
        }

        addView(cachedConfirmationView, CONTENT_VIEW_LAYOUT_PARAMS);
    }

    private void addQuestionView() {
        if (cachedQuestionView == null) {
            try {
                final View view = LayoutInflater.from(getContext()).inflate(questionLayoutResId, this, false);

                cachedQuestionView = new DefaultQuestionView(view);

                cachedQuestionView.getPositiveButton().setOnClickListener(positiveButtonClickListener);
                cachedQuestionView.getNegativeButton().setOnClickListener(negativeButtonClickListener);

            } catch (Resources.NotFoundException exception) {
                // TODO: consolidate and set wording
                throw new IllegalArgumentException("Must provide a valid layout resource.", exception);
            }
        }

        addView(cachedQuestionView.getView(), CONTENT_VIEW_LAYOUT_PARAMS);
    }

    public void show() {
        setVisibility(VISIBLE);
    }

    private void hide() {
        setVisibility(GONE);
    }

    private final OnClickListener positiveButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch (userOpinion) {
                case UNKNOWN:
                    userOpinion = UserOpinion.POSITIVE;
                    askSecondQuestion();
                    break;
                case POSITIVE:
                    ratingStateTracker.notifyEventTriggered(IntegratedEvent.USER_GAVE_POSITIVE_FEEDBACK);
                    thankUser();
                    respondToPositiveFeedback();
                    break;
                case NEGATIVE:
                    ratingStateTracker.notifyEventTriggered(IntegratedEvent.USER_GAVE_NEGATIVE_FEEDBACK);
                    thankUser();
                    respondToNegativeFeedback();
                    break;
                default:
                    break;
            }
        }
    };

    private final OnClickListener negativeButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch (userOpinion) {
                case UNKNOWN:
                    userOpinion = UserOpinion.NEGATIVE;
                    askSecondQuestion();
                    break;
                case POSITIVE:
                    hide();
                    ratingStateTracker.notifyEventTriggered(IntegratedEvent.USER_DECLINED_RATING);
                    break;
                case NEGATIVE:
                    hide();
                    ratingStateTracker.notifyEventTriggered(IntegratedEvent.USER_DECLINED_FEEDBACK);
                    break;
                default:
                    break;
            }
        }
    };

}
