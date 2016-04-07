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

import com.github.stkent.amplify.helpers.BaseTest;
import com.github.stkent.amplify.prompt.interfaces.IPromptPresenter;
import com.github.stkent.amplify.prompt.interfaces.IPromptView;
import com.github.stkent.amplify.tracking.interfaces.IEventListener;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;

public class PromptPresenterTest extends BaseTest {

    private PromptPresenter promptPresenter;

    @Mock
    private IEventListener mockEventListener;

    @Mock
    private IPromptView mockPromptView;

    @Override
    public void localSetUp() {
        promptPresenter = new PromptPresenter(mockEventListener, mockPromptView);
    }

    @Test
    public void testThatPresenterInstructsPromptViewToQueryUserOpinionWhenStarted() {
        // Act
        promptPresenter.start();

        // Assert
        verify(mockPromptView).queryUserOpinion(anyBoolean());
    }

    @Test
    public void testThatPresenterInstructsPromptViewToRequestPositiveFeedbackWhenUserReportsPositiveOpinion() {
        // Act
        promptPresenter.reportUserOpinion(IPromptPresenter.UserOpinion.POSITIVE);

        // Assert
        final InOrder inOrder = inOrder(mockPromptView);
        inOrder.verify(mockPromptView).requestPositiveFeedback();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testThatPresenterInstructsPromptViewToRequestCriticalFeedbackWhenUserReportsCriticalOpinion() {
        // Act
        promptPresenter.reportUserOpinion(IPromptPresenter.UserOpinion.CRITICAL);

        // Assert
        final InOrder inOrder = inOrder(mockPromptView);
        inOrder.verify(mockPromptView).requestCriticalFeedback();
        inOrder.verifyNoMoreInteractions();
    }

}
