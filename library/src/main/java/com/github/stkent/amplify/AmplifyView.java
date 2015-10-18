package com.github.stkent.amplify;

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

    private AmplifyStateTracker ratingStateTracker;
    private LayoutState layoutState;
    private UserOpinion userOpinion = UserOpinion.UNKNOWN;
    private Question userOpinionQuestion = Question.Builder
            .withTitle("First question title")
            .andPositiveButtonText("Positive button")
            .andNegativeButtonText("Negative button")
            .build();

    private Question positiveFeedbackQuestion = Question.Builder
            .withTitle("Second question (+ve)")
            .andPositiveButtonText("Positive button")
            .andNegativeButtonText("Negative button")
            .build();

    private Question criticalFeedbackQuestion = Question.Builder
            .withTitle("Second question (-ve)")
            .andPositiveButtonText("Positive button")
            .andNegativeButtonText("Negative button")
            .build();

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

    private void init(final Context context, @Nullable final AttributeSet attrs) {
        final TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Amplify, 0, 0);

        // TODO: add proper default handling; checking for resource type
        questionLayoutResId = typedArray.getResourceId(R.styleable.Amplify_amplify_question_layout, 0);
        confirmationLayoutResId = typedArray.getResourceId(R.styleable.Amplify_amplify_confirmation_layout, 0);

        final String userOpinionQuestionTitle = typedArray.getString(
                R.styleable.Amplify_amplify_user_opinion_question);
        final String positiveFeedbackQuestionTitle = typedArray.getString(
                R.styleable.Amplify_amplify_positive_feedback_question);
        final String criticalFeedbackQuestionTitle = typedArray.getString(
                R.styleable.Amplify_amplify_critical_feedback_question);

        // TODOg: check that all questions are non-null^

        final String userOpinionPositiveButtonText = typedArray.getString(
                R.styleable.Amplify_amplify_user_opinion_positive_button_text);
        final String userOpinionNegativeButtonText = typedArray.getString(
                R.styleable.Amplify_amplify_user_opinion_negative_button_text);
        final String positiveFeedbackPositiveButtonText = typedArray.getString(
                R.styleable.Amplify_amplify_positive_feedback_positive_button_text);
        final String positiveFeedbackNegativeButtonText = typedArray.getString(
                R.styleable.Amplify_amplify_positive_feedback_negative_button_text);
        final String criticalFeedbackPositiveButtonText = typedArray.getString(
                R.styleable.Amplify_amplify_critical_feedback_positive_button_text);
        final String criticalFeedbackNegativeButtonText = typedArray.getString(
                R.styleable.Amplify_amplify_critical_feedback_negative_button_text);

        // TODO: initialize all questions here

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

    private void setContentLayoutForNewState(@NonNull final LayoutState layoutState) {
        switch (layoutState) {
            case QUESTION:
//                if (this.layoutState != LayoutState.QUESTION) {
                    removeAllViews();
                    addQuestionView();
//                }

                break;
            case CONFIRMATION:
//                if (this.layoutState != LayoutState.CONFIRMATION) {
                    removeAllViews();
                    addConfirmationView();
//                }
        }

        this.layoutState = layoutState;
    }

    private void addConfirmationView() {
        if (cachedConfirmationView == null) {
            try {
                cachedConfirmationView = LayoutInflater.from(getContext()).inflate(confirmationLayoutResId, this, false);
            } catch (Resources.NotFoundException exception) {
                // TODO: consolidate and set wording
                throw new IllegalArgumentException("Must provide a valid layout resource.");
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
                throw new IllegalArgumentException("Must provide a valid layout resource.");
            }
        }

        addView(cachedQuestionView.getView(), CONTENT_VIEW_LAYOUT_PARAMS);
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
                    thankUser();
//                    PlayStoreUtil.openPlayStoreToRate((Activity) getContext());
//                    ratingStateTracker.notify(USER_GAVE_RATING);
                    break;
                case NEGATIVE:
                    thankUser();
//                    showFeedbackEmailChooser();
//                    ratingStateTracker.notify(USER_GAVE_FEEDBACK);
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
//                    ratingStateTracker.notify(USER_DECLINED_RATING);
                    break;
                case NEGATIVE:
                    hide();
//                    ratingStateTracker.notify(USER_DECLINED_FEEDBACK);
                    break;
                default:
                    break;
            }
        }
    };

}
