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

import android.annotation.SuppressLint;

import com.github.stkent.amplify.helpers.BaseTest;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StringUtilsTest extends BaseTest {

    @SuppressLint("Assert")
    @Test
    public void testThatDefaultIfBlankReturnsPrimaryStringIfItContainsAtLeastOneNonWhitespaceCharacter() {
        // Arrange
        final String primaryString = "any string containing at least one non-whitespace character";
        final String defaultString = "any other string";
        assert !primaryString.equals(defaultString);

        // Act
        final String sanitizedString = StringUtils.defaultIfBlank(primaryString, defaultString);

        // Assert
        assertEquals(
                "defaultIfBlank should have returned the primary string",
                primaryString,
                sanitizedString);
    }

    @SuppressLint("Assert")
    @Test
    public void testThatDefaultIfBlankReturnsDefaultStringIfPrimaryStringIsWhitespaceOnly() {
        // Arrange
        final String whitespaceOnlyPrimaryString = "    ";
        final String defaultString = "any other string";
        assert !defaultString.equals(whitespaceOnlyPrimaryString);

        // Act
        final String sanitizedString = StringUtils.defaultIfBlank(whitespaceOnlyPrimaryString, defaultString);

        // Assert
        assertEquals(
                "defaultIfBlank should have returned the default string",
                defaultString,
                sanitizedString);
    }

    @SuppressLint("Assert")
    @Test
    public void testThatDefaultIfBlankReturnsDefaultStringIfPrimaryStringIsEmpty() {
        // Arrange
        final String emptyPrimaryString = "";
        final String defaultString = "any other string";
        assert !defaultString.equals(emptyPrimaryString);

        // Act
        final String sanitizedString = StringUtils.defaultIfBlank(emptyPrimaryString, defaultString);

        // Assert
        assertEquals(
                "defaultIfBlank should have returned the default string",
                defaultString,
                sanitizedString);
    }

    @SuppressLint("Assert")
    @Test
    public void testThatDefaultIfBlankReturnsDefaultStringIfPrimaryStringIsNull() {
        // Arrange
        final String nullPrimaryString = null;
        final String defaultString = "any other string";
        assert !defaultString.equals(nullPrimaryString);

        // Act
        final String sanitizedString = StringUtils.defaultIfBlank(nullPrimaryString, defaultString);

        // Assert
        assertEquals(
                "defaultIfBlank should have returned the default string",
                defaultString, sanitizedString);
    }

    @SuppressLint("Assert")
    @SuppressWarnings("ConstantConditions")
    @Test
    public void testThatIsBlankReturnsTrueIfStringIsNull() {
        // Arrange
        final String nullString = null;

        // Act
        final Boolean isBlank = StringUtils.isBlank(nullString);

        // Assert
        assertTrue(
                "isBlank should have returned true",
                isBlank);
    }

    @SuppressLint("Assert")
    @SuppressWarnings("ConstantConditions")
    @Test
    public void testThatIsBlankReturnsTrueIfStringIsEmpty() {
        // Arrange
        final String emptyString = "";

        // Act
        final Boolean isBlank = StringUtils.isBlank(emptyString);

        // Assert
        assertTrue(
                "isBlank should have returned true",
                isBlank);
    }

    @SuppressLint("Assert")
    @SuppressWarnings("ConstantConditions")
    @Test
    public void testThatIsBlankReturnsTrueIfStringIsWhitespaceOnly() {
        // Arrange
        final String whitespaceOnlyString = "   ";

        // Act
        final Boolean isBlank = StringUtils.isBlank(whitespaceOnlyString);

        // Assert
        assertTrue(
                "isBlank should have returned true",
                isBlank);
    }

    @SuppressLint("Assert")
    @SuppressWarnings("ConstantConditions")
    @Test
    public void testThatIsBlankReturnsFalseIfStringContainsAtLeastOneNonWhitespaceCharacter() {
        // Arrange
        final String nullString = "any string containing at least one non-whitespace character";

        // Act
        final Boolean isBlank = StringUtils.isBlank(nullString);

        // Assert
        assertFalse(
                "isBlank should have returned false",
                isBlank);
    }

}
