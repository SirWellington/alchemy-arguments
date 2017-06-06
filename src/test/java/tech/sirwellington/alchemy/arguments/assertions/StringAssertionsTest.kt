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

package tech.sirwellington.alchemy.arguments.assertions

import org.hamcrest.Matchers.notNullValue
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tech.sirwellington.alchemy.arguments.Arguments.checkThat
import tech.sirwellington.alchemy.arguments.FailedAssertionException
import tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner
import tech.sirwellington.alchemy.test.junit.runners.DontRepeat
import tech.sirwellington.alchemy.test.junit.runners.Repeat
import java.lang.String.format
import java.util.regex.Pattern

/**

 * @author SirWellington
 */
@Repeat(5000)
@RunWith(AlchemyTestRunner::class)
class StringAssertionsTest
{

    @Before
    fun setUp()
    {
    }

    @DontRepeat
    @Test
    fun testCannotInstantiateClass()
    {
        assertThrows { StringAssertions::class.java.newInstance() }

        assertThrows { StringAssertions() }
                .isInstanceOf(IllegalAccessException::class.java)
    }

    @Test
    fun testEmptyString()
    {
        val instance = StringAssertions.emptyString()

        val badArguments = alphabeticString()
        val goodArguments = stringsFromFixedList(null, "")

        Tests.runTests(instance, badArguments, goodArguments)
    }

    @Test
    fun testStringWithLengthGreaterThan()
    {

        val minAccepted = one(integers(2, 10100))
        val instance = StringAssertions.stringWithLengthGreaterThan(minAccepted)

        var badArguments = {
            val length = one(integers(1, minAccepted))
            one(alphabeticString(length))
        }

        val goodArguments = {
            val length = minAccepted + one(smallPositiveIntegers())
            one(alphabeticString(length))
        }

        Tests.runTests(instance, badArguments, goodArguments)

        badArguments = strings(minAccepted)
        Tests.runTests(instance, badArguments, goodArguments)
    }

