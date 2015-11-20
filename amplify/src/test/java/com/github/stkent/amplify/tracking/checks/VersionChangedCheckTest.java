package com.github.stkent.amplify.tracking.checks;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;

import com.github.stkent.amplify.tracking.interfaces.IApplicationInfoProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class VersionChangedCheckTest {

    private VersionChangedCheck versionChangedCheck;

    @Mock
    private IApplicationInfoProvider mockApplicationInfoProvider;

    @Before
    public void setUp() {
        initMocks(this);

        versionChangedCheck = new VersionChangedCheck();
    }

    @Test
    public void testThatCheckBlocksPrompt_ifAppVersionHasNotChanged() throws PackageManager.NameNotFoundException {
        // Arrange
        final String fakeVersionName = "any string";

        when(mockApplicationInfoProvider.getVersionName()).thenReturn(fakeVersionName);

        // Act
        final boolean shouldBlockFeedbackPrompt = versionChangedCheck.shouldBlockFeedbackPrompt(
                fakeVersionName, mockApplicationInfoProvider);

        // Assert
        assertTrue("Feedback prompt should be blocked if the app version has not changed",
                shouldBlockFeedbackPrompt);
    }

    @SuppressLint("Assert")
    @SuppressWarnings("ConstantConditions")
    public void testThatCheckDoesNotBlockPrompt_ifAppVersionHasChanged() throws PackageManager.NameNotFoundException {
        // Arrange
        final String fakeCachedVersionName = "any string";
        final String fakeCurrentVersionName = "any other string";

        assert !fakeCachedVersionName.equals(fakeCurrentVersionName);

        when(mockApplicationInfoProvider.getVersionName()).thenReturn(fakeCurrentVersionName);

        // Act
        final boolean shouldBlockFeedbackPrompt = versionChangedCheck.shouldBlockFeedbackPrompt(
                fakeCachedVersionName, mockApplicationInfoProvider);

        // Assert
        assertFalse("Feedback prompt should not be blocked if the app version has changed",
                shouldBlockFeedbackPrompt);
    }

}
