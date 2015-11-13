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

/**
 * Created by bobbake4 on 11/13/15.
 */
public final class StringUtils {

    private StringUtils() {

    }

    /**
     * <p>Returns either the passed in CharSequence, or if the CharSequence is
     * whitespace, empty ("") or {@code null}, the value of {@code defaultStr}.</p>
     *
     * <pre>
     * StringUtils.defaultIfBlank(null, "NULL")  = "NULL"
     * StringUtils.defaultIfBlank("", "NULL")    = "NULL"
     * StringUtils.defaultIfBlank(" ", "NULL")   = "NULL"
     * StringUtils.defaultIfBlank("bat", "NULL") = "bat"
     * StringUtils.defaultIfBlank("", null)      = null
     * </pre>
     * @param <T> the specific kind of CharSequence
     * @param str the CharSequence to check, may be null
     * @param defaultStr  the default CharSequence to return
     *  if the input is whitespace, empty ("") or {@code null}, may be null
     * @return the passed in CharSequence, or the default
     */
    public static <T extends CharSequence> T defaultIfBlank(final T str, final T defaultStr) {
        return isBlank(str) ? defaultStr : str;
    }

    /**
     * <p>Checks if a CharSequence is whitespace, empty ("") or null.</p>
     *
     * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     *
     * @param cs  the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is null, empty or whitespace
     * @since 2.0
     * @since 3.0 Changed signature from isBlank(String) to isBlank(CharSequence)
     */
    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