    @Test
    fun testStringWithLengthGreaterThanEdgeCases()
    {
        assertThrows { StringAssertions.stringWithLengthGreaterThan(Integer.MAX_VALUE) }
                .isInstanceOf(IllegalArgumentException::class.java)

        val badArgument = one(integers(Integer.MIN_VALUE, 1))
        assertThrows { StringAssertions.stringWithLengthGreaterThan(badArgument) }
                .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun testStringWithLengthLessThan()
    {
        val upperBound = one(integers(2, 1000))
        val instance = StringAssertions.stringWithLengthLessThan(upperBound)

        val badArguments = {
            val length = one(integers(upperBound + 1, upperBound * 2))
            one(strings(length))
        }

        val goodArugments = {
            val length = one(integers(1, upperBound))
            one(strings(length))
        }

        Tests.runTests(instance, badArguments, goodArugments)
    }

    @Test
    fun testStringWithLengthLessThanEdgeCases()
    {
        val badArgument = one(integers(Integer.MIN_VALUE, 1))
        assertThrows { StringAssertions.stringWithLengthLessThan(badArgument) }
                .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun testStringThatMatches()
    {
        val letter = one(alphabeticString()).substring(0, 1)
        val pattern = Pattern.compile(".*$letter.*")
        val instance = StringAssertions.stringThatMatches(pattern)
        val badArguments = { alphabeticString().get().replace(letter.toRegex(), "") }
        val goodArguments = { alphabeticString().get() + letter }
        Tests.runTests(instance, badArguments, goodArguments)
    }

    @Test
    fun testStringThatMatchesEdgeCases()
    {
        assertThrows { StringAssertions.stringThatMatches(null!!) }
                .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testNonEmptyString()
    {
        val instance = StringAssertions.nonEmptyString()
        assertThat(instance, notNullValue())

        val arg = one(alphabeticString())
        instance.check(arg)

        assertThrows { instance.check("") }
                .isInstanceOf(FailedAssertionException::class.java)

        assertThrows { instance.check(null) }
                .isInstanceOf(FailedAssertionException::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testStringWithLength()
    {
        assertThrows { StringAssertions.stringWithLength(one(negativeIntegers())) }
                .isInstanceOf(IllegalArgumentException::class.java)

        val expectedLength = one(integers(5, 25))
        val instance = StringAssertions.stringWithLength(expectedLength)
        assertThat(instance, notNullValue())
        Tests.checkForNullCase(instance)

        val arg = one(alphabeticString(expectedLength))
        instance.check(arg)

        val tooShort = one(alphabeticString(expectedLength - 1))
        assertThrows { instance.check(tooShort) }.isInstanceOf(FailedAssertionException::class.java)

        val tooLong = one(strings(expectedLength + 1))
        assertThrows { instance.check(tooLong) }
                .isInstanceOf(FailedAssertionException::class.java)

    }

    @Test
    @Throws(Exception::class)
    fun testStringWithLengthEdgeCases()
    {
        val badArgument = one(negativeIntegers())

        assertThrows { StringAssertions.stringWithLength(badArgument) }
                .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testStringWithLengthGreaterThanOrEqualToWithBadArgs()
    {
        val negativeNumber = one(negativeIntegers())

        assertThrows { StringAssertions.stringWithLengthGreaterThanOrEqualTo(negativeNumber) }
                .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testStringWithLengthGreaterThanOrEqualTo()
    {
        val expectedSize = one(integers(10, 100))

        val instance = StringAssertions.stringWithLengthGreaterThanOrEqualTo(expectedSize)
        assertThat(instance, notNullValue())
        Tests.checkForNullCase(instance)

        val goodString = one(strings(expectedSize))
        instance.check(goodString)

        val amountToAdd = one(integers(1, 5))
        val anotherGoodString = one(strings(expectedSize + amountToAdd))
        instance.check(anotherGoodString)

        val amountToSubtract = one(integers(1, 5))
        val badString = one(strings(expectedSize - amountToSubtract))
        assertThrows { instance.check(badString) }
                .isInstanceOf(FailedAssertionException::class.java)

    }

    @Test
    @Throws(Exception::class)
    fun testStringWithLengthLessThanOrEqualToWithBadArgs()
    {
        val negativeNumber = one(negativeIntegers())

        assertThrows { StringAssertions.stringWithLengthLessThanOrEqualTo(negativeNumber) }
                .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testStringWithLengthLessThanOrEqualTo()
    {
        val expectedSize = one(integers(5, 100))
        val instance = StringAssertions.stringWithLengthLessThanOrEqualTo(expectedSize)

        assertThat(instance, notNullValue())
        Tests.checkForNullCase(instance)

        val goodStrings = strings(expectedSize)

        val amountToAdd = one(integers(5, 10))
        val badStrings = strings(expectedSize + amountToAdd)

        Tests.runTests(instance, badStrings, goodStrings)
    }

    @Test
    @Throws(Exception::class)
    fun testStringWithNoWhitespace()
    {
        val instance = StringAssertions.stringWithNoWhitespace()
        assertThat(instance, notNullValue())
        Tests.checkForNullCase(instance)

        val goodString = alphabeticString()
        val badStrings = {
            goodString
            +one(stringsFromFixedList(" ", "\n", "\t"))
            +goodString
        }

        Tests.runTests(instance, badStrings, goodString)
    }

    @Test
    @Throws(Exception::class)
    fun testStringWithLengthBetween()
    {
        val minimumLength = one(integers(10, 100))
        val maximumLength = one(integers(minimumLength + 1, 1000))

        val instance = StringAssertions.stringWithLengthBetween(minimumLength, maximumLength)
        assertThat(instance, notNullValue())
        Tests.checkForNullCase(instance)

        val tooShort = {
            //Sad cases
            val stringTooShortLength = minimumLength - one(integers(1, 9))
            val stringTooShort = one(strings(stringTooShortLength))
            stringTooShort
        }

        val tooLong = {
            val stringTooLongLength = maximumLength + one(smallPositiveIntegers())
            val stringTooLong = one(strings(stringTooLongLength))
            stringTooLong
        }

        var goodStrings = {
            val length = one(integers(minimumLength, maximumLength))
            one(strings(length))
        }

        Tests.runTests(instance, tooLong, goodStrings)

        goodStrings = strings(minimumLength)
        Tests.runTests(instance, tooShort, goodStrings)

    }

    @Test
    @Throws(Exception::class)
    fun testStringWithLengthBetweenEdgeCases()
    {

        val minimumLength = one(integers(10, 100))
        val maximumLength = one(integers(minimumLength, 1000))

        assertThrows { StringAssertions.stringWithLengthBetween(maximumLength, minimumLength) }
                .isInstanceOf(IllegalArgumentException::class.java)

        assertThrows { StringAssertions.stringWithLengthBetween(-minimumLength, maximumLength) }
                .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun testStringBeginningWith()
    {
        val string = one(strings(20))
        val prefix = one(strings(4))

        val instance = StringAssertions.stringBeginningWith(prefix)

        //Happy Cases
        instance.check(prefix + string)
        instance.check(prefix)

        //Sad Cases
        assertThrows { instance.check(null) }
                .isInstanceOf(FailedAssertionException::class.java)

        assertThrows { instance.check(string) }
                .isInstanceOf(FailedAssertionException::class.java)
    }

    @Test
    fun testStringBeginningWithEdgeCases()
    {
        assertThrows { StringAssertions.stringBeginningWith(null!!) }
                .isInstanceOf(IllegalArgumentException::class.java)

        assertThrows { StringAssertions.stringBeginningWith("") }
                .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun testStringContaining()
    {
        val longString = one(strings(1000))
        val substring = longString.substring(0, 100)

        // Happy Case
        StringAssertions.stringContaining(substring)
                .check(longString)

        //Sad Cases
        assertThrows { StringAssertions.stringContaining("") }
                .isInstanceOf(IllegalArgumentException::class.java)

        assertThrows { StringAssertions.stringContaining(null!!) }
                .isInstanceOf(IllegalArgumentException::class.java)

        val notSubstring = substring + one(uuids())
        assertThrows {
            StringAssertions.stringContaining(notSubstring)
                    .check(longString)
        }
                .isInstanceOf(FailedAssertionException::class.java)
    }

    @Test
    fun testAllUpperCaseString()
    {
        val allUpperCase = one(alphabeticString(50)).toUpperCase()

        val oneLowerCaseCharacter = lowerCasedRandomCharacter(allUpperCase)

        val instance = StringAssertions.allUpperCaseString()
        assertThat(instance, notNullValue())

        instance.check(allUpperCase)

        assertThrows { instance.check(oneLowerCaseCharacter) }
                .isInstanceOf(FailedAssertionException::class.java)

    }

    private fun lowerCasedRandomCharacter(string: String): String
    {
        val index = one(integers(0, string.length))
        val character = string[index]

        val builder = StringBuilder(string)
        builder.replace(index, index + 1, character.toString().toLowerCase())

        return builder.toString()
    }

    @Test
    fun testAllLowerCaseString()
    {
        val allLowerCase = one(alphabeticString(50)).toLowerCase()
        val oneUpperCaseCharacter = upperCaseRandomCharacter(allLowerCase)

        val instance = StringAssertions.allLowerCaseString()
        assertThat(instance, notNullValue())

        instance.check(allLowerCase)

        assertThrows { instance.check(oneUpperCaseCharacter) }
                .isInstanceOf(FailedAssertionException::class.java)
    }

    private fun upperCaseRandomCharacter(string: String): String
    {
        val index = one(integers(0, string.length))
        val character = string[index]

        val builder = StringBuilder(string)
        builder.replace(index, index + 1, character.toString().toUpperCase())

        return builder.toString()
    }

    @Test
    fun testStringEndingWith()
    {
        //Edge Cases
        assertThrows { StringAssertions.stringEndingWith(null!!) }
                .isInstanceOf(IllegalArgumentException::class.java)

        assertThrows { StringAssertions.stringEndingWith("") }
                .isInstanceOf(IllegalArgumentException::class.java)

        //Happy Cases
        val string = one(strings())
        val suffix = randomSuffixFrom(string)

        val instance = StringAssertions.stringEndingWith(suffix)
        assertThat(instance, notNullValue())

        instance.check(string)

        val anotherRandomString = one(strings())
        assertThrows { instance.check(anotherRandomString) }
                .isInstanceOf(FailedAssertionException::class.java)

    }

    private fun randomSuffixFrom(string: String): String
    {
        val suffixStartIndex = one(integers(0, string.length / 2))
        return string.substring(suffixStartIndex)
    }

    @Test
    fun testAlphabeticString()
    {
        val instance = StringAssertions.alphabeticString()
        checkThat(instance, notNullValue())

        val alphabetic = one(alphabeticString())
        instance.check(alphabetic)

        val alphanumeric = format("%s-%d", alphabetic, one(positiveIntegers()))
        assertThrows { instance.check(alphanumeric) }
                .isInstanceOf(FailedAssertionException::class.java)

        assertThrows { instance.check("") }
                .isInstanceOf(FailedAssertionException::class.java)
    }

    @Test
    fun testAlphanumericString()
    {
        val instance = StringAssertions.alphanumericString()
        checkThat(instance, notNullValue())

        val alphanumeric = one(alphanumericString())
        instance.check(alphanumeric)

        val specialCharacters = alphanumeric + one(strings()) + "-!%$"
        assertThrows { instance.check(specialCharacters) }
                .isInstanceOf(FailedAssertionException::class.java)

        assertThrows { instance.check("") }
                .isInstanceOf(FailedAssertionException::class.java)
    }

    @Test
    fun testStringRepresentingInteger()
    {
        val instance = StringAssertions.stringRepresentingInteger()
        checkThat(instance, notNullValue())

        val integer = one(longs(java.lang.Long.MIN_VALUE, java.lang.Long.MAX_VALUE))
        val integerString = integer.toString()
        instance.check(integerString)

        //Edge cases
        val floatingPoint = one(doubles(-java.lang.Double.MAX_VALUE, java.lang.Double.MAX_VALUE))
        val floatingPointString = floatingPoint.toString()

        assertThrows { instance.check(floatingPointString) }
                .isInstanceOf(FailedAssertionException::class.java)

        val text = one(strings())
        assertThrows { instance.check(text) }
                .isInstanceOf(FailedAssertionException::class.java)
    }

    @Test
    fun testValidUUID()
    {
        val assertion = StringAssertions.validUUID()
        assertThat(assertion, notNullValue())

        val uuid = one(uuids)
        assertion.check(uuid)

        val nonUUID = one(alphabeticString(10))
        assertThrows { assertion.check(nonUUID) }
                .isInstanceOf(FailedAssertionException::class.java)

    }

    @Test
    fun testIntegerStringWithGoodString()
    {
        val value = one(integers(Integer.MIN_VALUE, Integer.MAX_VALUE))
        val string = value.toString()

        val assertion = StringAssertions.integerString()
        assertThat(assertion, notNullValue())

        assertion.check(string)
    }

    @Test
    fun testIntegerStringWithBadString()
    {
        val assertion = StringAssertions.integerString()

        val alphabetic = one(alphabeticString())
        assertThrows { assertion.check(alphabetic) }.isInstanceOf(FailedAssertionException::class.java)

        val value = one(doubles(-java.lang.Double.MAX_VALUE, java.lang.Double.MAX_VALUE))
        val decimalString = value.toString()
        assertThrows { assertion.check(decimalString) }.isInstanceOf(FailedAssertionException::class.java)
    }

    @Test
    fun testDecimalString()
    {
        val assertion = StringAssertions.decimalString()
        assertThat(assertion, notNullValue())

        val value = one(doubles(-java.lang.Double.MAX_VALUE, java.lang.Double.MAX_VALUE))
        assertion.check(value.toString())
    }

    @Test
    fun testDecimalStringWithBadString()
    {
        val assertion = StringAssertions.decimalString()
        assertThat(assertion, notNullValue())

        val value = one(alphanumericString())
        assertThrows { assertion.check(value.toString()) }
    }

}
