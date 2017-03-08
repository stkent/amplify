/*
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

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.prompt.interfaces.IPromptPresenter;
import com.github.stkent.amplify.prompt.interfaces.IPromptView;
import com.github.stkent.amplify.tracking.PromptInteractionEvent;
import com.github.stkent.amplify.tracking.interfaces.IEvent;
import com.github.stkent.amplify.tracking.interfaces.IEventListener;

import java.util.ArrayList;
import java.util.List;

public final class PromptPresenter implements IPromptPresenter {

    private static final String PROMPT_FLOW_STATE_KEY = "PromptFlowStateKey";
    private static final PromptFlowState DEFAULT_PROMPT_FLOW_STATE = PromptFlowState.INITIALIZED;

    @NonNull
    private final IEventListener eventListener;

    @NonNull
    private final IPromptView promptView;

    @NonNull
    private PromptFlowState promptFlowState = DEFAULT_PROMPT_FLOW_STATE;

    @NonNull
    private final List<IEventListener> extraEventListeners = new ArrayList<>();

    public PromptPresenter(
            @NonNull final IEventListener eventListener,
            @NonNull final IPromptView promptView) {

        this.eventListener = eventListener;
        this.promptView = promptView;
    }

    @Override
    public void notifyEventTriggered(@NonNull final IEvent event) {
        eventListener.notifyEventTriggered(event);

        for (final IEventListener extraEventListener: extraEventListeners) {
            extraEventListener.notifyEventTriggered(event);
        }
    }

    @Override
    public void addPromptEventListener(@NonNull final IEventListener promptEventListener) {
        extraEventListeners.add(promptEventListener);
    }

    @Override
    public void start() {
        setToState(PromptFlowState.QUERYING_USER_OPINION);
    }

    @Override
    public void reportUserOpinion(@NonNull final UserOpinion userOpinion) {
        if (userOpinion == UserOpinion.POSITIVE) {
            notifyEventTriggered(PromptInteractionEvent.USER_INDICATED_POSITIVE_OPINION);
            setToState(PromptFlowState.REQUESTING_POSITIVE_FEEDBACK);
        } else if (userOpinion == UserOpinion.CRITICAL) {
            notifyEventTriggered(PromptInteractionEvent.USER_INDICATED_CRITICAL_OPINION);
            setToState(PromptFlowState.REQUESTING_CRITICAL_FEEDBACK);
        }
    }

    @Override
    public void reportUserFeedbackAction(@NonNull final UserFeedbackAction userFeedbackAction) {
        if (promptFlowState != PromptFlowState.REQUESTING_POSITIVE_FEEDBACK
                && promptFlowState != PromptFlowState.REQUESTING_CRITICAL_FEEDBACK) {

            throw new IllegalStateException(
                    "User opinion must be set before this method is called.");
        }

        if (userFeedbackAction == UserFeedbackAction.AGREED) {
            handleUserAgreedToGiveFeedback();
        } else if (userFeedbackAction == UserFeedbackAction.DECLINED) {
            handleUserDeclinedToGiveFeedback();
        }
    }

    @Override
    public void restoreStateFromBundle(@NonNull final Bundle bundle) {
        final PromptFlowState promptFlowState =
                PromptFlowState.values()[
                        bundle.getInt(
                                PROMPT_FLOW_STATE_KEY,
                                DEFAULT_PROMPT_FLOW_STATE.ordinal())];

        setToState(promptFlowState, true);
    }

    @NonNull
    @Override
    public Bundle generateStateBundle() {
        final Bundle result = new Bundle();
        result.putInt(PROMPT_FLOW_STATE_KEY, promptFlowState.ordinal());
        return result;
    }

    private void handleUserAgreedToGiveFeedback() {
        notifyEventTriggered(PromptInteractionEvent.USER_GAVE_FEEDBACK);

        if (promptFlowState == PromptFlowState.REQUESTING_POSITIVE_FEEDBACK) {
            notifyEventTriggered(PromptInteractionEvent.USER_GAVE_POSITIVE_FEEDBACK);
        } else if (promptFlowState == PromptFlowState.REQUESTING_CRITICAL_FEEDBACK) {
            notifyEventTriggered(PromptInteractionEvent.USER_GAVE_CRITICAL_FEEDBACK);
        }

        if (promptView.providesThanksView()) {
            setToState(PromptFlowState.THANKING_USER);
        } else {
            setToState(PromptFlowState.DISMISSED);
        }
    }

    private void handleUserDeclinedToGiveFeedback() {
        notifyEventTriggered(PromptInteractionEvent.USER_DECLINED_FEEDBACK);

        if (promptFlowState == PromptFlowState.REQUESTING_POSITIVE_FEEDBACK) {
            notifyEventTriggered(PromptInteractionEvent.USER_DECLINED_POSITIVE_FEEDBACK);
        } else if (promptFlowState == PromptFlowState.REQUESTING_CRITICAL_FEEDBACK) {
            notifyEventTriggered(PromptInteractionEvent.USER_DECLINED_CRITICAL_FEEDBACK);
        }

        setToState(PromptFlowState.DISMISSED);
    }

    private void setToState(@NonNull final PromptFlowState promptFlowState) {
        setToState(promptFlowState, false);
    }

    private void setToState(
            @NonNull final PromptFlowState promptFlowState,
            final boolean triggeredByConfigChange) {

        this.promptFlowState = promptFlowState;

        switch (promptFlowState) {
            case QUERYING_USER_OPINION:
                promptView.queryUserOpinion(triggeredByConfigChange);
                break;
            case REQUESTING_POSITIVE_FEEDBACK:
                promptView.requestPositiveFeedback();
                break;
            case REQUESTING_CRITICAL_FEEDBACK:
                promptView.requestCriticalFeedback();
                break;
            case THANKING_USER:
                promptView.thankUser(triggeredByConfigChange);
                break;
            case DISMISSED:
                promptView.dismiss(triggeredByConfigChange);
                break;
            default:
                break;
        }
    }

}
