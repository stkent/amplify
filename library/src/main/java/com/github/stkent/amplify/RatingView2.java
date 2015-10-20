package com.github.stkent.amplify;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class RatingView2 extends FrameLayout {

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
    private Question firstQuestion = new Question(
            "First question title",
            "Positive button",
            "Negative button"
    );

    private Question secondQuestionForPositiveOpinion = new Question(
            "Second question (+ve)",
            "Positive button",
            "Negative button"
    );

    private Question secondQuestionForNegativeOpinion = new Question(
            "Second question (-ve)",
            "Positive button",
            "Negative button"
    );

    @LayoutRes
    private int questionLayoutResId;

    @LayoutRes
    private int confirmationLayoutResId;

    @Nullable
    private QuestionView cachedQuestionView;

    @Nullable
    private View cachedConfirmationView;

    public RatingView2(final Context context) {
        this(context, null);
    }

    public RatingView2(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatingView2(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (attrs != null) {
            init(context, attrs);
        }
    }

    private void init(final Context context, @Nullable final AttributeSet attrs) {
        final TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Amplify, 0, 0);

        // TODO: add proper default handling; checking for resource type
        questionLayoutResId = typedArray.getResourceId(R.styleable.Amplify_amplify_question_layout, 0);
        confirmationLayoutResId = typedArray.getResourceId(R.styleable.Amplify_amplify_confirmation_layout, 0);

        typedArray.recycle();

        askFirstQuestion();
    }

    private void askFirstQuestion() {
        setContentLayoutForNewState(LayoutState.QUESTION);

        if (cachedQuestionView != null) {
            cachedQuestionView.setQuestion(firstQuestion);
        }
    }

    private void askSecondQuestion() {
        setContentLayoutForNewState(LayoutState.QUESTION);

        if (cachedQuestionView != null) {
            if (userOpinion == UserOpinion.POSITIVE) {
                cachedQuestionView.setQuestion(secondQuestionForPositiveOpinion);
            } else if (userOpinion == UserOpinion.NEGATIVE) {
                cachedQuestionView.setQuestion(secondQuestionForNegativeOpinion);
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
            cachedConfirmationView =
                    LayoutInflater.from(getContext()).inflate(confirmationLayoutResId, this, false);
        }

        addView(cachedConfirmationView, CONTENT_VIEW_LAYOUT_PARAMS);
    }

    private void addQuestionView() {
        if (cachedQuestionView == null) {
            final View view =
                    LayoutInflater.from(getContext()).inflate(questionLayoutResId, this, false);

            cachedQuestionView = new DefaultQuestionView(view);

            cachedQuestionView.getPositiveButton().setOnClickListener(positiveButtonClickListener);
            cachedQuestionView.getNegativeButton().setOnClickListener(negativeButtonClickListener);
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
