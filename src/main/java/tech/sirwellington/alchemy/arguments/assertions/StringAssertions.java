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

import java.util.regex.Pattern;

import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.annotations.arguments.NonEmpty;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.arguments.Arguments;
import tech.sirwellington.alchemy.arguments.FailedAssertionException;

import static tech.sirwellington.alchemy.arguments.Arguments.checkThat;
import static tech.sirwellington.alchemy.arguments.internal.Checks.*;

/**
 * Library assertions for String validation.
 *
 * @author SirWellington
 */
@NonInstantiable
public final class StringAssertions {

    private static final Pattern UUID_PATTERN = Pattern.compile(
        "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"
    );

    private StringAssertions() throws IllegalAccessException {
        throw new IllegalAccessException("Utility class should not be instantiated.");
    }

    /**
     * Asserts that the argument String matches the specified pattern.
     *
     * @param pattern The pattern to match against (non-null)
     * @return an {@link AlchemyAssertion} for the given pattern
     */
    public static AlchemyAssertion<String> stringThatMatches(Pattern pattern) {
        checkNotNull(pattern, "missing pattern");

        return s -> {
            nonEmptyString().check(s);

            if (!pattern.matcher(s).matches()) {
                throw new FailedAssertionException("Expected String to match pattern: " + pattern);
            }
        };
    }

    /**
     * Asserts that a given string is empty (that it has no value).
     *
     * @return an {@link AlchemyAssertion} for empty strings
     */
    public static AlchemyAssertion<String> emptyString() {
        return s -> {
            if (!isNullOrEmpty(s)) {
                failAssertion("Expected empty string but got: [{0}]", s);
            }
        };
    }

    /**
     * Asserts that the argument string has a length `>= minimumLength`.
     *
     * @param minimumLength The minimum allowed length (must be ≥ 0)
     * @return an {@link AlchemyAssertion} enforcing lower-bound length
     */
    public static AlchemyAssertion<String> stringWithLengthGreaterThanOrEqualTo(int minimumLength) {
        checkThat(minimumLength >= 0, "minimumLength must be >= 0");

        return s -> {
            nonEmptyString().check(s);

            if (s.length() < minimumLength) {
                failAssertion(
                    "Expecting a String with length >= {0}, but was {1}", minimumLength, s
                );
            }
        };
    }

    public static AlchemyAssertion<String> stringWithWhitespace() {
        return s ->{
            nonEmptyString().check(s);

            if (s.chars().noneMatch(Character::isWhitespace)) {
                failAssertion("Argument should have some whitespace but does not: [{0}]", s);
            }
        };
    }

    /**
     * Asserts that the argument string has no whitespace characters.
     *
     * @return an {@link AlchemyAssertion} for strings without any whitespace
     */
    public static AlchemyAssertion<String> stringWithNoWhitespace() {
        return s -> {
            nonEmptyString().check(s);

            if (s.chars().anyMatch(Character::isWhitespace)) {
                failAssertion(
                    "Argument should not have any whitespace: [{0}]", s
                );
            }
        };
    }

    /**
     * Asserts that the argument string has a length of exactly `expectedLength`.
     *
     * @param expectedLength the exact required length (must be ≥ 0)
     * @return an {@link AlchemyAssertion} enforcing fixed-length strings
     */
    public static AlchemyAssertion<String> stringWithLength(int expectedLength) {
        checkThat(expectedLength >= 0, "expectedLength must be >= 0");

        return s -> {
            nonEmptyString().check(s);

            if (s.length() != expectedLength) {
                failAssertion(
                    "Expecting a String with length [{0}] but got [{1}]", expectedLength, s
                );
            }
        };
    }

    /**
     * Asserts that the length of the argument string is less than the specified upper bound.
     *
     * @param upperBound The exclusive upper limit (must be > 0)
     * @return an {@link AlchemyAssertion} enforcing strict upper-bound length
     */
    public static AlchemyAssertion<String> stringWithLengthLessThan(int upperBound) {
        checkThat(upperBound > 0, "upperBound must be > 0");

        return s -> {
            Arguments.checkThat(s).isA(nonEmptyString());

            if (s.length() >= upperBound) {
                throw new FailedAssertionException(
                    "Expecting a String with length < " + upperBound
                );
            }
        };
    }

