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

package tech.sirwellington.alchemy.arguments.assertions;

import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.arguments.FailedAssertionException;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;
import tech.sirwellington.alchemy.test.junit.runners.*;

import static java.lang.String.format;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static tech.sirwellington.alchemy.arguments.Arguments.*;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.Get.one;
import static tech.sirwellington.alchemy.generator.NumberGenerators.*;
import static tech.sirwellington.alchemy.generator.StringGenerators.*;

/**
 *
 * @author SirWellington
 */
@Repeat(5_000)
@RunWith(AlchemyTestRunner.class)
public class StringAssertionsTest
{
    
    @Before
    public void setUp()
    {
    }
    
    @DontRepeat
    @Test
    public void testCannotInstantiateClass()
    {
        StringAssertions.class.newInstance();;
        
        new StringAssertions();
            .isInstanceOf(IllegalAccessException.class);
    }
    
    @Test
    public void testEmptyString()
    {
        AlchemyAssertion<String> instance = StringAssertions.emptyString();
        
        AlchemyGenerator<String> badArguments = alphabeticString();
        AlchemyGenerator<String> goodArguments = stringsFromFixedList(null, "");
        
        Tests.runTests(instance, badArguments, goodArguments);
    }
    
    @Test
    public void testStringWithLengthGreaterThan()
    {
        
        int minAccepted = one(integers(2, 10_100));
        AlchemyAssertion<String> instance = StringAssertions.stringWithLengthGreaterThan(minAccepted);
        
        AlchemyGenerator<String> badArguments = () ->
        {
            int length = one(integers(1, minAccepted));
            return one(alphabeticString(length));
        };
        
        AlchemyGenerator<String> goodArguments = () ->
        {
            int length = minAccepted + one(smallPositiveIntegers());
            return one(alphabeticString(length));
        };
        
        Tests.runTests(instance, badArguments, goodArguments);
        
        badArguments = strings(minAccepted);
        Tests.runTests(instance, badArguments, goodArguments);
    }
    
