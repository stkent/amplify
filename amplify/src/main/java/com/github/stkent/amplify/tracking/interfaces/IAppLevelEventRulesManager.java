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
package com.github.stkent.amplify.tracking.interfaces;

/**
 * An abstract representation of a class that manages application-level rules.
 */
public interface IAppLevelEventRulesManager extends IRulesManager {

    /**
     * Set a new cooldown duration for application install.
     *
     * @param cooldownPeriodDays the number of days that must pass after the application is first
     *                           installed before the user can be prompted
     */
    void setInstallTimeCooldownDays(int cooldownPeriodDays);

    /**
     * Set a new cooldown duration for application updates.
     *
     * @param cooldownPeriodDays the number of days that must pass after the application is
     *                           updated before the user can be prompted
     */
    void setLastUpdateTimeCooldownDays(int cooldownPeriodDays);

    /**
     * Set a new cooldown duration for application crashes.
     *
     * @param cooldownPeriodDays the number of days that must pass after the application crashes
     *                           before the user can be prompted
     */
    void setLastCrashTimeCooldownDays(int cooldownPeriodDays);

    /**
     * Notifies the implementing class that the application has crashed.
     */
    void notifyOfCrash();

}
