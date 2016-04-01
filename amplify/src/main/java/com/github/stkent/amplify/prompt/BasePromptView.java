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

import android.animation.Animator;
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
    private boolean thanksDisplayTimeExpired;

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
        savedState.thanksDisplayTimeExpired = thanksDisplayTimeExpired;
        return savedState;
    }

    @CallSuper
    @Override
    protected void onRestoreInstanceState(@NonNull final Parcelable state) {
        final SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        thanksDisplayTimeExpired = savedState.thanksDisplayTimeExpired;
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
    }

    @Override
    public final void requestPositiveFeedback() {
        displayQuestionViewIfNeeded();
        displayedQuestionView.setPresenter(feedbackQuestionPresenter);
        displayedQuestionView.bind(basePromptViewConfig.getPositiveFeedbackQuestion());
    }

    @Override
    public final void requestCriticalFeedback() {
        displayQuestionViewIfNeeded();
        displayedQuestionView.setPresenter(feedbackQuestionPresenter);
        displayedQuestionView.bind(basePromptViewConfig.getCriticalFeedbackQuestion());
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public final void thankUser(final boolean triggeredByConfigChange) {
        if (!triggeredByConfigChange) {
            promptPresenter.notifyEventTriggered(PromptViewEvent.THANKS_SHOWN);
        }

        clearDisplayedQuestionViewReference();

        if (!thanksDisplayTimeExpired) {
            final U thanksView = getThanksView();
            thanksView.bind(basePromptViewConfig.getThanks());

            setDisplayedView(thanksView);

            final Long thanksDisplayTimeMs = basePromptViewConfig.getThanksDisplayTimeMs();

            if (thanksDisplayTimeMs != null) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        thanksDisplayTimeExpired = true;

                        final int fadeDurationMs = getResources()
                                .getInteger(android.R.integer.config_mediumAnimTime);

                        thanksView
                                .animate()
                                .setDuration(fadeDurationMs)
                                .alpha(0.0f)
                                .setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(final Animator animation) {
                                        // This method intentionally left blank
                                    }

                                    @Override
                                    public void onAnimationEnd(final Animator animation) {
                                        removeAllViews();
                                    }

                                    @Override
                                    public void onAnimationCancel(final Animator animation) {
                                        // This method intentionally left blank
                                    }

                                    @Override
                                    public void onAnimationRepeat(final Animator animation) {
                                        // This method intentionally left blank
                                    }
                                })
                                .start();
                    }
                }, thanksDisplayTimeMs);
            }
        } else {
            removeAllViews();
        }
    }

    @Override
    public final void dismiss(final boolean triggeredByConfigChange) {
        if (!triggeredByConfigChange) {
            promptPresenter.notifyEventTriggered(PromptViewEvent.PROMPT_DISMISSED);
        }

        clearDisplayedQuestionViewReference();
        removeAllViews();
    }

    @Override
    public final boolean providesThanksView() {
        return getThanksView() != null;
    }

    public final void applyBaseConfig(@NonNull final BasePromptViewConfig basePromptViewConfig) {
        if (isDisplayed()) {
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
        return getChildCount() > 0;
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

    private static class SavedState extends BaseSavedState {

        private static final int TRUTHY_INT = 1;
        private static final int FALSEY_INT = 0;

        private Bundle promptPresenterState;
        private boolean thanksDisplayTimeExpired;

        protected SavedState(final Parcelable superState) {
            super(superState);
        }

        protected SavedState(final Parcel in) {
            super(in);
            this.promptPresenterState = in.readBundle(getClass().getClassLoader());
            this.thanksDisplayTimeExpired = in.readInt() == TRUTHY_INT;
        }

        @Override
        public void writeToParcel(final Parcel out, final int flags) {
            super.writeToParcel(out, flags);
            out.writeBundle(this.promptPresenterState);
            out.writeInt(this.thanksDisplayTimeExpired ? TRUTHY_INT : FALSEY_INT);
        }

        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {

            @Override
            public SavedState createFromParcel(final Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(final int size) {
                return new SavedState[size];
            }

        };
    }

}