    /**
     * Assert that the argument String starts with a particular prefix.
     *
     * @param prefix The required starting substring (non-null and non-empty)
     * @return an {@link AlchemyAssertion} enforcing prefix match
     */
    public static AlchemyAssertion<String> stringBeginningWith(String prefix) {
        checkNotNullOrEmpty(prefix, "missing prefix");

        return s -> {
            nonEmptyString().check(s);

            if (!s.startsWith(prefix)) {
                failAssertion(
                    "Expected '{0} to start with `{1}`", s, prefix
                );
            }
        };
    }

    /**
     * Asserts that the length of the argument string is at most maximumLength.
     *
     * @param maximumLength The inclusive upper bound (must be ≥ 0)
     * @return an {@link AlchemyAssertion} enforcing max-length
     */
    public static AlchemyAssertion<String> stringWithLengthLessThanOrEqualTo(int maximumLength) {
        checkThat(maximumLength >= 0);

        return s -> {
            nonEmptyString().check(s);

            if (s.length() > maximumLength) {
                failAssertion(
                    "Argument exceeds the maximum string length of [{0}] ", maximumLength
                );
            }
        };
    }

    /**
     * Asserts that the argument string has a length `> minimumLength`.
     *
     * @param minimumLength The exclusive lower bound (must be > 0 and < Integer.MAX_VALUE)
     * @return an {@link AlchemyAssertion} enforcing strict lower-bound
     */
    public static AlchemyAssertion<String> stringWithLengthGreaterThan(int minimumLength) {
        checkThat(minimumLength > 0, "minimumLength must be > 0");
        checkThat(
            minimumLength < Integer.MAX_VALUE,
            "not possible to have a String larger than " + Integer.MAX_VALUE
        );

        return s -> {
            nonEmptyString().check(s);

            if (s.length() <= minimumLength) {
                failAssertion(
                    "Expected a String with length > {0}, but got [{1}] ", minimumLength, s
                );
            }
        };
    }

    /**
     * Asserts that a given string is not empty (neither null nor completely empty).
     *
     * @return an {@link AlchemyAssertion} for non-empty strings
     */
    public static AlchemyAssertion<String> nonEmptyString() {
        return s -> {
            if (isNullOrEmpty(s)) {
                throw new FailedAssertionException("string argument is empty");
            }
        };
    }

    /**
     * Asserts that the argument string's length is between the specified lengths, inclusively.
     *
     * @param minimumLength Minimum String Length, inclusive
     * @param maximumLength Maximum String Length, inclusive
     * @return an {@link AlchemyAssertion} enforcing bounded length
     */
    public static AlchemyAssertion<String> stringWithLengthBetween(int minimumLength, int maximumLength) {
        checkThat(minimumLength >= 0, "Minimum length must be at least 0");
        checkThat(
            minimumLength < maximumLength,
            "Minimum length must be < maximum length."
        );

        return s -> {
            nonEmptyString().check(s);

            int len = s.length();
            if (len < minimumLength || len > maximumLength) {
                failAssertion(
                    "String size [{0}] is not between acceptable range of {1}-{2}",
                    len,
                    minimumLength,
                    maximumLength
                );
            }
        };
    }

    /**
     * Checks that a string contains another substring.
     *
     * @param substring The substring to search for (must be non-null and non-empty)
     * @return an {@link AlchemyAssertion} enforcing substring presence
     */
    public static AlchemyAssertion<String> stringContaining(@NonEmpty String substring) {
        checkNotNullOrEmpty(substring, "substring cannot be empty");

        return s -> {
            nonEmptyString().check(s);

            if (!s.contains(substring)) {
                failAssertion(
                    "Expected '{0}' to contain '{1}'", s, substring
                );
            }
        };
    }

    /**
     * Checks that a String has All Upper-Cased characters (ALL-CAPS).
     *
     * @return an {@link AlchemyAssertion} for all-uppercase strings
     */
    public static AlchemyAssertion<String> allUpperCaseString() {
        return s -> {
            nonEmptyString().check(s);

            var areAllUppercase = s.chars().allMatch(Character::isUpperCase);
            if (!areAllUppercase) {
                failAssertion(
                    "Expected string to be all upper-case, but '{0} is not", s
                );
            }
        };
    }

