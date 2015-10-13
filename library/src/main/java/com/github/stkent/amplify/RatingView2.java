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
import static com.github.stkent.amplify.AmplifyStateTracker.ActionType.USER_DECLINED_FEEDBACK;
import static com.github.stkent.amplify.AmplifyStateTracker.ActionType.USER_DECLINED_RATING;
import static com.github.stkent.amplify.AmplifyStateTracker.ActionType.USER_GAVE_FEEDBACK;
import static com.github.stkent.amplify.AmplifyStateTracker.ActionType.USER_GAVE_RATING;

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
    private LayoutState layoutState = LayoutState.QUESTION;
    private UserOpinion userOpinion = UserOpinion.UNKNOWN;
    private Question firstQuestion;
    private Question secondQuestion;

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
        final TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.Amp, 0, 0);

        typedArray.getResourceId(R.styleable.Amp_questionLayout, R.layout.amp_include_default_rating_view);

        // inflate a layout if provided in xml
        // inflate a default layout

        typedArray.recycle();
    }

    private void updateContentView() {
        switch (layoutState) {
            case QUESTION:
                if (userOpinion == UserOpinion.UNKNOWN) {
                    askFirstQuestion();
                } else {
                    askSecondQuestion();
                }
                break;
            case CONFIRMATION:
                thankUser();
        }
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
            cachedQuestionView.setQuestion(secondQuestion);
        }
    }

    private void thankUser() {
        setContentLayoutForNewState(LayoutState.CONFIRMATION);
    }

    private void setContentLayoutForNewState(@NonNull final LayoutState newLayoutState) {
        switch (newLayoutState) {
            case QUESTION:
                if (layoutState != LayoutState.QUESTION) {
                    removeAllViews();
                    addQuestionView();
                }

                break;
            case CONFIRMATION:
                if (layoutState != LayoutState.CONFIRMATION) {
                    removeAllViews();
                    addConfirmationView();
                }
        }

        layoutState = newLayoutState;
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
                    ratingStateTracker.notify(USER_GAVE_RATING);
                    break;
                case NEGATIVE:
                    thankUser();
//                    showFeedbackEmailChooser();
                    ratingStateTracker.notify(USER_GAVE_FEEDBACK);
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
                    ratingStateTracker.notify(USER_DECLINED_RATING);
                    break;
                case NEGATIVE:
                    hide();
                    ratingStateTracker.notify(USER_DECLINED_FEEDBACK);
                    break;
                default:
                    break;
            }
        }
    };

}
