/*
 * Copyright © 2026. Sir Wellington.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.sirwellington.alchemy.arguments.assertions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;
import tech.sirwellington.alchemy.generator.CollectionGenerators;
import tech.sirwellington.alchemy.generator.StringGenerators;
import tech.sirwellington.alchemy.test.AlchemyTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static tech.sirwellington.alchemy.arguments.TestHelpers.assertThrowsFailedAssertion;
import static tech.sirwellington.alchemy.arguments.assertions.StringAssertions.*;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.Get.one;
import static tech.sirwellington.alchemy.generator.NumberGenerators.*;
import static tech.sirwellington.alchemy.generator.StringGenerators.*;
import static tech.sirwellington.alchemy.test.ThrowableAssertion.assertThrows;

/**
 * Tests for {@link StringAssertions}.
 *
 * @author SirWellington
 */
@DisplayName("StringAssertions Tests")
@AlchemyTest
final class StringAssertionsTest {

    //==============================
    // EMPTY / NULL CHECKS
    //==============================

    @Test
    @DisplayName("testEmptyString: emptyString assertion works correctly")
    void testEmptyString() {
        // Given
        var instance = emptyString();
        // Then
        assertNotNull(instance);

        assertThrowsFailedAssertion(
            () -> instance.check(one(alphabeticStrings()))
        );
        instance.check("");
        instance.check(null);
    }

    @Test
    @DisplayName("testNonEmptyString: nonEmptyString assertion works correctly")
    void testNonEmptyString() {
        // Given
        var instance = nonEmptyString();
        assertNotNull(instance, "nonEmptyString should return a non-null assertion");
        // Then
        Tests.checkForNullCase(instance);

        instance.check(one(alphabeticStrings()));
        assertThrowsFailedAssertion(() -> instance.check(""));
    }

    //==============================
    // LENGTH-BASED ASSERTIONS
    //==============================

    @Test
    @DisplayName("testStringWithLengthGreaterThan: stringWithLengthGreaterThan works correctly")
    void testStringWithLengthGreaterThan() {
        // Given
        int min = one(integers(2, 10100));
        var instance = stringWithLengthGreaterThan(min);
        // Then
        Tests.checkForNullCase(instance);

        // Given
        var badLengths = integers(1, min).mapping(x -> one(alphabeticStrings(x)));
        var goodLengths = AlchemyGenerator.of(
            () -> min + one(smallPositiveIntegers())
        ).mapping(x -> one(alphabeticStrings(x)));
        // Then
        Tests.runTests(instance, badLengths, goodLengths);
    }

    @Test
    @DisplayName("testStringWithLengthGreaterThanEdgeCases: edge cases for stringWithLengthGreaterThan")
    void testStringWithLengthGreaterThanEdgeCases() {
        assertThrows(() -> stringWithLengthGreaterThan(Integer.MAX_VALUE))
            .isIllegalArgumentException();

        int badArg = one(integers(Integer.MIN_VALUE, 1));
        assertThrows(() -> stringWithLengthGreaterThan(badArg))
            .isIllegalArgumentException();
    }

    @Test
    @DisplayName("testStringWithLengthLessThan: stringWithLengthLessThan works correctly")
    void testStringWithLengthLessThan() {
        // Given
        var upperBound = one(integers(2, 1000));
        var instance = stringWithLengthLessThan(upperBound);
        // Then
        Tests.checkForNullCase(instance);

        // Given
        var badLengths = integers(upperBound, upperBound + 50).mapping(x -> one(strings(x)));
        var goodLengths = integers(1, upperBound - 1).mapping(x -> one(strings(x)));
        // Then
        Tests.runTests(instance, badLengths, goodLengths);
    }

    @Test
    @DisplayName("testStringWithLengthLessThanEdgeCases: edge cases for stringWithLengthLessThan")
    void testStringWithLengthLessThanEdgeCases() {
        var badArg = one(integers(Integer.MIN_VALUE, 1));
        assertThrows(() -> stringWithLengthLessThan(badArg))
            .isIllegalArgumentException();
    }

    @Test
    @DisplayName("testStringWithLength: stringWithLength works correctly")
    void testStringWithLength() {
        // Given
        var expectedLength = one(integers(5, 25));
        var instance = stringWithLength(expectedLength);
        // Then
        Tests.checkForNullCase(instance);

        // Given
        var goodStrings = alphabeticStrings(expectedLength);
        var tooShort = alphabeticStrings(expectedLength - 1);
        var tooLong = alphabeticStrings(expectedLength + 1);
        // Then
        Tests.runTests(instance, tooShort, goodStrings);
        Tests.runTests(instance, tooLong, goodStrings);
    }

