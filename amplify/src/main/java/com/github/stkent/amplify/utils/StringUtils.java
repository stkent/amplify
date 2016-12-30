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
package com.github.stkent.amplify.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Locale;

public final class StringUtils {

    public static String capitalizeFully(@Nullable final String string) {
        if (isBlank(string)) {
            return string;
        }

        return capitalize(string.toLowerCase(Locale.getDefault()));
    }

    /**
     * Capitalizes a String changing the first letter to title case.
     *
     * @param string the String to capitalize, may be null
     * @return the String with the first letter capitalized or null
     */
    @Nullable
    public static String capitalize(@Nullable final String string) {
        if (isBlank(string)) {
            return string;
        }

        final char firstChar = string.charAt(0);
        final char newChar = Character.toUpperCase(firstChar);
        if (firstChar == newChar) {
            return string;
        }

        int strLen = string.length();

        char[] newChars = new char[strLen];
        newChars[0] = newChar;
        string.getChars(1, strLen, newChars, 1);

        return String.valueOf(newChars);
    }


    /**
     * @return true if <code>charSequence</code> is null, empty, or whitespace; false otherwise
     */
    public static boolean isBlank(@Nullable final CharSequence charSequence) {
        if (charSequence == null) {
            return true;
        }

        final int sequenceLength = charSequence.length();

        if (sequenceLength == 0) {
            return true;
        }

        for (int i = 0; i < sequenceLength; i++) {
            if (!Character.isWhitespace(charSequence.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * @return true if <code>charSequence</code> is non-null, non-empty, and not pure whitespace;
     *         false otherwise
     */
    public static boolean isNotBlank(@Nullable final CharSequence charSequence) {
        return !isBlank(charSequence);
    }

    /**
     * @return <code>primaryString</code> if it is not blank (i.e. non-null, non-empty, and not pure
     *         whitespace); <code>defaultString</code> otherwise
     */
    @NonNull
    public static String defaultIfBlank(
            @Nullable final String primaryString,
            @NonNull final String defaultString) {

        return StringUtils.isNotBlank(primaryString) ? primaryString : defaultString;
    }

    private StringUtils() {
        // This constructor intentionally left blank.
    }

}
