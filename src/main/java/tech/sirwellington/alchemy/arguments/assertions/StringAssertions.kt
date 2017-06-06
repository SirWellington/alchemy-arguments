/*
 * Copyright 2015 SirWellington Tech.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:JvmName("StringAssertions")

package tech.sirwellington.alchemy.arguments.assertions


import tech.sirwellington.alchemy.annotations.arguments.NonEmpty
import tech.sirwellington.alchemy.arguments.AlchemyAssertion
import tech.sirwellington.alchemy.arguments.Checks
import tech.sirwellington.alchemy.arguments.Checks.Internal.isNullOrEmpty
import tech.sirwellington.alchemy.arguments.FailedAssertionException
import java.lang.String.format
import java.util.*
import java.util.regex.Pattern

/**

 * @author SirWellington
 */


/**
 * Asserts that the argument String matches the specified pattern.

 * @param pattern The pattern to match against.
 *
 *
 * @return
 */

fun stringThatMatches(pattern: Pattern): AlchemyAssertion<String>
{
    Checks.Internal.checkNotNull(pattern, "missing pattern")

    return AlchemyAssertion { string: String ->

        if (!pattern.matcher(string).matches())
        {
            throw FailedAssertionException("Expected String to match pattern: $pattern")
        }
    }
}

/**
 * Asserts that a given string is empty (that it has no value).

 * @return
 */

fun emptyString(): AlchemyAssertion<String>
{
    return AlchemyAssertion { string: String ->

        if (!Checks.Internal.isNullOrEmpty(string))
        {
            throw FailedAssertionException("Expected empty string but got: $string")
        }
    }
}

/**
 * Asserts that the argument string has a length `>= minimumLength`.

 * @param minimumLength The length of the argument string must `>= minimumLength`
 *
 *
 * @return
 */

fun stringWithLengthGreaterThanOrEqualTo(minimumLength: Int): AlchemyAssertion<String>
{
    Checks.Internal.checkThat(minimumLength >= 0)

    return AlchemyAssertion { string: String ->

        notNull<String>().check(string)

        if (string.length < minimumLength)
        {
            throw FailedAssertionException("Expecting a String with length >= $minimumLength")
        }
    }
}

/**
 * Asserts that the argument String does not have any whitespace characters whatsoever.

 * @return
 */

fun stringWithNoWhitespace(): AlchemyAssertion<String>
{
    return AlchemyAssertion { string: String ->

        notNull<Any>().check(string)

        string.toCharArray()
                .filter { it.isWhitespace() }
                .forEach { throw FailedAssertionException("Argument should not have whitespace: [$string]") }
    }
}

/**
 * Asserts that the argument string has a length of exactly `expectedLength`

 * @param expectedLength The expected length of the string.
 *
 *
 * @return
 */

fun stringWithLength(expectedLength: Int): AlchemyAssertion<String>
{
    Checks.Internal.checkThat(expectedLength >= 0, "expectedLength must be >= 0")

    return AlchemyAssertion { string: String ->

        notNull<Any>().check(string)

        if (string.length != expectedLength)
        {
            throw FailedAssertionException("Expecting a String with length $expectedLength")
        }
    }
}

/**
 * Asserts that the length of the argument string is less than the specified upper bound.

 * @param upperBound The length of the string must be `<upperBound`
 *
 *
 * @return
 */

fun stringWithLengthLessThan(upperBound: Int): AlchemyAssertion<String>
{
    Checks.Internal.checkThat(upperBound > 0, "upperBound must be > 0")

    return AlchemyAssertion { string: String ->

        nonEmptyString().check(string)

        if (string.length >= upperBound)
        {
            throw FailedAssertionException("Expecting a String with length < $upperBound")
        }
    }
}

/**
 * Assert that the argument String starts with a particular prefix.

 * @param prefix The prefix to check that argument against.
 *
 *
 * @return
 */

fun stringBeginningWith(prefix: String): AlchemyAssertion<String>
{
    Checks.Internal.checkThat(!isNullOrEmpty(prefix), "missing prefix")

    return AlchemyAssertion { string: String ->

        nonEmptyString().check(string)

        if (!string.startsWith(prefix))
        {
            throw FailedAssertionException("Expected \"$string\" to start with \"$prefix\"")
        }
    }
}

