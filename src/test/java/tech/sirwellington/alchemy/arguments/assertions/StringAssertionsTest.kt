/*
 * Copyright 2017 SirWellington Tech.
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
import tech.sirwellington.alchemy.arguments.failedAssertion
import tech.sirwellington.alchemy.arguments.illegalArgument
import tech.sirwellington.alchemy.arguments.nullPointer
import tech.sirwellington.alchemy.generator.AlchemyGenerator
import tech.sirwellington.alchemy.generator.NumberGenerators
import tech.sirwellington.alchemy.generator.NumberGenerators.Companion.doubles
import tech.sirwellington.alchemy.generator.NumberGenerators.Companion.integers
import tech.sirwellington.alchemy.generator.NumberGenerators.Companion.longs
import tech.sirwellington.alchemy.generator.NumberGenerators.Companion.negativeIntegers
import tech.sirwellington.alchemy.generator.NumberGenerators.Companion.positiveIntegers
import tech.sirwellington.alchemy.generator.NumberGenerators.Companion.smallPositiveIntegers
import tech.sirwellington.alchemy.generator.StringGenerators
import tech.sirwellington.alchemy.generator.StringGenerators.Companion.alphabeticStrings
import tech.sirwellington.alchemy.generator.StringGenerators.Companion.alphanumericStrings
import tech.sirwellington.alchemy.generator.StringGenerators.Companion.strings
import tech.sirwellington.alchemy.generator.StringGenerators.Companion.stringsFromFixedList
import tech.sirwellington.alchemy.generator.one
import tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner
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

    @Test
    fun testEmptyString()
    {
        val instance = emptyString()

        val badArguments = alphabeticStrings()
        val goodArguments = stringsFromFixedList("", "")

        Tests.runTests(instance, badArguments, goodArguments)
    }

    @Test
    fun testStringWithLengthGreaterThan()
    {

        val minAccepted = one(integers(2, 10100))
        val instance = stringWithLengthGreaterThan(minAccepted)

        var badArguments = AlchemyGenerator<String> {
            val length = one(integers(1, minAccepted))
            one(alphabeticStrings(length))
        }

        val goodArguments = AlchemyGenerator<String> {
            val length = minAccepted + one(smallPositiveIntegers())
            one(alphabeticStrings(length))
        }

        Tests.runTests(instance, badArguments, goodArguments)

        badArguments = strings(minAccepted)
        Tests.runTests(instance, badArguments, goodArguments)
    }

    @Test
    fun testStringWithLengthGreaterThanEdgeCases()
    {
        assertThrows { stringWithLengthGreaterThan(Integer.MAX_VALUE) }
                .illegalArgument()

        val badArgument = one(integers(Integer.MIN_VALUE, 1))
        assertThrows { stringWithLengthGreaterThan(badArgument) }
                .illegalArgument()
    }

    @Test
    fun testStringWithLengthLessThan()
    {
        val upperBound = one(integers(2, 1000))
        val instance = stringWithLengthLessThan(upperBound)

        val badArguments = AlchemyGenerator {
            val length = one(integers(upperBound + 1, upperBound * 2))
            one(strings(length))
        }

        val goodArguments = AlchemyGenerator {
            val length = one(integers(1, upperBound))
            one(strings(length))
        }

        Tests.runTests(instance, badArguments, goodArguments)
    }

    @Test
    fun testStringWithLengthLessThanEdgeCases()
    {
        val badArgument = one(integers(Integer.MIN_VALUE, 1))
        assertThrows { stringWithLengthLessThan(badArgument) }
                .illegalArgument()
    }

    @Test
    fun testStringThatMatches()
    {
        val letter = one(alphabeticStrings()).substring(0, 1)
        val pattern = Pattern.compile(".*$letter.*")
        val instance = stringThatMatches(pattern)
        val badArguments = AlchemyGenerator { alphabeticStrings().get().replace(letter.toRegex(), "") }
        val goodArguments = AlchemyGenerator { alphabeticStrings().get() + letter }
        Tests.runTests(instance, badArguments, goodArguments)
    }

    @Test
    fun testStringThatMatchesEdgeCases()
    {
        assertThrows { stringThatMatches(null!!) }.nullPointer()
    }

    @Test
    @Throws(Exception::class)
    fun testNonEmptyString()
    {
        val instance = nonEmptyString()
        assertThat(instance, notNullValue())

        val arg = one(alphabeticStrings())
        instance.check(arg)

        assertThrows { instance.check("") }.failedAssertion()
        assertThrows { instance.check(null) }.failedAssertion()
    }

    @Test
    @Throws(Exception::class)
    fun testStringWithLength()
    {
        assertThrows { stringWithLength(one(NumberGenerators.negativeIntegers())) }
                .illegalArgument()

        val expectedLength = one(integers(5, 25))
        val instance = stringWithLength(expectedLength)
        assertThat(instance, notNullValue())
        Tests.checkForNullCase(instance)

        val arg = one(alphabeticStrings(expectedLength))
        instance.check(arg)

        val tooShort = one(alphabeticStrings(expectedLength - 1))
        assertThrows { instance.check(tooShort) }.failedAssertion()

        val tooLong = one(strings(expectedLength + 1))
        assertThrows { instance.check(tooLong) }.failedAssertion()
    }

    @Test
    @Throws(Exception::class)
    fun testStringWithLengthEdgeCases()
    {
        val badArgument = one(negativeIntegers())

        assertThrows { stringWithLength(badArgument) }
                .illegalArgument()
    }

    @Test
    @Throws(Exception::class)
    fun testStringWithLengthGreaterThanOrEqualToWithBadArgs()
    {
        val negativeNumber = one(negativeIntegers())

        assertThrows { stringWithLengthGreaterThanOrEqualTo(negativeNumber) }
                .illegalArgument()
    }

    @Test
    @Throws(Exception::class)
    fun testStringWithLengthGreaterThanOrEqualTo()
    {
        val expectedSize = one(integers(10, 100))

        val instance = stringWithLengthGreaterThanOrEqualTo(expectedSize)
        assertThat(instance, notNullValue())
        Tests.checkForNullCase(instance)

        val goodString = one(strings(expectedSize))
        instance.check(goodString)

        val amountToAdd = one(integers(1, 5))
        val anotherGoodString = one(strings(expectedSize + amountToAdd))
        instance.check(anotherGoodString)

        val amountToSubtract = one(integers(1, 5))
        val badString = one(strings(expectedSize - amountToSubtract))
        assertThrows { instance.check(badString) }.failedAssertion()
    }

    @Test
    @Throws(Exception::class)
    fun testStringWithLengthLessThanOrEqualToWithBadArgs()
    {
        val negativeNumber = one(negativeIntegers())

        assertThrows { stringWithLengthLessThanOrEqualTo(negativeNumber) }
                .illegalArgument()
    }

    @Test
    @Throws(Exception::class)
    fun testStringWithLengthLessThanOrEqualTo()
    {
        val expectedSize = one(integers(5, 100))
        val instance = stringWithLengthLessThanOrEqualTo(expectedSize)

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
        val instance = stringWithNoWhitespace()
        assertThat(instance, notNullValue())
        Tests.checkForNullCase(instance)

        val goodStrings = alphabeticStrings()

        val badStrings = AlchemyGenerator {
            one(goodStrings) + one(stringsFromFixedList(" ", "\n", "\t")) + one(goodStrings)
        }

        Tests.runTests(instance, badStrings, goodStrings)
    }

    @Test
    @Throws(Exception::class)
    fun testStringWithLengthBetween()
    {
        val minimumLength = one(integers(10, 100))
        val maximumLength = one(integers(minimumLength + 1, 1000))

        val instance = stringWithLengthBetween(minimumLength, maximumLength)
        assertThat(instance, notNullValue())
        Tests.checkForNullCase(instance)

        val tooShort = AlchemyGenerator {
            //Sad cases
            val stringTooShortLength = minimumLength - one(integers(1, 9))
            val stringTooShort = one(strings(stringTooShortLength))
            stringTooShort
        }

        val tooLong = AlchemyGenerator {
            val stringTooLongLength = maximumLength + one(smallPositiveIntegers())
            val stringTooLong = one(strings(stringTooLongLength))
            stringTooLong
        }

        var goodStrings = AlchemyGenerator {
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

        assertThrows { stringWithLengthBetween(maximumLength, minimumLength) }
                .illegalArgument()

        assertThrows { stringWithLengthBetween(-minimumLength, maximumLength) }
                .illegalArgument()
    }

    @Test
    fun testStringBeginningWith()
    {
        val string = one(strings(20))
        val prefix = one(strings(4))

        val instance = stringBeginningWith(prefix)

        //Happy Cases
        instance.check(prefix + string)
        instance.check(prefix)

        //Sad Cases
        assertThrows { instance.check(null) }.failedAssertion()
        assertThrows { instance.check(string) }.failedAssertion()
    }

    @Test
    fun testStringBeginningWithEdgeCases()
    {
        assertThrows { stringBeginningWith(null!!) }.nullPointer()

        assertThrows { stringBeginningWith("") }.illegalArgument()
    }

    @Test
    fun testStringContaining()
    {
        val longString = one(strings(1000))
        val substring = longString.substring(0, 100)

        // Happy Case
        stringContaining(substring)
                .check(longString)

        //Sad Cases
        assertThrows { stringContaining("") }
                .illegalArgument()

        assertThrows { stringContaining(null!!) }.nullPointer()

        val notSubstring = substring + one(StringGenerators.uuids())

        assertThrows {
            stringContaining(notSubstring).check(longString)
        }.failedAssertion()
    }

    @Test
    fun testAllUpperCaseString()
    {
        val allUpperCase = one(alphabeticStrings(50)).toUpperCase()

        val oneLowerCaseCharacter = lowerCasedRandomCharacter(allUpperCase)

        val instance = allUpperCaseString()
        assertThat(instance, notNullValue())

        instance.check(allUpperCase)

        assertThrows { instance.check(oneLowerCaseCharacter) }.failedAssertion()
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
        val allLowerCase = one(alphabeticStrings(50)).toLowerCase()
        val oneUpperCaseCharacter = upperCaseRandomCharacter(allLowerCase)

        val instance = allLowerCaseString()
        assertThat(instance, notNullValue())

        instance.check(allLowerCase)

        assertThrows { instance.check(oneUpperCaseCharacter) }
                .failedAssertion()
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
        assertThrows { stringEndingWith(null!!) }.nullPointer()
        assertThrows { stringEndingWith("") }.illegalArgument()

        //Happy Cases
        val string = one(strings())
        val suffix = randomSuffixFrom(string)

        val instance = stringEndingWith(suffix)
        assertThat(instance, notNullValue())

        instance.check(string)

        val anotherRandomString = one(strings())
        assertThrows { instance.check(anotherRandomString) }.failedAssertion()
    }

    private fun randomSuffixFrom(string: String): String
    {
        val suffixStartIndex = one(integers(0, string.length / 2))
        return string.substring(suffixStartIndex)
    }

    @Test
    fun testAlphabeticString()
    {
        val instance = tech.sirwellington.alchemy.arguments.assertions.alphabeticString()
        checkThat(instance, notNullValue())

        val alphabetic = one(alphabeticStrings())
        instance.check(alphabetic)

        assertThrows { instance.check("") }.failedAssertion()

        val alphanumeric = format("%s-%d", alphabetic, one(positiveIntegers()))
        assertThrows { instance.check(alphanumeric) }.failedAssertion()
    }

    @Test
    fun testAlphanumericString()
    {
        val instance = alphanumericString()
        checkThat(instance, notNullValue())

        val alphanumeric = one(StringGenerators.alphanumericStrings())
        instance.check(alphanumeric)

        val specialCharacters = alphanumeric + one(strings()) + "-!%$"
        assertThrows { instance.check(specialCharacters) }.failedAssertion()
        assertThrows { instance.check("") }
                .failedAssertion()
    }

    @Test
    fun testStringRepresentingInteger()
    {
        val instance = stringRepresentingInteger()
        checkThat(instance, notNullValue())

        val integer = one(longs(Long.MIN_VALUE, Long.MAX_VALUE))
        val integerString = integer.toString()
        instance.check(integerString)

        //Edge cases
        val floatingPoint = one(doubles(-Double.MAX_VALUE, Double.MAX_VALUE))
        val floatingPointString = floatingPoint.toString()

        assertThrows { instance.check(floatingPointString) }.failedAssertion()

        val text = one(strings())
        assertThrows { instance.check(text) }.failedAssertion()
    }

    @Test
    fun testValidUUID()
    {
        val assertion = validUUID()
        assertThat(assertion, notNullValue())

        val uuid = one(StringGenerators.uuids)
        assertion.check(uuid)

        val nonUUID = one(alphabeticStrings(10))
        assertThrows { assertion.check(nonUUID) }.failedAssertion()
    }

    @Test
    fun testIntegerStringWithGoodString()
    {
        val value = one(integers(Integer.MIN_VALUE, Integer.MAX_VALUE))
        val string = value.toString()

        val assertion = integerString()
        assertThat(assertion, notNullValue())

        assertion.check(string)
    }

    @Test
    fun testIntegerStringWithBadString()
    {
        val assertion = integerString()

        val alphabetic = one(alphabeticStrings())
        assertThrows { assertion.check(alphabetic) }.failedAssertion()

        val value = one(doubles(-Double.MAX_VALUE, Double.MAX_VALUE))
        val decimalString = value.toString()
        assertThrows { assertion.check(decimalString) }.failedAssertion()
    }

    @Test
    fun testDecimalString()
    {
        val assertion = decimalString()
        assertThat(assertion, notNullValue())

        val value = one(doubles(-Double.MAX_VALUE, Double.MAX_VALUE))
        assertion.check(value.toString())
    }

    @Test
    fun testDecimalStringWithBadString()
    {
        val assertion = decimalString()
        assertThat(assertion, notNullValue())

        val value = one(alphanumericStrings())
        assertThrows { assertion.check(value) }.failedAssertion()
    }

}
