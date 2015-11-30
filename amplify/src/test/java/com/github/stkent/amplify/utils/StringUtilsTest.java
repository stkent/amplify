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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class StringUtilsTest extends BaseTest {

    private static final String SUPPRESS_ASSERT = "Assert";
    private static final String SUPPRESS_CONSTANT_CONDITIONS = "ConstantConditions";
    private static final String DEFAULT_STRING = "any other string";

    @SuppressLint(SUPPRESS_ASSERT)
    @Test
    public void testThatDefaultIfBlankReturnsPrimaryStringIfItContainsAtLeastOneNonWhitespaceCharacter() {
        // Arrange
        final String primaryString = "any string containing at least one non-whitespace character";
        assert !primaryString.equals(DEFAULT_STRING);

        // Act
        final String sanitizedString = StringUtils.defaultIfBlank(primaryString, DEFAULT_STRING);

        // Assert
        assertEquals(
                "defaultIfBlank should have returned the primary string",
                primaryString,
                sanitizedString);
    }

    @SuppressLint(SUPPRESS_ASSERT)
    @Test
    public void testThatDefaultIfBlankReturnsDefaultStringIfPrimaryStringIsWhitespaceOnly() {
        // Arrange
        final String whitespaceOnlyPrimaryString = "    ";
        assert !DEFAULT_STRING.equals(whitespaceOnlyPrimaryString);

        // Act
        final String sanitizedString = StringUtils.defaultIfBlank(whitespaceOnlyPrimaryString, DEFAULT_STRING);

        // Assert
        assertEquals(
                "defaultIfBlank should have returned the default string",
                DEFAULT_STRING,
                sanitizedString);
    }

    @SuppressLint(SUPPRESS_ASSERT)
    @Test
    public void testThatDefaultIfBlankReturnsDefaultStringIfPrimaryStringIsEmpty() {
        // Arrange
        final String emptyPrimaryString = "";
        assert !DEFAULT_STRING.equals(emptyPrimaryString);

        // Act
        final String sanitizedString = StringUtils.defaultIfBlank(emptyPrimaryString, DEFAULT_STRING);

        // Assert
        assertEquals(
                "defaultIfBlank should have returned the default string",
                DEFAULT_STRING,
                sanitizedString);
    }

    @SuppressLint(SUPPRESS_ASSERT)
    @Test
    public void testThatDefaultIfBlankReturnsDefaultStringIfPrimaryStringIsNull() {
        // Arrange
        final String nullPrimaryString = null;
        assert !DEFAULT_STRING.equals(nullPrimaryString);

        // Act
        final String sanitizedString = StringUtils.defaultIfBlank(nullPrimaryString, DEFAULT_STRING);

        // Assert
        assertEquals(
                "defaultIfBlank should have returned the default string",
                DEFAULT_STRING, sanitizedString);
    }

    @SuppressLint(SUPPRESS_ASSERT)
    @SuppressWarnings(SUPPRESS_CONSTANT_CONDITIONS)
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

    @SuppressLint(SUPPRESS_ASSERT)
    @SuppressWarnings(SUPPRESS_CONSTANT_CONDITIONS)
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

    @SuppressLint(SUPPRESS_ASSERT)
    @SuppressWarnings(SUPPRESS_CONSTANT_CONDITIONS)
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

    @SuppressLint(SUPPRESS_ASSERT)
    @SuppressWarnings(SUPPRESS_CONSTANT_CONDITIONS)
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

    @Test
    public void testThatCapitalizeOnlyFirstCharacterCaseIsChangedOnMixedCase() {
        // Arrange
        final String testString = "hElLO";
        final String expectedString = "HElLO";

        // Act
        final String modifiedString = StringUtils.capitalize(testString);

        // Assert
        assertEquals(
                "Only the first letter case should have been changed",
                expectedString,
                modifiedString);

    }

    @Test
    public void testThatCapitalizeOnlyFirstCharacterIsCapitalizedOnAllLowercase() {
        // Arrange
        final String testString = "hello";
        final String expectedString = "Hello";

        // Act
        final String modifiedString = StringUtils.capitalize(testString);

        // Assert
        assertEquals(
                "The first letter of the string should have been capitalized",
                expectedString,
                modifiedString);

    }

    @Test
    public void testThatCapitalizeNullReturnsNull() {
        // Arrange
        final String testString = null;

        // Act
        final String modifiedString = StringUtils.capitalize(testString);

        // Assert
        assertNull(
                "Modified string should be null as null was passed in",
                modifiedString);

    }

}
