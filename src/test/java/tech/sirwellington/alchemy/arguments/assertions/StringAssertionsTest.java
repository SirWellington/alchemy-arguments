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
import tech.sirwellington.alchemy.test.junit.runners.Repeat;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.one;
import static tech.sirwellington.alchemy.generator.NumberGenerators.integers;
import static tech.sirwellington.alchemy.generator.NumberGenerators.negativeIntegers;
import static tech.sirwellington.alchemy.generator.NumberGenerators.smallPositiveIntegers;
import static tech.sirwellington.alchemy.generator.StringGenerators.alphabeticString;
import static tech.sirwellington.alchemy.generator.StringGenerators.hexadecimalString;
import static tech.sirwellington.alchemy.generator.StringGenerators.strings;
import static tech.sirwellington.alchemy.generator.StringGenerators.stringsFromFixedList;
import static tech.sirwellington.alchemy.generator.StringGenerators.uuids;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;

/**
 *
 * @author SirWellington
 */
@Repeat
@RunWith(AlchemyTestRunner.class)
public class StringAssertionsTest
{

    @Before
    public void setUp()
    {
    }

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

        int minAccepted = one(smallPositiveIntegers());
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
    public void testSTringWithLengthGreaterThanEdgeCases()
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
        int upperBound = one(smallPositiveIntegers());
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
        AlchemyGenerator<String> badStrings = () -> goodString + one(stringsFromFixedList(" ", "\n", "\t")) + goodString;

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
    public void testHasNoWhitespace()
    {
        AlchemyAssertion<String> instance = StringAssertions.stringWithNoWhitespace();
        assertThat(instance, notNullValue());
        Tests.checkForNullCase(instance);

        String alphabetic = one(alphabeticString());
        instance.check(alphabetic);

        String hex = one(hexadecimalString(10));
        instance.check(hex);

        assertThrows(() -> instance.check(one(alphabeticString()) + " "))
                .isInstanceOf(FailedAssertionException.class);

        assertThrows(() -> instance.check("some white space here"))
                .isInstanceOf(FailedAssertionException.class);

        assertThrows(() -> instance.check(" " + one(uuids)))
                .isInstanceOf(FailedAssertionException.class);

    }

    @Test
    public void testStringStartsWith()
    {
        String string = one(strings(20));
        String prefix = one(strings(4));

        AlchemyAssertion<String> instance = StringAssertions.stringThatStartsWith(prefix);

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
    public void testStringStartsWithEdgeCases()
    {
        assertThrows(() -> StringAssertions.stringThatStartsWith(null))
                .isInstanceOf(IllegalArgumentException.class);

        assertThrows(() -> StringAssertions.stringThatStartsWith(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
