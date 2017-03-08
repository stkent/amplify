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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public final class StringUtils {

    /**
     * @return <code>primaryString</code> if it is not blank (i.e. non-null, non-empty, and not pure whitespace);
     *         <code>defaultString</code> otherwise
     */
    @NonNull
    public static String defaultIfBlank(@Nullable final String primaryString, @NonNull final String defaultString) {
        return isBlank(primaryString) ? defaultString : primaryString;
    }

    /**
     * @return true if <code>charSequence</code> is null, empty, or whitespace; false otherwise
     */
    private static boolean isBlank(@Nullable final CharSequence charSequence) {
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

    private StringUtils() {
        // This constructor intentionally left blank.
    }

}
