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
package com.github.stkent.amplify.tracking.interfaces;

import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.annotation.NonNull;

/**
 * An abstract representation of a class that provides the (necessary and nice-to-have) data for
 * receiving critical feedback from the user.
 */
public interface IApplicationFeedbackDataProvider {

    /**
     * @return the human-readable name of the current device
     */
    @NonNull
    String getDeviceName();

    /**
     * @return the current version string of the application in which this library is embedded
     * @throws PackageManager.NameNotFoundException
     */
    @NonNull
    String getVersionDisplayString() throws PackageManager.NameNotFoundException;

    /**
     * @return the email address to which critical feedback should be sent
     * @throws Resources.NotFoundException
     */
    @NonNull
    String getFeedbackEmailAddress() throws Resources.NotFoundException;

}
