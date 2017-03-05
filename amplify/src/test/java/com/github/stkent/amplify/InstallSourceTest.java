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
package com.github.stkent.amplify;

import com.github.stkent.amplify.InstallSource.AmazonAppStoreInstallSource;
import com.github.stkent.amplify.InstallSource.AmazonUndergroundInstallSource;
import com.github.stkent.amplify.InstallSource.GooglePlayStoreInstallSource;
import com.github.stkent.amplify.InstallSource.PackageInstallerInstallSource;
import com.github.stkent.amplify.InstallSource.UnknownInstallSource;
import com.github.stkent.amplify.InstallSource.UnrecognizedInstallSource;
import com.github.stkent.amplify.helpers.BaseTest;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InstallSourceTest extends BaseTest {

    @Test
    public void testGooglePlayStoreInstallSourceIsParsedCorrectly() {
        // Act
        final InstallSource installSource = InstallSource.fromInstallerPackageName("com.android.vending");

        // Assert
        assertTrue(installSource instanceof GooglePlayStoreInstallSource);
        assertEquals("Google Play Store", installSource.toString());
    }

    @Test
    public void testAmazonAppStoreInstallSourceIsParsedCorrectly() {
        // Act
        InstallSource installSource = InstallSource.fromInstallerPackageName("com.amazon.venezia");

        // Assert
        assertTrue(installSource instanceof AmazonAppStoreInstallSource);
        assertEquals("Amazon Appstore", installSource.toString());
    }

    @Test
    public void testAmazonUndergroundInstallSourceIsParsedCorrectly() {
        // Act
        final InstallSource installSource = InstallSource.fromInstallerPackageName("com.amazon.mshop.android");

        // Assert
        assertTrue(installSource instanceof AmazonUndergroundInstallSource);
        assertEquals("Amazon Underground", installSource.toString());
    }

    @Test
    public void testPackageInstallerInstallSourceIsParsedCorrectly() {
        // Act
        final InstallSource installSource = InstallSource.fromInstallerPackageName("com.google.android.packageinstaller");

        // Assert
        assertTrue(installSource instanceof PackageInstallerInstallSource);
        assertEquals("Package Installer", installSource.toString());
    }

    @Test
    public void testUnrecognizedInstallSourceIsParsedCorrectly() {
        // Arrange
        final String unrecognizedInstallerPackageName = "com.unrecognized";

        // Act
        final InstallSource installSource = InstallSource.fromInstallerPackageName(unrecognizedInstallerPackageName);

        // Assert
        assertTrue(installSource instanceof UnrecognizedInstallSource);
        assertEquals(unrecognizedInstallerPackageName, installSource.toString());
    }


    @Test
    public void testUnknownInstallSourceIsParsedCorrectly() {
        // Act
        final InstallSource installSource = InstallSource.fromInstallerPackageName(null);

        // Assert
        assertTrue(installSource instanceof UnknownInstallSource);
        assertEquals("Unknown", installSource.toString());
    }

}
