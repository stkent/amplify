package com.github.stkent.amplify.prompt;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.stkent.amplify.prompt.interfaces.IPromptPresenter;
import com.github.stkent.amplify.prompt.interfaces.IPromptView;
import com.github.stkent.amplify.tracking.PromptViewEvent;
import com.github.stkent.amplify.tracking.interfaces.IEventListener;

import static com.github.stkent.amplify.tracking.PromptViewEvent.USER_DECLINED_CRITICAL_FEEDBACK;
import static com.github.stkent.amplify.tracking.PromptViewEvent.USER_DECLINED_POSITIVE_FEEDBACK;
import static com.github.stkent.amplify.tracking.PromptViewEvent.USER_GAVE_CRITICAL_FEEDBACK;
import static com.github.stkent.amplify.tracking.PromptViewEvent.USER_GAVE_POSITIVE_FEEDBACK;

public final class PromptPresenter implements IPromptPresenter {

    private static final String EXHAUSTIVE_SWITCH_EXCEPTION_MESSAGE
            = "This switch statement should be exhaustive.";

    private static final String USER_OPINION_EXCEPTION_MESSAGE
            = "User opinion must be specified before this method is called.";


    @NonNull
    private final IEventListener<PromptViewEvent> eventListener;

    @NonNull
    private final IPromptView promptView;

    @Nullable
    private UserOpinion userOpinion;

    public PromptPresenter(
            @NonNull final IEventListener<PromptViewEvent> eventListener,
            @NonNull final IPromptView promptView) {

        this.eventListener = eventListener;
        this.promptView = promptView;

        promptView.setPresenter(this);
    }

    public void start() {
        promptView.queryUserOpinion();
    }

    @Override
    public void setUserOpinion(@NonNull final UserOpinion userOpinion) {
        this.userOpinion = userOpinion;

        switch (userOpinion) {
            case POSITIVE:
                promptView.requestPositiveFeedback();
                break;
            case NEGATIVE:
                promptView.requestCriticalFeedback();
                break;
            default:
                throw new IllegalStateException(EXHAUSTIVE_SWITCH_EXCEPTION_MESSAGE);
        }
    }

    @Override
    public void userAgreedToGiveFeedback() {
        if (userOpinion == null) {
            throw new IllegalStateException(USER_OPINION_EXCEPTION_MESSAGE);
        }

        switch (userOpinion) {
            case POSITIVE:
                eventListener.notifyEventTriggered(USER_GAVE_POSITIVE_FEEDBACK);
                break;
            case NEGATIVE:
                eventListener.notifyEventTriggered(USER_GAVE_CRITICAL_FEEDBACK);
                break;
            default:
                throw new IllegalStateException(EXHAUSTIVE_SWITCH_EXCEPTION_MESSAGE);
        }

        promptView.thankUser();
    }

    @Override
    public void userDeclinedToGiveFeedback() {
        if (userOpinion == null) {
            throw new IllegalStateException(USER_OPINION_EXCEPTION_MESSAGE);
        }

        switch (userOpinion) {
            case POSITIVE:
                eventListener.notifyEventTriggered(USER_DECLINED_POSITIVE_FEEDBACK);
                break;
            case NEGATIVE:
                eventListener.notifyEventTriggered(USER_DECLINED_CRITICAL_FEEDBACK);
                break;
            default:
                throw new IllegalStateException(EXHAUSTIVE_SWITCH_EXCEPTION_MESSAGE);
        }

        promptView.dismiss();
    }

}