    @Test
    @DisplayName("testStringWithLengthEdgeCases: edge cases for stringWithLength")
    void testStringWithLengthEdgeCases() {
        int badArg = one(negativeIntegers());
        assertThrows(() -> stringWithLength(badArg))
            .isIllegalArgumentException();
    }

    @Test
    @DisplayName("testStringWithLengthGreaterThanOrEqualTo: works correctly")
    void testStringWithLengthGreaterThanOrEqualTo() {
        // Given
        var expectedSize = one(integers(10, 100));
        var instance = stringWithLengthGreaterThanOrEqualTo(expectedSize);
        // Then
        Tests.checkForNullCase(instance);

        // Given
        // At-bound cases
        var goodAtBound = strings(expectedSize);
        var goodAboveBound = integers(1, 50).mapping(x -> one(strings(x)));
        var badAboveBound = integers(expectedSize+1, expectedSize+50).mapping(x -> one(strings(x)));
        var badBelowBound = integers(1, expectedSize).mapping(x -> one(strings(x)));

        // Then
        Tests.runTests(instance, badAboveBound, goodAtBound);
        Tests.runTests(instance, badBelowBound, goodAboveBound);
    }

    @Test
    @DisplayName("testStringWithLengthGreaterThanOrEqualToEdgeCases: edge cases")
    void testStringWithLengthGreaterThanOrEqualToEdgeCases() {
        int negative = one(negativeIntegers());
        assertThrows(() -> stringWithLengthGreaterThanOrEqualTo(negative))
            .isIllegalArgumentException();
    }

    @Test
    @DisplayName("testStringWithLengthLessThanOrEqualTo: works correctly")
    void testStringWithLengthLessThanOrEqualTo() {
        // Given
        var expectedSize = one(integers(5, 100));
        var instance = stringWithLengthLessThanOrEqualTo(expectedSize);
        // Then
        Tests.checkForNullCase(instance);

        // Given
        var goodAtBound = strings(expectedSize);
        var goodBelowBound = integers(1, expectedSize).mapping(x -> one(strings(x)));
        var badBelowBound = integers(1, expectedSize).mapping(x -> one(strings(x)));
        // Then
        Tests.runTests(instance, badBelowBound, goodAtBound);
        Tests.runTests(instance, badBelowBound, goodBelowBound);
    }

    @Test
    @DisplayName("testStringWithLengthLessThanOrEqualToEdgeCases: edge cases")
    void testStringWithLengthLessThanOrEqualToEdgeCases() {
        int negative = one(negativeIntegers());
        assertThrows(() -> stringWithLengthLessThanOrEqualTo(negative))
            .isIllegalArgumentException();
    }

    @Test
    @DisplayName("testStringWithLengthBetween: works correctly")
    void testStringWithLengthBetween() {
        // Given
        var min = one(integers(10, 100));
        var max = one(integers(min + 1, 1000));
        var instance = stringWithLengthBetween(min, max);
        // Then
        Tests.checkForNullCase(instance);

        // Given
        var goodArguments = integers(min, max).mapping(x -> one(strings(x)));
        var tooShortLen = integers(1, min - 1).mapping(x -> one(strings(x)));
        // Then
        Tests.runTests(instance, tooShortLen, goodArguments);

        // Given
        var tooLongLen = integers(max + 1, max + 50).mapping(x -> one(strings(x)));
        // Then
        Tests.runTests(instance, tooLongLen, goodArguments);
    }

    @Test
    @DisplayName("testStringWithLengthBetweenEdgeCases: edge cases")
    void testStringWithLengthBetweenEdgeCases() {
        var min = one(integers(10, 100));
        var max = one(integers(min + 1, 1000));

        // Reversed range
        assertThrows(() -> stringWithLengthBetween(max, min))
            .isIllegalArgumentException();

        // Negative min
        int negMin = one(negativeIntegers());
        assertThrows(() -> stringWithLengthBetween(negMin, max))
            .isIllegalArgumentException();
    }

    @Test
    @DisplayName("testStringWithWhitespace: works correctly")
    void testStringWithWhitespace() {
        // Given
        var instance = stringWithWhitespace();
        // Then
        Tests.checkForNullCase(instance);

        assertThrowsFailedAssertion(() -> instance.check(""));
        assertThrowsFailedAssertion(() -> instance.check(one(alphabeticStrings())));

        // Given
        var goodWithWhitespace = strings(1024).mapping(s -> s + " ");
        var badNoWhitespace = alphabeticStrings();
        // Then
        Tests.runTests(instance, badNoWhitespace, goodWithWhitespace);
        // Then
        instance.check(" \t\n");
        instance.check("hello world");
    }

