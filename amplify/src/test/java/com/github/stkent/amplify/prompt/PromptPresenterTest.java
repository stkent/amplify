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
