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

import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.annotations.arguments.NonEmpty;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.arguments.FailedAssertionException;
import tech.sirwellington.alchemy.arguments.internal.Checks;

import java.util.regex.Pattern;

import static tech.sirwellington.alchemy.arguments.internal.Checks.checkNotNullOrEmpty;
import static tech.sirwellington.alchemy.arguments.internal.Checks.isNullOrEmpty;

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
        Checks.checkNotNull(pattern, "missing pattern");

        return s -> {
            if (isNullOrEmpty(s)) {
                throw new FailedAssertionException("string argument is empty");
            }

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
                throw new FailedAssertionException("Expected empty string but got: " + s);
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
        Checks.checkThat(minimumLength >= 0, "minimumLength must be >= 0");

        return s -> {
            if (isNullOrEmpty(s)) {
                throw new FailedAssertionException("string argument is empty");
            }
            
            if (s.length() < minimumLength) {
                throw new FailedAssertionException(
                    "Expecting a String with length >= " + minimumLength
                );
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
            if (isNullOrEmpty(s)) {
                throw new FailedAssertionException("string argument is empty");
            }
            if (s.chars().anyMatch(Character::isWhitespace)) {
                throw new FailedAssertionException(
                    "Argument should not have whitespace: [" + s + "]"
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
        Checks.checkThat(expectedLength >= 0, "expectedLength must be >= 0");

        return s -> {
            if (isNullOrEmpty(s)) {
                throw new FailedAssertionException("string argument is empty");
            }
            if (s.length() != expectedLength) {
                throw new FailedAssertionException(
                    "Expecting a String with length " + expectedLength
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
        Checks.checkThat(upperBound > 0, "upperBound must be > 0");

        return s -> {
            if (isNullOrEmpty(s)) {
                throw new FailedAssertionException("string argument is empty");
            }
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
            if (isNullOrEmpty(s)) {
                throw new FailedAssertionException("string argument is empty");
            }
            if (!s.startsWith(prefix)) {
                throw new FailedAssertionException(
                    "Expected \"" + s + "\" to start with \"" + prefix + "\""
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
        Checks.checkThat(maximumLength >= 0);

        return s -> {
            if (isNullOrEmpty(s)) {
                throw new FailedAssertionException("string argument is empty");
            }
            if (s.length() > maximumLength) {
                throw new FailedAssertionException(
                    "Argument exceeds the maximum string length of: " + maximumLength
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
        Checks.checkThat(minimumLength > 0, "minimumLength must be > 0");
        Checks.checkThat(
            minimumLength < Integer.MAX_VALUE,
            "not possible to have a String larger than " + Integer.MAX_VALUE
        );

        return s -> {
            if (isNullOrEmpty(s)) {
                throw new FailedAssertionException("string argument is empty");
            }
            if (s.length() <= minimumLength) {
                throw new FailedAssertionException(
                    "Expected a String with length > " + minimumLength
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
        Checks.checkThat(minimumLength >= 0, "Minimum length must be at least 0");
        Checks.checkThat(
            minimumLength < maximumLength,
            "Minimum length must be < maximum length."
        );

        return s -> {
            if (isNullOrEmpty(s)) {
                throw new FailedAssertionException("string argument is empty");
            }
            int len = s.length();
            if (len < minimumLength || len > maximumLength) {
                throw new FailedAssertionException(
                    "Argument size is not between acceptable range of [" + minimumLength +
                        " -> " + maximumLength + "]"
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
            if (isNullOrEmpty(s)) {
                throw new FailedAssertionException("string argument is empty");
            }
            if (!s.contains(substring)) {
                throw new FailedAssertionException(
                    "Expected " + s + " to contain " + substring
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
            if (isNullOrEmpty(s)) {
                throw new FailedAssertionException("string argument is empty");
            }
            var areAllUppercase = s.chars().allMatch(Character::isUpperCase);
            if (!areAllUppercase) {
                throw new FailedAssertionException(
                    "Expected string to be all upper-case, but '" + s + "' isn't"
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
            if (isNullOrEmpty(s)) {
                throw new FailedAssertionException("string argument is empty");
            }
            var areAllLowercase = s.chars().allMatch(Character::isLowerCase);
            if (!areAllLowercase) {
                throw new FailedAssertionException(
                    "Expected string to be all lower-case, but '" + s + "' isn't"
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
            if (isNullOrEmpty(s)) {
                throw new FailedAssertionException("string argument is empty");
            }
            if (!s.endsWith(suffix)) {
                throw new FailedAssertionException(
                    "Expected " + s + " to end with " + suffix
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
            if (isNullOrEmpty(s)) {
                throw new FailedAssertionException("string argument is empty");
            }
            var areAllAlphabetic = s.chars().allMatch(Character::isAlphabetic);
            if (!areAllAlphabetic) {
                throw new FailedAssertionException(
                    "Expected alphabetic string, but '" + s +
                        "' is not entirely alphabetic"
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
            if (isNullOrEmpty(s)) {
                throw new FailedAssertionException("string argument is empty");
            }
            if (!s.chars().allMatch(Character::isLetterOrDigit)) {
                throw new FailedAssertionException(
                    "Expected alphanumeric string, but '" + s + "' is not"
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
            if (isNullOrEmpty(s)) {
                throw new FailedAssertionException("string argument is empty");
            }
            try {
                Integer.parseInt(s);
            } catch (NumberFormatException e) {
                throw new FailedAssertionException(
                    "Expecting a number, instead: " + s
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
            if (isNullOrEmpty(s)) {
                throw new FailedAssertionException("string argument is empty");
            }
            try {
                Double.parseDouble(s);
            } catch (NumberFormatException e) {
                throw new FailedAssertionException(
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
            if (isNullOrEmpty(s)) {
                throw new FailedAssertionException("string argument is empty");
            }
            if (!UUID_PATTERN.matcher(s).matches()) {
                throw new FailedAssertionException(
                    "String is not a valid UUID: " + s
                );
            }
        };
    }

    /**
     * Checks that a String represents an Integer, as determined by {@link Integer#parseInt}.
     *
     * @return an {@link AlchemyAssertion} for integer-formatted strings (with optional +/- prefix)
     */
    public static AlchemyAssertion<String> stringRepresentingInteger() {
        return s -> {
            if (isNullOrEmpty(s)) {
                throw new FailedAssertionException("string argument is empty");
            }

            int len = s.length();
            for (int i = 0; i < len; i++) {
                char c = s.charAt(i);
                if (i == 0 && isNumericalSign(c)) {
                    continue;
                }
                if (!Character.isDigit(c)) {
                    throw new FailedAssertionException(
                        "Expected an Integer String, but '" + c +
                            "' is not a digit in [" + s + "]"
                    );
                }
            }
        };
    }

    // Helper methods (private)

    private static boolean isNumericalSign(char c) {
        return c == '-' || c == '+';
    }

    private static boolean isAlphabetic(char c) {
        return Character.isAlphabetic(c);
    }

    private static boolean isNotAlphabetic(char c) {
        return !isAlphabetic(c);
    }

    private static boolean isLetterOrDigit(char c) {
        return Character.isLetterOrDigit(c);
    }

    private static boolean isNotLetterOrDigit(char c) {
        return !isLetterOrDigit(c);
    }
}
