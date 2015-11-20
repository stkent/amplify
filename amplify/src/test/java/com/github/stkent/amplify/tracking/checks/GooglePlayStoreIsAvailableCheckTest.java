package com.github.stkent.amplify.tracking.checks;

import com.github.stkent.amplify.tracking.interfaces.IEnvironmentInfoProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class GooglePlayStoreIsAvailableCheckTest {

    private GooglePlayStoreIsAvailableCheck googlePlayStoreIsAvailableCheck;

    @Mock
    private IEnvironmentInfoProvider mockEnvironmentInfoProvider;

    @Before
    public void setUp() {
        initMocks(this);

        googlePlayStoreIsAvailableCheck = new GooglePlayStoreIsAvailableCheck();
    }

    @Test
    public void testThatEnvironmentCheckIsMet_ifGooglePlayStoreIsInstalledOnDevice() {
        // Arrange
       when(mockEnvironmentInfoProvider.isGooglePlayStoreInstalled()).thenReturn(true);

        // Act
        final boolean isEnvironmentCheckMet =
                googlePlayStoreIsAvailableCheck.isMet(mockEnvironmentInfoProvider);

        // Assert
        assertTrue("Environment check should be met", isEnvironmentCheckMet);
    }

    @Test
    public void testThatEnvironmentCheckIsNotMet_ifGooglePlayStoreIsNotInstalledOnDevice() {
        // Arrange
        when(mockEnvironmentInfoProvider.isGooglePlayStoreInstalled()).thenReturn(false);

        // Act
        final boolean isEnvironmentCheckMet =
                googlePlayStoreIsAvailableCheck.isMet(mockEnvironmentInfoProvider);

        // Assert
        assertFalse("Environment check should not be met", isEnvironmentCheckMet);
    }

}
