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
package com.github.stkent.amplify.utils;

@SuppressWarnings("PMD.ClassNamingConventions")
public final class Constants {

    /**
     * Package name for the Amazon App Store.
     */
    public static final String AMAZON_APP_STORE_PACKAGE_NAME = "com.amazon.venezia";

    /**
     * Package name for Amazon Underground.
     */
    public static final String AMAZON_UNDERGROUND_PACKAGE_NAME = "com.amazon.mshop.android";

    /**
     * Package name for the Google Play Store. Value can be verified here:
     * https://developers.google.com/android/reference/com/google/android/gms/common/GooglePlayServicesUtil.html#GOOGLE_PLAY_STORE_PACKAGE
     */
    public static final String GOOGLE_PLAY_STORE_PACKAGE_NAME = "com.android.vending";

    /**
     * Package name for Google's Package Installer. My guess is that apps installed using the
     * <a href="https://developer.android.com/reference/android/content/pm/PackageInstaller.html">PackageInstaller</a>
     * APIs will report as having been installed from this source. Since this installer package name result hides the
     * originating app package name from us, a consuming application that *really* needs to know how an app was
     * installed will need to fall back on inspecting the apps currently installed on the device and making an educated
     * guess.
     */
    public static final String PACKAGE_INSTALLER_PACKAGE_NAME = "com.google.android.packageinstaller";

    public static final String EXHAUSTIVE_SWITCH_EXCEPTION_MESSAGE
            = "This switch statement should be exhaustive.";

    public static final String MISSING_LAYOUT_IDS_EXCEPTION_MESSAGE
            = "Provided layout does not include views with required ids.";

    /**
     * The name of the SharedPreferences instance used to store all tracked event data by default.
     * DO NOT MODIFY! (If you do, any apps relying on the default Amplify SharedPreferences instance
     * will have all their tracking data reset automatically when they next update the library...)
     */
    public static final String DEFAULT_BACKING_SHARED_PREFERENCES_NAME
            = "AMPLIFY_SHARED_PREFERENCES_NAME";

    private Constants() {
        // This constructor intentionally left blank.
    }

}
