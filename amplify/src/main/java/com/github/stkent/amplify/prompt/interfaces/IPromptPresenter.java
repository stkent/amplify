package com.github.stkent.amplify.prompt.interfaces;

import android.support.annotation.NonNull;

public interface IPromptPresenter {

    enum UserOpinion {
        POSITIVE,
        NEGATIVE
    }

    void start();
    void setUserOpinion(@NonNull final UserOpinion userOpinion);
    void userAgreedToGiveFeedback();
    void userDeclinedToGiveFeedback();

}