    @Test
    @DisplayName("testStringWithNoWhitespace: works correctly")
    void testStringWithNoWhitespace() {
        // Given
        var instance = stringWithNoWhitespace();
        // Then
        Tests.checkForNullCase(instance);
        assertThrowsFailedAssertion(() -> instance.check(""));
        assertThrowsFailedAssertion(() -> instance.check(one(alphabeticStrings()) + " "));
        assertThrowsFailedAssertion(() -> instance.check("hello\nworld"));

        // Given
        var goodNoWhitespace = alphanumericStrings();
        var badWithWhitespace = alphabeticStrings().mapping(s -> " " + s);
        // Then
        Tests.runTests(instance, badWithWhitespace, goodNoWhitespace);
    }

    //==============================
    // PREFIX / SUFFIX / CONTAINS
    //==============================

    @Test
    @DisplayName("testStringBeginningWith: works correctly")
    void testStringBeginningWith() {
        // Given
        var prefix = one(strings(4));
        var instance = stringBeginningWith(prefix);
        // Then
        Tests.checkForNullCase(instance);

        // Given
        CollectionGenerators.listOf(
            strings(20),
            50
        ).forEach(s -> {

        });
        var sampleString = one(strings(20));
        // Then
        instance.check(prefix + sampleString);
        instance.check(prefix);

        // Does not match
        assertThrowsFailedAssertion(
            () -> instance.check(sampleString)
        );
    }

    @Test
    @DisplayName("testStringBeginningWithEdgeCases: edge cases")
    void testStringBeginningWithEdgeCases() {
        assertThrows(() -> stringBeginningWith(null))
            .isIllegalArgumentException();

        assertThrows(() -> stringBeginningWith(""))
            .isIllegalArgumentException();
    }

    @Test
    @DisplayName("testStringEndingWith: works correctly")
    void testStringEndingWith() {
        var suffix = one(strings(4));
        var instance = stringEndingWith(suffix);

        assertNotNull(instance);
        assertThrowsFailedAssertion(() -> instance.check(null));

        var fullString = one(strings());
        // Matches
        instance.check(fullString + suffix);

        // Does not match
        assertThrowsFailedAssertion(() -> instance.check(fullString));
    }

    @Test
    @DisplayName("testStringEndingWithEdgeCases: edge cases")
    void testStringEndingWithEdgeCases() {
        assertThrows(() -> stringEndingWith(null))
            .isIllegalArgumentException();

        assertThrows(() -> stringEndingWith(""))
            .isIllegalArgumentException();
    }

    @Test
    @DisplayName("testStringContaining: works correctly")
    void testStringContaining() {
        var substring = one(strings(10));
        var instance = stringContaining(substring);

        assertNotNull(instance);
        assertThrowsFailedAssertion(() -> instance.check(null));

        var fullString = one(strings());
        // Matches
        instance.check(fullString + substring + fullString);

        // Does not match
        assertThrowsFailedAssertion(() -> instance.check(one(alphabeticStrings())));
    }

    @Test
    @DisplayName("testStringContainingEdgeCases: edge cases")
    void testStringContainingEdgeCases() {
        assertThrows(() -> stringContaining(""))
            .isIllegalArgumentException();

        assertThrows(() -> stringContaining(null))
            .isIllegalArgumentException();
    }

    //==============================
    // CASE / FORMAT CHECKS
    //==============================

    @Test
    @DisplayName("testAllUpperCaseString: works correctly")
    void testAllUpperCaseString() {
        var instance = allUpperCaseString();

        assertNotNull(instance);
        assertThrowsFailedAssertion(() -> instance.check(null));
        assertThrowsFailedAssertion(() -> instance.check(""));
        assertThrowsFailedAssertion(() -> instance.check(one(alphabeticStrings()).toLowerCase()));

        var upper = one(alphabeticStrings(50)).toUpperCase();
        instance.check(upper);

        // Mix-case fails
        var idx = one(integers(0, upper.length() - 1));
        var sb = new StringBuilder(upper);
        sb.setCharAt(idx, Character.toLowerCase(upper.charAt(idx)));
        assertThrowsFailedAssertion(() -> instance.check(sb.toString()));
    }

