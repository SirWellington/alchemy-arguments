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
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner;
import tech.sirwellington.alchemy.test.junit.runners.DontRepeat;
import tech.sirwellington.alchemy.test.junit.runners.Repeat;

import static java.lang.String.format;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static tech.sirwellington.alchemy.arguments.Arguments.checkThat;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.one;
import static tech.sirwellington.alchemy.generator.NumberGenerators.integers;
import static tech.sirwellington.alchemy.generator.NumberGenerators.negativeIntegers;
import static tech.sirwellington.alchemy.generator.NumberGenerators.positiveIntegers;
import static tech.sirwellington.alchemy.generator.NumberGenerators.smallPositiveIntegers;
import static tech.sirwellington.alchemy.generator.StringGenerators.alphabeticString;
import static tech.sirwellington.alchemy.generator.StringGenerators.alphanumericString;
import static tech.sirwellington.alchemy.generator.StringGenerators.strings;
import static tech.sirwellington.alchemy.generator.StringGenerators.stringsFromFixedList;
import static tech.sirwellington.alchemy.generator.StringGenerators.uuids;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;

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
        assertThrows(() -> StringAssertions.class.newInstance());

        assertThrows(() -> new StringAssertions())
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
        assertThrows(() -> StringAssertions.stringWithLengthGreaterThan(Integer.MAX_VALUE))
                .isInstanceOf(IllegalArgumentException.class);

        int badArgument = one(integers(Integer.MIN_VALUE, 1));
        assertThrows(() -> StringAssertions.stringWithLengthGreaterThan(badArgument))
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
        assertThrows(() -> StringAssertions.stringWithLengthLessThan(badArgument))
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
        assertThrows(() -> StringAssertions.stringThatMatches(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testNonEmptyString() throws Exception
    {
        AlchemyAssertion<String> instance = StringAssertions.nonEmptyString();
        assertThat(instance, notNullValue());

        String arg = one(alphabeticString());
        instance.check(arg);

        assertThrows(() -> instance.check(""))
                .isInstanceOf(FailedAssertionException.class);

        assertThrows(() -> instance.check(null))
                .isInstanceOf(FailedAssertionException.class);
    }

    @Test
    public void testStringWithLength() throws Exception
    {
        assertThrows(() -> StringAssertions.stringWithLength(one(negativeIntegers())))
                .isInstanceOf(IllegalArgumentException.class);

        int expectedLength = one(integers(5, 25));
        AlchemyAssertion<String> instance = StringAssertions.stringWithLength(expectedLength);
        assertThat(instance, notNullValue());
        Tests.checkForNullCase(instance);

        String arg = one(alphabeticString(expectedLength));
        instance.check(arg);

        String tooShort = one(alphabeticString(expectedLength - 1));
        assertThrows(() -> instance.check(tooShort)).
                isInstanceOf(FailedAssertionException.class);

        String tooLong = one(strings(expectedLength + 1));
        assertThrows(() -> instance.check(tooLong))
                .isInstanceOf(FailedAssertionException.class);

    }

    @Test
    public void testStringWithLengthEdgeCases() throws Exception
    {
        int badArgument = one(negativeIntegers());

        assertThrows(() -> StringAssertions.stringWithLength(badArgument))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testStringWithLengthGreaterThanOrEqualToWithBadArgs() throws Exception
    {
        int negativeNumber = one(negativeIntegers());

        assertThrows(() -> StringAssertions.stringWithLengthGreaterThanOrEqualTo(negativeNumber))
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
        assertThrows(() -> instance.check(badString))
                .isInstanceOf(FailedAssertionException.class);

    }

    @Test
    public void testStringWithLengthLessThanOrEqualToWithBadArgs() throws Exception
    {
        int negativeNumber = one(negativeIntegers());

        assertThrows(() -> StringAssertions.stringWithLengthLessThanOrEqualTo(negativeNumber))
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

        assertThrows(() -> StringAssertions.stringWithLengthBetween(maximumLength, minimumLength))
                .isInstanceOf(IllegalArgumentException.class);

        assertThrows(() -> StringAssertions.stringWithLengthBetween(-minimumLength, maximumLength))
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
        assertThrows(() -> instance.check(null))
                .isInstanceOf(FailedAssertionException.class);

        assertThrows(() -> instance.check(string))
                .isInstanceOf(FailedAssertionException.class);
    }

    @Test
    public void testStringBeginningWithEdgeCases()
    {
        assertThrows(() -> StringAssertions.stringBeginningWith(null))
                .isInstanceOf(IllegalArgumentException.class);

        assertThrows(() -> StringAssertions.stringBeginningWith(""))
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
        assertThrows(() -> StringAssertions.stringContaining(""))
            .isInstanceOf(IllegalArgumentException.class);

        assertThrows(() -> StringAssertions.stringContaining(null))
            .isInstanceOf(IllegalArgumentException.class);

        String notSubstring = substring + one(uuids());
        assertThrows(() -> StringAssertions.stringContaining(notSubstring)
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
        
        assertThrows(() -> instance.check(oneLowerCaseCharacter))
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
        
        assertThrows(() -> instance.check(oneUpperCaseCharacter))
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
        assertThrows(() -> StringAssertions.stringEndingWith(null))
            .isInstanceOf(IllegalArgumentException.class);

        assertThrows(() -> StringAssertions.stringEndingWith(""))
            .isInstanceOf(IllegalArgumentException.class);

        //Happy Cases
        String string = one(strings());
        String suffix = randomSuffixFrom(string);

        AlchemyAssertion<String> instance = StringAssertions.stringEndingWith(suffix);
        assertThat(instance, notNullValue());
        
        instance.check(string);
        
        String anotherRandomString = one(strings());
        assertThrows(() -> instance.check(anotherRandomString))
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
        assertThrows(() -> instance.check(alphanumeric))
            .isInstanceOf(FailedAssertionException.class);
        
        assertThrows(() -> instance.check(""))
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
        assertThrows(() -> instance.check(specialCharacters))
            .isInstanceOf(FailedAssertionException.class);
        
        assertThrows(() -> instance.check(""))
            .isInstanceOf(FailedAssertionException.class);
    }

}
