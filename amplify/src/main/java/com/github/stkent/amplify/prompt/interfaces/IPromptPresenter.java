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
package com.github.stkent.amplify.prompt.interfaces;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.interfaces.IEventListener;

public interface IPromptPresenter extends IEventListener {

    enum UserOpinion {
        POSITIVE,
        CRITICAL
    }

    enum UserFeedbackAction {
        AGREED,
        DECLINED
    }

    enum PromptFlowState {
        INITIALIZED,
        QUERYING_USER_OPINION,
        REQUESTING_POSITIVE_FEEDBACK,
        REQUESTING_CRITICAL_FEEDBACK,
        THANKING_USER,
        DISMISSED
    }

    void addPromptEventListener(@NonNull IEventListener promptEventListener);

    void start();
    void reportUserOpinion(@NonNull UserOpinion userOpinion);
    void reportUserFeedbackAction(@NonNull UserFeedbackAction userFeedbackAction);

    @NonNull
    Bundle generateStateBundle();

    void restoreStateFromBundle(@NonNull Bundle bundle);

}