    /**
     * Checks that a String has All Lower-Cased characters.
     *
     * @return an {@link AlchemyAssertion} for all-lowercase strings
     */
    public static AlchemyAssertion<String> allLowerCaseString() {
        return s -> {
            nonEmptyString().check(s);

            var areAllLowercase = s.chars().allMatch(Character::isLowerCase);
            if (!areAllLowercase) {
                failAssertion(
                    "Expected string to be all lower-case, but '{0} is not", s
                );
            }
        };
    }

    /**
     * Checks that a String ends with the specified non-empty string.
     *
     * @param suffix The required ending substring (non-null and non-empty)
     * @return an {@link AlchemyAssertion} enforcing suffix match
     */
    public static AlchemyAssertion<String> stringEndingWith(@NonEmpty String suffix) {
        checkNotNullOrEmpty(suffix, "string should not be empty");

        return s -> {
            nonEmptyString().check(s);

            if (!s.endsWith(suffix)) {
                failAssertion(
                    "Expected '{0}' to end with '{1}'", s, suffix
                );
            }
        };
    }

    /**
     * Checks that a String is composed only of Alphabetic Characters.
     *
     * @return an {@link AlchemyAssertion} for alphabetic-only strings
     */
    public static AlchemyAssertion<String> alphabeticString() {
        return s -> {
            nonEmptyString().check(s);

            var areAllAlphabetic = s.chars().allMatch(Character::isAlphabetic);
            if (!areAllAlphabetic) {
                failAssertion(
                    "Expected alphabetic string, but '{0}] is not entirely alphabetic", s
                );
            }
        };
    }

    /**
     * Checks that a String is composed of only Alphanumeric Characters.
     *
     * @return an {@link AlchemyAssertion} for alphanumeric strings
     */
    public static AlchemyAssertion<String> alphanumericString() {
        return s -> {
            nonEmptyString().check(s);

            if (!s.chars().allMatch(Character::isLetterOrDigit)) {
                failAssertion(
                    "Expected alphanumeric string, but '{0}' is not", s
                );
            }
        };
    }

    /**
     * Checks that a String can be represented as a Java Integer.
     *
     * @return an {@link AlchemyAssertion} for integer-formatted strings
     */
    public static AlchemyAssertion<String> integerString() {
        return s -> {
            nonEmptyString().check(s);

            try {
                Integer.parseInt(s);
            } catch (NumberFormatException e) {
                failAssertion(
                    "Expecting a number, instead: '{0}'", s
                );
            }
        };
    }

    /**
     * Checks that a String can be represented as a Java Double.
     *
     * @return an {@link AlchemyAssertion} for double-formatted strings
     */
    public static AlchemyAssertion<String> decimalString() {
        return s -> {
            nonEmptyString().check(s);

            try {
                Double.parseDouble(s);
            } catch (NumberFormatException e) {
                failAssertion(
                    "Expecting a decimal number, instead: " + s
                );
            }
        };
    }

    /**
     * Checks that a String represents a valid Type-4 UUID.
     *
     * @return an {@link AlchemyAssertion} for valid UUID strings
     */
    public static AlchemyAssertion<String> validUUID() {
        return s -> {
            nonEmptyString().check(s);

            if (!UUID_PATTERN.matcher(s).matches()) {
                failAssertion(
                    "String is not a valid UUID: " + s
                );
            }
        };
    }

    /**
     * Checks that a ZIP code string is:
     * <ul>
     *   <li>Not null</li>
     *   <li>Represents an Integer number (e.g., {@code "90012"})</li>
     *   <li>Exactly 5 digits long (e.g., {@code "01693"})</li>
     *   <li>In the valid numeric range [00000, 99999]</li>
     * </ul>
     *
     * @return an {@link AlchemyAssertion} for strict ZIP code strings
     */
    public static AlchemyAssertion<String> validZipCode() {
        return zip -> checkThat(zip)
                            .usingMessage("zip must consist of 4-5 characters")
                            .isA(stringWithLengthGreaterThanOrEqualTo(4))
                            .isA(stringWithLengthLessThanOrEqualTo(5))
                            .usingMessage("zip must consist of numbers only")
                            .is(integerString());
    }
}
