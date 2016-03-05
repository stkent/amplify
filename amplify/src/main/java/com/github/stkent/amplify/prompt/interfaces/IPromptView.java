package com.github.stkent.amplify.prompt.interfaces;

import android.support.annotation.NonNull;

public interface IPromptView {

    void setPresenter(@NonNull final IPromptPresenter promptPresenter);
    void queryUserOpinion();
    void requestPositiveFeedback();
    void requestCriticalFeedback();
    void thankUser();
    void dismiss();

}
