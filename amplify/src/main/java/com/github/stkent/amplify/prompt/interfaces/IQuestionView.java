package com.github.stkent.amplify.prompt.interfaces;

import android.support.annotation.NonNull;

public interface IQuestionView {

    void setPresenter(@NonNull final IQuestionPresenter questionPresenter);
    void bind(@NonNull final IQuestion question);

}
