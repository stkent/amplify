package com.github.stkent.amplify;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.github.stkent.amplify.AmplifyStateTracker.ActionType.USER_DECLINED_FEEDBACK;
import static com.github.stkent.amplify.AmplifyStateTracker.ActionType.USER_DECLINED_RATING;
import static com.github.stkent.amplify.AmplifyStateTracker.ActionType.USER_GAVE_FEEDBACK;
import static com.github.stkent.amplify.AmplifyStateTracker.ActionType.USER_GAVE_RATING;

public class AmplifyView extends LinearLayout {

    private OnClickListener l;

    private enum UserOpinion {
        UNKNOWN,
        POSITIVE,
        NEGATIVE
    }

    private TextView title;
    private View buttonContainer;

    private AmplifyStateTracker amplifyStateTracker = AmplifyStateTracker.getInstance();
    private UserOpinion userOpinion = UserOpinion.UNKNOWN;

    public AmplifyView(@NonNull final Context context) {
        this(context, null);
    }

    public AmplifyView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);

        // todo: attribute things

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


        final TextView noButton = (TextView) findViewById(R.id.noButton);
        final TextView yesButton = (TextView) findViewById(R.id.yesButton);
        noButton.setOnClickListener(noButtonOnClickListener);
        yesButton.setOnClickListener(yesButtonOnClickListener);

        setVisibility(GONE);
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

    @NonNull
    final OnClickListener noButtonOnClickListener = new OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch (userOpinion) {
                case UNKNOWN:
                    userOpinion = UserOpinion.POSITIVE;
                    askSecondQuestion();
                    break;
                case POSITIVE:
                    thankUser();
                    PlayStoreUtil.openPlayStoreToRate((Activity) getContext());
                    amplifyStateTracker.notify(USER_GAVE_RATING);
                    break;
                case NEGATIVE:
                    thankUser();
                    showFeedbackEmailChooser();
                    amplifyStateTracker.notify(USER_GAVE_FEEDBACK);
                    break;
                default:
                    break;
            }
        }
    };

    @NonNull
    final OnClickListener yesButtonOnClickListener = new OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch (userOpinion) {
                case UNKNOWN:
                    userOpinion = UserOpinion.NEGATIVE;
                    askSecondQuestion();
                    break;
                case POSITIVE:
                    hide();
                    amplifyStateTracker.notify(USER_DECLINED_RATING);
                    break;
                case NEGATIVE:
                    hide();
                    amplifyStateTracker.notify(USER_DECLINED_FEEDBACK);
                    break;
                default:
                    break;
            }
        }
    };

}