/**
 * Asserts that the length of the argument string is at most maximumLength.
 *
 * @param maximumLength The length of the argument must be `<= maximumLength`
 *
 *
 * @return
 */

fun stringWithLengthLessThanOrEqualTo(maximumLength: Int): AlchemyAssertion<String>
{
    Checks.Internal.checkThat(maximumLength >= 0)
    return AlchemyAssertion { string: String ->

        notNull<Any>().check(string)

        if (string.length > maximumLength)
        {
            throw FailedAssertionException("Argument exceeds the maximum string length of: $maximumLength")
        }
    }
}

/**
 * Asserts that the argument string has a length `> minimumLength`
 *
 * @param minimumLength The exclusive lower bound for the size of the argument string.
 *
 *
 * @return
 */

fun stringWithLengthGreaterThan(minimumLength: Int): AlchemyAssertion<String>
{
    Checks.Internal.checkThat(minimumLength > 0, "minimumLength must be > 0")
    Checks.Internal.checkThat(minimumLength < Integer.MAX_VALUE, "not possible to have a String larger than ${Integer.MAX_VALUE}")

    return AlchemyAssertion { string: String ->

        nonEmptyString().check(string)

        if (string.length <= minimumLength)
        {
            throw FailedAssertionException("Expected a String with length > $minimumLength")
        }
    }
}

/**
 * Asserts that a given string is not empty (neither null nor completely empty).
 *
 * @return
 */

fun nonEmptyString(): AlchemyAssertion<String>
{
    return AlchemyAssertion { string: String ->

        if (Checks.Internal.isNullOrEmpty(string))
        {
            throw FailedAssertionException("String argument is empty")
        }
    }
}

/**
 * Asserts that the argument string's length is between the specified lengths, inclusively.
 *
 * @param minimumLength Minimum String Length, inclusive.
 * @param maximumLength Maximum String Length, inclusive.
 *
 *
 * @return
 */

fun stringWithLengthBetween(minimumLength: Int, maximumLength: Int): AlchemyAssertion<String>
{
    Checks.Internal.checkThat(minimumLength >= 0, "Minimum length must be at least 0")
    Checks.Internal.checkThat(minimumLength < maximumLength, "Minimum length must be < maximum length.")

    return AlchemyAssertion { string ->
        notNull<Any>().check(string)
        if (string.length < minimumLength || string.length > maximumLength)
        {
            val message = String.format("Argument size is not between acceptable range of [%d -> %d]", minimumLength, maximumLength)
            throw FailedAssertionException(message)
        }
    }
}

/**
 * Checks that a string contains another.

 * @param substring
 *
 * @throws IllegalArgumentException If `substring` is null or empty.
 */
@Throws(IllegalArgumentException::class)

fun stringContaining(@NonEmpty substring: String): AlchemyAssertion<String>
{
    Checks.Internal.checkNotNullOrEmpty(substring, "substring cannot be empty")

    return AlchemyAssertion { string ->
        nonEmptyString().check(string)

        if (!string.contains(substring))
        {
            throw FailedAssertionException(format("Expected %s to contain %s", string, substring))
        }
    }
}

/**
 * Checks that a String has All Upper-Cased characters (also known as ALL-CAPS).

 * @return
 */

fun allUpperCaseString(): AlchemyAssertion<String>
{
    return AlchemyAssertion { string ->
        nonEmptyString().check(string)

        for (character in string.toCharArray())
        {
            if (!Character.isUpperCase(character))
            {
                throw FailedAssertionException(format("Expected %s to be all upper-case, but %s isn't",
                                                      string,
                                                      character))
            }
        }
    }
}

/**
 * Checks that a String has All Lower-Cased characters.

 * @return
 */

fun allLowerCaseString(): AlchemyAssertion<String>
{
    return AlchemyAssertion { string ->
        nonEmptyString().check(string)

        for (character in string.toCharArray())
        {
            if (!Character.isLowerCase(character))
            {
                throw FailedAssertionException(format("Expected %s to be all lower-case, but %s isn't",
                                                      string,
                                                      character))
            }
        }

    }
}