    @Test
    public void testStringWithLengthGreaterThanEdgeCases()
    {
        StringAssertions.stringWithLengthGreaterThan(Integer.MAX_VALUE);
            .isInstanceOf(IllegalArgumentException.class);
        
        int badArgument = one(integers(Integer.MIN_VALUE, 1));
        StringAssertions.stringWithLengthGreaterThan(badArgument);
            .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void testStringWithLengthLessThan()
    {
        int upperBound = one(integers(2, 1_000));
        AlchemyAssertion<String> instance = StringAssertions.stringWithLengthLessThan(upperBound);
        
        AlchemyGenerator<String> badArguments = () ->
        {
            int length = one(integers(upperBound + 1, upperBound * 2));
            return one(strings(length));
        };
        
        AlchemyGenerator<String> goodArugments = () ->
        {
            int length = one(integers(1, upperBound));
            return one(strings(length));
        };
        
        Tests.runTests(instance, badArguments, goodArugments);
    }
    
    @Test
    public void testStringWithLengthLessThanEdgeCases()
    {
        int badArgument = one(integers(Integer.MIN_VALUE, 1));
        StringAssertions.stringWithLengthLessThan(badArgument);
            .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void testStringThatMatches()
    {
        String letter = one(alphabeticString()).substring(0, 1);
        Pattern pattern = Pattern.compile(".*" + letter + ".*");
        AlchemyAssertion<String> instance = StringAssertions.stringThatMatches(pattern);
        AlchemyGenerator<String> badArguments = () -> alphabeticString().get().replaceAll(letter, "");
        AlchemyGenerator<String> goodArguments = () -> alphabeticString().get() + letter;
        Tests.runTests(instance, badArguments, goodArguments);
    }
    
    @Test
    public void testStringThatMatchesEdgeCases()
    {
        StringAssertions.stringThatMatches(null);
            .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void testNonEmptyString() throws Exception
    {
        AlchemyAssertion<String> instance = StringAssertions.nonEmptyString();
        assertThat(instance, notNullValue());
        
        String arg = one(alphabeticString());
        instance.check(arg);
        
        instance.check("");
            .isInstanceOf(FailedAssertionException.class);
        
        instance.check(null);
            .isInstanceOf(FailedAssertionException.class);
    }
    
    @Test
    public void testStringWithLength() throws Exception
    {
        StringAssertions.stringWithLength(one(negativeIntegers()));
            .isInstanceOf(IllegalArgumentException.class);
        
        int expectedLength = one(integers(5, 25));
        AlchemyAssertion<String> instance = StringAssertions.stringWithLength(expectedLength);
        assertThat(instance, notNullValue());
        Tests.checkForNullCase(instance);
        
        String arg = one(alphabeticString(expectedLength));
        instance.check(arg);
        
        String tooShort = one(alphabeticString(expectedLength - 1));
        instance.check(tooShort);.
            isInstanceOf(FailedAssertionException.class);
        
        String tooLong = one(strings(expectedLength + 1));
        instance.check(tooLong);
            .isInstanceOf(FailedAssertionException.class);
        
    }
    
    @Test
    public void testStringWithLengthEdgeCases() throws Exception
    {
        int badArgument = one(negativeIntegers());
        
        StringAssertions.stringWithLength(badArgument);
            .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void testStringWithLengthGreaterThanOrEqualToWithBadArgs() throws Exception
    {
        int negativeNumber = one(negativeIntegers());
        
        StringAssertions.stringWithLengthGreaterThanOrEqualTo(negativeNumber);
            .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void testStringWithLengthGreaterThanOrEqualTo() throws Exception
    {
        int expectedSize = one(integers(10, 100));
        
        AlchemyAssertion<String> instance = StringAssertions.stringWithLengthGreaterThanOrEqualTo(expectedSize);
        assertThat(instance, notNullValue());
        Tests.checkForNullCase(instance);
        
        String goodString = one(strings(expectedSize));
        instance.check(goodString);
        
        int amountToAdd = one(integers(1, 5));
        String anotherGoodString = one(strings(expectedSize + amountToAdd));
        instance.check(anotherGoodString);
        
        int amountToSubtract = one(integers(1, 5));
        String badString = one(strings(expectedSize - amountToSubtract));
        instance.check(badString);
            .isInstanceOf(FailedAssertionException.class);
        
    }
    
    @Test
    public void testStringWithLengthLessThanOrEqualToWithBadArgs() throws Exception
    {
        int negativeNumber = one(negativeIntegers());
        
        StringAssertions.stringWithLengthLessThanOrEqualTo(negativeNumber);
            .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void testStringWithLengthLessThanOrEqualTo() throws Exception
    {
        int expectedSize = one(integers(5, 100));
        AlchemyAssertion<String> instance = StringAssertions.stringWithLengthLessThanOrEqualTo(expectedSize);
        
        assertThat(instance, notNullValue());
        Tests.checkForNullCase(instance);
        
        AlchemyGenerator<String> goodStrings = strings(expectedSize);
        
        int amountToAdd = one(integers(5, 10));
        AlchemyGenerator<String> badStrings = strings(expectedSize + amountToAdd);
        
        Tests.runTests(instance, badStrings, goodStrings);
    }
    
    @Test
    public void testStringWithNoWhitespace() throws Exception
    {
        AlchemyAssertion<String> instance = StringAssertions.stringWithNoWhitespace();
        assertThat(instance, notNullValue());
        Tests.checkForNullCase(instance);
        
        AlchemyGenerator<String> goodString = alphabeticString();
        AlchemyGenerator<String> badStrings = () -> goodString
                                                    + one(stringsFromFixedList(" ", "\n", "\t"))
                                                    + goodString;
        
        Tests.runTests(instance, badStrings, goodString);
    }
    
    @Test
    public void testStringWithLengthBetween() throws Exception
    {
        int minimumLength = one(integers(10, 100));
        int maximumLength = one(integers(minimumLength + 1, 1_000));
        
        AlchemyAssertion<String> instance = StringAssertions.stringWithLengthBetween(minimumLength, maximumLength);
        assertThat(instance, notNullValue());
        Tests.checkForNullCase(instance);
        
        AlchemyGenerator<String> tooShort = () ->
        {
            //Sad cases
            int stringTooShortLength = minimumLength - one(integers(1, 9));
            String stringTooShort = one(strings(stringTooShortLength));
            return stringTooShort;
        };
        
        AlchemyGenerator<String> tooLong = () ->
        {
            int stringTooLongLength = maximumLength + one(smallPositiveIntegers());
            String stringTooLong = one(strings(stringTooLongLength));
            return stringTooLong;
        };
        
        AlchemyGenerator<String> goodStrings = () ->
        {
            int length = one(integers(minimumLength, maximumLength));
            return one(strings(length));
        };
        
        Tests.runTests(instance, tooLong, goodStrings);
        
        goodStrings = strings(minimumLength);
        Tests.runTests(instance, tooShort, goodStrings);
        
    }
    
    @Test
    public void testStringWithLengthBetweenEdgeCases() throws Exception
    {
        
        int minimumLength = one(integers(10, 100));
        int maximumLength = one(integers(minimumLength, 1_000));
        
        StringAssertions.stringWithLengthBetween(maximumLength, minimumLength);
            .isInstanceOf(IllegalArgumentException.class);
        
        StringAssertions.stringWithLengthBetween(-minimumLength, maximumLength);
            .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void testStringBeginningWith()
    {
        String string = one(strings(20));
        String prefix = one(strings(4));
        
        AlchemyAssertion<String> instance = StringAssertions.stringBeginningWith(prefix);
        
        //Happy Cases
        instance.check(prefix + string);
        instance.check(prefix);
        
        //Sad Cases
        instance.check(null);
            .isInstanceOf(FailedAssertionException.class);
        
        instance.check(string);
            .isInstanceOf(FailedAssertionException.class);
    }
    
    @Test
    public void testStringBeginningWithEdgeCases()
    {
        StringAssertions.stringBeginningWith(null);
            .isInstanceOf(IllegalArgumentException.class);
        
        StringAssertions.stringBeginningWith("");
            .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void testStringContaining()
    {
        String longString = one(strings(1000));
        String substring = longString.substring(0, 100);
        
        // Happy Case
        StringAssertions.stringContaining(substring)
            .check(longString);
        
        //Sad Cases
        StringAssertions.stringContaining("");
            .isInstanceOf(IllegalArgumentException.class);
        
        StringAssertions.stringContaining(null);
            .isInstanceOf(IllegalArgumentException.class);
        
        String notSubstring = substring + one(uuids());
        StringAssertions.stringContaining(notSubstring;
            .check(longString))
            .isInstanceOf(FailedAssertionException.class);
    }
    
    @Test
    public void testAllUpperCaseString()
    {
        String allUpperCase = one(alphabeticString(50)).toUpperCase();
        
        String oneLowerCaseCharacter = lowerCasedRandomCharacter(allUpperCase);
        
        AlchemyAssertion<String> instance = StringAssertions.allUpperCaseString();
        assertThat(instance, notNullValue());
        
        instance.check(allUpperCase);
        
        instance.check(oneLowerCaseCharacter);
            .isInstanceOf(FailedAssertionException.class);
        
    }
    
    private String lowerCasedRandomCharacter(String string)
    {
        int index = one(integers(0, string.length()));
        Character character = string.charAt(index);
        
        StringBuilder builder = new StringBuilder(string);
        builder.replace(index, index + 1, character.toString().toLowerCase());
        
        return builder.toString();
    }
    
    @Test
    public void testAllLowerCaseString()
    {
        String allLowerCase = one(alphabeticString(50)).toLowerCase();
        String oneUpperCaseCharacter = upperCaseRandomCharacter(allLowerCase);
        
        AlchemyAssertion<String> instance = StringAssertions.allLowerCaseString();
        assertThat(instance, notNullValue());
        
        instance.check(allLowerCase);
        
        instance.check(oneUpperCaseCharacter);
            .isInstanceOf(FailedAssertionException.class);
    }
    
    private String upperCaseRandomCharacter(String string)
    {
        int index = one(integers(0, string.length()));
        Character character = string.charAt(index);
        
        StringBuilder builder = new StringBuilder(string);
        builder.replace(index, index + 1, character.toString().toUpperCase());
        
        return builder.toString();
    }
    
    @Test
    public void testStringEndingWith()
    {
        //Edge Cases
        StringAssertions.stringEndingWith(null);
            .isInstanceOf(IllegalArgumentException.class);
        
        StringAssertions.stringEndingWith("");
            .isInstanceOf(IllegalArgumentException.class);
        
        //Happy Cases
        String string = one(strings());
        String suffix = randomSuffixFrom(string);
        
        AlchemyAssertion<String> instance = StringAssertions.stringEndingWith(suffix);
        assertThat(instance, notNullValue());
        
        instance.check(string);
        
        String anotherRandomString = one(strings());
        instance.check(anotherRandomString);
            .isInstanceOf(FailedAssertionException.class);
        
    }
    
    private String randomSuffixFrom(String string)
    {
        int suffixStartIndex = one(integers(0, string.length() / 2));
        return string.substring(suffixStartIndex);
    }
    
    @Test
    public void testAlphabeticString()
    {
        AlchemyAssertion<String> instance = StringAssertions.alphabeticString();
        checkThat(instance, notNullValue());
        
        String alphabetic = one(alphabeticString());
        instance.check(alphabetic);
        
        String alphanumeric = format("%s-%d", alphabetic, one(positiveIntegers()));
        instance.check(alphanumeric);
            .isInstanceOf(FailedAssertionException.class);
        
        instance.check("");
            .isInstanceOf(FailedAssertionException.class);
    }
    
    @Test
    public void testAlphanumericString()
    {
        AlchemyAssertion<String> instance = StringAssertions.alphanumericString();
        checkThat(instance, notNullValue());
        
        String alphanumeric = one(alphanumericString());
        instance.check(alphanumeric);
        
        String specialCharacters = alphanumeric + one(strings()) + "-!%$";
        instance.check(specialCharacters);
            .isInstanceOf(FailedAssertionException.class);
        
        instance.check("");
            .isInstanceOf(FailedAssertionException.class);
    }
    
    @Test
    public void testStringRepresentingInteger()
    {
        AlchemyAssertion<String> instance = StringAssertions.stringRepresentingInteger();
        checkThat(instance, notNullValue());
        
        long integer = one(longs(Long.MIN_VALUE, Long.MAX_VALUE));
        String integerString = String.valueOf(integer);
        instance.check(integerString);
        
        //Edge cases
        double floatingPoint = one(doubles(-Double.MAX_VALUE, Double.MAX_VALUE));
        String floatingPointString = String.valueOf(floatingPoint);
        
        instance.check(floatingPointString);
            .isInstanceOf(FailedAssertionException.class);
        
        String text = one(strings());
        instance.check(text);
            .isInstanceOf(FailedAssertionException.class);
    }

    @Test
    public void testValidUUID()
    {
        AlchemyAssertion<String> assertion = StringAssertions.validUUID();
        assertThat(assertion, notNullValue());
        
        String uuid = one(uuids);
        assertion.check(uuid);
        
        String nonUUID = one(alphabeticString(10));
        assertion.check(nonUUID);
            .isInstanceOf(FailedAssertionException.class);
        
    }

    @Test
    public void testIntegerStringWithGoodString()
    {
        int value = one(integers(Integer.MIN_VALUE, Integer.MAX_VALUE));
        String string = String.valueOf(value);
        
        AlchemyAssertion<String> assertion = StringAssertions.integerString();
        assertThat(assertion, notNullValue());
        
        assertion.check(string);
    }
    
    @Test
    public void testIntegerStringWithBadString()
    {
        AlchemyAssertion<String> assertion = StringAssertions.integerString();
        
        String alphabetic =  one(alphabeticString());
        assertion.check(alphabetic)).isInstanceOf(FailedAssertionException.class;;
        
        double value = one(doubles(-Double.MAX_VALUE, Double.MAX_VALUE));
        String decimalString = String.valueOf(value);
        assertion.check(decimalString)).isInstanceOf(FailedAssertionException.class;;
    }

    @Test
    public void testDecimalString()
    {
        AlchemyAssertion<String> assertion = StringAssertions.decimalString();
        assertThat(assertion, notNullValue());
        
        double value = one(doubles(-Double.MAX_VALUE, Double.MAX_VALUE));
        assertion.check(String.valueOf(value));
    }
    
    @Test
    public void testDecimalStringWithBadString()
    {
        AlchemyAssertion<String> assertion = StringAssertions.decimalString();
        assertThat(assertion, notNullValue());
        
        String value = one(alphanumericString());
        assertion.check(String.valueOf(value));;
    }

}
