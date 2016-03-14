package com.github.stkent.amplify.prompt;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.FrameLayout;

import com.github.stkent.amplify.prompt.interfaces.IThanks;
import com.github.stkent.amplify.prompt.interfaces.IThanksView;

public class DefaultThanksView extends FrameLayout implements IThanksView {

    public DefaultThanksView(final Context context) {
        super(context);
    }

    @Override
    public void bind(@NonNull final IThanks thanks) {

    }

}
