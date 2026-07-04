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
        var instance = emptyString();
        assertNotNull(instance, "emptyString should return a non-null assertion");

        assertThrowsFailedAssertion(() -> instance.check(one(alphabeticStrings())));
        instance.check("");
        instance.check(null);
    }

    @Test
    @DisplayName("testNonEmptyString: nonEmptyString assertion works correctly")
    void testNonEmptyString() {
        var instance = nonEmptyString();
        assertNotNull(instance, "nonEmptyString should return a non-null assertion");

        instance.check(one(alphabeticStrings()));
        assertThrowsFailedAssertion(() -> instance.check(""));
        assertThrowsFailedAssertion(() -> instance.check(null));
    }

    //==============================
    // LENGTH-BASED ASSERTIONS
    //==============================

    @Test
    @DisplayName("testStringWithLengthGreaterThan: stringWithLengthGreaterThan works correctly")
    void testStringWithLengthGreaterThan() {
        int min = one(integers(2, 10100));
        var instance = stringWithLengthGreaterThan(min);

        assertNotNull(instance);
        assertThrowsFailedAssertion(() -> instance.check(null));

        // Bad cases: length ≤ min
        int badLen = one(integers(1, min));
        assertThrowsFailedAssertion(() -> instance.check(one(alphabeticStrings(badLen))));

        // Good case: length > min
        int goodLen = min + one(smallPositiveIntegers());
        instance.check(one(alphabeticStrings(goodLen)));
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
        int upperBound = one(integers(2, 1000));
        var instance = stringWithLengthLessThan(upperBound);

        assertNotNull(instance);
        assertThrowsFailedAssertion(() -> instance.check(null));

        // Bad case: length ≥ upperBound
        int badLen = one(integers(upperBound, upperBound + 50));
        assertThrowsFailedAssertion(() -> instance.check(one(strings(badLen))));

        // Good case: length < upperBound
        int goodLen = one(integers(1, upperBound - 1));
        instance.check(one(strings(goodLen)));
    }

    @Test
    @DisplayName("testStringWithLengthLessThanEdgeCases: edge cases for stringWithLengthLessThan")
    void testStringWithLengthLessThanEdgeCases() {
        int badArg = one(integers(Integer.MIN_VALUE, 1));
        assertThrows(() -> stringWithLengthLessThan(badArg))
            .isIllegalArgumentException();
    }

    @Test
    @DisplayName("testStringWithLength: stringWithLength works correctly")
    void testStringWithLength() {
        var expectedLength = one(integers(5, 25));
        var instance = stringWithLength(expectedLength);

        assertNotNull(instance);
        assertThrowsFailedAssertion(() -> instance.check(null));

        // Good case
        instance.check(one(alphabeticStrings(expectedLength)));

        // Bad cases: too short or too long
        assertThrowsFailedAssertion(() -> instance.check(one(alphabeticStrings(expectedLength - 1))));
        assertThrowsFailedAssertion(() -> instance.check(one(strings(expectedLength + 1))));
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
        var expectedSize = one(integers(10, 100));
        var instance = stringWithLengthGreaterThanOrEqualTo(expectedSize);

        assertNotNull(instance);
        assertThrowsFailedAssertion(() -> instance.check(null));

        // At-bound case
        instance.check(one(strings(expectedSize)));

        // Above bound
        int extraLen = one(integers(1, 5));
        instance.check(one(strings(expectedSize + extraLen)));

        // Below bound
        int lessLen = one(integers(1, 5));
        assertThrowsFailedAssertion(() -> instance.check(one(strings(expectedSize - lessLen))));
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
        var expectedSize = one(integers(5, 100));
        var instance = stringWithLengthLessThanOrEqualTo(expectedSize);

        assertNotNull(instance);
        assertThrowsFailedAssertion(() -> instance.check(null));

        // At-bound case
        instance.check(one(strings(expectedSize)));

        // Below bound
        int lessLen = one(integers(1, 10));
        instance.check(one(strings(expectedSize - lessLen)));

        // Above bound
        int extraLen = one(integers(5, 10));
        assertThrowsFailedAssertion(() -> instance.check(one(strings(expectedSize + extraLen))));
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
        var min = one(integers(10, 100));
        var max = one(integers(min + 1, 1000));

        var instance = stringWithLengthBetween(min, max);
        assertNotNull(instance);
        assertThrowsFailedAssertion(() -> instance.check(null));

        // Within range
        int goodLen = one(integers(min, max));
        instance.check(one(strings(goodLen)));

        // Too short
        int tooShortLen = one(integers(1, min - 1));
        assertThrowsFailedAssertion(() -> instance.check(one(strings(tooShortLen))));

        // Too long
        int tooLongLen = one(integers(max + 1, max + 50));
        assertThrowsFailedAssertion(() -> instance.check(one(strings(tooLongLen))));
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
        var instance = stringWithWhitespace();

        assertNotNull(instance);
        assertThrowsFailedAssertion(() -> instance.check(null));
        assertThrowsFailedAssertion(() -> instance.check(""));
        assertThrowsFailedAssertion(() -> instance.check(one(alphabeticStrings())));

        // Good cases: strings with whitespace
        instance.check(" \t\n");
        instance.check("hello world");
    }

    @Test
    @DisplayName("testStringWithNoWhitespace: works correctly")
    void testStringWithNoWhitespace() {
        var instance = stringWithNoWhitespace();

        assertNotNull(instance);
        assertThrowsFailedAssertion(() -> instance.check(null));
        assertThrowsFailedAssertion(() -> instance.check(""));
        assertThrowsFailedAssertion(() -> instance.check(one(alphabeticStrings()) + " "));
        assertThrowsFailedAssertion(() -> instance.check("hello\nworld"));

        // Good case: no whitespace
        instance.check(one(alphabeticStrings()));
    }

    //==============================
    // PREFIX / SUFFIX / CONTAINS
    //==============================

    @Test
    @DisplayName("testStringBeginningWith: works correctly")
    void testStringBeginningWith() {
        var prefix = one(strings(4));
        var instance = stringBeginningWith(prefix);

        assertNotNull(instance);
        assertThrowsFailedAssertion(() -> instance.check(null));

        // Matches
        var fullString = one(strings(20));
        instance.check(prefix + fullString);
        instance.check(prefix);

        // Does not match
        assertThrowsFailedAssertion(() -> instance.check(fullString));
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
