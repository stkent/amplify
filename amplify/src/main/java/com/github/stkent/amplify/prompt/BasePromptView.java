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

    private T displayedQuestionView;

    private boolean displayed;

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

    @NonNull
    @Override
    public IPromptPresenter getPresenter() {
        return promptPresenter;
    }

    @Override
    public final void queryUserOpinion() {
        if (!isConfigured()) {
            throw new IllegalStateException("PromptView is not fully configured.");
        }

        final T userOpinionQuestionView = getQuestionView();
        userOpinionQuestionView.setPresenter(userOpinionQuestionPresenter);
        userOpinionQuestionView.bind(basePromptViewConfig.getUserOpinionQuestion());

        setContentView(userOpinionQuestionView);

        displayedQuestionView = userOpinionQuestionView;

        displayed = true;
    }

    @Override
    public final void requestPositiveFeedback() {
        displayedQuestionView.setPresenter(feedbackQuestionPresenter);
        displayedQuestionView.bind(basePromptViewConfig.getPositiveFeedbackQuestion());
    }

    @Override
    public final void requestCriticalFeedback() {
        final T criticalFeedbackQuestionView = getQuestionView();
        criticalFeedbackQuestionView.setPresenter(feedbackQuestionPresenter);
        criticalFeedbackQuestionView.bind(basePromptViewConfig.getCriticalFeedbackQuestion());

        setContentView(criticalFeedbackQuestionView);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public final void thankUser() {
        final U thanksView = getThanksView();
        thanksView.bind(basePromptViewConfig.getThanks());

        setContentView(thanksView);
    }

    @Override
    public final void dismiss() {
        setVisibility(GONE);
    }

    @Override
    public boolean providesThanksView() {
        return getThanksView() != null;
    }

    public void applyBaseConfig(@NonNull final BasePromptViewConfig basePromptViewConfig) {
        this.basePromptViewConfig = basePromptViewConfig;
    }

    protected boolean isDisplayed() {
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

    private void setContentView(@NonNull final View view) {
        removeAllViews();
        addView(view, new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        final AugmentedSavedState augmentedSavedState = new AugmentedSavedState(superState);
        augmentedSavedState.setAugmentedState(promptPresenter.saveStateToBundle());
        return augmentedSavedState;
    }

    @Override
    protected void onRestoreInstanceState(final Parcelable state) {
        final AugmentedSavedState augmentedSavedState = (AugmentedSavedState) state;
        super.onRestoreInstanceState(augmentedSavedState.getSuperState());
        promptPresenter.restoreStateFromBundle(augmentedSavedState.getAugmentedState());
    }

    private static class AugmentedSavedState extends BaseSavedState {

        private Bundle augmentedState;

        protected AugmentedSavedState(final Parcelable superState) {
            super(superState);
        }

        protected AugmentedSavedState(final Parcel in) {
            super(in);
            augmentedState = in.readBundle(getClass().getClassLoader());
        }

        private Bundle getAugmentedState() {
            return augmentedState;
        }

        private void setAugmentedState(final Bundle augmentedState) {
            this.augmentedState = augmentedState;
        }

        @Override
        public void writeToParcel(final Parcel out, final int flags) {
            super.writeToParcel(out, flags);
            out.writeBundle(augmentedState);
        }

        public static final Parcelable.Creator<AugmentedSavedState> CREATOR
                = new Parcelable.Creator<AugmentedSavedState>() {

            public AugmentedSavedState createFromParcel(final Parcel in) {
                return new AugmentedSavedState(in);
            }

            public AugmentedSavedState[] newArray(final int size) {
                return new AugmentedSavedState[size];
            }

        };
    }

}