    @Test
    @DisplayName("testAllLowerCaseString: works correctly")
    void testAllLowerCaseString() {
        var instance = allLowerCaseString();

        assertNotNull(instance);
        assertThrowsFailedAssertion(() -> instance.check(null));
        assertThrowsFailedAssertion(() -> instance.check(""));
        assertThrowsFailedAssertion(() -> instance.check(one(alphabeticStrings()).toUpperCase()));

        var lower = one(alphabeticStrings(50)).toLowerCase();
        instance.check(lower);

        // Mix-case fails
        var idx = one(integers(0, lower.length() - 1));
        StringBuilder sb = new StringBuilder(lower);
        sb.setCharAt(idx, Character.toUpperCase(lower.charAt(idx)));
        assertThrowsFailedAssertion(() -> instance.check(sb.toString()));
    }

    @Test
    @DisplayName("testAlphabeticString: works correctly")
    void testAlphabeticString() {
        var instance = alphabeticString();

        assertNotNull(instance);
        assertThat(one(alphabeticStrings()), notNullValue());

        var alpha = one(alphabeticStrings());
        instance.check(alpha);

        assertThrowsFailedAssertion(() -> instance.check(""));
        assertThrowsFailedAssertion(() -> instance.check(alpha + "1"));
    }

    @Test
    @DisplayName("testAlphanumericString: works correctly")
    void testAlphanumericString() {
        var instance = alphanumericString();

        assertNotNull(instance);
        var alphanum = one(alphanumericStrings());
        instance.check(alphanum);

        assertThrowsFailedAssertion(() -> instance.check(""));
        assertThrowsFailedAssertion(() -> instance.check(alphanum + "!"));
    }

    //==============================
    // NUMERIC / UUID VALIDATION
    //==============================

    @Test
    @DisplayName("testStringRepresentingInteger: works correctly")
    void testStringRepresentingInteger() {
        var instance = stringRepresentingInteger();

        assertNotNull(instance);

        var value = one(positiveIntegers());
        var intStr = Integer.toString(value);
        instance.check(intStr);

        // Bad cases
        double d = one(doubles(-100.0, 100.0));
        assertThrowsFailedAssertion(() -> instance.check(Double.toString(d)));
        assertThrowsFailedAssertion(() -> instance.check(one(alphabeticStrings())));
    }

    @Test
    @DisplayName("testIntegerString: works correctly")
    void testIntegerString() {
        var instance = integerString();

        assertNotNull(instance);

        var value = one(integers(Integer.MIN_VALUE, Integer.MAX_VALUE));
        var intStr = Integer.toString(value);
        instance.check(intStr);

        // Bad cases
        double d = one(doubles(-10.0, 10.0));
        assertThrowsFailedAssertion(() -> instance.check(Double.toString(d)));
        assertThrowsFailedAssertion(() -> instance.check(one(alphabeticStrings())));
    }

    @Test
    @DisplayName("testDecimalString: works correctly")
    void testDecimalString() {
        var instance = decimalString();

        assertNotNull(instance);

        var value = one(doubles(-100.0, 100.0));
        instance.check(Double.toString(value));

        // Bad cases
        assertThrowsFailedAssertion(() -> instance.check(one(alphabeticStrings())));
    }

    @Test
    @DisplayName("testValidUUID: works correctly")
    void testValidUUID() {
        var instance = validUUID();

        assertNotNull(instance);

        var uuid = one(uuids());
        instance.check(uuid);

        assertThrowsFailedAssertion(() -> instance.check(one(alphabeticStrings(10))));
    }

    @Test
    @DisplayName("testIntegerStringWithGoodString: integerString works with valid integer strings")
    void testIntegerStringWithGoodString() {
        var value = one(integers(Integer.MIN_VALUE, Integer.MAX_VALUE));
        var string = Integer.toString(value);

        var assertion = integerString();
        assertNotNull(assertion);

        assertion.check(string);
    }

    @Test
    @DisplayName("testIntegerStringWithBadString: integerString rejects non-integer strings")
    void testIntegerStringWithBadString() {
        var assertion = integerString();

        var alphabetic = one(alphabeticStrings());
        assertThrowsFailedAssertion(() -> assertion.check(alphabetic));

        var value = one(doubles(-Double.MAX_VALUE, Double.MAX_VALUE));
        var decimalString = Double.toString(value);
        assertThrowsFailedAssertion(() -> assertion.check(decimalString));
    }

    @Test
    @DisplayName("testDecimalStringWithBadString: decimalString rejects non-decimal strings")
    void testDecimalStringWithBadString() {
        var assertion = decimalString();

        var alphanumeric = one(alphanumericStrings());
        assertThrowsFailedAssertion(() -> assertion.check(alphanumeric));
    }

}
