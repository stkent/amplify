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

import android.support.annotation.NonNull;

/**
 * An abstract representation of a consuming class that is interested in receiving notifications
 * when events of interest occur.
 */
public interface IAmplifyStateTracker {

    /**
     * Call this method to notify an implementing class that an event occurred.
     *
     * @param event the event that occurred.
     */
    // TODO: handle event not being registered with the tracker
    void notifyEventTriggered(@NonNull final ITrackableEvent event);

}
