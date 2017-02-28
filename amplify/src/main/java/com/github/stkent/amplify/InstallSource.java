package com.github.stkent.amplify;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.github.stkent.amplify.utils.Constants.AMAZON_APP_STORE_PACKAGE_NAME;
import static com.github.stkent.amplify.utils.Constants.AMAZON_UNDERGROUND_PACKAGE_NAME;
import static com.github.stkent.amplify.utils.Constants.GOOGLE_PLAY_STORE_PACKAGE_NAME;
import static com.github.stkent.amplify.utils.Constants.PACKAGE_INSTALLER_PACKAGE_NAME;

/*
 * An attempt to make an Algebraic data type in Java (https://en.wikipedia.org/wiki/Algebraic_data_type). See also
 * https://grundlefleck.github.io/2014/07/17/sealing-algebraic-data-types-in-java.html
 */
public abstract class InstallSource {

    @NonNull
    /* default */ static InstallSource fromInstallerPackageName(@Nullable final String installerPackageName) {
        if (GOOGLE_PLAY_STORE_PACKAGE_NAME.equalsIgnoreCase(installerPackageName)) {
            return new GooglePlayStoreInstallSource();
        } else if (AMAZON_APP_STORE_PACKAGE_NAME.equalsIgnoreCase(installerPackageName)) {
            return new AmazonAppStoreInstallSource();
        } else if (AMAZON_UNDERGROUND_PACKAGE_NAME.equalsIgnoreCase(installerPackageName)) {
            return new AmazonUndergroundInstallSource();
        } else if (PACKAGE_INSTALLER_PACKAGE_NAME.equalsIgnoreCase(installerPackageName)) {
            return new PackageInstallerInstallSource();
        } else if (installerPackageName != null) {
            return new UnrecognizedInstallSource(installerPackageName);
        } else {
            return new UnknownInstallSource();
        }
    }

    private InstallSource() {
        // This constructor intentionally left blank.
    }

    public static final class GooglePlayStoreInstallSource extends InstallSource {
        @Override
        public String toString() {
            return "Google Play Store";
        }
    }

    public static final class AmazonAppStoreInstallSource extends InstallSource {
        @Override
        public String toString() {
            return "Amazon Appstore";
        }
    }

    public static final class AmazonUndergroundInstallSource extends InstallSource {
        @Override
        public String toString() {
            return "Amazon Underground";
        }
    }

    public static final class PackageInstallerInstallSource extends InstallSource {
        @Override
        public String toString() {
            return "Package Installer";
        }
    }

    public static final class UnknownInstallSource extends InstallSource {
        @Override
        public String toString() {
            return "Unknown";
        }
    }

    public static final class UnrecognizedInstallSource extends InstallSource {

        @NonNull
        private final String installerPackageName;

        private UnrecognizedInstallSource(@NonNull final String installerPackageName) {
            this.installerPackageName = installerPackageName;
        }

        @Override
        public String toString() {
            return installerPackageName;
        }

    }

}