/**
 * Checks that a String ends with the specified non-empty string, as determined by
 * [String.endsWith]. In other words, the Argument String must have this suffix.

 * @param suffix The Argument String must end with this non-empty String.
 *
 * @return
 *
 * @throws IllegalArgumentException If `substring` is empty or null.
 */
@Throws(IllegalArgumentException::class)

fun stringEndingWith(@NonEmpty suffix: String): AlchemyAssertion<String>
{
    Checks.Internal.checkNotNullOrEmpty(suffix, "string should not be empty")

    return AlchemyAssertion { string ->
        nonEmptyString().check(string)

        if (!string.endsWith(suffix))
        {
            throw FailedAssertionException(format("Expected %s to end with %s", string, suffix))
        }
    }
}

/**
 * Checks that a String is composed only of Alphabetic Characters, as determined by
 * [Character.isAlphabetic].

 * @return
 */

fun alphabeticString(): AlchemyAssertion<String>
{
    return AlchemyAssertion { string ->
        nonEmptyString().check(string)

        for (character in string.toCharArray())
        {
            if (!Character.isAlphabetic(character.toInt()))
            {
                throw FailedAssertionException(format("Expected alphabetic string, but '%s' is not alphabetic",
                                                      character))
            }
        }
    }
}

/**
 * Checks that a String is composed of only Alphanumeric Characters, as determined by
 * [Character.isDigit] and [Character.isAlphabetic].

 * @return
 */

fun alphanumericString(): AlchemyAssertion<String>
{
    return AlchemyAssertion { string ->
        nonEmptyString().check(string)

        for (character in string.toCharArray())
        {
            if (!Character.isAlphabetic(character.toInt()) && !Character.isDigit(character))
            {
                throw FailedAssertionException(format("Expected alphanumeric string, but chracter '%s' is not",
                                                      character))
            }
        }
    }
}

/**
 * Checks that a String can be represented as a Java Integer, as determined by
 * [Integer.valueOf].

 * @return
 */

fun integerString(): AlchemyAssertion<String>
{
    return AlchemyAssertion { string ->
        nonEmptyString().check(string)

        try
        {
            Integer.valueOf(string)
        }
        catch (ex: NumberFormatException)
        {
            throw FailedAssertionException("Expecting a number, instead: " + string)
        }
    }
}

/**
 * Checks that a String can be represented as a Java Double, as determined by
 * [Double.valueOf].

 * @return
 */

fun decimalString(): AlchemyAssertion<String>
{
    return AlchemyAssertion { string ->
        nonEmptyString().check(string)

        try
        {
            java.lang.Double.valueOf(string)
        }
        catch (ex: NumberFormatException)
        {
            throw FailedAssertionException("Expecting a decimal number, instead: " + string)
        }
    }
}

/**
 * Checks that a String represents a valid [Type-4 UUID][UUID.fromString].

 * @return
 */

fun validUUID(): AlchemyAssertion<String>
{
    return AlchemyAssertion { string ->
        nonEmptyString().check(string)

        try
        {
            UUID.fromString(string)
        }
        catch (ex: Exception)
        {
            throw FailedAssertionException("String is not a valid UUID: " + string)
        }
    }
}

/**
 * Checks that a String represents an [Integer], as determined by [Integer.parseInt].
 * In other words, that it contains only Digits, as determined by 2
 * [Character.isDigit], or the characters `-` (negative sign),
 * `+` (positive sign)
 *
 *
 * Valid examples include:
 * <pre>
 * 0
 * -054
 * 954
 * -674572
</pre> *

 * @return
 */

fun stringRepresentingInteger(): AlchemyAssertion<String>
{
    return AlchemyAssertion { string ->
        nonEmptyString().check(string)

        for (i in 0..string.length - 1)
        {
            val character = string.get(i)

            //The first character is allowed to be a sign character '-' or '+'
            if (i == 0 && isSignCharacter(character))
            {
                continue
            }

            if (!Character.isDigit(character))
            {
                throw FailedAssertionException(format("Expected an Integer String, but %s is not a digi",
                                                      character))
            }
        }
    }
}

private fun isSignCharacter(character: Char): Boolean
{
    return character == '-' || character == '+'
}