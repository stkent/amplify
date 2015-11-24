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
package com.github.stkent.amplify.views;

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

import com.github.stkent.amplify.ILogger;
import com.github.stkent.amplify.R;
import com.github.stkent.amplify.tracking.AmplifyStateTracker;
import com.github.stkent.amplify.tracking.ApplicationInfoProvider;
import com.github.stkent.amplify.tracking.EnvironmentInfoProvider;
import com.github.stkent.amplify.tracking.IntegratedEvent;
import com.github.stkent.amplify.tracking.interfaces.IAmplifyStateTracker;
import com.github.stkent.amplify.utils.FeedbackUtil;
import com.github.stkent.amplify.utils.PlayStoreUtil;
import com.github.stkent.amplify.utils.StringUtils;

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

    @Nullable
    private IAmplifyStateTracker amplifyStateTracker;

    @Nullable
    private ILogger logger;

    private LayoutState layoutState;
    private UserOpinion userOpinion = UserOpinion.UNKNOWN;

    private String userOpinionQuestion;
    private String userOpinionTitle;
    private String positiveFeedbackQuestion;
    private String positiveFeedbackTitle;
    private String criticalFeedbackQuestion;
    private String criticalFeedbackTitle;

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

    public void injectDependencies(@NonNull final IAmplifyStateTracker amplifyStateTracker, @NonNull final ILogger logger) {
        this.amplifyStateTracker = amplifyStateTracker;
        this.logger = logger;
    }

    @SuppressWarnings("ConstantConditions")
    protected void respondToNegativeFeedback() {
        checkDependenciesHaveBeenInjected();

        final Context applicationContext = getContext().getApplicationContext();

        // todo: replace this logger with an injected logger:
        final FeedbackUtil feedbackUtil = new FeedbackUtil(
                new ApplicationInfoProvider(applicationContext),
                new EnvironmentInfoProvider(applicationContext),
                logger);

        if (getContext() instanceof Activity) {
            feedbackUtil.showFeedbackEmailChooser((Activity) getContext());
        }
    }

    protected void respondToPositiveFeedback() {
        PlayStoreUtil.openPlayStoreToRate((Activity) getContext());
    }

    private void init(final Context context, @Nullable final AttributeSet attrs) {
        hide();

        amplifyStateTracker = AmplifyStateTracker.get(context);

        final TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AmplifyView, 0, 0);

        // TODO: add proper default handling; checking for resource type
        questionLayoutResId = typedArray.getResourceId(R.styleable.AmplifyView_amplify_question_layout, 0);
        confirmationLayoutResId = typedArray.getResourceId(R.styleable.AmplifyView_amplify_confirmation_layout, 0);

        //TODO add default string loading
        userOpinionQuestion = StringUtils.defaultIfBlank(typedArray.getString(
                R.styleable.AmplifyView_amplify_user_opinion_question), "");
        userOpinionTitle = StringUtils.defaultIfBlank(typedArray.getString(
                R.styleable.AmplifyView_amplify_user_opinion_title), "");

        //TODO add default string loading
        positiveFeedbackQuestion = StringUtils.defaultIfBlank(typedArray.getString(
                R.styleable.AmplifyView_amplify_positive_feedback_question), "");
        positiveFeedbackTitle = StringUtils.defaultIfBlank(typedArray.getString(
                R.styleable.AmplifyView_amplify_positive_feedback_title), "");

        //TODO add default string loading
        criticalFeedbackQuestion = StringUtils.defaultIfBlank(typedArray.getString(
                R.styleable.AmplifyView_amplify_critical_feedback_question), "");
        criticalFeedbackTitle = StringUtils.defaultIfBlank(typedArray.getString(
                R.styleable.AmplifyView_amplify_critical_feedback_title), "");

        typedArray.recycle();

        askFirstQuestion();
    }

    private void askFirstQuestion() {
        setContentLayoutForNewState(LayoutState.QUESTION);

        if (cachedQuestionView != null) {
            cachedQuestionView.setQuestion(userOpinionQuestion);
            cachedQuestionView.setTitle(userOpinionTitle);
        }
    }

    private void askSecondQuestion() {
        setContentLayoutForNewState(LayoutState.QUESTION);

        if (cachedQuestionView != null) {
            if (userOpinion == UserOpinion.POSITIVE) {
                cachedQuestionView.setQuestion(positiveFeedbackQuestion);
                cachedQuestionView.setTitle(positiveFeedbackTitle);
            } else if (userOpinion == UserOpinion.NEGATIVE) {
                cachedQuestionView.setQuestion(criticalFeedbackQuestion);
                cachedQuestionView.setTitle(criticalFeedbackTitle);
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
        @SuppressWarnings("ConstantConditions")
        @Override
        public void onClick(final View v) {
            checkDependenciesHaveBeenInjected();

            switch (userOpinion) {
                case UNKNOWN:
                    userOpinion = UserOpinion.POSITIVE;
                    askSecondQuestion();
                    break;
                case POSITIVE:
                    amplifyStateTracker.notifyEventTriggered(IntegratedEvent.USER_GAVE_POSITIVE_FEEDBACK);
                    thankUser();
                    respondToPositiveFeedback();
                    break;
                case NEGATIVE:
                    amplifyStateTracker.notifyEventTriggered(IntegratedEvent.USER_GAVE_CRITICAL_FEEDBACK);
                    thankUser();
                    respondToNegativeFeedback();
                    break;
                default:
                    break;
            }
        }
    };

    private final OnClickListener negativeButtonClickListener = new OnClickListener() {
        @SuppressWarnings("ConstantConditions")
        @Override
        public void onClick(final View v) {
            checkDependenciesHaveBeenInjected();

            switch (userOpinion) {
                case UNKNOWN:
                    userOpinion = UserOpinion.NEGATIVE;
                    askSecondQuestion();
                    break;
                case POSITIVE:
                    hide();
                    amplifyStateTracker.notifyEventTriggered(IntegratedEvent.USER_DECLINED_POSITIVE_FEEDBACK);
                    break;
                case NEGATIVE:
                    hide();
                    amplifyStateTracker.notifyEventTriggered(IntegratedEvent.USER_DECLINED_CRITICAL_FEEDBACK);
                    break;
                default:
                    break;
            }
        }
    };

    private void checkDependenciesHaveBeenInjected() {
        if (amplifyStateTracker == null || logger == null) {
            // todo: use the stack trace to print out the calling method name here!
            throw new IllegalStateException("Dependencies must be injected before this method is called");
        }
    }

}
