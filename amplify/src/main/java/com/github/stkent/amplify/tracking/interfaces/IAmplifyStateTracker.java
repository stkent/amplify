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

import com.github.stkent.amplify.Logger;
import com.github.stkent.amplify.views.AmplifyView;

public interface IAmplifyStateTracker {

    IAmplifyStateTracker configureWithDefaults();

    IAmplifyStateTracker setLogLevel(@NonNull final Logger.LogLevel logLevel);

    IAmplifyStateTracker setAlwaysShow(final boolean alwaysShow);

    IAmplifyStateTracker addEnvironmentCheck(@NonNull final IEnvironmentCheck requirement);

    IAmplifyStateTracker trackTotalEventCount(@NonNull final IEvent event, @NonNull final IEventCheck<Integer> eventCheck);

    IAmplifyStateTracker trackTotalEventCount(
            @NonNull final IEvent event,
            @NonNull final IEventCheck<Integer> eventCheck,
            @NonNull final ITrackingInitializer trackingInitializer);

    IAmplifyStateTracker trackFirstEventTime(@NonNull final IEvent event, @NonNull final IEventCheck<Long> eventCheck);

    IAmplifyStateTracker trackFirstEventTime(
            @NonNull final IEvent event,
            @NonNull final IEventCheck<Long> eventCheck,
            @NonNull final ITrackingInitializer trackingInitializer);

    IAmplifyStateTracker trackLastEventTime(@NonNull final IEvent event, @NonNull final IEventCheck<Long> eventCheck);

    IAmplifyStateTracker trackLastEventTime(
            @NonNull final IEvent event,
            @NonNull final IEventCheck<Long> eventCheck,
            @NonNull final ITrackingInitializer trackingInitializer);

    IAmplifyStateTracker trackLastEventVersion(@NonNull final IEvent event, @NonNull final IEventCheck<String> eventCheck);

    IAmplifyStateTracker trackLastEventVersion(
            @NonNull final IEvent event,
            @NonNull final IEventCheck<String> eventCheck,
            @NonNull final ITrackingInitializer trackingInitializer);

    IAmplifyStateTracker notifyEventTriggered(@NonNull final IEvent event);

    void promptIfReady(@NonNull final AmplifyView amplifyView);

    boolean shouldAskForRating();
}
