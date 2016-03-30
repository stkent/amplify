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

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.github.stkent.amplify.R;
import com.github.stkent.amplify.prompt.interfaces.IPromptPresenter;
import com.github.stkent.amplify.prompt.interfaces.IPromptView;
import com.github.stkent.amplify.prompt.interfaces.IQuestionPresenter;
import com.github.stkent.amplify.prompt.interfaces.IQuestionView;
import com.github.stkent.amplify.prompt.interfaces.IThanksView;
import com.github.stkent.amplify.tracking.Amplify;
import com.github.stkent.amplify.tracking.PromptViewEvent;
import com.github.stkent.amplify.tracking.interfaces.IEventListener;

import static com.github.stkent.amplify.prompt.interfaces.IPromptPresenter.UserFeedbackAction.AGREED;
import static com.github.stkent.amplify.prompt.interfaces.IPromptPresenter.UserFeedbackAction.DECLINED;
import static com.github.stkent.amplify.prompt.interfaces.IPromptPresenter.UserOpinion.CRITICAL;
import static com.github.stkent.amplify.prompt.interfaces.IPromptPresenter.UserOpinion.POSITIVE;

abstract class BasePromptView<T extends View & IQuestionView, U extends View & IThanksView>
        extends FrameLayout implements IPromptView {

    protected abstract boolean isConfigured();

    @NonNull
    protected abstract T getQuestionView();

    @Nullable
    protected abstract U getThanksView();

    private final IQuestionPresenter userOpinionQuestionPresenter =
            new IQuestionPresenter() {
                @Override
                public void userRespondedPositively() {
                    promptPresenter.reportUserOpinion(POSITIVE);
                }

                @Override
                public void userRespondedNegatively() {
                    promptPresenter.reportUserOpinion(CRITICAL);
                }
            };

    private final IQuestionPresenter feedbackQuestionPresenter =
            new IQuestionPresenter() {
                @Override
                public void userRespondedPositively() {
                    promptPresenter.reportUserFeedbackAction(AGREED);
                }

                @Override
                public void userRespondedNegatively() {
                    promptPresenter.reportUserFeedbackAction(DECLINED);
                }
            };

    private IPromptPresenter promptPresenter;
    private BasePromptViewConfig basePromptViewConfig;
    private T displayedQuestionView;
    private boolean displayed;

    BasePromptView(final Context context) {
        this(context, null);
    }

    BasePromptView(final Context context, @Nullable final AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    BasePromptView(
            final Context context,
            @Nullable final AttributeSet attributeSet,
            final int defStyleAttr) {

        super(context, attributeSet, defStyleAttr);
        setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        initializeBaseConfig(attributeSet);

        promptPresenter = new PromptPresenter(Amplify.getSharedInstance(), this);
    }

    @CallSuper
    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        final SavedState savedState = new SavedState(superState);
        savedState.promptPresenterState = promptPresenter.generateStateBundle();
        return savedState;
    }

    @CallSuper
    @Override
    protected void onRestoreInstanceState(@NonNull final Parcelable state) {
        final SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
    }

    @NonNull
    @Override
    public final IPromptPresenter getPresenter() {
        return promptPresenter;
    }

    @Override
    public final void queryUserOpinion(final boolean triggeredByConfigChange) {
        if (!isConfigured()) {
            throw new IllegalStateException("PromptView is not fully configured.");
        }

        if (!triggeredByConfigChange) {
            promptPresenter.notifyEventTriggered(PromptViewEvent.PROMPT_SHOWN);
        }

        displayQuestionViewIfNeeded();
        displayedQuestionView.setPresenter(userOpinionQuestionPresenter);
        displayedQuestionView.bind(basePromptViewConfig.getUserOpinionQuestion());
        setDisplayed(true);
    }

    @Override
    public final void requestPositiveFeedback() {
        displayQuestionViewIfNeeded();
        displayedQuestionView.setPresenter(feedbackQuestionPresenter);
        displayedQuestionView.bind(basePromptViewConfig.getPositiveFeedbackQuestion());
        setDisplayed(true);
    }

    @Override
    public final void requestCriticalFeedback() {
        displayQuestionViewIfNeeded();
        displayedQuestionView.setPresenter(feedbackQuestionPresenter);
        displayedQuestionView.bind(basePromptViewConfig.getCriticalFeedbackQuestion());
        setDisplayed(true);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public final void thankUser(final boolean triggeredByConfigChange) {
        if (!triggeredByConfigChange) {
            promptPresenter.notifyEventTriggered(PromptViewEvent.THANKS_SHOWN);
        }

        final U thanksView = getThanksView();
        thanksView.bind(basePromptViewConfig.getThanks());

        clearDisplayedQuestionViewReference();
        setDisplayedView(thanksView);
        setDisplayed(true);
    }

    @Override
    public final void dismiss(final boolean triggeredByConfigChange) {
        if (!triggeredByConfigChange) {
            promptPresenter.notifyEventTriggered(PromptViewEvent.PROMPT_DISMISSED);
        }

        clearDisplayedQuestionViewReference();
        setVisibility(GONE);
        setDisplayed(false);
    }

    @Override
    public final boolean providesThanksView() {
        return getThanksView() != null;
    }

    public final void applyBaseConfig(@NonNull final BasePromptViewConfig basePromptViewConfig) {
        if (displayed) {
            throw new IllegalStateException(
                    "Configuration cannot be changed after the prompt is first displayed.");
        }

        this.basePromptViewConfig = basePromptViewConfig;
    }

    public final void addPromptEventListener(@NonNull final IEventListener promptEventListener) {
        promptPresenter.addPromptEventListener(promptEventListener);
    }

    /**
     * This method must be called by subclasses at the end of their onRestoreInstanceState
     * implementations. This is to allow all configuration to be restored before the prompt
     * presenter triggers a change in state, and is required because configuration changes are not
     * allowed after a BasePromptView subclass is displayed.
     */
    protected final void restorePresenterState(@NonNull final Parcelable state) {
        promptPresenter.restoreStateFromBundle(((SavedState) state).promptPresenterState);
    }

    protected final boolean isDisplayed() {
        return displayed;
    }

    /**
     * Note: <code>Theme.obtainStyledAttributes</code> accepts a null <code>AttributeSet</code>; see
     * documentation of that method for confirmation.
     */
    private void initializeBaseConfig(@Nullable final AttributeSet attributeSet) {
        final TypedArray typedArray = getContext().getTheme()
                .obtainStyledAttributes(attributeSet, R.styleable.BasePromptView, 0, 0);

        basePromptViewConfig = new BasePromptViewConfig(typedArray);

        typedArray.recycle();
    }

    private void setDisplayedView(@NonNull final View view) {
        removeAllViews();

        addView(view, new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private void displayQuestionViewIfNeeded() {
        if (displayedQuestionView == null) {
            final T questionViewToDisplay = getQuestionView();
            displayedQuestionView = questionViewToDisplay;
            setDisplayedView(questionViewToDisplay);
        }
    }

    private void clearDisplayedQuestionViewReference() {
        displayedQuestionView = null;
    }

    private void setDisplayed(final boolean displayed) {
        this.displayed = displayed;
    }

    private static class SavedState extends BaseSavedState {

        private Bundle promptPresenterState;

        protected SavedState(final Parcelable superState) {
            super(superState);
        }

        protected SavedState(final Parcel in) {
            super(in);
            promptPresenterState = in.readBundle(getClass().getClassLoader());
        }

        @Override
        public void writeToParcel(final Parcel out, final int flags) {
            super.writeToParcel(out, flags);
            out.writeBundle(promptPresenterState);
        }

        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {

            public SavedState createFromParcel(final Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(final int size) {
                return new SavedState[size];
            }

        };
    }

}
