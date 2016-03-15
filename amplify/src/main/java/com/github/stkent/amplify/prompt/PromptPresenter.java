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
package com.github.stkent.amplify.prompt;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.stkent.amplify.prompt.interfaces.IPromptPresenter;
import com.github.stkent.amplify.prompt.interfaces.IPromptView;
import com.github.stkent.amplify.tracking.PromptViewEvent;
import com.github.stkent.amplify.tracking.interfaces.IEventListener;

import static com.github.stkent.amplify.utils.Constants.EXHAUSTIVE_SWITCH_EXCEPTION_MESSAGE;

public final class PromptPresenter implements IPromptPresenter {

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
        eventListener.notifyEventTriggered(PromptViewEvent.PROMPT_SHOWN);
    }

    @Override
    public void setUserOpinion(@NonNull final UserOpinion userOpinion) {
        this.userOpinion = userOpinion;

        switch (userOpinion) {
            case POSITIVE:
                promptView.requestPositiveFeedback();
                eventListener.notifyEventTriggered(PromptViewEvent.USER_INDICATED_POSITIVE_OPINION);
                break;
            case NEGATIVE:
                promptView.requestCriticalFeedback();
                eventListener.notifyEventTriggered(PromptViewEvent.USER_INDICATED_CRITICAL_OPINION);
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
                eventListener.notifyEventTriggered(PromptViewEvent.USER_GAVE_POSITIVE_FEEDBACK);
                eventListener.notifyEventTriggered(PromptViewEvent.USER_GAVE_FEEDBACK);
                break;
            case NEGATIVE:
                eventListener.notifyEventTriggered(PromptViewEvent.USER_GAVE_CRITICAL_FEEDBACK);
                eventListener.notifyEventTriggered(PromptViewEvent.USER_GAVE_FEEDBACK);
                break;
            default:
                throw new IllegalStateException(EXHAUSTIVE_SWITCH_EXCEPTION_MESSAGE);
        }

        if (promptView.providesThanksView()) {
            promptView.thankUser();
            eventListener.notifyEventTriggered(PromptViewEvent.THANKS_SHOWN);
        } else {
            promptView.dismiss();
            eventListener.notifyEventTriggered(PromptViewEvent.PROMPT_DISMISSED);
        }
    }

    @Override
    public void userDeclinedToGiveFeedback() {
        if (userOpinion == null) {
            throw new IllegalStateException(USER_OPINION_EXCEPTION_MESSAGE);
        }

        switch (userOpinion) {
            case POSITIVE:
                eventListener.notifyEventTriggered(PromptViewEvent.USER_DECLINED_POSITIVE_FEEDBACK);
                eventListener.notifyEventTriggered(PromptViewEvent.USER_DECLINED_FEEDBACK);
                break;
            case NEGATIVE:
                eventListener.notifyEventTriggered(PromptViewEvent.USER_DECLINED_CRITICAL_FEEDBACK);
                eventListener.notifyEventTriggered(PromptViewEvent.USER_DECLINED_FEEDBACK);
                break;
            default:
                throw new IllegalStateException(EXHAUSTIVE_SWITCH_EXCEPTION_MESSAGE);
        }

        promptView.dismiss();
        eventListener.notifyEventTriggered(PromptViewEvent.PROMPT_DISMISSED);
    }

}
