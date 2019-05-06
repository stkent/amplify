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

import android.support.annotation.NonNull;

public interface IPromptView {

    @NonNull
    IPromptPresenter getPresenter();

    void queryUserOpinion(boolean triggeredByConfigChange);
    void requestPositiveFeedback();
    void requestCriticalFeedback();
    void dismiss(boolean triggeredByConfigChange);

    boolean providesThanksView();

    /**
     * <code>IPromptPresenter</code> instances should only call this method if
     * <code>providesThanksView</code> returns <code>true</code>.
     */
    void thankUser(boolean triggeredByConfigChange);

}
