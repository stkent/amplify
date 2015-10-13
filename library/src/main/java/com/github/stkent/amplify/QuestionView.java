package com.github.stkent.amplify;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

public interface QuestionView {

    @NonNull
    View getView();

    @NonNull
    Button getPositiveButton();

    @NonNull
    Button getNegativeButton();

    void setQuestion(@NonNull final Question question);

}
