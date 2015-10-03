package com.github.stkent.amplify;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.github.stkent.amplify.RatingStateTracker.ActionType.USER_DECLINED_FEEDBACK;
import static com.github.stkent.amplify.RatingStateTracker.ActionType.USER_DECLINED_RATING;
import static com.github.stkent.amplify.RatingStateTracker.ActionType.USER_GAVE_FEEDBACK;
import static com.github.stkent.amplify.RatingStateTracker.ActionType.USER_GAVE_RATING;

public class RatingView extends LinearLayout {

    private enum UserOpinion {
        UNKNOWN,
        POSITIVE,
        NEGATIVE
    }

    private TextView title;
    private View buttonContainer;

    private RatingStateTracker ratingStateTracker = RatingStateTracker.getInstance();
    private UserOpinion userOpinion = UserOpinion.UNKNOWN;

    public RatingView(Context context) {
        super(context);
        init();
    }

    public RatingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);

        final int padding = getResources().getDimensionPixelSize(R.dimen.amp_default_rating_view_padding);
        setPadding(padding, 0, padding, padding);

        final LayoutParams layoutParams = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, padding);
        setLayoutParams(layoutParams);
        setBackgroundResource(R.color.amp_default_rating_view_background_color);

        inflate(getContext(), R.layout.amp_include_default_rating_view, this);
        title = (TextView) findViewById(R.id.title);
        buttonContainer = findViewById(R.id.buttonContainer);

        setVisibility(GONE);
    }

    //    TODO: hook this up
    protected void noButton() {
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

//    TODO: hook this up
    protected void yesButton() {
        switch (userOpinion) {
            case UNKNOWN:
                userOpinion = UserOpinion.POSITIVE;
                askSecondQuestion();
                break;
            case POSITIVE:
                thankUser();
                PlayStoreUtil.openPlayStoreToRate((Activity) getContext());
                ratingStateTracker.notify(USER_GAVE_RATING);
                break;
            case NEGATIVE:
                thankUser();
                showFeedbackEmailChooser();
                ratingStateTracker.notify(USER_GAVE_FEEDBACK);
                break;
            default:
                break;
        }
    }

    public void show() {
        setVisibility(VISIBLE);
    }

    private void askSecondQuestion() {
        switch (userOpinion) {
            case POSITIVE:
                title.setText(getContext().getString(R.string.amp_title_rating_question_2_rating));
                break;
            case NEGATIVE:
                title.setText(getContext().getString(R.string.amp_title_rating_question_2_feedback));
                break;
            default:
                throw new IllegalStateException("Should only call this method when user opinion is known to be positive or negative.");
        }
    }

    private void showFeedbackEmailChooser() {
        if (FeedbackUtils.canHandleFeedbackEmailIntent(getContext())) {
            FeedbackUtils.showFeedbackEmailChooser(getActivity());
        }
    }

    private void thankUser() {
        buttonContainer.setVisibility(GONE);
        title.setText(getContext().getString(R.string.amp_title_rating_thanks));
        title.setLines(1);
        setPadding(0, 0, 0, 0);
    }

    private void hide() {
        setVisibility(GONE);
    }

    private Activity getActivity() {
        return (Activity) getContext();
    }

}
