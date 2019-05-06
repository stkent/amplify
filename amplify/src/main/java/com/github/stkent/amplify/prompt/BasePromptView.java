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

import android.animation.Animator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
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

@SuppressWarnings({"PMD.TooManyMethods"})
abstract class BasePromptView<T extends View & IQuestionView, U extends View & IThanksView>
        extends FrameLayout implements IPromptView {

    protected static final String SUPER_STATE_KEY = "SUPER_STATE_KEY";
    private static final String THANKS_DISPLAY_TIME_EXPIRED_KEY = "THANKS_DISPLAY_TIME_EXPIRED_KEY";
    private static final String BASE_PROMPT_VIEW_CONFIG_KEY = "BASE_PROMPT_VIEW_CONFIG_KEY";
    private static final String PROMPT_PRESENTER_STATE_BUNDLE_KEY
            = "PROMPT_PRESENTER_STATE_BUNDLE_KEY";

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

    /* default */ BasePromptView(final Context context) {
        this(context, null);
    }

    /* default */ BasePromptView(final Context context, @Nullable final AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    /* default */ BasePromptView(final Context context, @Nullable final AttributeSet attributeSet, final int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        initializeBaseConfig(attributeSet);

        if (!isInEditMode()) {
            promptPresenter = new PromptPresenter(Amplify.getSharedInstance(), this);
        }
    }

    @CallSuper
    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();

        final Bundle result = new Bundle();
        result.putParcelable(SUPER_STATE_KEY, superState);
        result.putParcelable(BASE_PROMPT_VIEW_CONFIG_KEY, basePromptViewConfig);
        result.putBoolean(THANKS_DISPLAY_TIME_EXPIRED_KEY, thanksDisplayTimeExpired);
        result.putBundle(PROMPT_PRESENTER_STATE_BUNDLE_KEY, promptPresenter.generateStateBundle());
        return result;
    }

    @CallSuper
    @Override
    protected void onRestoreInstanceState(@Nullable final Parcelable state) {
        final Bundle savedState = (Bundle) state;

        if (savedState != null) {
            final Parcelable superSavedState = savedState.getParcelable(SUPER_STATE_KEY);
            super.onRestoreInstanceState(superSavedState);

            final BasePromptViewConfig config = savedState.getParcelable(BASE_PROMPT_VIEW_CONFIG_KEY);

            if (config != null) {
                basePromptViewConfig = config;
            }

            thanksDisplayTimeExpired = savedState.getBoolean(THANKS_DISPLAY_TIME_EXPIRED_KEY);
        }
    }

    @Override
    protected void dispatchSaveInstanceState(final SparseArray<Parcelable> container) {
        super.dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(final SparseArray<Parcelable> container) {
        super.dispatchThawSelfOnly(container);
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
                                        hide();

                                        promptPresenter.notifyEventTriggered(
                                                PromptViewEvent.PROMPT_DISMISSED);
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
            hide();
        }
    }

    @Override
    public final void dismiss(final boolean triggeredByConfigChange) {
        if (!triggeredByConfigChange) {
            promptPresenter.notifyEventTriggered(PromptViewEvent.PROMPT_DISMISSED);
        }

        clearDisplayedQuestionViewReference();
        hide();
    }

    @Override
    public final boolean providesThanksView() {
        return getThanksView() != null;
    }

    public final void applyBaseConfig(@NonNull final BasePromptViewConfig basePromptViewConfig) {
        if (isDisplayed()) {
            throw new IllegalStateException("Configuration cannot be changed after the prompt is first displayed.");
        }

        this.basePromptViewConfig = basePromptViewConfig;
    }

    public final void addPromptEventListener(@NonNull final IEventListener promptEventListener) {
        promptPresenter.addPromptEventListener(promptEventListener);
    }

    /**
     * This method must be called by subclasses at the end of their onRestoreInstanceState
     * implementations. This is to allow all configuration to be restored before the prompt
     * presenter triggers a change in state, and is required because config changes are not allowed
     * after a BasePromptView subclass is displayed.
     */
    protected final void restorePresenterState(@Nullable final Parcelable state) {
        final Bundle bundle = (Bundle) state;

        if (bundle != null) {
            final Bundle promptPresenterState = bundle.getBundle(PROMPT_PRESENTER_STATE_BUNDLE_KEY);

            if (promptPresenterState != null) {
                promptPresenter.restoreStateFromBundle(promptPresenterState);
            }
        }
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

        setVisibility(VISIBLE);
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

    private void hide() {
        removeAllViews();
        setVisibility(GONE);
    }

}
